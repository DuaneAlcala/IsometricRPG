package editor.views;

import editor.RoomMap;
import editor.EditorRoom;
import editor.Tile;
import utility.Sprite;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

public class EditFragment extends JPanel {

    private EditorGui gui;
    private Graphics2D g2;

    private boolean showingHeights;

    public EditFragment(EditorGui gui) {
        this.gui = gui;
        setFocusable(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(gui.getEditor().isViewingMap()) {
            drawMap(g);
        }else if(gui.getEditor().isViewingRoom()) {
            drawRoom(g);
        }
    }

    private void drawMap(Graphics g) {
        RoomMap map = gui.getEditor().getMap();

        int xUnitSize = this.getWidth() / map.getWidth();
        int yUnitSize = this.getHeight() / map.getHeight();

        for(int x = 0; x < map.getWidth(); x++) {
            for(int y = 0; y < map.getHeight(); y++) {
                EditorRoom room = map.getRoom(x, y);

                // Drawing each room
                int xTileSize = xUnitSize / room.getWidth();
                int yTileSize = yUnitSize / room.getHeight();

                if(xTileSize > 0 && yTileSize > 0) {
                    BufferedImage displayImage = new BufferedImage(xTileSize, yTileSize, BufferedImage.TYPE_INT_RGB);
                    BufferedImage roomImage = createRoomImage(room);

                    g2 = displayImage.createGraphics();
                    g2.drawImage(roomImage.getScaledInstance(xTileSize, yTileSize, Image.SCALE_AREA_AVERAGING), 0, 0, null);

                    g.drawImage(displayImage, x * xUnitSize, y * yUnitSize, xUnitSize, yUnitSize, null);
                }

                // Drawing room borders
                g.setColor(Color.BLACK);
                g.drawRect(x * xUnitSize, y * yUnitSize, xUnitSize, yUnitSize);
            }
        }

        // I want to highlight the room where the user is at
        int editX = gui.getEditor().getCurrentRoomX();
        int editY = gui.getEditor().getCurrentRoomY();

        Graphics2D graphics = (Graphics2D) g;
        graphics.setStroke(new BasicStroke(2));
        graphics.setColor(Color.RED);
        graphics.drawRect(editX * xUnitSize, editY * yUnitSize, xUnitSize, yUnitSize);
    }

    private void drawRoom(Graphics g) {
        EditorRoom room = gui.getEditor().getCurrentRoom();

        int xUnitSize = this.getWidth() / room.getWidth();
        int yUnitSize = this.getHeight() / room.getHeight();

        // Drawing tiles
        for(int x = 0; x < room.getWidth(); x++) {
            for(int y = 0; y < room.getHeight(); y++) {
                Tile tile = room.getTile(x, y);

                String tileType = tile.getType();
                BufferedImage typeSprite;

                if(tileType.equals("") || tileType.equals("black")) {
                    typeSprite = null;
                }else {
                    typeSprite = gui.getSheet().getTileSprites().get(tileType).getImage();
                }

                String tileObject = tile.getTileObject();
                tileObject = tileObject.contains("=") ? tileObject.substring(0, tileObject.indexOf("=")) : tileObject;
                BufferedImage objectSprite;

                if(tileObject.equals("")) {
                    objectSprite = null;
                }else {
                    objectSprite = gui.getSheet().getObjectSprites().get(tileObject).getImage();
                }

                if(typeSprite != null) {
                    // Drawing the sprite
                    int scale = 3;
                    for(int i = 0; i < typeSprite.getWidth(); i++) {
                        for(int j = 0; j < typeSprite.getHeight(); j++) {
                            Color typeColor = new Color(typeSprite.getRGB(i, j));

                            // If there is an object, draw it and if there isn't, draw the tile
                            if(objectSprite != null) {
                                Color objectColor = new Color(objectSprite.getRGB(i ,j));

                                // If the object has no color on it, draw the tile instead
                                if(objectColor.getRed() == 0 && objectColor.getBlue() == 0 && objectColor.getGreen() == 0) {
                                    g.setColor(typeColor);
                                }else {
                                    g.setColor(objectColor);
                                }
                                g.fillRect((x * xUnitSize) + (i * scale), (y  * yUnitSize) + (j * scale), scale, scale);
                            }else {
                                g.setColor(typeColor);
                                g.fillRect((x * xUnitSize) + (i * scale), (y  * yUnitSize) + (j * scale), scale, scale);
                            }
                        }
                    }
                }

                // Drawing the tile outlines
                g.setColor(Color.BLACK);
                g.drawRect(x * xUnitSize, y * yUnitSize, xUnitSize, yUnitSize);

                // Showing tile heights
                if(showingHeights) {
                    if(!room.getTile(x, y).getType().equals("")) {
                        String height = Integer.toString(room.getTile(x, y).getHeight());

                        //Text
                        double width = g.getFontMetrics().stringWidth(height);
                        int stringX = (int) ((x * xUnitSize) + (xUnitSize/2) - (width/2));
                        int stringY = (int) ((y * yUnitSize) + (yUnitSize/2) - (width/2));

                        g.setColor(Color.RED);
                        g.drawString(height, stringX, stringY);
                    }
                }
            }
        }
    }

    private BufferedImage createRoomImage(EditorRoom room) {
        int imageWidth = room.getWidth();
        int imageHeight = room.getHeight();
        BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);

        for(int i = 0; i < room.getWidth(); i++) {
            for(int j = 0; j < room.getHeight(); j++) {
                image.setRGB(i, j, getTileColor(room.getTile(i, j).getType()).getRGB());
            }
        }
        return image;
    }

    private Color getTileColor(String tileType) {
        Sprite tileSprite = gui.getSheet().getTileSprites().get(tileType);
        if(tileSprite == null) {
            return Color.WHITE;
        }else {
            int colorX = tileSprite.getWidth() / 2;
            int colorY = tileSprite.getHeight() / 2;
            return new Color(tileSprite.getColors()[colorX][colorY]);
        }
    }

    public boolean isShowingHeights() {
        return showingHeights;
    }

    public void setShowingHeights(boolean showingHeights) {
        this.showingHeights = showingHeights;
    }

    public void setMouseMotionListener(MouseMotionListener motionListener) {
        this.addMouseMotionListener(motionListener);
    }

    public void setMouseListener(MouseListener editListener) {
        this.addMouseListener(editListener);
    }
}
