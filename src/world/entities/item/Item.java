package world.entities.item;

import renderer.Renderer;
import world.Game;
import world.Tile;
import world.commands.TextCommand;
import world.entities.Entity;
import world.entities.mobs.Player;

public abstract class Item extends Entity {
    /**
     * Fields used to contain use messages,
     * amd any other item-relevant information.
     */
    protected TextCommand text;
    protected String useMessage = "";

    /**
     * Constructor for a new item object, requiring both a name and a
     * item description.
     * @param name
     * @param description
     */
    public Item(String name, String description) {
        super(name, description);
        text = new TextCommand(Entity.getRenderer());

        this.interactMessage = "Pick up " + this.name;
    }

    /**
     * Allows players to interact with the item, once it has been
     * placed within their inventory.
     * @param player
     */
    public void interact(Player player) {
        player.addInventory(this);
        text.execute("You add a " + this.getName() + " to your inventory");
    }

    /**
     * Returns the interaction method for any selected item within the
     * players inventory. Updates in-game button references to reflect this.
     * @return
     */
    public String getInteractMessage() {
        return interactMessage;
    }

    /**
     * Returns the interaction method for any selected item within the
     * players inventory. Updates in-game button references to reflect this,
     * based on the tile provided to this method.
     * @return
     */
    public abstract String getUseMessage(Tile tile);

    /**
     * Uses the item and does necessary interactions
     *
     * @param game
     * @param player
     * @param itemIndex
     * @param rowFacing
     * @param colFacing
     */
    public abstract void use(Game game, Player player, int itemIndex, int rowFacing, int colFacing);

}
