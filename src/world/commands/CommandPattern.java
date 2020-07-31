package world.commands;

/**
 * The game uses a command-pattern to handle all user-driven
 * events which occur throughout the game. The command pattern
 * interface is responsible for relaying the required
 * command based on the user-input provided and invoked through the
 * GUI (updating all corresponding components of the game) that
 * interact with the specified pattern.
 */
public interface CommandPattern {
    /**
     * This execute is used to invoke the required in-game response
     * for any given action.
     * @param message
     */
    public void execute(String message);
}
