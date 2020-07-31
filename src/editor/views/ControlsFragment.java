package editor.views;

import editor.RoomMap;
import utility.Sprite;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

/**
 * ControlsFragment consists of all the elements and logic that occurs
 * with the controls at the right side of the map editor.
 */
public class ControlsFragment extends JPanel {

    private EditorGui gui;

    private JPanel controlsPanel = new JPanel();
    private List<JToggleButton> controlButtons = new ArrayList<>();

    // Navigation
    private JPanel navHolder = new JPanel();
    private JButton northButton;
    private JButton southButton;
    private JButton eastButton;
    private JButton westButton;

    private List<JButton> navButtons = new ArrayList<>();

    // Save and load
    private JButton saveButton;
    private JButton loadButton;

    private JButton heightsButton;
    private JButton mapButton;

    private JPanel tileGridPanel;

    /**
     * Constructor for ControlsFragment.
     *
     * @param gui The gui where this fragment will be placed
     */
    ControlsFragment(EditorGui gui) {
        this.gui = gui;

        initialise();
    }

    /**
     * Initialises the whole panel.
     */
    private void initialise() {
        // Labels
        JLabel tilesLabel = new JLabel("Tiles");
        JLabel objectsLabel = new JLabel("Objects");

        tilesLabel.setAlignmentX(Window.CENTER_ALIGNMENT);
        objectsLabel.setAlignmentX(Window.CENTER_ALIGNMENT);

        controlsPanel.setLayout(new BoxLayout(controlsPanel, BoxLayout.Y_AXIS));

        // Make the tile and tile object display buttons
        JPanel tilesPanel = new JPanel(new GridLayout(0, 5, 10, 10));
        JPanel objectsPanel = new JPanel(new GridLayout(0, 5, 10, 10));
        setupTileControls(gui.getSheet().getTileSprites(), tilesPanel);
        setupTileControls(gui.getSheet().getObjectSprites(), objectsPanel);

        JPanel tilesHolder = new JPanel();
        JPanel objectsHolder = new JPanel();
        tilesHolder.add(tilesPanel);
        objectsHolder.add(objectsPanel);

        // Setting the heights panel
        JPanel heightsPanel = new JPanel();
        setupHeightControls(heightsPanel);

        // Setting the navigation panel
        setupNavControls(navHolder);

        // Setting the saving and loading
        saveButton = new JButton("Save");
        saveButton.addActionListener(e -> gui.getEditor().save());
        saveButton.setAlignmentX(Window.CENTER_ALIGNMENT);

        loadButton = new JButton("Load");
        loadButton.addActionListener(e -> gui.getEditor().load());
        loadButton.setAlignmentX(Window.CENTER_ALIGNMENT);

        // Making the grid layout for both tile types (display and objects)
        JPanel allTilesPanel = new JPanel();
        allTilesPanel.setLayout(new BoxLayout(allTilesPanel, BoxLayout.Y_AXIS));
        allTilesPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        allTilesPanel.add(tilesLabel);
        allTilesPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        allTilesPanel.add(tilesHolder);

        JPanel allObjectsPanel = new JPanel();
        allObjectsPanel.setLayout(new BoxLayout(allObjectsPanel, BoxLayout.Y_AXIS));
        allObjectsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        allObjectsPanel.add(objectsLabel);
        allObjectsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        allObjectsPanel.add(objectsHolder);

        tileGridPanel = new JPanel();
        tileGridPanel.setLayout(new BoxLayout(tileGridPanel, BoxLayout.X_AXIS));
        tileGridPanel.add(allTilesPanel);
        tileGridPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        tileGridPanel.add(allObjectsPanel);

        // Finally adding everything
        controlsPanel.add(tileGridPanel);
        controlsPanel.add(heightsPanel);
        controlsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        controlsPanel.add(navHolder);
        controlsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        controlsPanel.add(saveButton);
        controlsPanel.add(loadButton);

        resizeButtons();
        setActions();

        tileGridPanel.setVisible(false);
        add(controlsPanel, BorderLayout.NORTH);
        setFocusable(false);
        setVisible(true);
    }

