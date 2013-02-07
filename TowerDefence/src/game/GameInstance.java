/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.IllegalComponentStateException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.Timer;

import terrain.MapTile;
import terrain.TerrainMap;
import units.Enemy;
import units.TileRangeComparator;
import units.Tower;

/**
 *
 * This is the central class of all game logic. It contains the game loop and draws all objects.
 * It also handles adding and removing of buildings and enemies.
 * 
 * 
 * @author Göran Maconi
 */
public class GameInstance extends JPanel implements ActionListener {

    public static enum mode {

        BUILD, SELECT, SELL
    }
    public static mode mode;
    public static int credits = 100;
    private Timer timer;
    private Random random;
    private Point mouseCoords;
    private Point mouseTileCoords = new Point();
    public TerrainMap map;
    public HashSet<Tower> towerList;
    public List<Enemy> enemyList;
    private boolean buildable;

    public GameInstance(String mapName) {

        addKeyListener(new TAdapter());
        setFocusable(true);
        setDoubleBuffered(true);
        addMouseListener(new mouseInput());

        mode = mode.SELECT;
        map = new TerrainMap(mapName);
        random = new Random();
        towerList = new HashSet<>();
        enemyList = new ArrayList<>();

        addEnemy(47, 19);
        addEnemy(47, 18);
        addEnemy(47, 17);
        addEnemy(47, 16);
        addEnemy(47, 15);
        addEnemy(47, 14);
        addEnemy(47, 13);
        addEnemy(47, 12);
        addEnemy(47, 11);

        timer = new Timer(10, this);
        timer.start();
    }

    public static void setMode(mode mode) {
        GameInstance.mode = mode;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        map.paint(g, this);

        for (Tower tower : towerList) {
            tower.paintBase(g, this);
        }
        for (Enemy enemy : enemyList) {
            enemy.paint(g, this);
        }
        for (Tower tower : towerList) {
            tower.paintTurret(g, this);
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.RED);
        g2d.drawString(mouseCoords.x + " " + mouseCoords.y + " " + mode, 0, 15);
        paintCursor(g);


        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }

