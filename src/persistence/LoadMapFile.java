package persistence;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import editor.EditorRoom;
import editor.RoomMap;
import world.Game;
import world.Room;
import editor.Tile;
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

public class LoadMapFile {

	public static RoomMap load(File file) {

		try {
			XMLInputFactory factory = XMLInputFactory.newInstance();
			XMLEventReader eventReader = factory.createXMLEventReader(new FileReader(file));

			eventReader.nextEvent(); //Open header info tag

			eventReader.nextEvent(); //Open game tag

			eventReader.nextEvent(); //Open torch width tag
			int torchlight = Integer.parseInt((eventReader.nextEvent().asCharacters()).getData());
			eventReader.nextEvent(); //Close torch width tag

			eventReader.nextEvent(); //Open world tag

			eventReader.nextEvent(); //Open world width tag
			int worldWidth = Integer.parseInt((eventReader.nextEvent().asCharacters()).getData());
			eventReader.nextEvent(); //Close world width tag

			eventReader.nextEvent(); //Open world height tag
			int worldHeight = Integer.parseInt(eventReader.nextEvent().asCharacters().getData());
			eventReader.nextEvent(); //Close world height tag

			EditorRoom[][] rooms = new EditorRoom[worldHeight][worldWidth];

			RoomMap world = new RoomMap(rooms, worldWidth, worldHeight);

			for(int row = 0; row < worldHeight; row++) {
				for(int col = 0; col < worldWidth; col++) {
					rooms[row][col] = parseRoom(eventReader, row,col);
				}
			}

			eventReader.nextEvent(); //Close world tag
			eventReader.nextEvent(); //Close game tag

			return world;

		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Loading failure");
			return null;
		}
	}

	private static EditorRoom parseRoom(XMLEventReader eventReader, int roomRow, int roomCol) throws XMLStreamException {

		eventReader.nextEvent(); //Open room tag

		eventReader.nextEvent();
		int roomWidth = Integer.parseInt(eventReader.nextEvent().asCharacters().getData());
		eventReader.nextEvent(); //Close room width tag

		eventReader.nextEvent(); //Open room height tag
		int roomHeight = Integer.parseInt(eventReader.nextEvent().asCharacters().getData());
		eventReader.nextEvent(); //Close room height tag

		eventReader.nextEvent(); //Open room height tag
		boolean isDark = (eventReader.nextEvent().asCharacters().getData()).equals("true") ? true : false;
		eventReader.nextEvent(); //Close room height tag

		Tile[][] tiles = new Tile[roomHeight][roomWidth];

		for(int row = 0; row < roomHeight; row++) {
			for(int col = 0; col < roomWidth; col++) {
				tiles[row][col] = parseTile(eventReader, row, col);
			}
		}

		eventReader.nextEvent(); //Close room tag

		return new EditorRoom(tiles, isDark,roomWidth,roomHeight);
	}

	private static Tile parseTile(XMLEventReader eventReader, int row, int col) throws XMLStreamException {

		eventReader.nextEvent(); //Open tile tag

		eventReader.nextEvent(); //Open display type tag
		String displayType = (eventReader.nextEvent().asCharacters().getData());
		eventReader.nextEvent(); //Close display type tag

		eventReader.nextEvent(); //Open height tag
		int height = Integer.parseInt(eventReader.nextEvent().asCharacters().getData());
		eventReader.nextEvent(); //Close height tag

		String entity = "";

		XMLEvent event = eventReader.peek(); //Next tag

		if(event.isStartElement()) { //Then this tiles entity is not null
			entity = parseEntity(eventReader);
		}

		eventReader.nextEvent(); //Close tile tag

		return new Tile(displayType, height, entity, row, col);
	}

