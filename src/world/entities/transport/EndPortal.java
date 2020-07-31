package world.entities.transport;
import world.Game;
import world.Room;
import world.Tile;
import world.entities.mobs.Player;

/**
 * Allows the users to end and win the game, by interacting with the
 * an EndPortal object.
 */
public class EndPortal extends Transport{

    public boolean activated;

    /**
     * Creates a new EndPortal object (unactivated by default)
     */
    public EndPortal() {
        super("endportal", "Unlock this with a gemstone");
        activated = false;
    }

    /**
     * Allows you to create a new unlocked EndPortal within the game as well.
     * @param activated
     */
    public EndPortal(boolean activated) {
        super("endportal", "Unlock this with a gemstone");
        this.activated = activated;
    }

    /**
     * Allows for interactions to occur between player, EndPortals within the
     * current game. A method required by the Transport class.
     * @param game
     * @param player
     */
    @Override
    public void interact(Game game, Player player) { }

    /**
     * Manages the interaction between the player and EndPortals.
     * Outputs a message indicating that the player has won the game.
     * @param game
     * @param player
     * @param rowFacing
     * @param colFacing
     */
    @Override
    public void interact(Game game, Player player, int rowFacing, int colFacing) {
        if(!activated) {
            return;
        }

        Room nextRoom = game.getWorld().getRoom(roomX, roomY);
        Tile tile = nextRoom.getTile(tileX, tileY);
        text.execute("You won the game.");
    }

    /**
     * Allows and relays information to the player to end the game by exiting through
     * the dungeon, through the use of an activated EndPortal.
     * @param activated
     */
    public void setActivated(boolean activated) {
        this.activated = activated;
        if(this.activated) {
            this.interactMessage = "Exit dungeon";
            this.description = "Use this end portal to end the game";
        }
    }

    /**
     * Checks and returns whether or not the current EndPortal has bene activated
     * or not.
     * @return
     */
    public boolean isActivated() {
        return activated;
    }
}
