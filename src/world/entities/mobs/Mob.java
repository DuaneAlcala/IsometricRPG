package world.entities.mobs;
import world.Game;
import world.Room;
import world.Tile;
import world.commands.TextCommand;
import world.entities.Entity;

/**
 * Mobility class serves as basis for all in-game items
 * which are able to move.
 */
public abstract class Mob extends Entity {
    /**
     * Fields used to track mobile object health
     * and a reference to the type of object that
     * they are.
     */
    protected TextCommand text;
    protected int health;

    /**
     * Constructs a new mobile in-game objects.
     * @param name
     * @param description
     */
    public Mob(String name, String description) {
        super(name, description);
        this.text = new TextCommand(Entity.getRenderer());

        this.health = 10;
    }

    /**
     * A movement method which defines how the mobile object
     * will move, which must be implemented by all inheriting
     * classes.
     * @param room
     * @param tile
     */
    public void move(Room room, Tile tile) {

    }

    /**
     * Should a mobile object be attacked, they should
     * have the capability to attack back - must also
     * be implemented by all inheriting classes.
     * @param game
     * @param player
     */
    public void attack(Game game, Player player) {

    }

}