	private static String parseEntity(XMLEventReader eventReader) throws XMLStreamException {

		eventReader.nextEvent(); //Open entity tag

		XMLEvent event = eventReader.peek(); //Next tag
		StartElement startElement = event.asStartElement();
		String qName = startElement.getName().getLocalPart();

		switch(qName) {
		case "Potion":
			eventReader.nextEvent(); //Open item tag
			eventReader.nextEvent(); //read characters
			eventReader.nextEvent(); //Close item tag
			eventReader.nextEvent(); //Close entity tag
			return "potion";

		case "Key":
			eventReader.nextEvent(); //Open item tag
			eventReader.nextEvent(); //read characters
			eventReader.nextEvent(); //Close item tag
			eventReader.nextEvent(); //Close entity tag
			return "key";

		case "Lamp":
			eventReader.nextEvent(); //Open item tag
			eventReader.nextEvent(); //read characters
			eventReader.nextEvent(); //Close item tag
			eventReader.nextEvent(); //Close entity tag
			return "lamp";

		case "Spade":
			eventReader.nextEvent(); //Open item tag
			eventReader.nextEvent(); //read characters
			eventReader.nextEvent(); //Close item tag
			eventReader.nextEvent(); //Close entity tag
			return "spade";

		case "Coin":
			eventReader.nextEvent(); //Open item tag
			eventReader.nextEvent(); //read characters
			eventReader.nextEvent(); //Close item tag
			eventReader.nextEvent(); //Close entity tag
			return "coin";

		case "GemStone":
			eventReader.nextEvent(); //Open item tag
			eventReader.nextEvent(); //read characters
			eventReader.nextEvent(); //Close item tag
			eventReader.nextEvent(); //Close entity tag
			return "gemstone";

		case "Map":
			eventReader.nextEvent(); //Open item tag
			eventReader.nextEvent(); //read characters
			eventReader.nextEvent(); //Close item tag
			eventReader.nextEvent(); //Close entity tag
			return "map";

		case "Armour":
			eventReader.nextEvent(); //Open item tag
			eventReader.nextEvent(); //read characters
			eventReader.nextEvent(); //Close item tag
			eventReader.nextEvent(); //Close entity tag
			return "armour";

		case "Sword":
			eventReader.nextEvent(); //Open item tag
			eventReader.nextEvent(); //read characters
			eventReader.nextEvent(); //Close item tag
			eventReader.nextEvent(); //Close entity tag
			return "sword";

		case "Bookshelf":
			Entity b = parseBookshelf(eventReader);
			eventReader.nextEvent(); //Close entity tag
			return "bookshelf";

		case "Chest":
			Entity c = parseChest(eventReader);
			eventReader.nextEvent(); //Close entity tag
			return "chest";

		case "Backpack":
			Entity e = parseBackpack(eventReader);
			eventReader.nextEvent(); //Close entity tag
			return "backpack";

		case "Book":
			Entity book = parseBook(eventReader);
			eventReader.nextEvent(); //Close entity tag
			return "book";

		case "Trader":
			Entity trader = parseTrader(eventReader);
			eventReader.nextEvent(); //Close entity tag
			return "trader";

		case "Skeleton":
			Entity skeleton = parseSkeleton(eventReader);
			eventReader.nextEvent(); //Close entity tag
			return "skeleton";

		case "Player":
			Entity player = parsePlayer(eventReader);
			eventReader.nextEvent(); //Close entity tag
			return "player";

		case "Door":
			String door = parseDoor(eventReader);
			eventReader.nextEvent(); //Close entity tag
			return door;

		case "Portal":
			String portal = parsePortal(eventReader);
			eventReader.nextEvent(); //Close entity tag
			return portal;

		case "EndPortal":
			Entity endPortal = parseEndPortal(eventReader);
			eventReader.nextEvent(); //Close entity tag
			return "endPortal";
		}

		return null;
	}

	private static EndPortal parseEndPortal(XMLEventReader eventReader) throws XMLStreamException {

		eventReader.nextEvent(); //Open endPortal tag
		eventReader.nextEvent(); //Open endPortal characters

		eventReader.nextEvent(); //Open activated tag
		boolean activated = (eventReader.nextEvent().asCharacters().getData()).equals("true") ? true : false;
		eventReader.nextEvent(); //Close activated tag

		eventReader.nextEvent(); //Close endPoratl tag

		return new EndPortal(activated);
	}

	private static String parsePortal(XMLEventReader eventReader) throws NumberFormatException, XMLStreamException {

		eventReader.nextEvent(); //Open portal tag
		eventReader.nextEvent(); //Open portal characters

		int[] integers = new int[4]; //The four integers stored in this node for room row and col and tile row and col

		for(int index = 0; index < 4; index++) {
			eventReader.nextEvent(); //Open int tag
			integers[index] = Integer.parseInt(eventReader.nextEvent().asCharacters().getData());
			eventReader.nextEvent(); //close int tag
		}

		eventReader.nextEvent(); //close portal tag


		return "portal=" + integers[0] + "," + integers[1] + "," + integers[2] + "," + integers[3];
	}