    public void paintCursor(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        if (mode == mode.SELECT) {
            g2d.drawRect(mouseTileCoords.x * GlobalConstants.tileSize, mouseTileCoords.y * GlobalConstants.tileSize, GlobalConstants.tileSize, GlobalConstants.tileSize);
            g2d.setColor(new Color(200, 20, 20, 96));
            g2d.fillRect(mouseTileCoords.x * GlobalConstants.tileSize, mouseTileCoords.y * GlobalConstants.tileSize, GlobalConstants.tileSize, GlobalConstants.tileSize);
        } else if (mode == mode.BUILD) {
            if (buildable) {
                g2d.setColor(Color.GREEN);
            } else {
                g2d.setColor(Color.RED);
            }
            g2d.drawRect(mouseTileCoords.x * GlobalConstants.tileSize, mouseTileCoords.y * GlobalConstants.tileSize, 2 * GlobalConstants.tileSize, 2 * GlobalConstants.tileSize);
            if (buildable) {
                g2d.setColor(new Color(20, 200, 20, 96));
            } else {
                g2d.setColor(new Color(200, 20, 20, 96));
            }
            g2d.fillRect(mouseTileCoords.x * GlobalConstants.tileSize, mouseTileCoords.y * GlobalConstants.tileSize, 2 * GlobalConstants.tileSize, 2 * GlobalConstants.tileSize);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        mouseCoords = MouseInfo.getPointerInfo().getLocation();
        try {
            mouseCoords.x -= this.getLocationOnScreen().x;
            mouseCoords.y -= this.getLocationOnScreen().y;
            mouseTileCoords.x = mouseCoords.x / GlobalConstants.tileSize;
            mouseTileCoords.y = mouseCoords.y / GlobalConstants.tileSize;
        } catch (IllegalComponentStateException ex) {
        }
        if (mode == mode.BUILD) {
            checkIfBuildable(2, 2);
        }

        for (Tower tower : towerList) {
            tower.update();
        }
        for (int i = 0; i < enemyList.size(); i++) {
            enemyList.get(i).update();
            if(enemyList.get(i).isArrived()){
                //TODO: add damage calculation
                enemyList.remove(i);
                i--;
            }
        }

        credits++;
        repaint();
    }

//    public boolean mouseInsideWindow() {
//        if (mouseCoords.x < 0) {
//            return false;
//        }
//        if (mouseCoords.y < 0) {
//            return false;
//        }
//        if (mouseCoords.x >= this.getWidth()) {
//            return false;
//        }
//        if (mouseCoords.y >= this.getHeight()) {
//            return false;
//        }
//        return true;
//    }
    private void checkIfBuildable(int sizeX, int sizeY) {
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                try {
                    if (map.getTile(mouseTileCoords.x + i, mouseTileCoords.y + j) == null
                            || !map.getTile(mouseTileCoords.x + i, mouseTileCoords.y + j).isPassable()
                            || map.getTile(mouseTileCoords.x + i, mouseTileCoords.y + j).getEnemy() != null) {
                        buildable = false;
                        return;
                    }
                } catch (IndexOutOfBoundsException e) {
                    buildable = false;
                    return;
                }
            }
        }
        buildable = true;
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if (key == KeyEvent.VK_ESCAPE) {
                setMode(mode.SELECT);
            }
        }
    }

    private class mouseInput extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                if (mode == mode.BUILD && buildable) {
                    addTower("", mouseTileCoords.x, mouseTileCoords.y);
                } else if (mode == mode.SELL) {
                    sellTower(mouseTileCoords.x, mouseTileCoords.y);
                }
            }
        }
    }

    private ArrayList<MapTile> CalculateTowerRange(int x, int y, int radius) {
        ArrayList<MapTile> area = new ArrayList<>();
        Comparator<MapTile> compareDistance = new TileRangeComparator(x + 0.5, y + 0.5);
        for (int i = Math.max(x - radius, 0); i < Math.min( x + radius + 2, map.getPixels().length); i++) {
            for (int j = Math.max(y - radius, 0); j < Math.min(y + radius + 2, map.getPixels()[0].length); j++) {
                if (map.getTile(i, j) != null) {
                    if (Point2D.distance(x + 0.5, y + 0.5, map.getTile(i, j).getX(), map.getTile(i, j).getY()) <= radius) {
                        area.add(map.getTile(i, j));
                    }
                }
            }
        }
        Collections.sort(area, compareDistance);
        return area;
    }

    public void addTower(String type, int x, int y) {
        Tower t = new Tower(x, y, CalculateTowerRange(x, y, 5));
        towerList.add(t);
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                map.getTile(x + i, y + j).setPassable(false);
                map.getTile(x + i, y + j).setTower(t);
            }
        }
        recalculatePaths();
    }

    public void sellTower(int x, int y) {
        if (map.getTile(x, y).getTower() != null) {
            Tower t = map.getTile(x, y).getTower();
            towerList.remove(t);
            //TODO: add money handling
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    map.getTile(t.getTileX() + i, t.getTileY() + j).setPassable(true);
                    map.getTile(t.getTileX() + i, t.getTileY() + j).setTower(null);
                }
            }
            recalculatePaths();
        }
    }

    public void addEnemy(int x, int y) {
        if (map.getTile(x, y) != null) {
            enemyList.add(new Enemy(map.generatePath(x, y)));
        } else {
            System.out.println("cannot place enemy");
        }
    }

    public void recalculatePaths() {
        for (Enemy enemy : enemyList) {
            enemy.setPath(map.generatePath(enemy.getCurrentTile().getX(), enemy.getCurrentTile().getY()));
        }
    }
}
