package world;
import gui.GUI;
import utility.Direction;
import world.entities.Entity;
import world.entities.containers.Chest;
import world.entities.item.*;
import world.entities.mobs.Mob;
import world.entities.mobs.Player;
import world.entities.mobs.Skeleton;
import world.entities.mobs.Trader;
import world.entities.transport.Door;
import world.entities.transport.EndPortal;
import world.entities.transport.Portal;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;

import static utility.Direction.*;

/**
 * Program Entry Point which also defines the
 * current state of the program at any given
 * point in time. Through this class the entirety
 * of the game can be serialised and saved through the
 * use of the Persistence package.
 */
public class Game {

    private GUI gui;
    private Player player;
    private Room currentRoom;
    private World world;

    private int currRoomX;
    private int currRoomY;

    public boolean isTesting = false;

    /**
     * Represents the potential number of moves and time before the
     * torchlight goes out.
     */
    private int torchLight = 1000;

    /**
     * Constructs a new game world, setting up the player and all required references
     * as required for the functioning of the game.
     */
    public Game() {
        world = new World();
        //world = Persistance.load(new File("mapEditor.xml"));
        player = new Player(this);
        currentRoom = world.getRoom(0, 0);

        currentRoom.getTile(0,0).setEntity(player);
        player.setRoom(currentRoom);
        player.setTile(currentRoom.getTile(0, 0));

        addItems();
    }

    /**
     * Constructor used for creating new games based on the persistence package.
     * @param player2
     * @param playerRoom
     * @param world2
     * @param torchLight
     */
    public Game(Player player2, Room playerRoom, World world2, int torchLight) {
        this.player = player2;
        this.currentRoom = playerRoom;
        this.world = world2;
        this.torchLight = torchLight;
    }

    /**
     * Used to instantiate and link the provided GUI instance with
     * this game.
     * @param gui
     */
    public void setGui(GUI gui) {
        this.gui = gui;
    }

    /**
     * Adds the specified and required items into the opening room.
     */
    public void addItems() {
        currentRoom.getTile(0, 1).setEntity(new Book());
        currentRoom.getTile(0, 2).setEntity(new Key());
        currentRoom.getTile(0, 5).setEntity(new Lamp());
        currentRoom.getTile(3, 5).setEntity(new Book("I want this team to lower me at my funeral, so they can let me down one last time - Micheal Saily\n" ));
        currentRoom.getTile(7, 8).setEntity(new Potion());
        currentRoom.getTile(10, 13).setEntity(new Chest());
        currentRoom.getTile(0, 10).setEntity(new Map());

        currentRoom.getTile(9, 9).setEntity(new Door(0,0,11, 11));
        currentRoom.getTile(10, 6).setEntity(new Portal(0, 1, 15, 15));

        currentRoom.getTile(0, 3).setEntity(new Coin());
        currentRoom.getTile(0, 8).setEntity(new Trader());

        currentRoom.getTile(2, 8).setEntity(new Skeleton());

        currentRoom.getTile(0, 4).setEntity(new Spade());
        currentRoom.getTile(0, 8).setEntity(new GemStone());

        currentRoom.getTile(2, 9).setEntity(new EndPortal());

        currentRoom.getTile(2, 11).setEntity(new Trader());

        world.getRoom(0, 1).setDark(false);
    }

    /**
     * Allows for player direction references to be updated within the game.
     * @param direction
     */
    public void movePlayer(Direction direction) {
    	 player.setDirection(direction);
    	 player.move(direction);

    	 updateMobs();
    }

    /**
     * Update all other mobile objects based on their respective move methods.
     * This includes the movement strategies used by the skeleton objects
     * within the game.
     */
    public void updateMobs() {
        for(int x = 0; x < currentRoom.getWidth(); x++) {
            for(int y = 0; y < currentRoom.getHeight(); y++) {
                if(currentRoom.getTile(x, y) != null && currentRoom.getTile(x, y).getEntity() != null) {
                    Entity entity = currentRoom.getTile(x, y).getEntity();
                    if(entity instanceof Mob) {
                        Mob mob = (Mob) entity;
                        mob.move(currentRoom, currentRoom.getTile(x, y));
                    }
                }
            }
        }
    }

    /**
     * Return a reference to the current room.
     * @return
     */
    public int getCurrRoomX() {
        return currRoomX;
    }

    /**
     * Return a reference to the current room.
     * @return
     */
    public int getCurrRoomY() {
        return currRoomY;
    }

    /**
     * Used to simulate the passing of time over a given
     * period of time and number of moves.
     */
    public void subtractTorch() {
        torchLight--;
    }

    /**
     * Returns the count remaining on the torchlight.
     * @return
     */
    public int getTorchLight() {
        return torchLight;
    }

    /**
     * Allows for players to drop items
     */
    public void dropItem() {
        player.dropItem();
    }

    /**
     * Allows for players to be transported through different rooms within the game,
     * based on their interaction with Transport objects.
     * @param player
     * @param room
     * @param tile
     */
    public void transportPlayer(Player player, Room room, Tile tile) {
        player.getTile().setEntity(null);
        torchLight = 100;
        tile.setEntity(player);
        player.setTile(tile);

        currentRoom = room;

        player.setRoom(currentRoom);
    }

    /**
     * Allows for the user to select items contained within their inventory
     * space. The selected item is then shifted into the 'HeldItem' reference
     * as well.
     * @param place
     */
    public void selectInventory(int place) {
        if(place < player.getInventory().size()) {
            player.setHeldItem(place);
            System.out.println("HOLDING: " + player.getHeldItem().getName());
        }else {
            player.setHeldItem(null);
        }
    }

    /**
     * Should a game be loaded in through the use of the load function,
     * the world reference is changed to the provided world reference
     * instead. This world reference is provided and gained through the
     * use of the Persistence package.
     * @param world
     */
    public void setWorld(World world) {
        this.world = world;
    }

    /**
     * Return the current instance of this game's world.
     * @return
     */
    public World getWorld() {
        return world;
    }

    /**
     * Returns the current instance of this game's GUI.
     * @return
     */
    public GUI getGui() {
        return gui;
    }

    /**
     * Returns a reference to the current room of the player.
     * @return
     */
    public Room getCurrentRoom() {
        return currentRoom;
    }

    /**
     * Update the current room reference of the player, every
     * time the player is transported to another room.
     * @param currentRoom
     */
    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }

    /**
     * Returns a direct reference to the player.
     * @return
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Returns a counter-clockwise rotation of the current room,
     * for when the player may want to switch viewing perspectives.
     */
	public void rotateCounterClockwise() {
		currentRoom.rotateCounterClockwise();
	}

    /**
     * Returns a clockwise rotation of the current room,
     * for when the player may want to switch viewing perspectives.
     */
	public void rotateClockwise() {
		currentRoom.rotateClockwise();
	}

    /**
     *
     */
	public void interaction() {
		player.interact();
	}
}
