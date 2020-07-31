package world.entities.mobs;
import renderer.Renderer;
import utility.Direction;
import world.Game;
import world.Room;
import world.Tile;
import world.commands.DropCommand;
import world.commands.InteractCommand;
import world.commands.UseCommand;
import world.entities.Entity;
import world.entities.containers.Container;
import world.entities.item.*;
import world.entities.transport.Transport;
import java.util.ArrayList;
import java.util.List;
import static utility.Direction.*;

/**
 * Player class to represent the user's in-game character.
 */
public class Player extends Mob {

    private Game game; //Reference to current game being played.
    private Room room; //Reference to the current room the player is within.
    private Tile tile; //Reference to the current tile the player is standing on.

    private Direction direction = NORTH; //Direction player is facing

    /**
     * Inventory and held-item references
     */
    private List<Item> inventory = new ArrayList<>();
    private Item heldItem = null;
    private Integer holdIndex = null;

    /**
     * The different commands the player is able to carry out.
     */
    private InteractCommand interactCommand;
    private UseCommand useCommand;
    private DropCommand dropCommand;

    /**
     * Constructor for a new player object within the game. Sets all of
     * the appropriate attributes required by a player within the game.
     * @param game
     */
    public Player(Game game) {
        super("player", "a player");
        this.game = game;
        this.direction = SOUTH;
        this.health = 10;
    }

    /**
     * Constructor used by the persistence package
     * @param items
     * @param holdIndex
     * @param health
     */
    public Player(List<Item> items, int holdIndex, int health) {
        super("player", "a player");
        this.inventory = items;
        this.health = health;
        this.holdIndex = holdIndex;
    }

    /**
     * Sets the commands so that they may be visually represented
     * within the rendered version of the game.
     * @param renderer
     */
    public void setCommands(Renderer renderer) {
        this.interactCommand = new InteractCommand(renderer);
        this.useCommand = new UseCommand(renderer);
        this.dropCommand = new DropCommand(renderer);

    }

    /**
     * Updates the in-game button references based on the tile the current player
     * is standing on, and the different actions which can be performed on the
     * objects in the general direction that the player is facing within.
     */
    public void updateStrings() {
        int row = getRowFacing(direction);
        int col = getColFacing(direction);

        if(actionsCheck(row, col)) {
            Tile tileInfront = room.getTile(row, col);
            if(tileInfront.getEntity() != null) {
                Entity entity = tileInfront.getEntity();
                interactCommand.execute(entity.getInteractMessage());
            }else {
                interactCommand.execute("");
            }
        }

        if(heldItem == null){
            useCommand.execute("");
            dropCommand.execute("");
        }
        else {
            Tile tileInfront = room.getTile(row ,col);
            useCommand.execute(heldItem.getUseMessage(tileInfront));

            dropCommand.execute("Drop " + heldItem.getName());
        }
    }

    /**
     * Checks to see if the player is in-fact directly within one-tile space of the
     * object the player wishes to carry out the action upon.
     * @param row
     * @param col
     * @return
     */
    public boolean actionsCheck(int row, int col) {
        if(row < 0 || row >= room.getHeight() || col < 0 || col >= room.getWidth()) { return false; }
        if(room.getTile(row, col) == null) { return false; }
        if(room.getTile(row, col).getDisplayType().equals("black")) { return false; }
        return true;
    }

    /**
     * Checks to see if the player can move within the specified direction. Should the
     * player be able to occupy the given tile, the player is moved and all corresponding
     * references are updated.
     * @param direction
     */
    public void move(Direction direction) {
        this.direction = direction;

        int row = getRowFacing(direction);
        int col = getColFacing(direction);

        if(canMove(row, col)) {
            Tile nextTile = room.getTile(row, col);
            tile.setEntity(null);
            tile = nextTile;
            tile.setEntity(this);
        }
    }

    /**
     * Checks to see if the player can in fact move into any given tile, should
     * it be unreserved by any other entity.
     * @param row
     * @param col
     * @return
     */
    private boolean canMove(int row, int col) {
        if(row < 0 || row >= room.getHeight() || col < 0 || col >= room.getWidth()) { return false; }
        if(room.getTile(row, col) == null) { return false; }
        if(room.getTile(row, col).getEntity() != null) { return false; }
        if(room.getTile(row, col).getDisplayType().equals("black")) { return false; }
        if(room.getTile(row, col).getHeight() > tile.getHeight()) { return false; }
        return true;
    }

