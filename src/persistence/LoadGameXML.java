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
 * This class loads a Game objects from an xml object that has the game state objects saved.
 *
 * @author riadsher
 *
 */
public class LoadGameXML {

	/**
	 * This method creates a Game object by reading information about the game and its state from the file given in the parameters.
	 *
	 * @param file the file that has the game state saved in
	 * @return a Game object that describes the state of the game saved in the file
	 */
	public static Game load(File file) {

		try {
			/*
			 * setting the xml reader and directing the xml reader to the file specified.
			 */
			XMLInputFactory factory = XMLInputFactory.newInstance();
			XMLEventReader eventReader = factory.createXMLEventReader(new FileReader(file));
			/*
			 * reading the values for the torch light, width and height of the game
			 */
			eventReader.nextEvent();

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

			Room[][] rooms = new Room[worldHeight][worldWidth];

			/*
			 * Creating the world inside the game
			 */
			World world = new World(rooms, worldWidth, worldHeight);
			/*
			 * Iterating through the rooms that are saved in the file and parsing each room.
			 */
			for(int row = 0; row < worldHeight; row++) {
				for(int col = 0; col < worldWidth; col++) {
					rooms[row][col] = parseRoom(eventReader, row,col);
				}
			}

			/*
			 * Checking for the closing tags
			 */
			eventReader.nextEvent(); //Close world tag
			eventReader.nextEvent(); //Close game tag
			
			eventReader.close();

			/*
			 * These methods search for tiles and rooms
			 */
			Tile playerTile = findPlayerTile(world);
			Room playerRoom = findPlayerRoom(world);
			/*
			 * Creating a player from the entity
			 */
			Player player = (Player) playerTile.getEntity();

			/*
			 * Setting the player tiles
			 */
			player.setTile(playerTile);
			player.setRoom(playerRoom);

			/*
			 * Creating a new game
			 */
			Game game = new Game(player, playerRoom, world, torchlight);
			//setting the fields
			player.setGame(game);

			return game;

		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Loading failure");
			return null;
		}
	}

	/**
	 * This method is used as a helper method to parse the content of a room.
	 *
	 * @param eventReader the eventwriter that is used to read the content of the room from the file
	 * @param roomRow this is the row position of the room in the matrix of rooms in the world
	 * @param roomCol this is the col position of the room in the matrix of rooms in the world
	 * @return a room object with all the the fields that it contains
	 * @throws XMLStreamException
	 */
	private static Room parseRoom(XMLEventReader eventReader, int roomRow, int roomCol) throws XMLStreamException {

		eventReader.nextEvent(); //Open room tag

		eventReader.nextEvent(); //Open room width tag
		int roomWidth = Integer.parseInt(eventReader.nextEvent().asCharacters().getData());
		eventReader.nextEvent(); //Close room width tag

		eventReader.nextEvent(); //Open room height tag
		int roomHeight = Integer.parseInt(eventReader.nextEvent().asCharacters().getData());
		eventReader.nextEvent(); //Close room height tag

		eventReader.nextEvent(); //Open room height tag
		boolean isDark = (eventReader.nextEvent().asCharacters().getData()).equals("true") ? true : false;
		eventReader.nextEvent(); //Close room height tag

		/*
		 * Creating a matrix of tiles that are inside the room
		 */
		Tile[][] tiles = new Tile[roomHeight][roomWidth];


		for(int row = 0; row < roomHeight; row++) {
			for(int col = 0; col < roomWidth; col++) {
				/*
				 * This method parses the content of a tile
				 */
				tiles[row][col] = parseTile(eventReader, row, col);
			}
		}

		eventReader.nextEvent(); //Close room tag

		return new Room(tiles, isDark,roomRow,roomCol,roomWidth,roomHeight);
	}

	/**
	 * This is a private helper method that parses the content of a tile and is called from inside the parseRoom method.
	 *
	 * @param eventReader the eventwriter that is used to read the content of the room from the file
	 * @param row the row position of the tile in the matirx of rooms
	 * @param col the row position of the tile in the matirx of rooms
	 * @return a tile objectsthat has all the fields in the xml file
	 * @throws XMLStreamException
	 */
	private static Tile parseTile(XMLEventReader eventReader, int row, int col) throws XMLStreamException {

		eventReader.nextEvent(); //Open tile tag

		eventReader.nextEvent(); //Open display type tag
		String displayType = (eventReader.nextEvent().asCharacters().getData());
		eventReader.nextEvent(); //Close display type tag

		eventReader.nextEvent(); //Open height tag
		int height = Integer.parseInt(eventReader.nextEvent().asCharacters().getData());
		eventReader.nextEvent(); //Close height tag

		Entity entity = null;

		XMLEvent event = eventReader.peek(); //Next tag

		/*
		 * We check if an entity exists and if it does then it is parsed
		 */
		if(event.isStartElement()) { //Then this tiles entity is not null
			entity = parseEntity(eventReader);
		}

		eventReader.nextEvent(); //Close tile tag

		return new Tile(displayType, height, entity, row, col);
	}

