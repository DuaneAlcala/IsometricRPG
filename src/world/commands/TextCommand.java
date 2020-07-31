package world.commands;

import renderer.Renderer;

public class TextCommand implements CommandPattern {
    /**
     * The following renderer reference is used to directly
     * update the GUI and output (visually) the executed
     * action.
     */
    private Renderer renderer;

    /**
     * Constructor for a new TextCommand
     *
     * @param renderer
     */
    public TextCommand(Renderer renderer) {
        this.renderer = renderer;
    }

    /**
     * The following method is tasked with the responsibility of updating
     * the corresponding button-values within the GUI, based
     * on the positional orientation of the player.
     *
     * @param message
     */
    @Override
    public void execute(String message) {
       renderer.addMessage(message);
    }
}
