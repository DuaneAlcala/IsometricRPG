package gui;

import persistence.LoadGameXML;
import persistence.LoadMapFile;
import persistence.SaveGameXML;
import renderer.Renderer;
import utility.Direction;
import utility.Sprite;
import utility.SpriteSheet;
import world.Game;
import world.Room;
import world.Tile;
import world.entities.Entity;
import world.entities.item.Item;
import world.entities.mobs.Player;
import editor.EditorController;

import javax.swing.*;

import static utility.Direction.EAST;
import static utility.Direction.NORTH;
import static utility.Direction.SOUTH;
import static utility.Direction.WEST;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**This GUI class handles the main Frame and buttons for this application as well as input from the mouse and keyboard
 *
 * @author sailymahe
 */
public class GUI {

	/* GUI Components */
	private JFrame frame;
	private Game game;
	private Player player;
	private Renderer renderer;
	private Set<Button> buttons; // "newGame" , "loadGame", "mapEditor", "menu", "X" are current buttons
	private Button menuButton;
	private JFileChooser fileChooser;
	private Button fButton;
	private Button rButton;
	private Button spaceButton;

	/* GUI Field Values */
	private int guiWidth = 608;
	private int guiHeight = 608;
	private int tileSize = 32;
	private int gameScreenHeight = 720;
	private int gameScreenWidth = (gameScreenHeight * 16) / 9;
	private int inventorySelected = 0;
	private boolean firstMenu = true;

	public void setTestingState() {
		this.state = states.inGame;
	}

	/* Game States */
	private enum states{ mainMenu, inGame }; //Only require two states as other potential states use new JFrames with the focus set to them
	private states state;

	public static boolean testing = false;

	/**Main method creates a new GUI instance
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		new GUI();
	}

	/**
	 * Constructor which is used to return a new instance of the GUI used to
	 * visually display and illustrate the game.
	 */
	public GUI() {
		this.buttons = new HashSet<Button>();
		makeButtons();
		this.renderer = new Renderer(buttons, menuButton, fButton, rButton, spaceButton);
		Entity.setRenderer(renderer);

		initialise();
	}

	/**
	 * Returns a reference to the current renderer object, used to output the game.
	 * @return
	 */
	public Renderer getRenderer() {
		return renderer;
	}

	/**
	 * Redraws an updated instance of the game, following all player
	 * player interaction with the game.
	 */
	private void redraw() {
		frame.repaint();
	}

	/**
	 * Initialises all GUI components to allow for the game to be visually illustrated.
	 * The following method deals with the following responsibilities:
	 * 		1. Initialising all frame and panel information.
	 * 		2. Handling game-state information.
	 * 		3. Adds all of the required key, event and mouse listeners required by the GUI.
	 * 		4. Displays the main game menu.
	 */
	private void initialise() {
		frame = new JFrame();
		frame.setTitle("Game");
		state = states.mainMenu; //initial state is main menu

		//Create display panel which the whole game is rendered to
		JPanel display = new JPanel() {
			protected void paintComponent(Graphics g) {
				if(state.equals(states.mainMenu)) {
					renderer.renderMenu(g);
				}else {
					renderer.render(g, game.getCurrentRoom().getTiles(), player);
				}
			};
		};

		//fix frame and display size
		display.setSize(gameScreenWidth, gameScreenHeight);
		frame.setSize(gameScreenWidth, gameScreenHeight);
KeyListener k = new KeyListener() {

	@Override
	public void keyPressed(KeyEvent keyEvent) {
		respond(keyEvent.getKeyCode());
	}

	@Override
	public void keyReleased(KeyEvent arg0) { }

	@Override
	public void keyTyped(KeyEvent arg0) { }
};

		frame.addKeyListener(k);

		display.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				//If in game then look for events that could need to respont to mouse clicks
				if(state == states.inGame) {
					//Gets the index of inventory item mouse could be over (-1 if no item under mouse)
					int index = renderer.getInventoryIndex(arg0.getX(), arg0.getY());

					//if over item then have player select item
					if(index != -1 && index<player.getInventory().size()) {
						player.selectItem(index);
						player.setHoldIndex(index);
						redraw();
						return;
					}

					//menu button
					if(menuButton.isOver(arg0.getX(), arg0.getY())) {
						openMenu();
						return;
					}
					//f/drop button
					if(fButton.isOver(arg0.getX(), arg0.getY())){
						game.dropItem();
						player.updateStrings();
						redraw();
						return;
					}
					//r/use button
					if(rButton.isOver(arg0.getX(), arg0.getY())){
						game.getPlayer().useItem();
						player.updateStrings();
						redraw();
						return;
					}
					//interact button
					if(spaceButton.isOver(arg0.getX(), arg0.getY())){
						game.interaction();
						player.updateStrings();
						redraw();
						return;
					}

					//get tile that mouse is over, null if mouse not over a tile
					Tile currentTile = renderer.getTileFromMouse(arg0.getX(), arg0.getY(), player, player.getRoom());
					if(currentTile != null) {
						//if tile has an entity, then add entities decription to renderers message box
						if(currentTile.getEntity() != null){
							renderer.addMessage(currentTile.getEntity().getDescription());
							redraw();
							return;
						}
					}
					//finally update strings of actions that will happen for next set of possible inputs
					player.updateStrings();
				}
				//If in menu
				else {
					//for each button, check if mouse is over and then call appropriate method
					for(Button button : buttons) {
						if(button.isOver(arg0.getX(), arg0.getY())) {
							if(button.getName().equals("newGame")) {
								doNewGame();
								return;
							}
							if(button.getName().equals("loadGame")) {
								doLoadGame();
								return;
							}
							if(button.getName().equals("mapEditor")) {
								doMapEditor();
								return;
							}
							if(button.getName().equals("saveGame")) {
								doSaveGame();
								return;
							}
							if(button.getName().equals("X")) {
								doCloseMenu();
								return;
							}
						}
					}
				}
			}