    /**
     * Drop an item in the next viable tile, based on the direction that the player is
     * current facing.
     */
    public void dropItem() {
        int row = getRowFacing(direction);
        int col = getColFacing(direction);

        if (canDrop(row, col)) {
            Tile tileInfront = room.getTile(row, col);

            tileInfront.setEntity(heldItem);
            this.heldItem = null;
            consumeItem(holdIndex);
        }
    }

    /**
     * Checks to see if the item can be dropped or not, based on whether
     * the closest tile within the direction is reserved by another entity or not.
     * @param row
     * @param col
     * @return
     */
    private boolean canDrop(int row, int col) {
        if(row < 0 || row >= room.getHeight() || col < 0 || col >= room.getWidth()) { return false; }
        if(room.getTile(row, col) == null) { return false; }
        if(room.getTile(row, col).getEntity() != null) { return false; }
        if(room.getTile(row, col).getDisplayType().equals("black")) { return false; }
        if(heldItem == null) { return false; }
        return true;
    }

    /**
     * Allows the player to interact with the entity that the player is currently facing towards,
     * must be within one-tile-space for the interaction to occur.
     */
    public void interact() {
        int row = getRowFacing(direction);
        int col = getColFacing(direction);

        if(canInteract(row, col)) {
            Tile tileInfront = room.getTile(row, col);
            manageInteraction(tileInfront, tileInfront.getEntity());
        }
    }

    /**
     * Checks to see if the one-tile-space condition is met for player-entity interactions
     * to be carried out.
     * @param row
     * @param col
     * @return
     */
    private boolean canInteract(int row, int col) {
        if(row < 0 || row >= room.getHeight() || col < 0 || col >= room.getWidth()) { return false; }
        if(room.getTile(row, col) == null) { return false; }
        if(room.getTile(row, col).getEntity() == null) { return false; }
        return true;
    }

    /**
     * Manages the interaction based on the all of the different
     * types of entities the player could potentially ever interact
     * with.
     * @param tileInfront
     * @param entity
     */
    private void manageInteraction(Tile tileInfront, Entity entity) {
        if(entity instanceof Item) {
            // Picking item up
            Item item = (Item) entity;
            if(!isInvenFull()) {
                item.interact(this);

                if (!(item instanceof GemStone)) {
                    tileInfront.setEntity(null);
                }
            }
        }else if(entity instanceof Transport) {
            Transport transport = (Transport) entity;
            transport.interact(game, this, getRowFacing(direction), getColFacing(direction));
        }else if(entity instanceof Container) {
            Container container = (Container) entity;
            container.open(game, this);
        }else if(entity instanceof Trader) {
            Trader trader = (Trader) entity;
            trader.trade(game, this);
        }else if(entity instanceof Mob) {
            Mob mob = (Mob) entity;
            mob.attack(game, this);
        }
    }

    /**
     * Selects an item within the players inventory.
     * @param item
     */
    public void selectItem(int item) {
        if(inventory.get(item) == null) {
            heldItem = null;
        }else {
            heldItem = inventory.get(item);
        }
    }

    /**
     * Allows the player to use any given selected item within
     * the player's own inventory.
     */
    public void useItem() {
        if(heldItem == null) { return; }

        if(heldItem instanceof Carry) {
            // Holdable container
        }else {
            int rowFacing = getRowFacing(direction);
            int colFacing = getColFacing(direction);

            heldItem.use(game, this, holdIndex, rowFacing, colFacing);
        }
    }

    /**
     * Returns a reference to the row that the player is currently facing.
     * This is NOT a reference to the current row, but a reference to the
     * row next to the player's position.
     * @param direction
     * @return
     */
    public int getRowFacing(Direction direction) {
        if(direction.equals(NORTH)) {
            return tile.getRow() - 1;
        }else if(direction.equals(SOUTH)) {
            return tile.getRow() + 1;
        }else if(direction.equals(WEST)) {
            return tile.getRow();
        }else if(direction.equals(EAST)) {
            return tile.getRow();
        }
        return tile.getRow();
    }

