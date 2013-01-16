/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package terrain;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Göran
 */
public class TerrainMap {

    private BufferedImage mapImage;
    private BufferedImage tile1;
    private BufferedImage tile2;
    private int[][] pixels; // tile index array
    private MapTile[][] navigationGraph;

    public TerrainMap(String mapName) {
        loadImages(mapName);

        pixels = new int[mapImage.getWidth()][mapImage.getHeight()];
        navigationGraph = new MapTile[mapImage.getWidth()][mapImage.getHeight()];

        fillTileArrays();
        generateConnectivity();
    }

    private void loadImages(String mapName) {
        try {
            mapImage = ImageIO.read(new File(mapName));
            tile1 = ImageIO.read(new File("ground1.png")); //TODO: unhardcode tileset
            tile2 = ImageIO.read(new File("rock1.png"));
        } catch (IOException e) {
            System.out.println("couldn't find image");
        }
    }

    private void fillTileArrays() {
        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[0].length; j++) {
                int value = mapImage.getRGB(i, j);
                value = value & 255;
                pixels[i][j] = value;
                if (value == 0) {
                    navigationGraph[i][j] = new MapTile(i, j);
                }
                //System.out.print(pixels[i][j] + " ");
            }
            //System.out.println();
        }
    }

    private void generateConnectivity() {
        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[0].length; j++) {
                if (navigationGraph[i][j] != null) {
                    if (i > 0 && navigationGraph[i - 1][j] != null) { // left tile
                        navigationGraph[i - 1][j].neighbours.put(navigationGraph[i][j], 1.0);
                        navigationGraph[i][j].neighbours.put(navigationGraph[i - 1][j], 1.0);
                    }
                    if (j > 0 && navigationGraph[i][j - 1] != null) { // top tile
                        navigationGraph[i][j - 1].neighbours.put(navigationGraph[i][j], 1.0);
                        navigationGraph[i][j].neighbours.put(navigationGraph[i][j - 1], 1.0);
                    }
                    if (i * j > 0 && navigationGraph[i - 1][j] != null && navigationGraph[i][j - 1] != null 
                            && navigationGraph[i - 1][j - 1] != null) { // top left tile
                        navigationGraph[i - 1][j - 1].neighbours.put(navigationGraph[i][j], Math.sqrt(2));
                        navigationGraph[i][j].neighbours.put(navigationGraph[i - 1][j - 1], Math.sqrt(2));
                    }
                    if (j > 0 && i < navigationGraph.length - 1 && navigationGraph[i + 1][j] != null 
                            && navigationGraph[i][j - 1] != null && navigationGraph[i + 1][j - 1] != null) { // top right tile
                        navigationGraph[i + 1][j - 1].neighbours.put(navigationGraph[i][j], Math.sqrt(2));
                        navigationGraph[i][j].neighbours.put(navigationGraph[i + 1][j - 1], Math.sqrt(2));
                    }
                }
            }
        }
    }

    public void paint(Graphics g, ImageObserver imOb) {
        Graphics2D g2d = (Graphics2D) g;
        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[0].length; j++) {
                if (pixels[i][j] == 0) {
                    g2d.drawImage(tile1, 16 * i, 16 * j, imOb);
                } else {
                    g2d.drawImage(tile2, 16 * i, 16 * j, imOb);
                }
            }
        }


    }
}