	/**
	 * This is a private helper method that parses the contents of an entity.
	 *
	 * @param eventReader the eventwriter that is used to read the content of the room from the file
	 * @return an entity that has the content between the xml tags
	 * @throws XMLStreamException
	 */
	private static Entity parseEntity(XMLEventReader eventReader) throws XMLStreamException {

		eventReader.nextEvent(); //Open entity tag

		XMLEvent event = eventReader.peek(); //Next tag
		StartElement startElement = event.asStartElement();
		String qName = startElement.getName().getLocalPart();

		/*
		 * a switch-case that has all the possible values that the entity could have
		 */
		switch(qName) {
		case "Potion":
			eventReader.nextEvent(); //Open item tag
			eventReader.nextEvent(); //read characters
			eventReader.nextEvent(); //Close item tag
			eventReader.nextEvent(); //Close entity tag
			return new Potion();

		case "Key":
			eventReader.nextEvent(); //Open item tag
			eventReader.nextEvent(); //read characters
			eventReader.nextEvent(); //Close item tag
			eventReader.nextEvent(); //Close entity tag
			return new Key();

		case "Lamp":
			eventReader.nextEvent(); //Open item tag
			eventReader.nextEvent(); //read characters
			eventReader.nextEvent(); //Close item tag
			eventReader.nextEvent(); //Close entity tag
			return new Lamp();

		case "Spade":
			eventReader.nextEvent(); //Open item tag
			eventReader.nextEvent(); //read characters
			eventReader.nextEvent(); //Close item tag
			eventReader.nextEvent(); //Close entity tag
			return new Spade();

		case "Coin":
			eventReader.nextEvent(); //Open item tag
			eventReader.nextEvent(); //read characters
			eventReader.nextEvent(); //Close item tag
			eventReader.nextEvent(); //Close entity tag
			return new Coin();

		case "GemStone":
			eventReader.nextEvent(); //Open item tag
			eventReader.nextEvent(); //read characters
			eventReader.nextEvent(); //Close item tag
			eventReader.nextEvent(); //Close entity tag
			return new GemStone();

		case "Map":
			eventReader.nextEvent(); //Open item tag
			eventReader.nextEvent(); //read characters
			eventReader.nextEvent(); //Close item tag
			eventReader.nextEvent(); //Close entity tag
			return new Map();

		case "Armour":
			eventReader.nextEvent(); //Open item tag
			eventReader.nextEvent(); //read characters
			eventReader.nextEvent(); //Close item tag
			eventReader.nextEvent(); //Close entity tag
			return new Armour();

		case "Sword":
			eventReader.nextEvent(); //Open item tag
			eventReader.nextEvent(); //read characters
			eventReader.nextEvent(); //Close item tag
			eventReader.nextEvent(); //Close entity tag
			return new Sword();

		case "Bookshelf":
			Entity b = parseBookshelf(eventReader);
			eventReader.nextEvent(); //Close entity tag
			return b;

		case "Chest":
			Entity c = parseChest(eventReader);
			eventReader.nextEvent(); //Close entity tag
			return c;

		case "Backpack":
			Entity e = parseBackpack(eventReader);
			eventReader.nextEvent(); //Close entity tag
			return e;

		case "Book":
			Entity book = parseBook(eventReader);
			eventReader.nextEvent(); //Close entity tag
			return book;

		case "Trader":
			Entity trader = parseTrader(eventReader);
			eventReader.nextEvent(); //Close entity tag
			return trader;

		case "Skeleton":
			Entity skeleton = parseSkeleton(eventReader);
			eventReader.nextEvent(); //Close entity tag
			return skeleton;

		case "Player":
			Entity player = parsePlayer(eventReader);
			eventReader.nextEvent(); //Close entity tag
			return player;

		case "Door":
			Entity door = parseDoor(eventReader);
			eventReader.nextEvent(); //Close entity tag
			return door;

		case "Portal":
			Entity portal = parsePortal(eventReader);
			eventReader.nextEvent(); //Close entity tag
			return portal;

		case "EndPortal":
			Entity endPortal = parseEndPortal(eventReader);
			eventReader.nextEvent(); //Close entity tag
			return endPortal;
		}

		return null;
	}

