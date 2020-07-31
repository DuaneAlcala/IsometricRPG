package world.commands;
import renderer.Renderer;

/**
 * The Use-Command implements the CommandPatern-interface,
 * and executes all of the required actions when users
 * choose to use the currently 'held' (or selected)
 * item from within the inventory, updating the
 * game accordingly.
 */
public class UseCommand implements CommandPattern {
    /**
     * The following renderer reference is used to directly
     * update the GUI and output (visually) the executed
     * action.
     */
    private Renderer renderer;

    /**
     * Constructor
     *
     * @param renderer
     */
    public UseCommand(Renderer renderer) {
        this.renderer = renderer;
    }

    /**
     * The following method is tasked with the responsibility of updating
     * the corresponding button-values within the GUI, based
     * on the positional orientation of the player.
     */
    @Override
    public void execute(String message) {
        renderer.changeUseMessage(message);
    }
}
