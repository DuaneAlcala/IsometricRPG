package world.entities.item;
import gui.BookDialog;
import utility.BookMaker;
import world.Game;
import world.Tile;
import world.entities.mobs.Player;

/**
 * Book items may contain text and or clues to help the user to complete the game.
 */
public class Book extends Item {
    // Field stores the content of the book.
    private String contents = "";

    /**
     * A constructor for an empty (unfilled) book object
     */
    public Book() {
        super("book", "a book");
        this.contents = generateContent();
    }

    /**
     * Constructor for a new book object containing text.
     * @param contents
     */
    public Book(String contents) {
        super("book", "a book");
        this.contents = generateContent();
    }

    /**
     *  Allows a number of books to be filled with predefined content,
     *  which can then be scattered throughout the game.
     * @return random content to fill a book.
     */
    private static String generateContent() {
        BookMaker bookMaker = new BookMaker();
        return bookMaker.getRandContent();
    }

    /**
     * Allows the player to open the book, and therefore read the contents that
     * is stored within it.
     * @param game
     * @param player
     * @param itemIndex
     * @param rowFacing
     * @param colFacing
     */
    @Override
    public void use(Game game, Player player, int itemIndex, int rowFacing, int colFacing) {
        new BookDialog(game, contents);
        this.useMessage = "Open book";
    }

    /**
     * Updates the corresponding in-game buttons to reflect that the item has been
     * selected and can be used by the player.
     * @param tile
     * @return
     */
    @Override
    public String getUseMessage(Tile tile) {
        return useMessage;
    }

    public String getContents() {
        return contents;
    }
}
