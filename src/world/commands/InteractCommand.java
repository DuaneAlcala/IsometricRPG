package world.commands;
import renderer.Renderer;

public class InteractCommand implements CommandPattern {
    /**
     * The following renderer reference is used to directly
     * update the GUI and output (visually) the executed
     * action.
     */
    private Renderer renderer;

    /**
     * Constructor for a new InteractCommand
     * @param renderer
     */
    public InteractCommand(Renderer renderer) {
        this.renderer = renderer;
    }

    /**
     * The following method is tasked with the responsibility of updating
     * the corresponding button-text values based on specified conditions for
     * when these actions are viable in-game.
     * @param message
     */
    @Override
    public void execute(String message) {
        renderer.changeInteractMessage(message);
    }
}
