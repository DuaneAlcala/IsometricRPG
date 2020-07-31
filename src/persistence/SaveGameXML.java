package persistence;

import java.io.IOException;
import java.io.PrintStream;
import java.io.StringWriter;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import java.io.File;
import java.io.FileOutputStream;

import world.Game;
import world.Room;
import world.Tile;
import world.World;
import world.entities.Entity;
import world.entities.containers.Bookshelf;
import world.entities.containers.Chest;
import world.entities.item.Armour;
import world.entities.item.Backpack;
import world.entities.item.Book;
import world.entities.item.Coin;
import world.entities.item.GemStone;
import world.entities.item.Item;
import world.entities.item.Key;
import world.entities.item.Lamp;
import world.entities.item.Map;
import world.entities.item.Potion;
import world.entities.item.Spade;
import world.entities.item.Sword;
import world.entities.mobs.Player;
import world.entities.mobs.Skeleton;
import world.entities.mobs.Trader;
import world.entities.transport.Door;
import world.entities.transport.EndPortal;
import world.entities.transport.Portal;

/**
 * This class is resposible for saving the Game into a file in xml format
 *
 * @author riadsher
 *
 */
public class SaveGameXML {

	/**
	 * This method is takes a game object and saves it the file with the filename in the parameter.
	 *
	 * @param game the game to be seralized in xml format
	 * @param filename the name of the file that the game will be saved in.
	 */
	public static void save(Game game, String filename) {
		try {
			/*
			 * Setting up the xml writer
			 */
			 StringWriter stringWriter = new StringWriter();

	         XMLOutputFactory xMLOutputFactory = XMLOutputFactory.newInstance();
	         XMLStreamWriter xMLStreamWriter = xMLOutputFactory.createXMLStreamWriter(stringWriter);

	         //Game node
	         xMLStreamWriter.writeStartDocument();
	         xMLStreamWriter.writeStartElement("Game");

			World world = game.getWorld();

			xMLStreamWriter.writeStartElement("TorchLight");
			xMLStreamWriter.writeCharacters(game.getTorchLight()+"");
			xMLStreamWriter.writeEndElement();

			//World node
			xMLStreamWriter.writeStartElement("World");

			/*
			 * Writing the dimensions of the game (i.e its width and height)
			 */
			writeWorldDimension(xMLStreamWriter, world);

			/*
			 * Writing the content of every room in the game
			 */
			for(int row = 0; row < world.getHeight(); row++) {
				for(int col = 0; col < world.getWidth(); col++) {
					writeRoom(xMLStreamWriter, world, row, col);
				}
			}

			/*
			 * Closing the tags
			 */
	        xMLStreamWriter.writeEndElement();
	        xMLStreamWriter.writeEndElement();

	        xMLStreamWriter.writeEndDocument();

	        xMLStreamWriter.flush();
	        xMLStreamWriter.close();

	        String xmlString = stringWriter.getBuffer().toString();

	         stringWriter.close();
	         /*
	          * Writing the content of the game in xml format
	          */
	         try (PrintStream out = new PrintStream(new FileOutputStream(filename))) {
	        	    out.print(xmlString);
	        	    out.close();
	        	}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method writes the dimensions of the game (i.e width and height)
	 *
	 * @param xMLStreamWriter the writer that is directed to the output file and is used to write the width and height of the world into the room.
	 * @param world the world that will have its dimensions serliazed
	 * @throws XMLStreamException
	 */
	private static void writeWorldDimension(XMLStreamWriter xMLStreamWriter, World world) throws XMLStreamException {
		//World width node
		xMLStreamWriter.writeStartElement("WorldWidth");
		xMLStreamWriter.writeCharacters(world.getWidth()+"");
        xMLStreamWriter.writeEndElement();

      //World width node
        xMLStreamWriter.writeStartElement("WorldHeight");
      	xMLStreamWriter.writeCharacters(world.getHeight()+"");
        xMLStreamWriter.writeEndElement();
	}

	/**
	 * This is a helper method that saves the fields of the room and the tiles that it has the room.
	 *
	 * @param xMLStreamWriter the writer that is directed to the output file and is used to write the width and height of the world into the room.
	 * @param world the world that has its fields serilazed
	 * @param roomRow the row position of the room in the room matrix in the world
	 * @param roomCol the col position of the room in the room matrix in the world
	 * @throws XMLStreamException
	 */
	private static void writeRoom(XMLStreamWriter xMLStreamWriter, World world, int roomRow, int roomCol) throws XMLStreamException {
		Room room = world.getRoom(roomRow, roomCol);

		/*
		 * Creating a room tag
		 */
		xMLStreamWriter.writeStartElement("Room");

		/*
		 * Creating tags for the fields of the room (width, height, isDark)
		 */
		xMLStreamWriter.writeStartElement("RoomWidth");
		xMLStreamWriter.writeCharacters(room.getWidth()+"");
		xMLStreamWriter.writeEndElement();

		xMLStreamWriter.writeStartElement("RoomHeight");
		xMLStreamWriter.writeCharacters(room.getHeight()+"");
		xMLStreamWriter.writeEndElement();

		xMLStreamWriter.writeStartElement("IsDark");
		xMLStreamWriter.writeCharacters(room.isDark()+"");
		xMLStreamWriter.writeEndElement();

		/*
		 * Serliasing the tiles in the room
		 */
		for(int row = 0; row < room.getHeight(); row++) {
			for(int col = 0; col < room.getWidth(); col++) {
				writeTile(xMLStreamWriter, room, row, col);
			}
		}

		xMLStreamWriter.writeEndElement();
	}

	/**
	 * This is a private helper method that saves the fields of the tile.
	 *
	 * @param xMLStreamWriter the writer that is directed to the output file and is used to write the width and height of the world into the room.
	 * @param room the room to be serialised
	 * @param row the row position of the tile in the rooms matrix
	 * @param col the col position of the tile in the rooms matrix
	 * @throws XMLStreamException
	 */
	private static void writeTile(XMLStreamWriter xMLStreamWriter, Room room, int row, int col) throws XMLStreamException {
		Tile tile = room.getTile(row, col);

		/*
		 * Creating a tile tag
		 */
		xMLStreamWriter.writeStartElement("Tile");

		/*
		 * This is fields that the tile has.
		 */
		xMLStreamWriter.writeStartElement("DisplayType");
		xMLStreamWriter.writeCharacters(tile.getDisplayType());
		xMLStreamWriter.writeEndElement();

		xMLStreamWriter.writeStartElement("Height");
		xMLStreamWriter.writeCharacters(tile.getHeight()+"");
		xMLStreamWriter.writeEndElement();

		if(tile.getEntity() != null) {
			writeEntity(xMLStreamWriter, tile.getEntity());
		}

		xMLStreamWriter.writeEndElement();
	}

	/**
	 * This is a private method that serilizes the Entity
	 *
	 * @param xMLStreamWriter the writer that is directed to the output file and is used to write the width and height of the world into the room.
	 * @param entity the entity to be serliased
	 * @throws XMLStreamException
	 */
	private static void writeEntity(XMLStreamWriter xMLStreamWriter, Entity entity) throws XMLStreamException {

		xMLStreamWriter.writeStartElement("Entity");

		/*
		 * This is all the possible Objects that an entity could be.
		 */
		if(entity instanceof Potion) {
			xMLStreamWriter.writeStartElement("Potion");
			xMLStreamWriter.writeCharacters("Potion");
			xMLStreamWriter.writeEndElement();
		}
		if(entity instanceof Key) {
			xMLStreamWriter.writeStartElement("Key");
			xMLStreamWriter.writeCharacters("Key");
			xMLStreamWriter.writeEndElement();
		}
		if(entity instanceof Lamp) {
			xMLStreamWriter.writeStartElement("Lamp");
			xMLStreamWriter.writeCharacters("Lamp");
			xMLStreamWriter.writeEndElement();
		}
		if(entity instanceof Spade) {
			xMLStreamWriter.writeStartElement("Spade");
			xMLStreamWriter.writeCharacters("Spade");
			xMLStreamWriter.writeEndElement();
		}
		if(entity instanceof Coin) {
			xMLStreamWriter.writeStartElement("Coin");
			xMLStreamWriter.writeCharacters("Coin");
			xMLStreamWriter.writeEndElement();
		}
		if(entity instanceof GemStone) {
			xMLStreamWriter.writeStartElement("GemStone");
			xMLStreamWriter.writeCharacters("GemStone");
			xMLStreamWriter.writeEndElement();
		}
		if(entity instanceof Map) {
			xMLStreamWriter.writeStartElement("Map");
			xMLStreamWriter.writeCharacters("Map");
			xMLStreamWriter.writeEndElement();
		}
		if(entity instanceof Armour) {
			xMLStreamWriter.writeStartElement("Armour");
			xMLStreamWriter.writeCharacters("Armour");
			xMLStreamWriter.writeEndElement();
		}
		if(entity instanceof Sword) {
			xMLStreamWriter.writeStartElement("Sword");
			xMLStreamWriter.writeCharacters("Sword");
			xMLStreamWriter.writeEndElement();
		}
		if(entity instanceof Bookshelf) {
			xMLStreamWriter.writeStartElement("Bookshelf");
			xMLStreamWriter.writeCharacters("Bookshelf");

			Bookshelf bookshelf = (Bookshelf) entity;

			for(Item item : bookshelf.getItems()) {
				writeEntity(xMLStreamWriter, item);
			}

			xMLStreamWriter.writeEndElement();
		}
		/*
		 * seriliazing the values in chest
		 */
		if(entity instanceof Chest) {
			xMLStreamWriter.writeStartElement("Chest");
			xMLStreamWriter.writeCharacters("Chest");

			Chest chest = (Chest) entity;

			for(Item item : chest.getItems()) {
				writeEntity(xMLStreamWriter, item);
			}

			xMLStreamWriter.writeEndElement();
		}
		/*
		 * seriliazing the values in Backpack
		 */
		if(entity instanceof Backpack) {
			xMLStreamWriter.writeStartElement("Sword");
			xMLStreamWriter.writeCharacters("Sword");
			xMLStreamWriter.writeEndElement();
			//NEED TO FIX THIS BACKPACKS BEING TURNED INTO SWORDS
		}
		if(entity instanceof Book) {
			xMLStreamWriter.writeStartElement("Book");

			Book book = (Book) entity;

			xMLStreamWriter.writeCharacters(book.getContents());
			xMLStreamWriter.writeEndElement();
		}
		/*
		 * seriliazing the values in Trader
		 */
		if(entity instanceof Trader) {
			xMLStreamWriter.writeStartElement("Trader");
			xMLStreamWriter.writeCharacters("Trader");

			Trader trader = (Trader) entity;

			for(Item item : trader.getItems()) {
				writeEntity(xMLStreamWriter, item);
			}

			xMLStreamWriter.writeEndElement();
		}
		if(entity instanceof Skeleton) {
			xMLStreamWriter.writeStartElement("Skeleton");
			xMLStreamWriter.writeCharacters("Skeleton");

			Skeleton skeleton = (Skeleton) entity;

			xMLStreamWriter.writeEndElement();
		}
		/*
		 * seriliazing the inventory of items that the player has
		 */
		if(entity instanceof Player) {
			xMLStreamWriter.writeStartElement("Player");
			xMLStreamWriter.writeCharacters("Player");

			Player player = (Player) entity;

			for(Item item : player.getInventory()) {
				writeEntity(xMLStreamWriter, item);
			}

			xMLStreamWriter.writeStartElement("holdIndex");
			xMLStreamWriter.writeCharacters(player.getHoldIndex()+"");
			xMLStreamWriter.writeEndElement();

			xMLStreamWriter.writeStartElement("Health");
			xMLStreamWriter.writeCharacters(player.getHealth()+"");
			xMLStreamWriter.writeEndElement();

			xMLStreamWriter.writeEndElement();
		}
		/*
		 * saving the state of the door and whether its locked or not
		 */
		if(entity instanceof Door) {
			xMLStreamWriter.writeStartElement("Door");
			xMLStreamWriter.writeCharacters("Door");

			Door door = (Door) entity;

			boolean locked = door.isLocked();
			int roomRow = door.getRoomRow();
		    int roomCol = door.getRoomCol();
		    int tileRow = door.getTileRow();
		    int tileCol = door.getTileCol();

		    xMLStreamWriter.writeStartElement("Locked");
			xMLStreamWriter.writeCharacters(locked+"");
			xMLStreamWriter.writeEndElement();

			xMLStreamWriter.writeStartElement("RoomRow");
			xMLStreamWriter.writeCharacters(roomRow+"");
			xMLStreamWriter.writeEndElement();

			xMLStreamWriter.writeStartElement("RoomCol");
			xMLStreamWriter.writeCharacters(roomCol+"");
			xMLStreamWriter.writeEndElement();

			xMLStreamWriter.writeStartElement("TileRow");
			xMLStreamWriter.writeCharacters(tileRow+"");
			xMLStreamWriter.writeEndElement();

			xMLStreamWriter.writeStartElement("TileCol");
			xMLStreamWriter.writeCharacters(tileCol+"");
			xMLStreamWriter.writeEndElement();

			xMLStreamWriter.writeEndElement();
		}
		/*
		 * saving the state of the portal and whether its locked or not
		 */
		if(entity instanceof Portal) {
			xMLStreamWriter.writeStartElement("Portal");
			xMLStreamWriter.writeCharacters("Portal");

			Portal portal = (Portal) entity;

			int roomRow = portal.getRoomRow();
		    int roomCol = portal.getRoomCol();
		    int tileRow = portal.getTileRow();
		    int tileCol = portal.getTileCol();

			xMLStreamWriter.writeStartElement("RoomRow");
			xMLStreamWriter.writeCharacters(roomRow+"");
			xMLStreamWriter.writeEndElement();

			xMLStreamWriter.writeStartElement("RoomCol");
			xMLStreamWriter.writeCharacters(roomCol+"");
			xMLStreamWriter.writeEndElement();
			xMLStreamWriter.writeStartElement("TileRow");
			xMLStreamWriter.writeCharacters(tileRow+"");
			xMLStreamWriter.writeEndElement();

			xMLStreamWriter.writeStartElement("TileCol");
			xMLStreamWriter.writeCharacters(tileCol+"");
			xMLStreamWriter.writeEndElement();

			xMLStreamWriter.writeEndElement();

		}
		/*
		 * saving the state of the endPortral
		 */
		if(entity instanceof EndPortal) {
			xMLStreamWriter.writeStartElement("EndPortal");
			xMLStreamWriter.writeCharacters("EndPortal");

			EndPortal endPortal = (EndPortal) entity;

			xMLStreamWriter.writeStartElement("Activated");
			xMLStreamWriter.writeCharacters(endPortal.isActivated()+"");
			xMLStreamWriter.writeEndElement();

			xMLStreamWriter.writeEndElement();
		}

		xMLStreamWriter.writeEndElement();
	}
}
