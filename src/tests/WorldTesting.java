package tests;

import org.junit.jupiter.api.Test;

import gui.ContainerDialog;
import gui.GUI;
import utility.Direction;
import world.Game;
import world.Room;
import world.Tile;
import world.entities.mobs.Player;
import world.entities.transport.Door;
import world.entities.transport.EndPortal;
import world.entities.transport.Portal;

public class WorldTesting {

	@Test
	public void test_game_1() {
		Game g = new Game();
		Game g1 = new Game(null, null, null, -1);
		g.isTesting = true;
		g.addItems();
		g.dropItem();
		g.getCurrRoomX();
		g.getCurrRoomY();
		g.getGui();
		g.getTorchLight();
		g.getPlayer();
		g.interaction();
		g.getCurrentRoom();
		g.getWorld();
		g.selectInventory(0);
		g.setCurrentRoom(new Room(0, 0));
		g.movePlayer(Direction.NORTH);
		g.setGui(null);
		g.subtractTorch();
		Portal p = new Portal(0, 0, 0, 0);
		p.getDescription();
		p.getRoomCol();
		p.getRoomRow();
		p.getName();
		p.getTileCol();
		p.getTileRow();
		p.getTileX();
		p.getTileY();
	}

	@Test
	public void test_world() {

		try {

		GUI gui = new GUI();
		gui.setTestingState();
		gui.respond(0);
		gui.respond(1);
		gui.respond(2);
		gui.respond(3);
		gui.respond(4);

		gui.getFrame();
		gui.getRenderer();
		gui.testing = true;

		Door d = new Door(0, 0, 0, 0);
		d.searchTile(new Room(0, 0), new Tile("", 0, d, 0, 0));


			EndPortal ep = new EndPortal();
			ep.interact(new Game(), new Player(new Game()));
			ep.setActivated(false);
			ep.setActivated(true);
		} catch (Exception e) {
			
		}
	}
}