    /**
     * Sets up the different tile buttons that can be placed in the editor.
     * This includes the tile displays and the objects that can be placed on
     * a tile.
     *
     * @param spriteMap Map of strings to sprites
     * @param panel     Panel to place buttons
     */
    private void setupTileControls(Map<String, Sprite> spriteMap, JPanel panel) {
        for(Map.Entry<String, Sprite> s : spriteMap.entrySet()) {
            //if(s.getKey().equals("player")) { continue; }
            if(s.getKey().equals("portalExit")) { continue; }
            if(s.getKey().equals("black")) { continue; }

            // For these tile sprites, make a button and add them to the controls
            BufferedImage image = s.getValue().getImage();
            JToggleButton button = new JToggleButton(s.getKey(), new ImageIcon(image.getScaledInstance(image.getWidth() * 2, image.getHeight() * 2, Image.SCALE_DEFAULT)));
            controlButtons.add(button);
            panel.add(button);
        }
    }

    /**
     * Sets up the buttons for showing all the heights of tiles and also
     * the buttons for changing the heights of each tile.
     *
     * @param heightsPanel Panel to place the height controls
     */
    private void setupHeightControls(JPanel heightsPanel) {
        heightsPanel.setLayout(new BoxLayout(heightsPanel, BoxLayout.Y_AXIS));

        // Creating a label and a panel for the label to be placed in the center
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JLabel heightsLabel = new JLabel("View Heights");
        heightsLabel.setAlignmentX(Window.CENTER_ALIGNMENT);
        labelPanel.add(heightsLabel);

        // Creating the buttons that allow the editor to change tile heights
        JPanel heightOptions = new JPanel();
        heightOptions.setLayout(new FlowLayout(FlowLayout.CENTER));

        JToggleButton zero = new JToggleButton("0");
        JToggleButton one = new JToggleButton("1");
        heightOptions.add(zero);
        heightOptions.add(one);

        heightsButton = new JButton("Heights");
        heightsButton.addActionListener(e -> showHeights());
        heightsButton.setAlignmentX(Window.CENTER_ALIGNMENT);

        // Adding to panel
        heightsPanel.add(labelPanel);
        heightsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        heightsPanel.add(heightsButton);
        heightsPanel.add(heightOptions);

        controlButtons.add(zero);
        controlButtons.add(one);
    }

    /**
     * Sets up the buttons that allow the editor to navigate between rooms.
     *
     * @param navHolder Panel to place the nav controls
     */
    private void setupNavControls(JPanel navHolder) {
        JLabel navLabel = new JLabel("Navigation");
        navLabel.setAlignmentX(Window.CENTER_ALIGNMENT);

        navHolder.add(navLabel);

        // Controls for moving around rooms and map
        northButton = new JButton("North");
        southButton = new JButton("South");
        eastButton = new JButton("East");
        westButton = new JButton("West");

        navButtons.add(northButton);
        navButtons.add(southButton);
        navButtons.add(eastButton);
        navButtons.add(westButton);

        // Button to switch between the whole map view or just one room
        mapButton = new JButton("Switch Views");
        mapButton.addActionListener(e -> {
            untoggle();
            gui.getEditor().viewSwitch();
        });

        // Making panels for a better button layout
        JPanel navRowOne = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel navRowTwo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel navRowThree = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // Adding
        navRowOne.add(northButton);
        navRowTwo.add(westButton);
        navRowTwo.add(southButton);
        navRowTwo.add(eastButton);
        navRowThree.add(mapButton);

        navHolder.setLayout(new BoxLayout(navHolder, BoxLayout.Y_AXIS));
        navHolder.add(navRowOne);
        navHolder.add(navRowTwo);
        navHolder.add(navRowThree);
    }

    /**
     * Disables all controls.
     */
    public void disableControls() {
        controlsPanel.setVisible(false);
    }