	private static String parseDoor(XMLEventReader eventReader) throws XMLStreamException {

		eventReader.nextEvent(); //Open door tag
		eventReader.nextEvent(); //Open door characters


		eventReader.nextEvent(); //Open locked tag
		boolean locked = (eventReader.nextEvent().asCharacters().getData()).equals("true") ? true : false;
		eventReader.nextEvent(); //Close locked tag

		int[] integers = new int[4]; //The four integers stored in this node for room row and col and tile row and col

		for(int index = 0; index < 4; index++) {
			eventReader.nextEvent(); //Open int tag
			integers[index] = Integer.parseInt(eventReader.nextEvent().asCharacters().getData());
			eventReader.nextEvent(); //close int tag
		}

		eventReader.nextEvent(); //close door tag

		return "door=" + integers[0] + "," + integers[1] + "," + integers[2] + "," + integers[3];
	}

	private static Player parsePlayer(XMLEventReader eventReader) throws XMLStreamException {

		eventReader.nextEvent(); //Open player tag
		eventReader.nextEvent(); //Open player chars

		List<Item> items = new ArrayList<Item>();

		XMLEvent event = eventReader.peek();

		while(event.isStartElement() && event.asStartElement().getName().getLocalPart().equals("Entity")) {
			parseEntity(eventReader);

			event = eventReader.peek();
		}

		eventReader.nextEvent(); //Open holdindex tag
		String hold = eventReader.nextEvent().asCharacters().getData();
		eventReader.nextEvent(); //close holdindex tag

		eventReader.nextEvent(); //Open health tag
		int health  = Integer.parseInt(eventReader.nextEvent().asCharacters().getData());
		eventReader.nextEvent(); //close health tag

		eventReader.nextEvent(); //close player tag
		
		eventReader.close();

		return new Player(items, (hold.equals("null") ? 0 : Integer.parseInt(hold)), health);
	}

	private static Skeleton parseSkeleton(XMLEventReader eventReader) throws XMLStreamException {

		eventReader.nextEvent(); //Open skeleton tag
		eventReader.nextEvent(); //Open skeleton chars

		eventReader.nextEvent(); //close skeleton tag

		return new Skeleton();
	}

	private static Trader parseTrader(XMLEventReader eventReader) throws XMLStreamException {

		eventReader.nextEvent(); //Open trader tag
		eventReader.nextEvent(); //Open trader chars

		List<Item> items = new ArrayList<Item>();

		XMLEvent event = eventReader.peek();

		while(event.isStartElement()) {
			parseEntity(eventReader);

			event = eventReader.peek();
		}

		eventReader.nextEvent(); //Close trader tag


		return new Trader(items);
	}

	private static Book parseBook(XMLEventReader eventReader) throws XMLStreamException {

		eventReader.nextEvent(); //Open book tag

		String contents = eventReader.nextEvent().asCharacters().getData();

		eventReader.nextEvent(); //Close book tag

		return new Book(contents);
	}

	private static Backpack parseBackpack(XMLEventReader eventReader) throws XMLStreamException {

		eventReader.nextEvent(); //Open book tag
		eventReader.nextEvent(); //Open book tag
		eventReader.nextEvent(); //Open book tag

		/*
		 * DUANE STILL AINT DONE THE CARRYS SO I CANT DO THIS ONE :( :( :( :(
		 */

		return new Backpack();
	}

	private static Chest parseChest(XMLEventReader eventReader) throws XMLStreamException {

		eventReader.nextEvent(); //Open Chest tag
		eventReader.nextEvent(); //Open Chest chars

		List<Item> items = new ArrayList<Item>();

		XMLEvent event = eventReader.peek();

		while(event.isStartElement()) {
			parseEntity(eventReader);

			event = eventReader.peek();
		}

		eventReader.nextEvent(); //Close Chest tag

		return new Chest(items);
	}

	private static Bookshelf parseBookshelf(XMLEventReader eventReader) throws XMLStreamException {

		eventReader.nextEvent(); //Open Bookshelf tag
		eventReader.nextEvent(); //Open Bookshelf chars

		List<Item> items = new ArrayList<Item>();

		XMLEvent event = eventReader.peek();

		while(event.isStartElement()) {
			parseEntity(eventReader);

			event = eventReader.peek();
		}

		eventReader.nextEvent(); //Close Bookshelf tag

		return new Bookshelf(items);
	}
}
