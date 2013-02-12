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
import javax.imageio.ImageIO;
import resources.ImageLoader;

/**
 *
 * @author Göran
 */
public class Explosion {

    private double tileX;
    private double tileY;
    private BufferedImage image = ImageLoader.imageLibrary.get("explosion1");
    private int duration = 20;
    private int frameCount = 5;
    private int progress;
    private boolean finished;

    public Explosion(double tileX, double tileY) {
        this.tileX = tileX;
        this.tileY = tileY;
        progress = 0;
    }

    public void update() {
        progress++;
        if (progress >= duration) {
            progress = duration;
            finished = true;
        }
    }

    public void paint(Graphics g, ImageObserver imOb) {
        Graphics2D g2d = (Graphics2D) g;
        if (!finished) {
            AffineTransform a = g2d.getTransform();
            g2d.translate((tileX - 1) * GlobalConstants.tileSize, (tileY - 1) * GlobalConstants.tileSize);
            g2d.drawImage(image.getSubimage(((frameCount * progress) / duration) * 48, 0, 47, 47), 0, 0, imOb);
            g2d.setTransform(a);
        }
    }

    public boolean isFinished() {
        return finished;
    }
}
