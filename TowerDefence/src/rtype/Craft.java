/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rtype;

import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Craft {

    private String craft = "craft.png";
    private double dx;
    private double dy;
    private double x;
    private double y;
    private double v;
    private double angle;
    private double da;
    private BufferedImage image;

    public Craft() {
        try {
            image = ImageIO.read(new File(craft));
        } catch (IOException e) {
            System.out.println("couldn't find image " + image);
        }
        x = 40;
        y = 60;
    }

    public void move() {
        x += v * Math.cos(angle);
        y += v * Math.sin(angle);
        angle += da;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getAngle() {
        return angle;
    }

    public Image getImage() {
        return image;
    }

    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            v = -1;
        }

        if (key == KeyEvent.VK_RIGHT) {
            v = 1;
        }

        if (key == KeyEvent.VK_UP) {
            da = -0.01;
        }

        if (key == KeyEvent.VK_DOWN) {
            da = 0.01;
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            v = 0;
        }

        if (key == KeyEvent.VK_RIGHT) {
            v = 0;
        }

        if (key == KeyEvent.VK_UP) {
            da = 0;
        }

        if (key == KeyEvent.VK_DOWN) {
            da = 0;
        }
    }
}
