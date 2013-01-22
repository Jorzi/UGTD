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
import java.util.LinkedList;
import javax.imageio.ImageIO;
import terrain.MapTile;

/**
 *
 * @author Göran
 */
public class Enemy {

    private int centerX = 8;
    private int centerY = 8;
    private double x;
    private double y;
    private double angle;
    private double targetAngle;
    private double da = 0.05;
    private double tileProgress;
    private double invDistFac;
    private double speed; // tiles/frame, keep this between 0 and 1
    private BufferedImage image;
    private LinkedList<MapTile> path; // stack of tiles left to traverse
    private MapTile currentTile;

    public Enemy(LinkedList<MapTile> path) {
        try {
            image = ImageIO.read(new File("tank1.png"));
        } catch (IOException e) {
            System.out.println("couldn't find image");
        }
        this.path = path;
        currentTile = path.pop();
        tileProgress = 0;
        speed = 0.02;
        setTargetAngle();
        angle = targetAngle;
    }

    public void paint(Graphics g, ImageObserver imOb) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform a = g2d.getTransform();
        g2d.translate(x, y);
        g2d.translate(centerX, centerY);
        g2d.rotate(angle + Math.PI * 0.5);
        g2d.translate(-centerX, -centerY);
        g2d.drawImage(image, 0, 0, imOb);
        g2d.setTransform(a);
    }

    public void setPath(LinkedList<MapTile> path) {
        this.path = path;
    }

    public void update() {
        move();
        x = (currentTile.getX() * GlobalConstants.tileSize) * (1 - tileProgress) + (path.peek().getX() * GlobalConstants.tileSize) * tileProgress;
        y = (currentTile.getY() * GlobalConstants.tileSize) * (1 - tileProgress) + (path.peek().getY() * GlobalConstants.tileSize) * tileProgress;
    }

    private void move() {
        if (tileProgress >= 1) {
            if (path.peek().getEnemy() == null) {
                currentTile.setEnemy(null);
                currentTile = path.pop();
                currentTile.setEnemy(this);
                setTargetAngle();
                tileProgress = 0;
            } else {
                return;
            }
        }
        if (angle != targetAngle) {
            double angleDifference = angle - targetAngle;
            if (Math.min(Math.abs(angleDifference), 2 * Math.PI - Math.abs(angleDifference)) < da) {
                angle = targetAngle;
            } else if (angleDifference > Math.PI) {
                angle += da;
                angle %= 2 * Math.PI;
            } else if (angleDifference > 0) {
                angle -= da;
                angle %= 2 * Math.PI;
            } else if (angleDifference > -Math.PI) {
                angle += da;
                angle %= 2 * Math.PI;
            } else {
                angle -= da;
                angle %= 2 * Math.PI;
            }
        } else {
            tileProgress += speed * invDistFac;
        }
    }

    private void setTargetAngle() {
        int dx = -currentTile.getX() + path.peek().getX();
        int dy = -currentTile.getY() + path.peek().getY();
        if (dx == 1 && dy == 0) {
            targetAngle = 0;
            invDistFac = 1.0;
        } else if (dx == 1 && dy == 1) {
            targetAngle = Math.PI * 0.25;
            invDistFac = 1.0 / Math.sqrt(2);
        } else if (dx == 0 && dy == 1) {
            targetAngle = Math.PI * 0.5;
            invDistFac = 1.0;
        } else if (dx == -1 && dy == 1) {
            targetAngle = Math.PI * 0.75;
            invDistFac = 1.0 / Math.sqrt(2);
        } else if (dx == -1 && dy == 0) {
            targetAngle = Math.PI;
            invDistFac = 1.0;
        } else if (dx == -1 && dy == -1) {
            targetAngle = Math.PI * 1.25;
            invDistFac = 1.0 / Math.sqrt(2);
        } else if (dx == 0 && dy == -1) {
            targetAngle = Math.PI * 1.5;
            invDistFac = 1.0;
        } else if (dx == 1 && dy == -1) {
            targetAngle = Math.PI * 1.75;
            invDistFac = 1.0 / Math.sqrt(2);
        }

    }
}