    /**
     * Returns a reference to the column that the player is currently facing.
     * This is NOT a reference to the current column, but a reference to the
     * column next to the player's position.
     * @param direction
     * @return
     */
    public int getColFacing(Direction direction) {
        if(direction.equals(NORTH)) {
            return tile.getCol();
        }else if(direction.equals(SOUTH)) {
            return tile.getCol();
        }else if(direction.equals(WEST)) {
            return tile.getCol() - 1;
        }else if(direction.equals(EAST)) {
            return tile.getCol() + 1;
        }
        return tile.getCol();
    }

    /**
     * Returns a reference to the current game being played.
     * @return
     */
    public Game getGame() {
        return game;
    }

    /**
     * Returns a reference to the player's current direction.
     * @return
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Gain a reference to the player's current room.
     * @return
     */
    public Room getRoom() {
        return room;
    }

    /**
     * Returns a reference to the currently held item by the player.
     * @return
     */
    public Item getHeldItem() {
        return heldItem;
    }

    /**
     * Sets a held item for the player based on inventory index.
     * @param index
     */
    public void setHeldItem(int index) {
        this.heldItem = inventory.get(index);
        System.out.println(heldItem.getName());
        holdIndex = index;
    }

    /**
     * Sets a held item, based on an item present within the inventory.
     * @param heldItem
     */
    public void setHeldItem(Item heldItem) {
        this.heldItem = heldItem;
    }

    /**
     * Represents the held item index within the inventory.
     * @param holdIndex
     */
    public void setHoldIndex(Integer holdIndex) {
        this.holdIndex = holdIndex;
    }

    /**
     * Returns a list view of the player's inventory.
     * @return
     */
    public List<Item> getInventory() {
        return inventory;
    }

    /**
     * Checks to see if the inventory of the player is
     * full, or if more items can still be added.
     * @return
     */
    public boolean isInvenFull() {
        int cap = 6;
        return inventory.size() == cap;
    }

    /**
     * Checks to see if a item is currently present within
     * the player's inventory.
     * @param wantedItem
     * @return
     */
    public boolean hasItem(String wantedItem) {
        wantedItem = wantedItem.toLowerCase();
        for(Item item : inventory) {
            String itemName = item.getName();
            if(wantedItem.equals(itemName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds an item to the player's inventory.
     * @param item
     */
    public void addInventory(Item item) {
        this.inventory.add(item);
    }

    /**
     * Consume's consumable items such as potions, and
     * updates all player references throughout the process.
     * @param index
     */
    public void consumeItem(int index) {
        this.inventory.remove(index);
        this.heldItem = null;
    }

    /**
     * Returns a current reference to the player's current tile.
     * @return
     */
    public Tile getTile() {
        return tile;
    }

    /**
     * Allows the player to change rooms. And update the room
     * reference based on the room the player is currently within.
     * @param room
     */
    public void setRoom(Room room) {
        this.room = room;
    }

    public void setGame(Game g) {
        this.game = g;
    }

    /**
     * Used to update tile references for the player within the game.
     * @param tile
     */
    public void setTile(Tile tile) {
        this.tile = tile;
    }

    /**
     * Returns the health of the player/
     * @return
     */
    public int getHealth() {
        return this.health;
    }

    /**
     * Allows health to be manipulated by consumable items, such
     * as potions and armour.
     * @param health
     */
    public void setHealth(int health) {
        this.health = health;
        if(this.health > 10) {
            this.health = 10;
        }
    }

    /**
     * Returns player's current row tile reference.
     * @return
     */
    public int getRow() {
    	return tile.getRow();
    }

    /**
     * Returns a player's current column tile reference.
     * @return
     */
    public int getCol() {
    	return tile.getCol();
    }

    /**
     * Sets the direction the player is currently facing.
     * @param direction
     */
	public void setDirection(Direction direction) {
		this.direction = direction;
	}

    /**
     * Updates the interaction method of the player. A required
     * method inherited from the mob class.
     * @return
     */
    @Override
    public String getInteractMessage() {
        return "";
    }

    public Integer getHoldIndex() {
        return holdIndex;
    }
}
