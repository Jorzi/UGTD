/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package units;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Göran
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

    public Tower(int tileX, int tileY) {
        this.tileX = tileX;
        this.tileY = tileY;
        try {
            baseImage = ImageIO.read(new File("turretbase1.png"));
            turretImage = ImageIO.read(new File("turret1.png"));
        } catch (IOException e) {
            System.out.println("couldn't find image");
        }
    }
    
    
    public void update(){
        angle += da;
    }
    
    public void paint(Graphics g, ImageObserver imOb) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(tileX * 16, tileY * 16);
        g2d.drawImage(baseImage, 0, 0, imOb);
        AffineTransform a = g2d.getTransform();
        g2d.translate(centerX, centerX); //hack takes into account the varying heights(->positions) of the images
        g2d.rotate(angle);
        g2d.translate(-centerX, -centerY);
        g2d.drawImage(turretImage, 0, 0, imOb);
        g2d.setTransform(a);
        
    }
    
}
