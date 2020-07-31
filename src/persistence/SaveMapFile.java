package persistence;

import java.io.IOException;
import java.io.PrintStream;
import java.io.StringWriter;
import java.util.Scanner;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import editor.EditorRoom;
import editor.RoomMap;
import editor.Tile;

import java.io.File;
import java.io.FileOutputStream;

import world.Game;
import world.Room;
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

public class SaveMapFile {

	public static void save(RoomMap map, String filename) {
		try {

			StringWriter stringWriter = new StringWriter();

			XMLOutputFactory xMLOutputFactory = XMLOutputFactory.newInstance();
			XMLStreamWriter xMLStreamWriter = xMLOutputFactory.createXMLStreamWriter(stringWriter);

			//Game node
			xMLStreamWriter.writeStartDocument();
			xMLStreamWriter.writeStartElement("Game");

			xMLStreamWriter.writeStartElement("TorchLight");
			xMLStreamWriter.writeCharacters(100+"");
			xMLStreamWriter.writeEndElement();

			//World node
			xMLStreamWriter.writeStartElement("World");

			writeWorldDimension(xMLStreamWriter, map);

			for(int row = 0; row < map.getHeight(); row++) {
				for(int col = 0; col < map.getWidth(); col++) {
					writeRoom(xMLStreamWriter, map, row, col);
				}
			}

			xMLStreamWriter.writeEndElement();
			xMLStreamWriter.writeEndElement();

			xMLStreamWriter.writeEndDocument();

			xMLStreamWriter.flush();
			xMLStreamWriter.close();

			String xmlString = stringWriter.getBuffer().toString();

			stringWriter.close();

			try (PrintStream out = new PrintStream(new FileOutputStream(filename+".xml"))) {
				out.print(xmlString);
				out.close();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	private static void writeWorldDimension(XMLStreamWriter xMLStreamWriter, RoomMap world) throws XMLStreamException {
		//World width node
		xMLStreamWriter.writeStartElement("WorldWidth");
		xMLStreamWriter.writeCharacters(world.getWidth()+"");
		xMLStreamWriter.writeEndElement();

		//World width node
		xMLStreamWriter.writeStartElement("WorldHeight");
		xMLStreamWriter.writeCharacters(world.getHeight()+"");
		xMLStreamWriter.writeEndElement();
	}

	private static void writeRoom(XMLStreamWriter xMLStreamWriter, RoomMap map, int roomRow, int roomCol) throws XMLStreamException {
		EditorRoom room = map.getRoom(roomRow, roomCol);

		xMLStreamWriter.writeStartElement("Room");

		xMLStreamWriter.writeStartElement("RoomWidth");
		xMLStreamWriter.writeCharacters(room.getWidth()+"");
		xMLStreamWriter.writeEndElement();

		xMLStreamWriter.writeStartElement("RoomHeight");
		xMLStreamWriter.writeCharacters(room.getHeight()+"");
		xMLStreamWriter.writeEndElement();

		xMLStreamWriter.writeStartElement("IsDark");
		xMLStreamWriter.writeCharacters(room.isDark()+"");
		xMLStreamWriter.writeEndElement();

		for(int row = 0; row < room.getHeight(); row++) {
			for(int col = 0; col < room.getWidth(); col++) {
					writeTile(xMLStreamWriter, room, row, col);
			}
		}

		xMLStreamWriter.writeEndElement();
	}



	private static void writeTile(XMLStreamWriter xMLStreamWriter, EditorRoom room, int row, int col) throws XMLStreamException {
		Tile tile = room.getTile(row, col);

		xMLStreamWriter.writeStartElement("Tile");

		xMLStreamWriter.writeStartElement("DisplayType");
		xMLStreamWriter.writeCharacters(tile.getType());
		xMLStreamWriter.writeEndElement();

		xMLStreamWriter.writeStartElement("Height");
		xMLStreamWriter.writeCharacters(tile.getHeight()+"");
		xMLStreamWriter.writeEndElement();

		String tileObject = tile.getTileObject();

		if(!tileObject.equals("")) {
			writeEntity(xMLStreamWriter, tileObject);
		}

		xMLStreamWriter.writeEndElement();
	}

	public static void writeEntity(XMLStreamWriter xMLStreamWriter, String tileObject) throws XMLStreamException{
		xMLStreamWriter.writeStartElement("Entity");

		switch(tileObject) {

		case "coin":
			xMLStreamWriter.writeStartElement("Coin");
			xMLStreamWriter.writeCharacters("Coin");
			xMLStreamWriter.writeEndElement();
			break;

		case "lamp":
			xMLStreamWriter.writeStartElement("Lamp");
			xMLStreamWriter.writeCharacters("Lamp");
			xMLStreamWriter.writeEndElement();
			break;

		case "key":
			xMLStreamWriter.writeStartElement("Key");
			xMLStreamWriter.writeCharacters("Key");
			xMLStreamWriter.writeEndElement();
			break;

		case "book":
			xMLStreamWriter.writeStartElement("Book");
			xMLStreamWriter.writeCharacters("Book");
			xMLStreamWriter.writeEndElement();
			break;

		case "sword":
			xMLStreamWriter.writeStartElement("Sword");
			xMLStreamWriter.writeCharacters("Sword");
			xMLStreamWriter.writeEndElement();
			break;

		case "map":
			xMLStreamWriter.writeStartElement("Map");
			xMLStreamWriter.writeCharacters("Map");
			xMLStreamWriter.writeEndElement();
			break;

		case "armour":
			xMLStreamWriter.writeStartElement("Armour");
			xMLStreamWriter.writeCharacters("Armour");
			xMLStreamWriter.writeEndElement();
			break;

		case "potion":
			xMLStreamWriter.writeStartElement("Potion");
			xMLStreamWriter.writeCharacters("Potion");
			xMLStreamWriter.writeEndElement();
			break;

		case "gemstone":
			xMLStreamWriter.writeStartElement("GemStone");
			xMLStreamWriter.writeCharacters("GemStone");
			xMLStreamWriter.writeEndElement();
			break;

		case "spade":
			xMLStreamWriter.writeStartElement("Spade");
			xMLStreamWriter.writeCharacters("Spade");
			xMLStreamWriter.writeEndElement();
			break;

		case "bookshelf":
			xMLStreamWriter.writeStartElement("Bookshelf");
			xMLStreamWriter.writeCharacters("Bookshelf");
			xMLStreamWriter.writeEndElement();
			break;

		case "chest":
			xMLStreamWriter.writeStartElement("Chest");
			xMLStreamWriter.writeCharacters("Chest");
			xMLStreamWriter.writeEndElement();
			break;

		case "skeleton":
			xMLStreamWriter.writeStartElement("Skeleton");
			xMLStreamWriter.writeCharacters("Skeleton");
			xMLStreamWriter.writeEndElement();
			break;

		case "trader":
			xMLStreamWriter.writeStartElement("Trader");
			xMLStreamWriter.writeCharacters("Trader");
			xMLStreamWriter.writeEndElement();
			break;

		case "player":
			xMLStreamWriter.writeStartElement("Player");
			xMLStreamWriter.writeCharacters("Player");
			xMLStreamWriter.writeStartElement("holdIndex");
			xMLStreamWriter.writeCharacters(0+"");
			xMLStreamWriter.writeEndElement();

			xMLStreamWriter.writeStartElement("Health");
			xMLStreamWriter.writeCharacters(10+"");
			xMLStreamWriter.writeEndElement();
			xMLStreamWriter.writeEndElement();
			break;

		case "endportal":
			xMLStreamWriter.writeStartElement("EndPortal");
			xMLStreamWriter.writeCharacters("EndPortal");
			xMLStreamWriter.writeStartElement("Activated");
			xMLStreamWriter.writeCharacters("false");
			xMLStreamWriter.writeEndElement();
			xMLStreamWriter.writeEndElement();
			break;

		case "backpack":
			xMLStreamWriter.writeStartElement("Bacpack");
			xMLStreamWriter.writeCharacters("Backpack");
			xMLStreamWriter.writeEndElement();
			break;
		}

		if(tileObject.startsWith("door")) {
			writeDoor(xMLStreamWriter, tileObject);

		}else if(tileObject.startsWith("portal")) {
			writePortal(xMLStreamWriter, tileObject);
		}

		xMLStreamWriter.writeEndElement();
	}


	private static void writePortal(XMLStreamWriter xMLStreamWriter, String tileObject) throws XMLStreamException {
		Scanner scan = new Scanner(tileObject);
		scan.useDelimiter("=");
		scan.next();
		scan.useDelimiter(",");
		int roomRow = Integer.parseInt(scan.next());
		int roomCol = Integer.parseInt(scan.next());
		int tileRow = Integer.parseInt(scan.next());
		int tileCol = Integer.parseInt(scan.next());

		xMLStreamWriter.writeStartElement("Portal");
		xMLStreamWriter.writeCharacters("Portal");

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

	private static void writeDoor(XMLStreamWriter xMLStreamWriter, String tileObject) throws XMLStreamException {
		Scanner scan = new Scanner(tileObject);
		scan.useDelimiter(",");
		scan.next(); //read door=fromMapC
		scan.next();
		scan.next();
		scan.next();
		int roomRow = Integer.parseInt(scan.next());
		int roomCol = Integer.parseInt(scan.next());
		int tileRow = Integer.parseInt(scan.next());
		int tileCol = Integer.parseInt(scan.next());

		xMLStreamWriter.writeStartElement("Door");
		xMLStreamWriter.writeCharacters("Door");

		xMLStreamWriter.writeStartElement("Locked");
		xMLStreamWriter.writeCharacters(true+"");
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



























}
