package gui;

import world.Game;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**This class opens a simple JFrame that displays the text of a book in the game
 * 
 * @author sailymahe
 */
public class BookDialog extends JDialog {

    private JPanel bookPanel;

    private JButton closeButton;

    private int panelWidth = 250;
    private int panelHeight = 250;

    /**This constructor makes a new JFrame that has only a close button and displays the string supplied
     * 
     * @param game
     * @param contents
     */
    public BookDialog(Game game, String contents) {
        super(game.getGui().getFrame(), "Book", false);

        JPanel bookPanel = new JPanel();
        bookPanel.setPreferredSize(new Dimension(panelWidth, panelHeight));
        bookPanel.setSize(new Dimension(panelWidth, panelHeight));
        bookPanel.setLayout(new BoxLayout(bookPanel, BoxLayout.Y_AXIS));

        closeButton = new JButton("Close");
        closeButton.addActionListener(e -> this.dispose());
        closeButton.setAlignmentX(Window.CENTER_ALIGNMENT);

        //The following code gets a random string from the book file
        List<String> bookStrings = new ArrayList<>();
        bookStrings = Arrays.asList(contents.split(" "));

        String done = "<html><center>";
        for(int i = 0, j = 0; i < bookStrings.size(); i++) {
            done += bookStrings.get(i) + " ";
            j++;
            if(j == 3) {
                j = 0;
                done += "<br/>";
            }
        }
        done += "</center></html>";

        JLabel descLabel = new JLabel(done);
        JPanel descPanel = new JPanel();
        descPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        descPanel.add(descLabel);

        bookPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        bookPanel.add(descPanel);
        bookPanel.add(closeButton);

        add(bookPanel);
        pack();
        setLocationRelativeTo(game.getGui().getFrame());
        setVisible(true);
    }
}
