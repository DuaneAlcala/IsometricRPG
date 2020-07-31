package world.commands;
import renderer.Renderer;

/**
 * The Drop-Command implements the CommandPatern-interface,
 * and executes all of the required actions when users
 * choose to drop any selected in-game item, updating the
 * game accordingly.
 */
public class DropCommand implements CommandPattern {
    /**
     * The following renderer reference is used to directly
     * update the GUI and output (visually) the executed
     * action.
     */
    private Renderer renderer;

    /**
     * Constructor to create a new DropCommand
     * @param renderer
     */
    public DropCommand(Renderer renderer) {
        this.renderer = renderer;
    }

    /**
     * This method is tasked with the responsibility of updating
     * the corresponding button-values within the GUI, based
     * on the positional orientation of the player.
     *
     * @param message
     */
    @Override
    public void execute(String message) {
        renderer.changeDropMessage(message);
    }
}
