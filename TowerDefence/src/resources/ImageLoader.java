/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package resources;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.imageio.ImageIO;

/**
 *Contains tools for centralized loading and accessing of image resources
 * @author Göran
 */
public class ImageLoader {
    
    public static HashMap<String, BufferedImage> imageLibrary = new HashMap<>();

    
    
    /**
     * Reads images from files and stores them in the image library by a name tag
     */
    public static void loadImages(){
        //TODO: add automated loading
        try {
            imageLibrary.put("map1", ImageIO.read(new File("map1.png")));
        } catch (IOException e) {
            System.out.println("couldn't find image");
        }
        try {
            imageLibrary.put("map2", ImageIO.read(new File("map2.png")));
        } catch (IOException e) {
            System.out.println("couldn't find image");
        }
        try {
            imageLibrary.put("sandTile1", ImageIO.read(new File("ground1.png")));
        } catch (IOException e) {
            System.out.println("couldn't find image");
        }
        try {
            imageLibrary.put("rockTile1", ImageIO.read(new File("rock1.png")));
        } catch (IOException e) {
            System.out.println("couldn't find image");
        }
        try {
            imageLibrary.put("hqBunker", ImageIO.read(new File("bunker1.png")));
        } catch (IOException e) {
            System.out.println("couldn't find image");
        }
        try {
            imageLibrary.put("cannonball", ImageIO.read(new File("shell1.png")));
        } catch (IOException e) {
            System.out.println("couldn't find image");
        }
        try {
            imageLibrary.put("explosion1", ImageIO.read(new File("boom1_spritesheet.png")));
        } catch (IOException e) {
            System.out.println("couldn't find image");
        }
        try {
            imageLibrary.put("tank1", ImageIO.read(new File("tank1.png")));
        } catch (IOException e) {
            System.out.println("couldn't find image");
        }
        try {
            imageLibrary.put("towerBase", ImageIO.read(new File("turretbase1.png")));
        } catch (IOException e) {
            System.out.println("couldn't find image");
        }
        try {
            imageLibrary.put("turret1", ImageIO.read(new File("turret1.png")));
        } catch (IOException e) {
            System.out.println("couldn't find image");
        }
        try {
            imageLibrary.put("tileset", ImageIO.read(new File("tileset.png")));
        } catch (IOException e) {
            System.out.println("couldn't find image");
        }
        
    }
}
