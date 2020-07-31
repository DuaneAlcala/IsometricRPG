package tests;

import editor.*;
import editor.views.ControlsFragment;
import org.junit.jupiter.api.Test;
import world.Room;

import javax.swing.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EditorTests {

    private EditorController editor = makeEditor();

    @Test
    public void testMakeEdit() {
        try {
            editor.getGui().setupEditing();

            Method pickExit = editor.getClass().getDeclaredMethod("pickExit", int.class, int.class);
            pickExit.setAccessible(true);

            Method handleClick = editor.getClass().getDeclaredMethod("handleClick", int.class, int.class);
            handleClick.setAccessible(true);

            Method placeTile = editor.getClass().getDeclaredMethod("placeTile", int.class, int.class, String.class, EditorRoom.class, TileFactory.class);
            placeTile.setAccessible(true);

            Method findPlayer = editor.getClass().getDeclaredMethod("findPlayer");
            findPlayer.setAccessible(true);

            Method createTransportTile = editor.getClass().getDeclaredMethod("createTransportTile", int.class, int.class, String.class);
            createTransportTile.setAccessible(true);

            ControlsFragment fragment = editor.getGui().getControlsFragment();
            Field f = fragment.getClass().getDeclaredField("heightsButton");
            f.setAccessible(true);
            JButton heightsButton = (JButton) f.get(fragment);

            f = fragment.getClass().getDeclaredField("eastButton");
            f.setAccessible(true);
            JButton eastButton = (JButton) f.get(fragment);

            f = fragment.getClass().getDeclaredField("southButton");
            f.setAccessible(true);
            JButton southButton = (JButton) f.get(fragment);

            f = fragment.getClass().getDeclaredField("westButton");
            f.setAccessible(true);
            JButton westButton = (JButton) f.get(fragment);

            f = fragment.getClass().getDeclaredField("northButton");
            f.setAccessible(true);
            JButton northButton = (JButton) f.get(fragment);

            f = fragment.getClass().getDeclaredField("mapButton");
            f.setAccessible(true);
            JButton mapButton = (JButton) f.get(fragment);

            // Testing view switching
            assertEquals(editor.isViewingMap(), true, "Should be viewing map");
            editor.viewSwitch();
            assertEquals(editor.isViewingRoom(), true, "Should be viewing room");

            // Testing moving between rooms
            editor.moveRoom("East");
            editor.moveRoom("South");
            editor.moveRoom("West");
            editor.moveRoom("North");
            assertEquals(editor.getCurrentRoomX(), 0, "Current room x should be 0");
            assertEquals(editor.getCurrentRoomY(), 0, "Current room y should be 0");

            eastButton.doClick();
            southButton.doClick();
            westButton.doClick();
            northButton.doClick();
            assertEquals(editor.getCurrentRoomX(), 0, "Current room x should be 0");
            assertEquals(editor.getCurrentRoomY(), 0, "Current room y should be 0");

            mapButton.doClick();
            mapButton.doClick();

            // Testing changing views through mouse click
            editor.viewSwitch();
            handleClick.invoke(editor, 160, 160);
            assertEquals(editor.isViewingRoom(), true, "Should be viewing room");
            handleClick.invoke(editor, 10, 10);

            // Testing placing tiles now
            EditorRoom room = new EditorRoom();
            TileFactory factory = editor.getTileFactory();

            placeTile.invoke(editor, 0, 0, "grass", room, factory);
            assertEquals(room.getTile(0, 0).getType(), "grass", "Should be grass");

            placeTile.invoke(editor, 0, 0, "eraser", room, factory);
            assertEquals(room.getTile(0, 0).getType(), "black", "Should be black");

            placeTile.invoke(editor, 0, 0, "grass", room, factory);
            placeTile.invoke(editor, 0, 0, "player", room, factory);
            assertEquals(room.getTile(0, 0).getTileObject(), "player", "Should be player");
            placeTile.invoke(editor, 0, 1, "player", room, factory);
            findPlayer.invoke(editor);

            placeTile.invoke(editor, 0, 1, "coin", room, factory);
            editor.viewSwitch();
            editor.update();
            editor.viewSwitch();

            // Heights
            heightsButton.doClick();

            // Creating a door
            // Making a door
            placeTile.invoke(editor,0, 0, "door", editor.getCurrentRoom(), factory);
            pickExit.invoke(editor, 350, 160);
            pickExit.invoke(editor, 0, 0);
            assertEquals(!room.getTile(0, 0).getTileObject().contains("door"), true, "Should have door");
            factory.eraseTile(editor.getCurrentRoom().getTile(0, 0), "door=0,0,0,0,0,0,0,0", editor.getMap());
            factory.eraseTile(editor.getCurrentRoom().getTile(0, 0), "door=0,0,0,0", editor.getMap());

            factory.eraseTile(editor.getCurrentRoom().getTile(0, 0), "portal=0,0,0,0", editor.getMap());
            factory.eraseTile(editor.getCurrentRoom().getTile(0, 0), "portalExit=0,0,0,0", editor.getMap());

            //factory.addTransport(editor.getMap(), editor.getCurrentRoom(), editor.getCurrentRoom().getTile(0, 0), );

            // Making a portal
            placeTile.invoke(editor, 0, 0, "eraser", room, factory);
            placeTile.invoke(editor, 0, 0, "portal", room, factory);
            pickExit.invoke(editor, 350, 160);
            pickExit.invoke(editor, 0, 1);
            assertEquals(!room.getTile(0, 1).getTileObject().contains("portal"), true, "Should have portal");

            // Set placing door
            editor.setPlacingDoor(true);
            editor.setPlacingPortal(true);
            editor.setPlacingDoor(false);
            editor.setPlacingPortal(false);
            editor.setPlacedPlayer(true);
            editor.setPlacedPlayer(false);
            editor.setPlayerLoc(null);

            RoomMap map = new RoomMap();
            EditorRoom testRoom = map.getRoom(0, 0);
            editor.getMap().setRoom(0, 0, testRoom);
            assertEquals(testRoom.isDark(), false, "Should be not dark room");
            Tile[][] tiles = testRoom.getTiles();
            testRoom = new EditorRoom(tiles, false, 20, 20);
            assertEquals(testRoom.getWidth(), 20, "Width should be 20");

            map = new RoomMap(map.getRooms(), 3, 3);

            // Make a new tile
            Tile tile = new Tile("grass", 0, "", 0, 0);
            assertEquals(tile.getX(), 0, "Tile x should be 0");
            assertEquals(tile.getY(), 0, "Tile y should be 0");

            tile = factory.changeTileHeight(tile, 1);
            assertEquals(tile.getHeight(), 1, "Should be 1");

        }catch (Exception e) {

        }
    }


    public EditorController makeEditor() {
        return new EditorController(true);
    }

}