package utility;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class Sprite {

    private BufferedImage image;
    private int[][] colors;
    private int width;
    private int height;
    private Color avgColor;

    public Sprite(BufferedImage image, int[][] colors, int width, int height, Color avgColor) {
        this.image = image;
        this.colors = colors;
        this.width = width;
        this.height = height;
        this.avgColor = avgColor;
    }

    public Color getAvgColor() {
        return avgColor;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public BufferedImage getImage() {
        return image;
    }

    public int[][] getColors() {
        return colors;
    }
}
