package world.entities.mobs;

import utility.Direction;
import world.Game;
import world.Room;
import world.Tile;
import world.entities.item.Item;
import world.entities.item.Key;

import static utility.Direction.*;

public class Skeleton extends Mob {
    /**
     * An item which the skeleton may sometimes drop, allowing
     * the player to open doors to new rooms.
     */
    private Item item = new Key();

    /**
     * Constructor for a new skeleton object.
     */
    public Skeleton() {
        super("skeleton", "An enemy");
        this.interactMessage = "Attack " + this.name;
    }

    /**
     * Constructor used by the persistence package
     * @param health
     */
    public Skeleton(int health) {
        super("skeleton", "An enemy");
        this.health = health;
    }

    /**
     * Moves the skeleton into the next tile, based on the current
     * direction the skeleton is facing. This takes into account a
     * number of different conditions to ensure that the skeleton is
     * always moving (whenever the player chooses to move as well).
     * @param room
     * @param tile
     */
    @Override
    public void move(Room room, Tile tile) {
        int direction = (int) (Math.random() * 4);

        int row = tile.getRow();
        int col = tile.getCol();

        if(direction == 0) {
            if((row - 1) >= 0) {
                Tile nextTile = room.getTile(row - 1, col);
                if(checkTile(tile, nextTile)) {
                    tile.setEntity(null);
                    nextTile.setEntity(this);
                }
            }
        }else if(direction == 1) {
            if((row  + 1) < room.getWidth()) {
                Tile nextTile = room.getTile(row + 1, col);
                if(checkTile(tile, nextTile)) {
                    tile.setEntity(null);
                    nextTile.setEntity(this);
                }
            }
        }else if(direction == 2) {
            if((col - 1) >= 0) {
                Tile nextTile = room.getTile(row, col - 1);
                if(checkTile(tile, nextTile)) {
                    tile.setEntity(null);
                    nextTile.setEntity(this);
                }
            }
        }else if(direction == 4) {
            if((col + 1) < room.getHeight()) {
                Tile nextTile = room.getTile(row, col + 1);
                if(checkTile(tile, nextTile)) {
                    tile.setEntity(null);
                    nextTile.setEntity(this);
                }
            }
        }
    }

    /**
     * Is called whenever the player chooses to attack a skeleton object.
     * Allows skeleton objects to attack the players back as well.
     * @param game
     * @param player
     */
    public void attack(Game game, Player player) {
        text.execute("The skeleton hits you for 1 health");
        player.setHealth(player.getHealth() - 1);
        if(player.hasItem("sword")) {
            this.health -= 5;
            text.execute("You slash the skeleton for 5 health");
        }else {
            this.health -= 2;
            text.execute("You hit the skeleton for 2 health");
        }
        if(this.health <= 0) {
            getThisTile(player.getRoom(), player, player.getDirection()).setEntity(null);
            getThisTile(player.getRoom(), player, player.getDirection()).setEntity(item);

        }
    }

    /**
     * Returns the next tile in the direction that the player is currently facing.
     * @param room
     * @param player
     * @param direction
     * @return
     */
    private Tile getThisTile(Room room, Player player, Direction direction) {
        if(direction.equals(NORTH)) {
            return room.getTile(player.getRow() - 1, player.getCol());
        }else if(direction.equals(SOUTH)) {
            return room.getTile(player.getRow() + 1, player.getCol());
        }else if(direction.equals(WEST)) {
            return room.getTile(player.getRow(), player.getCol() - 1);
        }else if(direction.equals(EAST)) {
            return room.getTile(player.getRow(), player.getCol() + 1);
        }
        return null;
    }

    /**
     * Checks to see if the next tile is in-fact unreserved and can be moved
     * in to or not.
     * @param currentTile
     * @param nextTile
     * @return
     */
    private boolean checkTile(Tile currentTile, Tile nextTile) {
        if(nextTile == null) { return false; }
        if(nextTile.getEntity() != null) { return false; }
        if(nextTile.getHeight() != currentTile.getHeight()) { return false; }
        return true;
    }

    /**
     * Returns the interaction message of the player based on the
     * events which occurred between both player and skeleton.
     * @return
     */
    @Override
    public String getInteractMessage() {
        return interactMessage;
    }

    public Item getItem() {
        return item;
    }

    public int getHealth() {
        return health;
    }
}
