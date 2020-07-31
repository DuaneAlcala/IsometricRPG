package tests;

import org.junit.jupiter.api.Test;

import gui.BookDialog;
import gui.CarryDialog;
import gui.ContainerDialog;
import gui.GUI;
import gui.MapDialog;
import gui.TraderDialog;
import world.Game;
import world.entities.containers.Chest;
import world.entities.item.Backpack;
import world.entities.item.Carry;
import world.entities.item.Spade;
import world.entities.mobs.Trader;

public class ApplicationTests {
	
	
	@Test
	public void test1() {
		GUI g = new GUI();
		Game game = new Game();
		game.setGui(g);
		
		game.getPlayer().addInventory(new Spade());
		game.getPlayer().addInventory(new Spade());
		game.getPlayer().addInventory(new Spade());
		game.getPlayer().addInventory(new Spade());
		
		BookDialog b = new BookDialog(game, "");
		b.dispose();
		CarryDialog c = new CarryDialog(game, game.getPlayer(), new Backpack());
		c.dispose();
		ContainerDialog cont = new ContainerDialog(game, game.getPlayer(), new Chest());
		cont.dispose();
		MapDialog m = new MapDialog(game, game.getPlayer());
		m.dispose();
		TraderDialog t = new TraderDialog(game, game.getPlayer(), new Trader());
		t.dispose();
		g.getFrame().dispose();
	}
}
