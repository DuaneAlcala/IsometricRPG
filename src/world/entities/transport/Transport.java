package world.entities.transport;
import world.Game;
import world.commands.TextCommand;
import world.entities.Entity;
import world.entities.mobs.Player;

/**
 * The Transport class serves as the underlying foundation of
 * all transportable items, which allow the player to be moved
 * throughout multiple rooms.
 */
public abstract class Transport extends Entity {

    protected int roomX; //Represents the first room where the transport item is contained
    protected int roomY; //The target room the player must be transported to
    protected int tileX; //The x-position the transport object
    protected int tileY; //The y-position the transport object
    protected TextCommand text;


    /**
     * Constructs a new Transport object type.
     * Updating and setting its interaction message as required.
     * @param name
     * @param description
     */
    public Transport(String name, String description) {
        super(name, description);
        this.text = new TextCommand(Entity.getRenderer());

        this.interactMessage = "";
    }

    /**
     * Method stub inherited from the Entity class.
     * @param game
     * @param player
     */
    public abstract void interact(Game game, Player player);

    /**
     * Method stub which must be implemented by all Transport-subclasses.
     * @param game
     * @param player
     * @param rowFacing
     * @param colFacing
     */
    public abstract void interact(Game game, Player player, int rowFacing, int colFacing);

    /**
     * Returns the tile y-position of the Transport object.
     * @return
     */
    public int getTileY() {
        return tileY;
    }

    /**
     * Retyrbs the x-position of the Transport object.
     * @return
     */
    public int getTileX() {
        return tileX;
    }

    /**
     * Used to return a reference to the room of the Transport object.
     * @return
     */
    public int getRoomY() {
        return roomY;
    }

    /**
     * Used to return a reference to the room of the Transport object.
     * @return
     */
    public int getRoomX() {
        return roomX;
    }

    /**
     * Method stub required by Entity class.
     * @return
     */
    @Override
    public String getInteractMessage() {
        return interactMessage;
    }
}
