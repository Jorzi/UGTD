/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package units;

import game.GlobalConstants;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import terrain.MapTile;

/**
 *  The tower tracks and shoots at enemies within its active area.
 * 
 * @author Göran Maconi
 */
public class Tower {

    private int centerX = 16;
    private int centerY = 32;
    private int tileX;
    private int tileY;
    private double angle = 0;
    private double da = 0.01;
    private BufferedImage turretImage;
    private BufferedImage baseImage;
    
    private ArrayList<MapTile> activeArea;

    public Tower(int tileX, int tileY, ArrayList<MapTile> activeArea) {
        this.tileX = tileX;
        this.tileY = tileY;
        this.activeArea = activeArea;
        try {
            baseImage = ImageIO.read(new File("turretbase1.png"));
            turretImage = ImageIO.read(new File("turret1.png"));
        } catch (IOException e) {
            System.out.println("couldn't find image");
        }
    }

    public void update() {
        angle += da;
    }

    public void paint(Graphics g, ImageObserver imOb) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform a = g2d.getTransform();
        g2d.translate(tileX * GlobalConstants.tileSize, tileY * GlobalConstants.tileSize);
        g2d.drawImage(baseImage, 0, 0, imOb);
        g2d.translate(centerX, centerX); //hack takes into account the varying heights(->positions) of the images
        g2d.rotate(angle);
        g2d.translate(-centerX, -centerY);
        g2d.drawImage(turretImage, 0, 0, imOb);
        g2d.setTransform(a);
    }


    public int getTileX() {
        return tileX;
    }

    public int getTileY() {
        return tileY;
    }
    
    
}
