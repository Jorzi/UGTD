/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package units;

import java.awt.geom.Point2D;
import terrain.MapTile;

/**
 * Projectiles fly straight to the target point, dealing damage to any enemies in the area.
 *
 * @author Göran
 */
public class Projectile {
    
    private MapTile endTile;
    private double velocity = 0.5;
    private double angle;
    private double currentTileX;
    private double currentTileY;
    private boolean arrived;

    public Projectile(double startTileX, double startTileY, MapTile endTile) {
        this.endTile = endTile;
        this.angle = Math.atan2(endTile.getY() - startTileY, endTile.getX() - startTileX);
        this.currentTileX = startTileX;
        this.currentTileY = startTileY;
        this.arrived = false;
    }
    
    public void update(){
        if(Point2D.distance(currentTileX, currentTileY, endTile.getX(), endTile.getY()) < velocity){
            currentTileX = endTile.getX();
            currentTileY = endTile.getY();
            arrived = true;
            dealDamage();
        }else{
        currentTileX += Math.cos(angle) * velocity;
        currentTileY += Math.sin(angle) * velocity;
        }
    }
    
    private void dealDamage(){
        if(endTile.getEnemy() != null){
            endTile.getEnemy().takeDamage(50);
        }
        for(MapTile tile : endTile.neighbours.keySet()){
            if(tile.getEnemy() != null){
                tile.getEnemy().takeDamage(25);
            }
        }
    }

    public boolean isArrived() {
        return arrived;
    }
    
    
    
}
