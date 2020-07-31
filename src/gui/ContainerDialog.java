package gui;

import world.Game;
import world.entities.containers.Container;
import world.entities.item.Item;
import world.entities.mobs.Player;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**This class is created to handle the interaction between the player and a container, allowing them to store and retrieve items
 * This class handles the GUI for this interaction
 *
 * @author sailymahe
 */
public class ContainerDialog extends JDialog {

    public static boolean testing = false;

    private JButton addButoon;
    private JButton getButton;

    private int panelWidth = 250;
    private int panelHeight = 250;

    private Game game;
    private Player player;
    private Container container;

    private JPanel buttonPanel;
    private JPanel addPanel;
    private JPanel getPanel;

    private JComboBox<String> playerItems = new JComboBox<>();
    private JComboBox<String> containerItems = new JComboBox<>();

    private JButton confirmAdd;
    private JButton confirmGet;

    /**Constructs the dialog frame, initialises all components and adds arguments to fields
     *
     * @param game		reference to the current Game
     * @param player	player interacting with container
     * @param container container interacting with player
     */
    public ContainerDialog(Game game, Player player, Container container) {
        super(game.getGui().getFrame(), "Container", false);

        this.game = game;
        this.player = player;
        this.container = container;

        initialise(game);
    }

    /**
     * Initialises all components in the frame
     *
     * @param game
     */
    public void initialise(Game game) {
        buttonPanel = new JPanel();
        buttonPanel.setPreferredSize(new Dimension(panelWidth, panelHeight));
        buttonPanel.setSize(new Dimension(panelWidth, panelHeight));
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        addButoon = new JButton("Add");
        getButton = new JButton("Get");
        addButoon.addActionListener(e -> adding());
        getButton.addActionListener(e -> getting());
        addButoon.setAlignmentX(Window.CENTER_ALIGNMENT);
        getButton.setAlignmentX(Window.CENTER_ALIGNMENT);

        buttonPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        buttonPanel.add(addButoon);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        buttonPanel.add(getButton);

        this.setPreferredSize(new Dimension(panelWidth, panelHeight));
        this.setSize(new Dimension(panelWidth, panelHeight));

        //Check player inventory and container state to set initial button states
        if(player.getInventory().isEmpty()) { addButoon.setEnabled(false); }
        if(container.getItems().isEmpty() || player.isInvenFull()) { getButton.setEnabled(false); }

        add(buttonPanel);
        pack();
        setLocationRelativeTo(game.getGui().getFrame());
        setVisible(true);
    }

    /**
     * This method is called when the add button is selected and will add selected inventory item into the container
     */
    public void adding() {
        List<Item> inven = player.getInventory();
        for(Item item: inven) {
            String itemName = item.getName();
            playerItems.addItem(itemName);
        }
        playerItems.setSelectedIndex(0);

        confirmAdd = new JButton("Confirm");
        confirmAdd.addActionListener(e -> {
            container.addItem(inven.get(playerItems.getSelectedIndex()));
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
     * This method is called when the get button is clicked and takes the selected container item into the players inventory
     */
    public void getting() {
        List<Item> items = container.getItems();
        for(Item item : items) {
            String itemName = item.getName();
            containerItems.addItem(itemName);
        }
        containerItems.setSelectedIndex(0);

        confirmGet = new JButton("Confirm");
        confirmGet.addActionListener(e -> {
            player.addInventory(items.get(containerItems.getSelectedIndex()));
            container.removeItem(containerItems.getSelectedIndex());
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