			@Override
			public void mouseEntered(MouseEvent arg0) { }

			@Override
			public void mouseExited(MouseEvent arg0) { }

			@Override
			public void mousePressed(MouseEvent arg0) { }

			@Override
			public void mouseReleased(MouseEvent arg0) { }

		});

		frame.add(display);
		//frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

public void respond(int val){
	//If in main menu, then no key presses should trigger actions
	if(state.equals(states.mainMenu)) {
		return;
	}

	//responses for W,A,S,D,E,Q,V,SPACE,R,F
	if(val== KeyEvent.VK_W) {
		game.movePlayer(NORTH);
	}else if(val == KeyEvent.VK_S) {
		game.movePlayer(SOUTH);
	}else if(val == KeyEvent.VK_A) {
		game.movePlayer(WEST);
	}else if(val == KeyEvent.VK_D) {
		game.movePlayer(EAST);
	}else if(val == KeyEvent.VK_E) {
		game.rotateCounterClockwise();
	}else if(val== KeyEvent.VK_Q) {
		game.rotateClockwise();
	}else if(val == KeyEvent.VK_V) {
		renderer.switchView();
	}else if(val == KeyEvent.VK_SPACE) {
		game.interaction();
		System.out.println("Load Game");
	}else if(val == KeyEvent.VK_R) {
		game.getPlayer().useItem();
	}else if(val == KeyEvent.VK_F) {
		game.dropItem();
	}
	//Subtract torch lights remaining fuel to simulate passgin of time
	game.subtractTorch();
	//update strings for next available options
	player.updateStrings();
	player.getHeldItem();
	//redraw as model may have changed
	redraw();
}

	/**
	 * Creates the buttons required throughout the main game-state.
	 */
	private void makeButtons() {
		buttons.add(new Button(104, 40, 300, 70, "newGame"));
		buttons.add(new Button(104, 150, 300, 70, "loadGame"));
		buttons.add(new Button(104, 260, 300, 70, "mapEditor"));

		menuButton = new Button(30, 50, 100, 40, "menu");
		fButton = new Button(30, 525, 50, 50, "fButton"); //Used for dropping items
		rButton = new Button(30, 468, 50, 50, "rButton"); //Used for using items
		spaceButton = new Button(30, 411, 50, 50, "spaceButton"); //Used for picking up items.
	}

	/**
	 * Sets the opening state of the program, allowing for the main
	 * menu to be presented to the user.
	 */
	private void openMenu() {
		this.state = states.mainMenu;
		redraw();
	}

	/**
	 * Closes the menu state of the program, allows the
	 * player to interact with the game.
	 */
	private void doCloseMenu() {
		this.state = states.inGame;
		redraw();
	}

	/**
	 * This function is executed when the 'Save' button on the menu
	 * is clicked by the user.
	 */
	private void doSaveGame() {
		try {
			fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File("."));
			fileChooser.setDialogTitle("Please select your game input file.");
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			File file;
			// run the file chooser and check the user didn't hit cancel
			if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
				file = fileChooser.getSelectedFile();
				SaveGameXML.save(game, file.getName());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		//Notify player that game has been saved
		JOptionPane.showMessageDialog(frame, "Game has been saved.");
	}

	/**
	 * Instantiates a new version of the game to be played.
	 */
	private void doNewGame() {
		if(firstMenu) {
			firstMenu = false;
			buttons.add(new Button(219, 480, 70, 70, "X"));
			buttons.add(new Button(104, 370, 300, 70,"saveGame"));
		}


		try{
            this.game = LoadGameXML.load(new File("defaultLevel.xml"));
            //this.game = new Game();

            game.setGui(this);

            this.player = game.getPlayer();
			game.getPlayer().setCommands(renderer);
			this.state = states.inGame;
			redraw();
        }catch(Exception e){
            e.printStackTrace();
        }
	}

	/**
	 * Loads a previously saved version of the game, through the use of the
	 * Persistence package.
	 */
	private void doLoadGame() {
		if (firstMenu) {
			firstMenu = false;
			buttons.add(new Button(219, 480, 70, 70, "X"));
			buttons.add(new Button(104, 370, 300, 70,"saveGame"));
		}
		fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File("."));
		fileChooser.setDialogTitle("Please select your game input file.");
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		File file;
		// run the file chooser and check the user didn't hit cancel
		if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
			file = fileChooser.getSelectedFile();
			this.game = LoadGameXML.load(file);

			if(game != null) {
				game.setGui(this);

				this.player = game.getPlayer();
				game.getPlayer().setCommands(renderer);
				this.state = states.inGame;
				redraw();
			}
		}
	}

	/**
	 * Instantiate a new version of the game.
	 */
	private void doMapEditor() {
		new EditorController(false);
	}

	/**
	 * Returns a reference to the main game frame.
	 * @return
	 */
	public JFrame getFrame() {
		return frame;
	}
}
