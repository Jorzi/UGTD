/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package units;

import game.GlobalConstants;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import resources.ImageLoader;
import terrain.MapTile;

/**
 * Projectiles fly straight to the target point, dealing damage to any enemies
 * in the area.
 *
 * @author Göran
 */
public class Projectile {

    private int centerX = 8;
    private int centerY = 8;
    private MapTile endTile;
    private double velocity = 0.5;
    private double angle;
    private double currentTileX;
    private double currentTileY;
    private boolean arrived;
    private boolean finished;
    private BufferedImage image = ImageLoader.imageLibrary.get("cannonball");
    private Explosion boom;
    private int damage;

    public Projectile(double startTileX, double startTileY, MapTile endTile, int damage) {
        this.endTile = endTile;
        this.angle = Math.atan2(endTile.getY() - startTileY, endTile.getX() - startTileX);
        this.currentTileX = startTileX;
        this.currentTileY = startTileY;
        this.arrived = false;
        this.finished = false;
        this.damage = damage;
    }

    public void update() {
        if (!arrived) {
            if (Point2D.distance(currentTileX, currentTileY, endTile.getX(), endTile.getY()) < velocity) {
                currentTileX = endTile.getX();
                currentTileY = endTile.getY();
                arrived = true;
                dealDamage();
                boom = new Explosion(currentTileX, currentTileY);
            } else {
                currentTileX += Math.cos(angle) * velocity;
                currentTileY += Math.sin(angle) * velocity;
            }
        } else if (!finished) {
            boom.update();
            if (boom.isFinished()) {
                finished = true;
            }
        }
    }

    /**
     * Does area effect damage to any enemies in a 3x3 tile area. The central
     * tile takes full damage while neighboring tiles take only half the amount.
     */
    private void dealDamage() {
        if (endTile.getEnemy() != null) {
            endTile.getEnemy().takeDamage(damage);
        }
        for (MapTile tile : endTile.neighbours.keySet()) {
            if (tile.getEnemy() != null) {
                tile.getEnemy().takeDamage(damage / 2);
            }
        }
    }

    public void paint(Graphics g, ImageObserver imOb) {
        Graphics2D g2d = (Graphics2D) g;

        if (!arrived) {
            AffineTransform a = g2d.getTransform();
            g2d.translate(currentTileX * GlobalConstants.tileSize, currentTileY * GlobalConstants.tileSize);
            g2d.translate(centerX, centerY);
            g2d.rotate(angle + Math.PI * 0.5);
            g2d.translate(-centerX, -centerY);
            g2d.drawImage(image, 0, 0, imOb);
            g2d.setTransform(a);
        } else {
            boom.paint(g, imOb);
        }
    }

    public boolean isFinished() {
        return finished;
    }
}
