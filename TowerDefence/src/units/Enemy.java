/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package units;

import game.GlobalConstants;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import terrain.MapTile;

/**
 * The sole objective of the enemy class, except for drawing its own graphical representation, is to follow the path it is given.
 * Once it reaches the end of the path, its "arrived" status becomes true.
 *
 * @author Göran Maconi
 */
public class Enemy {

    private int centerX = 8;
    private int centerY = 8;
    private double tileX;
    private double tileY;
    private double angle;
    private double targetAngle;
    private double da = 0.05;
    private double speed; // tiles/frame, keep this between 0 and 1
    private BufferedImage image;
    private LinkedList<MapTile> path; // stack of tiles left to traverse
    private MapTile previousTile;
    
    private boolean arrived;
    private boolean destroyed;
    private int hp = 100;

    public Enemy(LinkedList<MapTile> path) {
        try {
            image = ImageIO.read(new File("tank1.png"));
        } catch (IOException e) {
            System.out.println("couldn't find image");
        }
        this.path = path;
        path.peek().setEnemy(this);
        previousTile = this.path.pop();
        tileX = previousTile.getX();
        tileY = previousTile.getY();
        speed = 0.02;
        setTargetAngle();
        angle = targetAngle;
        arrived = false;
        destroyed = false;
    }

    public void paint(Graphics g, ImageObserver imOb) {
        Graphics2D g2d = (Graphics2D) g;

//        try{
//        g2d.setColor(new Color(20, 200, 20, 64));
//        g2d.fillRect(path.peek().getX()*GlobalConstants.tileSize, path.peek().getY()*GlobalConstants.tileSize, GlobalConstants.tileSize, GlobalConstants.tileSize);
//        g2d.setColor(new Color(20, 20, 200, 64));
//        g2d.fillRect(previousTile.getX()*GlobalConstants.tileSize, previousTile.getY()*GlobalConstants.tileSize, GlobalConstants.tileSize, GlobalConstants.tileSize);
//        }catch(NullPointerException e){
//            
//        }

        AffineTransform a = g2d.getTransform();
        g2d.translate(tileX * GlobalConstants.tileSize, tileY * GlobalConstants.tileSize);
        g2d.translate(centerX, centerY);
        g2d.rotate(angle + Math.PI * 0.5);
        g2d.translate(-centerX, -centerY);
        g2d.drawImage(image, 0, 0, imOb);
        g2d.setTransform(a);
    }

    public MapTile getCurrentTile() {
        return previousTile;
    }

    public void setPath(LinkedList<MapTile> path) {
        this.path = path;
        setTargetAngle(); //recalculate angle in case target tile is changed
    }

    public void update() {
        move();
        if (!arrived) {
            calculateTileOccupation();
        }
    }

    private void move() {
        if (!arrived) {
            if (Point2D.distance(tileX, tileY, path.peek().getX(), path.peek().getY()) < speed) {
                nextTile();
                if(arrived){
                    return;
                }
            }
            if (angle != targetAngle) {
                turnTowardsTargetAngle();
            } else if (path.peek().getEnemy() == null || path.peek().getEnemy() == this) {
                tileX += speed * Math.cos(angle);
                tileY += speed * Math.sin(angle);
            } else if (Point2D.distance(path.peek().getEnemy().getTileX(), path.peek().getEnemy().getTileY(), path.peek().getX(), path.peek().getY()) > 1){
                // UGLY WORKAROUND FOR A BUG, compensates for insufficient tile occupation removal
                path.peek().setEnemy(this);
            }
        }
    }
    
    private void nextTile(){
        tileX = path.peek().getX();
                tileY = path.peek().getY();
                if (previousTile.getEnemy() == this) {
                    previousTile.setEnemy(null);
                }
                previousTile = path.pop();
                if (path.isEmpty()) {
                    arrived = true;
                    previousTile.setEnemy(null);
                    return;
                }
                setTargetAngle();
    }
    
    private void turnTowardsTargetAngle(){
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
    }

    private void calculateTileOccupation() {
        if (Point2D.distance(tileX, tileY, path.peek().getX(), path.peek().getY()) < 0.9 && path.peek().getEnemy() == null) {
            path.peek().setEnemy(this);
        }
        if (Point2D.distance(tileX, tileY, previousTile.getX(), previousTile.getY()) > 0.9 && previousTile.getEnemy() == this) {
            previousTile.setEnemy(null);
        }
    }

    private void setTargetAngle() {
        double dx = -tileX + path.peek().getX();
        double dy = -tileY + path.peek().getY();
        targetAngle = Math.atan2(dy, dx);
        targetAngle %= 2 * Math.PI;

    }

    public boolean isArrived() {
        return arrived;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public double getTileX() {
        return tileX;
    }

    public double getTileY() {
        return tileY;
    }
    
    public void takeDamage(int amount){
        hp -= amount;
        if(hp <= 0){
            hp = 0;
            destroyed = true;
        }
    }
}
