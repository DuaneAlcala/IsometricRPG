package gui;

import world.Game;
import world.entities.item.Carry;
import world.entities.item.Item;
import world.entities.mobs.Player;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**This class makes a new JFrame that allows items to be transferred between the player and a carry item
 * 
 * @author sailymahe
 *
 */
public class CarryDialog extends JDialog {

    private static boolean testing = false;

    private JButton addButton;
    private JButton getButton;

    private int panelWidth = 250;
    private int panelHeight = 250;

    private Game game;
    private Player player;
    private Carry carry;  //the carry that can store items and have them transferred to and from

    private JPanel buttonPanel;
    private JPanel addPanel;
    private JPanel getPanel;

    private JComboBox<String> playerItems = new JComboBox<>();
    private JComboBox<String> containerItems = new JComboBox<>();

    private JButton confirmAdd;
    private JButton confirmGet;

    /**Creates a new CarryDialog by setting up JFrame and buttons and storing arguments in fields
     * 
     * @param game		reference to current game
     * @param player	player interacting with carry
     * @param carry		carry interacting with plaer
     */
    public CarryDialog(Game game, Player player, Carry carry) {
        super(game.getGui().getFrame(), "Container", false);

        this.game = game;
        this.player = player;
        this.carry = carry;

        buttonPanel = new JPanel();
        buttonPanel.setPreferredSize(new Dimension(panelWidth, panelHeight));
        buttonPanel.setSize(new Dimension(panelWidth, panelHeight));
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        addButton = new JButton("Add");
        getButton = new JButton("Get");
        addButton.addActionListener(e -> adding());
        getButton.addActionListener(e -> getting());
        addButton.setAlignmentX(Window.CENTER_ALIGNMENT);
        getButton.setAlignmentX(Window.CENTER_ALIGNMENT);

        buttonPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        buttonPanel.add(addButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        buttonPanel.add(getButton);

        this.setPreferredSize(new Dimension(panelWidth, panelHeight));
        this.setSize(new Dimension(panelWidth, panelHeight));

        // Adding items that aren't of type carry so that you can't add a bag to bag
        List<Item> itemCheck = new ArrayList<>();
        for(Item item : player.getInventory()) {
            if(!(item instanceof Carry)) {
                itemCheck.add(item);
            }
        }
        //Don't allow item of type carry to be added or if carry capacity is full dont allow items to be added
        if(itemCheck.isEmpty() || carry.getItems().size() == carry.getCap() ) {                                                  
        	addButton.setEnabled(false); 
        }

        //Don't allow items to be retrieved if
        if(this.carry.getItems().isEmpty() || player.isInvenFull()) { getButton.setEnabled(false); }

        add(buttonPanel);
        pack();
        setLocationRelativeTo(game.getGui().getFrame());
        setVisible(true);
    }

    /**
     * Called by add button on JFrame
     * 
     * This method will add the selected item in players inventory into the carry object
     */
    public void adding() {
        List<Item> inven = player.getInventory();
        for(Item item: inven) {
            if(item instanceof Carry) { continue; }
            String itemName = item.getName();
            playerItems.addItem(itemName);
        }
        playerItems.setSelectedIndex(0);

        confirmAdd = new JButton("Confirm");
        confirmAdd.addActionListener(e -> {
            carry.addItem(inven.get(playerItems.getSelectedIndex()));
            player.consumeItem(playerItems.getSelectedIndex());
            this.dispose();
        });

        addPanel = new JPanel();
        addPanel.setLayout(new BoxLayout(addPanel, BoxLayout.Y_AXIS));

        addPanel.add(playerItems);
        addPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        addPanel.add(confirmAdd);

        buttonPanel.setVisible(false);
        add(addPanel);
        pack();
    }

    /**
     * Called by get button on frame
     * 
     * this method will take the selected item in carry and move it to the players inventory
     */
    public void getting() {
        List<Item> items = carry.getItems();
        for(Item item : items) {
            String itemName = item.getName();
            containerItems.addItem(itemName);
        }
        containerItems.setSelectedIndex(0);

        confirmGet = new JButton("Confirm");
        confirmGet.addActionListener(e -> {
            player.addInventory(items.get(containerItems.getSelectedIndex()));
            carry.removeItem(containerItems.getSelectedIndex());
            this.dispose();
        });

        getPanel = new JPanel();
        getPanel.setLayout(new BoxLayout(getPanel, BoxLayout.Y_AXIS));

        getPanel.add(containerItems);
        getPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        getPanel.add(confirmGet);

        buttonPanel.setVisible(false);
        add(getPanel);
        pack();
    }
}
