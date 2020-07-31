package gui;

import utility.Sprite;
import utility.SpriteSheet;
import world.Game;
import world.Room;
import world.Tile;
import world.World;
import world.entities.mobs.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * This class is created to handle the Frame which w
 *
 * @author sailymahe
 */
public class MapDialog extends JDialog {

    private JPanel mapPanel;
    private JButton closeButton;

    private int panelWidth = 250;
    private int panelHeight = 250;

    private SpriteSheet sheet = new SpriteSheet();

    // Change this shit to g.draw

    public MapDialog(Game game, Player player) {
        super(game.getGui().getFrame(), "Map", false);

        mapPanel = new JPanel();
        mapPanel.setPreferredSize(new Dimension(panelWidth, panelHeight));
        mapPanel.setSize(new Dimension(panelWidth, panelHeight));

        // I want to try a 400 * 400
        BufferedImage mapImage = new BufferedImage(400, 400, BufferedImage.TYPE_INT_RGB);

        World world = game.getWorld();
        for(int x = 0; x < world.getWidth(); x++) {
            for(int y = 0; y < world.getHeight(); y++) {
                Room room = world.getRoom(x, y);
                int roomWidth = room.getWidth();
                int roomHeight = room.getHeight();


                for(int i = 0; i < room.getWidth(); i++) {
                    for(int j = 0; j < room.getHeight(); j++) {
                        // Sample the image from the center of the tile\
                        if(room.getTile(i, j) != null) {
                            Tile tile = room.getTile(i, j);
                            String display = tile.getDisplayType();

                            Sprite sprite = sheet.getTileSprites().get(display);
                            int startX = sprite.getWidth() / 2;
                            int startY = sprite.getHeight() / 2;

                            int one = sprite.getColors()[startX][startY];
                            int two = sprite.getColors()[startX + 1][startY + 1];

                            int k = (x * roomWidth) + i;
                            int l = (y * roomHeight) + j;
                            int k2 = (k * 2) + 1;
                            int l2 = (l * 2) + 1;

                            mapImage.setRGB(k, l, one);
                            mapImage.setRGB(k2, l2, one);

                        }else {
                            int k = (x * roomWidth) + i;
                            int l = (y * roomHeight) + j;
                            int k2 = (k * 2) + 1;
                            int l2 = (l * 2) + 1;

                            mapImage.setRGB(k, l, 0xffffff);
                            mapImage.setRGB(k2, l2, 0xffffff);
                        }
                    }
                }
            }
        }
        JLabel imageLabel = new JLabel(new ImageIcon(mapImage));
        imageLabel.setAlignmentX(Window.CENTER_ALIGNMENT);
        mapPanel.add(imageLabel);

        add(imageLabel);
        pack();
        setLocationRelativeTo(game.getGui().getFrame());
        setVisible(true);
    }
}