	/**
	 * This is a private helper method that parses an endPortal and is called if an endpPortal exists.
	 *
	 * @param eventReader the eventwriter that is used to read the content of the room from the file
	 * @return an EndPortal object that has all the fields that are between the tags
	 * @throws XMLStreamException
	 */
	private static EndPortal parseEndPortal(XMLEventReader eventReader) throws XMLStreamException {

		eventReader.nextEvent(); //Open endPortal tag
		eventReader.nextEvent(); //Open endPortal characters

		eventReader.nextEvent(); //Open activated tag
		boolean activated = (eventReader.nextEvent().asCharacters().getData()).equals("true") ? true : false;
		eventReader.nextEvent(); //Close activated tag

		eventReader.nextEvent(); //Close endPoratl tag

		return new EndPortal(activated);
	}

	/**
	 * This is a private helper method that parses an Portal and is called if an endpPortal exists.
	 *
	 * @param eventReader the eventwriter that is used to read the content of the room from the file
	 * @return a Portal object that has all the fields that are between the tags
	 * @throws NumberFormatException
	 * @throws XMLStreamException
	 */
	private static Portal parsePortal(XMLEventReader eventReader) throws NumberFormatException, XMLStreamException {

		eventReader.nextEvent(); //Open portal tag
		eventReader.nextEvent(); //Open portal characters

		int[] integers = new int[4]; //The four integers stored in this node for room row and col and tile row and col

		for(int index = 0; index < 4; index++) {
			eventReader.nextEvent(); //Open int tag
			integers[index] = Integer.parseInt(eventReader.nextEvent().asCharacters().getData());
			eventReader.nextEvent(); //close int tag
		}

		eventReader.nextEvent(); //close portal tag


		return new Portal(integers[0], integers[1], integers[2], integers[3]);
	}

	/**
	 * This is a private helper method that parses a Door object.
	 *
	 * @param eventReader the eventwriter that is used to read the content of the room from the file
	 * @return the Door that has all the fields between the tags
	 * @throws XMLStreamException
	 */
	private static Door parseDoor(XMLEventReader eventReader) throws XMLStreamException {

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

		return new Door(integers[0], integers[1], integers[2], integers[3], locked);
	}

	/**
	 * This is a private helper method that parses the player objects.
	 *
	 * @param eventReader the eventwriter that is used to read the content of the room from the file
	 * @return a Player object that has all the fields between the tags
	 * @throws XMLStreamException
	 */
	private static Player parsePlayer(XMLEventReader eventReader) throws XMLStreamException {

		eventReader.nextEvent(); //Open player tag
		eventReader.nextEvent(); //Open player chars

		List<Item> items = new ArrayList<Item>();

		XMLEvent event = eventReader.peek();

		while(event.isStartElement() && event.asStartElement().getName().getLocalPart().equals("Entity")) {
			items.add((Item) parseEntity(eventReader));

			event = eventReader.peek();
		}

		eventReader.nextEvent(); //Open holdindex tag
		String hold = eventReader.nextEvent().asCharacters().getData();
		eventReader.nextEvent(); //close holdindex tag

		eventReader.nextEvent(); //Open health tag
		int health  = Integer.parseInt(eventReader.nextEvent().asCharacters().getData());
		eventReader.nextEvent(); //close health tag

		eventReader.nextEvent(); //close player tag

		return new Player(items, (hold.equals("null") ? 0 : Integer.parseInt(hold)), health);
	}

	/**
	 * This is a private helper method that parses the Skeleton objects.
	 *
	 * @param eventReader the eventwriter that is used to read the content of the room from the file
	 * @return a Skeleton object that has all the fields between the tags
	 * @throws XMLStreamException
	 */
	private static Skeleton parseSkeleton(XMLEventReader eventReader) throws XMLStreamException {

		eventReader.nextEvent(); //Open skeleton tag
		eventReader.nextEvent(); //Open skeleton chars


		eventReader.nextEvent(); //close skeleton tag

		return new Skeleton();
	}

	/**
	 * This is a private helper method that parses the Trade objects.
	 *
	 * @param eventReader the eventwriter that is used to read the content of the room from the file
	 * @return a Trade object that has all the fields between the tags
	 * @throws XMLStreamException
	 */
	private static Trader parseTrader(XMLEventReader eventReader) throws XMLStreamException {

		eventReader.nextEvent(); //Open trader tag
		eventReader.nextEvent(); //Open trader chars

		List<Item> items = new ArrayList<Item>();

		XMLEvent event = eventReader.peek();

		while(event.isStartElement()) {
			items.add((Item) parseEntity(eventReader));

			event = eventReader.peek();
		}

		eventReader.nextEvent(); //Close trader tag


		return new Trader(items);
	}

