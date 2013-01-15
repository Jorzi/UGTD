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
    private int[][] pixels;

    public TerrainMap(String mapName) {
        try {
            mapImage = ImageIO.read(new File(mapName));
            tile1 = ImageIO.read(new File("ground1.png"));
            tile2 = ImageIO.read(new File("rock1.png"));
        } catch (IOException e) {
            System.out.println("couldn't find image ");
        }
        pixels = new int[mapImage.getWidth()][mapImage.getHeight()];

        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[0].length; j++) {
                pixels[i][j] = mapImage.getRGB(i, j);
                System.out.print(pixels[i][j]);
            }
            System.out.println();
        }
    }
    public void paint(Graphics g, ImageObserver imOb) {
        Graphics2D g2d = (Graphics2D) g;
        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[0].length; j++) {
                if(pixels[i][j] == -16777216){
                    g2d.drawImage(tile1, 16 * i, 16 * j, imOb);
                }else{
                    g2d.drawImage(tile2, 16 * i, 16 * j, imOb);
                }
            }
        }


    }
}
