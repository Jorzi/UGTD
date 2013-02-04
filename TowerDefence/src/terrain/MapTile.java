/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package terrain;

import java.util.HashMap;
import units.Enemy;
import units.Tower;

/**
 *
 * @author Göran
 */
public class MapTile {
    
    private Boolean passable = true;
    private int x;
    private int y;
    public HashMap<MapTile, Double> neighbours = new HashMap<MapTile, Double>();
    
    private Enemy enemy;
    private Tower tower;

    public MapTile(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Boolean isPassable() {
        return passable;
    }

    public void setPassable(Boolean passable) {
        this.passable = passable;
    }

    public void setEnemy(Enemy enemy) {
        this.enemy = enemy;
    }

    public Enemy getEnemy() {
        return enemy;
    }

    public Tower getTower() {
        return tower;
    }

    public void setTower(Tower tower) {
        this.tower = tower;
    }
    
    
    
    
    
    
    
    
    
}
