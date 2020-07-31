package editor.views;

import editor.EditorController;
import utility.SpriteSheet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

/**
 * The main view for the map editor.
 */
public class EditorGui {

    private EditorController editor;
    private DrawingFragment drawingFragment;
    private ControlsFragment controlsFragment;

    private JFrame frame;
    private JPanel screen;
    private SpriteSheet sheet;

    private static final int EDIT_WIDTH = 960;
    private static final int EDIT_HEIGHT = 960;

    private static final int CONTROLS_WIDTH = 720;
    private static final int CONTROLS_HEIGHT = 960;

    /**
     * Constructor for EditorGui.
     *
     * @param editor The editor controller class
     * @param sheet  The sprite sheet for getting images
     */
    public EditorGui(EditorController editor, SpriteSheet sheet) {
        this.editor = editor;
        this.sheet = sheet;

        initialise();
        display();
    }

    /**
     * Initialises the frame.
     */
    private void initialise() {
        frame = new JFrame("Map Editor");

        screen = new JPanel();
        screen.setPreferredSize(new Dimension(EDIT_WIDTH + CONTROLS_WIDTH, EDIT_HEIGHT));
        screen.setSize(new Dimension(EDIT_WIDTH + CONTROLS_WIDTH, EDIT_HEIGHT));
        screen.setLayout(new BoxLayout(screen, BoxLayout.X_AXIS));

        makeMenu();
    }

    /**
     * Makes the menu bar.
     */
    private void makeMenu() {
        JMenuBar menuBar = new JMenuBar();

        // Creating menus
        JMenu editorMenu = new JMenu("Map Editor");
        JMenuItem make = new JMenuItem("Make Map");
        JMenuItem quit = new JMenuItem("Quit");

        JMenu helpMenu = new JMenu("Help");
        JMenuItem about = new JMenuItem("About");

        // Adding
        editorMenu.add(make);
        editorMenu.add(quit);
        helpMenu.add(about);
        menuBar.add(editorMenu);
        menuBar.add(helpMenu);

        make.addActionListener(e -> setupEditing());
        quit.addActionListener(e -> frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING)));

        frame.setJMenuBar(menuBar);
    }

    /**
     * Sets up the two main panels for the editor. One is for the graphics and
     * the other for all the controls.
     */
    public void setupEditing() {
        // Deleting any old fragments if they existed
        if(drawingFragment != null) {
            screen.remove(drawingFragment);
            drawingFragment = null;
        }
        if(controlsFragment != null) {
            screen.remove(controlsFragment);
            controlsFragment = null;
        }

        // Set up drawingFragment
        drawingFragment = new DrawingFragment(this);
        drawingFragment.setPreferredSize(new Dimension(EDIT_WIDTH, EDIT_HEIGHT));
        drawingFragment.setSize(new Dimension(EDIT_WIDTH, EDIT_HEIGHT));

        // Set up controlFragment
        controlsFragment = new ControlsFragment(this);
        controlsFragment.setPreferredSize(new Dimension(CONTROLS_WIDTH, CONTROLS_HEIGHT));
        controlsFragment.setSize(new Dimension(CONTROLS_WIDTH, CONTROLS_HEIGHT));

        screen.add(drawingFragment);
        screen.add(controlsFragment);

        // Listeners
        editor.setupListeners();

        // Buttons
        controlsFragment.updateNavOptions(editor.getMap(), editor.getCurrentRoomX(), editor.getCurrentRoomY());

        frame.pack();
        frame.repaint();
    }

    /**
     * Displays the whole frame.
     */
    private void display() {
        frame.add(screen);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Returns the editor controller class.
     *
     * @return The editor controller class
     */
    public EditorController getEditor() {
        return editor;
    }

    /**
     * Returns the drawing fragment.
     *
     * @return The drawing fragment
     */
    public DrawingFragment getDrawingFragment() {
        return drawingFragment;
    }

    /**
     * Returns the controls fragment.
     *
     * @return The controls fragment.
     */
    public ControlsFragment getControlsFragment() {
        return controlsFragment;
    }

    /**
     * Returns the frame.
     *
     * @return The frame.
     */
    public JFrame getFrame() {
        return frame;
    }

    /**
     * Returns the sprite sheet.
     *
     * @return The sprite sheet
     */
    public SpriteSheet getSheet() {
        return sheet;
    }
}
