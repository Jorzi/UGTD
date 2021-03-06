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
import java.util.ArrayList;
import java.util.LinkedList;
import resources.ImageLoader;
import terrain.MapTile;

/**
 * The tower tracks and shoots at enemies within its active area. It's
 * responsible for updating and painting its own projectiles.
 *
 * @author Göran Maconi
 */
public class Tower {

    private int centerX = 16;
    private int centerY = 32;
    private int tileX;
    private int tileY;
    private double angle = 0;
    private double da = 0.05;
    private BufferedImage turretImage = ImageLoader.imageLibrary.get("turret1");
    private BufferedImage baseImage = ImageLoader.imageLibrary.get("towerBase");
    private ArrayList<MapTile> activeArea;
    private Enemy target;
    private LinkedList<Projectile> projectiles;
    private double reloadSpeed = 0.01;
    private double reloadCycle;
    public int range;
    private int damage = 20;
    private int price = 70;

    public Tower(int tileX, int tileY, ArrayList<MapTile> activeArea, int range) {
        this.tileX = tileX;
        this.tileY = tileY;
        this.activeArea = activeArea;
        reloadCycle = 1;
        projectiles = new LinkedList<>();
        this.range = range;
    }

    public void update() {
        updateProjectiles();
        if (reloadCycle < 1) {
            reloadCycle += reloadSpeed;
        }
        if (target == null) {
            if (!findTarget()) {
                angle += da;
                angle %= 2 * Math.PI;
            }
        } else {
            double targetAngle = Math.atan2(target.getTileY() - tileY - 0.5, target.getTileX() - tileX - 0.5);
            turnTowardsTarget(targetAngle);
            if (reloadCycle >= 1 && angle == targetAngle) {
                fireAt(target.getCurrentTile());
                reloadCycle = 0;
            }
            if (!activeArea.contains(target.getCurrentTile()) || target.isDestroyed()) {
                target = null;
            }
        }
    }

    /**
     * Calls update for all projectiles fired by this tower.
     */
    public void updateProjectiles() {
        for (int i = 0; i < projectiles.size(); i++) {
            if (projectiles.get(i).isFinished()) {
                projectiles.remove(i);
                i--;
            } else {
                projectiles.get(i).update();
            }
        }
    }

    public void paintBase(Graphics g, ImageObserver imOb) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform a = g2d.getTransform();
        g2d.translate(tileX * GlobalConstants.tileSize, tileY * GlobalConstants.tileSize);
        g2d.drawImage(baseImage, 0, 0, imOb);
        g2d.setTransform(a);
    }

    public void paintProjectiles(Graphics g, ImageObserver imOb) {
        for (Projectile p : projectiles) {
            p.paint(g, imOb);
        }
    }

    public void paintTurret(Graphics g, ImageObserver imOb) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform a = g2d.getTransform();
        g2d.translate(tileX * GlobalConstants.tileSize, tileY * GlobalConstants.tileSize);
        g2d.translate(centerX, centerX); //hack takes into account the varying heights(->positions) of the images
        g2d.rotate(angle + Math.PI * 0.5);
        g2d.translate(-centerX, -centerY);
        g2d.drawImage(turretImage, 0, 0, imOb);
        g2d.setTransform(a);
    }

    /**
     * Loops through the tiles in the active area and locks on to the first
     * target it finds. Since the tile list is sorted according to distance, the
     * closest enemy will be chosen.
     *
     * @return true if a target is found, false otherwise
     */
    private boolean findTarget() {
        for (MapTile tile : activeArea) {
            if (tile.getEnemy() != null) {
                target = tile.getEnemy();
                return true;
            }
        }
        return false;
    }

    /**
     * Changes the angle by an increment of da towards the target angle. Always
     * chooses the shortest spin direction.
     *
     * @param targetAngle the angle towards which to turn.
     */
    private void turnTowardsTarget(double targetAngle) {
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

    /**
     * Creates a new projectile object that advances towards the target tile.
     * 
     * @param tile the target MapTile object.
     */
    private void fireAt(MapTile tile) {
        projectiles.add(new Projectile(tileX + 0.5 + Math.cos(angle), tileY + 0.5 + Math.sin(angle), tile, damage));
    }

    public int getTileX() {
        return tileX;
    }

    public int getTileY() {
        return tileY;
    }

    public int getPrice() {
        return price;
    }
}
