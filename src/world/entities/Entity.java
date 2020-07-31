package world.entities;
import renderer.Renderer;

/**
 * The entity class is the building-block which is
 * used for all interact-able items throughout the game.
 * Essentially this class provides the base layer of all
 * required fields for the game state to be effectively saved
 * and reloaded into another world.
 */
public abstract class Entity {

    protected String name;
    protected String description;
    //The renderer used to visually output each of the different game components.
    protected static Renderer renderer;

    protected String interactMessage = "";

    /**
     * Constructor which sets all base-field information allowing
     * for the game and its components to be serialisable by
     * the persistence package.
     * @param name
     * @param description
     */
    public Entity(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Returns a reference to the renderer used to render
     * the different game components.
     * @return
     */
    public static Renderer getRenderer() {
        return renderer;
    }

    /**
     * Changes the renderer for the different components involved
     * within the game. This is used when loading up different
     * rooms and or levels throughout the persistence package.
     * @param renderer
     */
    public static void setRenderer(Renderer renderer){
        Entity.renderer = renderer;
    }

    /**
     * A method stub which must be implemented by all inheriting
     * sub-classes.
     * @return
     */
    public abstract String getInteractMessage();

    /**
     * Returns object name.
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Returns object description.
     * @return
     */
    public String getDescription() {
        return description;
    }
}