	/**
	 * This is a private helper method that parses the Book objects.
	 *
	 * @param eventReader the eventwriter that is used to read the content of the room from the file
	 * @return a Book object that has all the fields between the tags
	 * @throws XMLStreamException
	 */
	private static Book parseBook(XMLEventReader eventReader) throws XMLStreamException {

		eventReader.nextEvent(); //Open book tag

		String contents = eventReader.nextEvent().asCharacters().getData();

		eventReader.nextEvent(); //Close book tag

		return new Book(contents);
	}

	/**
	 * This is a private helper method that parses the Backpack objects.
	 *
	 * @param eventReader the eventwriter that is used to read the content of the room from the file
	 * @return a Backpack object that has all the fields between the tags
	 * @throws XMLStreamException
	 */
	private static Backpack parseBackpack(XMLEventReader eventReader) throws XMLStreamException {

		eventReader.nextEvent(); //Open book tag
		eventReader.nextEvent(); //Open book tag
		eventReader.nextEvent(); //Open book tag

		/*
		 * DUANE STILL AINT DONE THE CARRYS SO I CANT DO THIS ONE :( :( :( :(
		 */

		return new Backpack();
	}

	/**
	 * This is a private helper method that parses the Chest objects.
	 *
	 * @param eventReader the eventwriter that is used to read the content of the room from the file
	 * @return a Chest object that has all the fields between the tags
	 * @throws XMLStreamException
	 */
	private static Chest parseChest(XMLEventReader eventReader) throws XMLStreamException {

		eventReader.nextEvent(); //Open Chest tag
		eventReader.nextEvent(); //Open Chest chars

		List<Item> items = new ArrayList<Item>();

		XMLEvent event = eventReader.peek();

		while(event.isStartElement()) {
			items.add((Item) parseEntity(eventReader));

			event = eventReader.peek();
		}

		eventReader.nextEvent(); //Close Chest tag

		return new Chest(items);
	}

	/**
	 * This is a private helper method that parses the Bookshelf objects.
	 *
	 * @param eventReader the eventwriter that is used to read the content of the room from the file
	 * @return a Bookshelf object that has all the fields between the tags
	 * @throws XMLStreamException
	 */
	private static Bookshelf parseBookshelf(XMLEventReader eventReader) throws XMLStreamException {

		eventReader.nextEvent(); //Open Bookshelf tag
		eventReader.nextEvent(); //Open Bookshelf chars

		List<Item> items = new ArrayList<Item>();

		XMLEvent event = eventReader.peek();

		while(event.isStartElement()) {
			items.add((Item) parseEntity(eventReader));

			event = eventReader.peek();
		}

		eventReader.nextEvent(); //Close Bookshelf tag

		return new Bookshelf(items);
	}

	/**
	 * This is a method that searched within the world given in the parameter for the player tile
	 *
	 * @param world the world to search in for the player's tile
	 * @return the tile that the player is on
	 */
	private static Tile findPlayerTile(World world) {

		for(int roomRow = 0; roomRow < world.getHeight(); roomRow++) {
			for(int roomCol = 0; roomCol < world.getWidth(); roomCol++) {
				Room room = world.getRoom(roomRow, roomCol);
				for(int row = 0; row < room.getHeight(); row++) {
					for(int col = 0; col < room.getHeight(); col++) {
						Tile tile = room.getTile(row, col);
						if(tile.getEntity() != null) {
							if(tile.getEntity().getName().equals("player")) {
								return tile;
							}
						}
					}
				}
			}
		}

		return null;
	}

	/**
	 * This is a method that searched within the world given in the parameter for the player room
	 *
	 * @param world the world to search in for the player's tile
	 * @return the room that the player is on
	 */
	private static Room findPlayerRoom(World world) {

		for(int roomRow = 0; roomRow < world.getHeight(); roomRow++) {
			for(int roomCol = 0; roomCol < world.getWidth(); roomCol++) {
				Room room = world.getRoom(roomRow, roomCol);

				for(int row = 0; row < room.getHeight(); row++) {
					for(int col = 0; col < room.getHeight(); col++) {
						Tile tile = room.getTile(row, col);

						if(tile.getEntity() != null) {
							if(tile.getEntity().getName().equals("player")) {
								return room;
							}
						}
					}
				}
			}
		}
		return null;
	}
}
