package utility;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SpriteSheet {

    private int sheetWidth;
    private int sheetHeight;

    private final int TILE_SIZE = 16;

    private int[][] sheetColors;

    private Map<String, Sprite> tileSprites = new HashMap<>();
    private Map<String, Sprite> objectSprites = new HashMap<>();

    public SpriteSheet() {
        // try and load the image
        URL url = this.getClass().getResource("res/sheet.png");

        BufferedImage sheet = null;
        try {
            sheet = ImageIO.read(url);
            if(sheet == null) {
                System.out.println("Could not find sprite sheet");
                return;
            }
            sheetWidth = sheet.getWidth();
            sheetHeight = sheet.getHeight();

            sheetColors = new int[sheetWidth][sheetHeight];

            for(int i = 0; i < sheetWidth; i++) {
                for(int j = 0; j < sheetHeight; j++) {

                    sheetColors[i][j] = sheet.getRGB(i, j);
                }
            }
            makeSprites();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void makeSprites() {
        // TILE SPRITES
        tileSprites.put("grass", makeSprite(0, 0));
        tileSprites.put("stone1", makeSprite(0, 1));
        tileSprites.put("stone2", makeSprite(2, 1));

        tileSprites.put("sand", makeSprite(1, 1));
        tileSprites.put("dirt", makeSprite(2, 0));

        tileSprites.put("cracked1", makeSprite(0, 5));
        tileSprites.put("cracked2", makeSprite(1, 5));
        tileSprites.put("cracked3", makeSprite(2, 5));
        tileSprites.put("cracked4", makeSprite(3, 5));

        tileSprites.put("ore1", makeSprite(0, 6));
        tileSprites.put("ore2", makeSprite(1, 6));
        tileSprites.put("ore3", makeSprite(2, 6));

        tileSprites.put("wall1", makeSprite(0 ,3));
        tileSprites.put("wall2", makeSprite(1, 3));
        tileSprites.put("wall3", makeSprite(2, 3));
        tileSprites.put("wall4", makeSprite(0, 4));
        tileSprites.put("wall5", makeSprite(1, 4));

        tileSprites.put("black", makeSprite(15, 7));

        // OBJECT SPRITES
        objectSprites.put("portal", makeSprite(1, 11));
        objectSprites.put("door", makeSprite(2, 11));
        objectSprites.put("bookshelf", makeSprite(0, 14));
        objectSprites.put("chest", makeSprite(1, 14));
        objectSprites.put("coin", makeSprite(2, 14));
        objectSprites.put("lamp", makeSprite(3, 14));
        objectSprites.put("key", makeSprite(4, 14));
        objectSprites.put("portalExit", makeSprite(5, 14));
        objectSprites.put("book", makeSprite(0, 15));
        objectSprites.put("sword", makeSprite(1, 15));
        objectSprites.put("map", makeSprite(2, 15));
        objectSprites.put("armour", makeSprite(3, 15));
        objectSprites.put("potion", makeSprite(4, 15));

        objectSprites.put("goblin", makeSprite(4, 12));
        objectSprites.put("skeleton", makeSprite(5, 12));
        objectSprites.put("trader", makeSprite(4, 13));
        objectSprites.put("bat", makeSprite(5, 13));

        objectSprites.put("player", makeSprite(6, 12));
        objectSprites.put("gemstone", makeSprite(5, 15));
        objectSprites.put("spade", makeSprite(3, 13));
        objectSprites.put("endportal", makeSprite(2, 13));

        objectSprites.put("add", makeSprite(6, 14));
        objectSprites.put("remove", makeSprite(7, 14));
        objectSprites.put("eraser", makeSprite(6, 15));


        /*
        door = makeSprite(11, 2);
        ladder = makeSprite(11, 3);

        tablePart1 = makeSprite(13, 0);
        tablePart2 = makeSprite(13, 1);

        chest = makeSprite(14, 0);

        candle = makeSprite(12 ,1);
        */
    }

    private Sprite makeSprite(int x, int y) {
        BufferedImage spriteImage = new BufferedImage(TILE_SIZE, TILE_SIZE, BufferedImage.TYPE_INT_ARGB);
        int[][] colors = new int[TILE_SIZE][TILE_SIZE];

        int sumRed = 0;
        int sumGreen = 0;
        int sumBlue = 0;

        for(int i = 0; i < TILE_SIZE; i++) {
            for(int j = 0; j < TILE_SIZE; j++) {
                int hexColor = sheetColors[(TILE_SIZE * x) + i][(TILE_SIZE * y) + j];

                Color C = new Color(hexColor);
                sumRed += C.getRed();
                sumGreen += C.getGreen();
                sumBlue += C.getBlue();
                if(C.getRed() == 255 && C.getBlue() == 255) {

                }
                else {
                    spriteImage.setRGB(i, j, hexColor);
                }


                colors[i][j] = hexColor;
            }
        }
        return new Sprite(spriteImage, colors, TILE_SIZE, TILE_SIZE, new Color(sumRed/(TILE_SIZE*TILE_SIZE), sumGreen/(TILE_SIZE*TILE_SIZE), sumBlue/(TILE_SIZE*TILE_SIZE)));
    }

    public Map<String, Sprite> getTileSprites() {
        return tileSprites;
    }

    public Map<String, Sprite> getObjectSprites() {
        return objectSprites;
    }
}