    /**
     * Enables all controls.
     */
    public void enableControls() {
        controlsPanel.setVisible(true);
    }

    /**
     * Sets the boolean that decides whether or not all the heights of tiles
     * get shown at the map editor.
     */
    private void showHeights() {
        boolean showing = gui.getDrawingFragment().isShowingHeights();
        gui.getDrawingFragment().setShowingHeights(!showing);
        gui.getDrawingFragment().repaint();
    }

    /**
     * Enables and disables controls based on what view the editor is at.
     * If the editor is viewing the whole map, the tile buttons are not visible.
     * If the editor is viewing a room, the tile buttons are visible.
     *
     * @param viewingMap    Boolean that indicates if the editor is in map view
     * @param viewingRoom   Boolean that indicates if the editor is in room view
     */
    public void configureControls(boolean viewingMap, boolean viewingRoom) {
        // Only allow the editor to change room tiles if they're in room view
        if(viewingMap) {
            tileGridPanel.setVisible(false);
        }else if(viewingRoom) {
            tileGridPanel.setVisible(true);
        }
    }

    /**
     * Updates the navigation buttons and decides whether they can be enabled
     * based on what room the editor is at and whether an adjacent room exists.
     *
     * @param rooms         The whole map of rooms
     * @param currentRoomX  The x index of where the editor is in terms of the rooms 2d array
     * @param currentRoomY  The y index of where the editor is in terms of the rooms 2d array
     */
    public void updateNavOptions(RoomMap rooms, int currentRoomX, int currentRoomY) {
        int mapWidth = rooms.getWidth();
        int mapHeight = rooms.getHeight();

        // Enable buttons if an adjacent room exists
        northButton.setEnabled((currentRoomY - 1) > -1);
        southButton.setEnabled((currentRoomY + 1) < mapHeight);
        eastButton.setEnabled((currentRoomX + 1) < mapWidth);
        westButton.setEnabled((currentRoomX - 1) > -1);
    }

    /**
     * Method for resizing buttons.
     */
    private void resizeButtons() {
        // Make a fixed size for the buttons
        Dimension size = new Dimension(31, 32);
        for(JToggleButton button : controlButtons) {
            button.setMaximumSize(size);
            button.setSize(size);
            button.setPreferredSize(size);
        }

        size = new Dimension(75, 30);
        for(JButton button : navButtons) {
            button.setMaximumSize(size);
            button.setSize(size);
            button.setPreferredSize(size);
        }
    }

    /**
     * Sets the actions for the tile buttons that change how a tile is displayed
     * and the navigation buttons.
     */
    private void setActions() {
        // Adding actions to all buttons
        for(JToggleButton button : controlButtons) {
            button.addActionListener(e -> {
                untoggle();
                button.setSelected(true);
            });
            Font dummyFont = new Font("Text", Font.PLAIN, 0);
            button.setFont(dummyFont);
        }

        // Setting navigation actions
        northButton.addActionListener(e -> {
            gui.getEditor().moveRoom("North");
            gui.getEditor().update();
        });
        southButton.addActionListener(e -> {
            gui.getEditor().moveRoom("South");
            gui.getEditor().update();
        });
        eastButton.addActionListener(e -> {
            gui.getEditor().moveRoom("East");
            gui.getEditor().update();
        });
        westButton.addActionListener(e -> {
            gui.getEditor().moveRoom("West");
            gui.getEditor().update();
        });
    }

    /**
     * Untoggles all the other buttons. Used when the editor clicks on a new one.
     */
    public void untoggle() {
        for(JToggleButton button : controlButtons) {
            button.setSelected(false);
        }
    }

    /**
     * Returns the tile button that was toggled by the editor.
     *
     * @return The selected tile button
     */
    public String getSelectedTileButton() {
        for(JToggleButton button : controlButtons) {
            if(button.isSelected()) { return button.getText(); }
        }
        return null;
    }
}
