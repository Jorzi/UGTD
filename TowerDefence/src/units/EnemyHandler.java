/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package units;

import game.GameFrame;
import game.GameInstance;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import terrain.TerrainMap;

/**
 *
 * @author Göran
 */
public class EnemyHandler {

    public List<Enemy> enemyList;
    private int ticksSinceLastWave;
    private int waveDelay = 1600;
    private boolean waveInProgress;
    private int waveNumber;
    private LinkedList<Integer> currentWaveQueue;
    private TerrainMap map;

    public EnemyHandler(TerrainMap map) {
        this.map = map;
        enemyList = new ArrayList<>();
        currentWaveQueue = new LinkedList<>();
        ticksSinceLastWave = waveDelay * 7 / 8;
        waveInProgress = false;
        waveNumber = 0;
    }

    public void paint(Graphics g, ImageObserver imOb) {
        for (Enemy enemy : enemyList) {
            enemy.paint(g, imOb);
        }
    }

    public void update() {
        updateEnemies();
        waveGenerator();
    }

    /**
     * sub-method of update. Calls the updates method of all enemies. Also
     * handles removal of destroyed/arrived enemies
     */
    private void updateEnemies() {
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

    private void waveGenerator() {
        ticksSinceLastWave++;
        if (waveInProgress) {
            if (map.getTile(TerrainMap.spawn).getEnemy() == null
                    || Point2D.distance(map.getTile(TerrainMap.spawn).getEnemy().getTileX(), map.getTile(TerrainMap.spawn).getEnemy().getTileY(), TerrainMap.spawn.x, TerrainMap.spawn.y) > 1) {
                try {
                    Enemy enemy = new Enemy(map.generatePath(TerrainMap.spawn), currentWaveQueue.pop());
                    enemyList.add(enemy);
                    map.getTile(TerrainMap.spawn).setEnemy(enemy);
                    enemy.update();
                } catch (Exception ex) {
                    System.out.println("cannot place enemy");
                }
            }
            if (currentWaveQueue.isEmpty()) {
                waveInProgress = false;
                ticksSinceLastWave = 0;
            }
        } else if (ticksSinceLastWave > waveDelay) {
            System.out.println("Creating new wave");
            for (int i = 0; i < 10; i++) {
                currentWaveQueue.addLast(100 + 100 * waveNumber);
            }
            waveNumber++;
            waveInProgress = true;
        }
    }

    /**
     * Adds a new enemy to the map and assigns a path to it.
     *
     * @param x the x tile coordinate
     * @param y the y tile coordinate
     * @param map the game's current TerrainMap object
     */
    public void addEnemy(int x, int y) {
        try {
            enemyList.add(new Enemy(map.generatePath(x, y)));
        } catch (Exception ex) {
            System.out.println("cannot place enemy");
        }

    }

    public void addEnemy(Point coords) {
        addEnemy(coords.x, coords.y);
    }

    /**
     * Calculates a new route for every enemy in the game.
     *
     * @param map the game's current TerrainMap object
     * @throws Exception
     */
    public void recalculatePaths() throws Exception {
        for (Enemy enemy : enemyList) {
            enemy.setPath(map.generatePath(enemy.getCurrentTile().getX(), enemy.getCurrentTile().getY()));
        }
    }
}
