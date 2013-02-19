/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package units;

import game.GameFrame;
import game.GameInstance;
import java.awt.Graphics;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.List;
import terrain.TerrainMap;

/**
 *
 * @author Göran
 */
public class EnemyHandler {

    public List<Enemy> enemyList;

    public EnemyHandler() {
        enemyList = new ArrayList<>();
    }

    public void paint(Graphics g, ImageObserver imOb) {
        for (Enemy enemy : enemyList) {
            enemy.paint(g, imOb);
        }
    }

    /**
     * sub-method of update. Calls the updates method of all enemies. Also
     * handles removal of destroyed/arrived enemies
     */
    private void updateEnemies(TerrainMap map) {
        for (int i = 0; i < enemyList.size(); i++) {
            enemyList.get(i).update();
            if (enemyList.get(i).isArrived() || enemyList.get(i).isDestroyed()) {
                //TODO: add damage calculation
                if (enemyList.get(i).isDestroyed()) {
                    GameInstance.credits += enemyList.get(i).getValue();
                    GameFrame.creditsLabel.setText("Credits: " + GameInstance.credits);
                }
                map.clearEnemy(enemyList.get(i));
                enemyList.remove(i);
                i--;
            }
        }
    }

    /**
     * Adds a new enemy to the map and assigns a path to it.
     *
     * @param x the x tile coordinate
     * @param y the y tile coordinate
     * @param map the game's current TerrainMap object
     */
    public void addEnemy(int x, int y, TerrainMap map) {
        try {
            enemyList.add(new Enemy(map.generatePath(x, y)));
        } catch (Exception ex) {
            System.out.println("cannot place enemy");
        }

    }

    /**
     * Calculates a new route for every enemy in the game.
     * 
     * @param map the game's current TerrainMap object
     * @throws Exception 
     */
    public void recalculatePaths(TerrainMap map) throws Exception {
        for (Enemy enemy : enemyList) {
            enemy.setPath(map.generatePath(enemy.getCurrentTile().getX(), enemy.getCurrentTile().getY()));
        }
    }

    /**
     * 
     * @param map 
     */
    public void update(TerrainMap map) {
        updateEnemies(map);
    }
}
