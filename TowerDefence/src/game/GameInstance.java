/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.awt.Color;
import java.awt.Font;
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
import javax.swing.JPanel;
import javax.swing.Timer;
import terrain.MapTile;
import terrain.TerrainMap;
import units.EnemyHandler;
import units.TileRangeComparator;
import units.Tower;

/**
 *
 * This is the central class of all game logic, as well as the main graphical
 * element in the interface. It contains the game loop and draws all objects. It
 * also handles adding and removing of buildings and enemies.
 *
 *
 * @author Göran Maconi
 */
public class GameInstance extends JPanel implements ActionListener {

    public static enum mode {

        BUILD, SELECT, SELL
    }
    public static mode mode;
    public static int credits;
    private Timer timer;
    private int targetFPS = 60;
    private Point mouseCoords = new Point();
    private Point mouseTileCoords = new Point();
    public TerrainMap map;
    public HashSet<Tower> towerList;
    private EnemyHandler enemyHandler;
    private boolean buildable;
    private boolean gameOver;

    /**
     * @param mapName the name of the map image in the imageLibrary
     * @param startCredits amount of money available at the start of the game
     */
    public GameInstance(String mapName, int startCredits) {

        addKeyListener(new KeyboardInput());
        setFocusable(true);
        setDoubleBuffered(true);
        addMouseListener(new MouseInput());

        mode = mode.SELECT;
        map = new TerrainMap(mapName);
        towerList = new HashSet<>();
        credits = startCredits;
        enemyHandler = new EnemyHandler(map);
        gameOver = false;
        timer = new Timer(1000/targetFPS, this);
        timer.start();
    }

    /**
     * This method is part of the GUI mechanism and should not be manually
     * called. The order of painting is not random, but "depth ordered" in the
     * following order to display correctly: -ground tiles -building bases
     * -enemies -projectiles (including their explosions) -turrets -cursor and
     * overlay elements
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        map.paint(g, this);

        for (Tower tower : towerList) {
            tower.paintBase(g, this);
        }
        enemyHandler.paint(g, this);

        for (Tower tower : towerList) {
            tower.paintProjectiles(g, this);
        }
        for (Tower tower : towerList) {
            tower.paintTurret(g, this);
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.RED);
        //g2d.drawString(mouseCoords.x + " " + mouseCoords.y + " " + mode, 0, 15);
        if (!gameOver) {
            paintCursor(g);
        } else {
            paintGameOverScreen(g);
        }


        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }

    /**
     * A sub-method to the paint method which highlights the tile beneath the
     * cursor.
     */
    private void paintCursor(Graphics g) {
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

    /**
     * Sub-method of paint, overlays a game over screen onto the canvas.
     */
    private void paintGameOverScreen(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(new Color(0, 0, 0, 96));
        g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
        g2d.setFont(new Font("Arial", Font.PLAIN, 72));
        g2d.setColor(Color.red);
        g2d.drawString("Game Over", this.getWidth() / 2, this.getHeight() / 2);
        g2d.setFont(new Font("Arial", Font.PLAIN, 20));
        g2d.drawString("Waves survived: " + enemyHandler.getWaveNumber(), this.getWidth() / 2, this.getHeight() / 2 + 30);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            update();
        }
        repaint();
    }

    /**
     * Called every game tick. Progresses the game further.
     */
    public void update() {
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
        enemyHandler.update();
        gameOver = enemyHandler.isGameOver();
    }

    /**
     * Method to check if there's enough free area under the mouse to be able to
     * place a tower of size x * y
     *
     * @param sizeX width of the tower to be placed, in tiles
     * @param sizeY height of the tower to be placed, in tiles
     */
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

    /**
     * Used for reading key input.
     */
    private class KeyboardInput extends KeyAdapter {

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

    /**
     * Used for reading mouse input.
     */
    private class MouseInput extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                if (mode == mode.BUILD && buildable) {
                    addTower("", mouseTileCoords.x, mouseTileCoords.y);
                } else if (mode == mode.SELL) {
                    sellTower(mouseTileCoords.x, mouseTileCoords.y);
                }
                GameFrame.creditsLabel.setText("Credits: " + credits);
            }
        }
    }

    /**
     * Calculates a circular area of tiles within which a tower can track and
     * shoot enemies. The tiles are sorted so that the tower will attack closer
     * enemies first.
     *
     * @param x the x coordinate of the tower
     * @param y the y coordinate of the tower
     * @param radius the range of the tower
     * @return a list containing all the tiles within the tower's range, sorted
     * from closest to furthest away.
     */
    private ArrayList<MapTile> CalculateTowerRange(int x, int y, int radius) {
        ArrayList<MapTile> area = new ArrayList<>();
        Comparator<MapTile> compareDistance = new TileRangeComparator(x + 0.5, y + 0.5);
        for (int i = Math.max(x - radius, 0); i < Math.min(x + radius + 2, map.getPixels().length); i++) {
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

    /**
     * Adds a tower to the map if there are enough credits to buy one. Also
     * deducts the price from the total credits. If the tower would prevent any
     * tank from reaching the target or block the spawn, the tower will be
     * removed and the cash returned
     *
     * @param type currently unused, for being able to build multiple tower
     * types. any string is valid.
     * @param x the tower's x tile coordinate
     * @param y the tower's y tile coordinate
     */
    public void addTower(String type, int x, int y) {
        Tower t = new Tower(x, y, CalculateTowerRange(x, y, 10), 10);
        if (credits >= t.getPrice()) {
            credits -= t.getPrice();
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    map.getTile(x + i, y + j).setPassable(false);
                    map.getTile(x + i, y + j).setTower(t);
                }
            }
            try {
                enemyHandler.recalculatePaths();
                map.generatePath(TerrainMap.spawn);
                towerList.add(t);
            } catch (Exception e) {
                for (int i = 0; i < 2; i++) {
                    for (int j = 0; j < 2; j++) {
                        map.getTile(x + i, y + j).setPassable(true);
                        map.getTile(x + i, y + j).setTower(null);
                    }
                }
                credits += t.getPrice();
            }
        }
    }

    /**
     * Removes the tower (if any) from the tile, adding half of its price to the
     * credits. If the tile contains no tower, nothing happens.
     *
     * @param x the selected tile's x coordinate
     * @param y the selected tile's y coordinate
     */
    public void sellTower(int x, int y) {
        if (map.getTile(x, y).getTower() != null) {
            Tower t = map.getTile(x, y).getTower();
            towerList.remove(t);
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    map.getTile(t.getTileX() + i, t.getTileY() + j).setPassable(true);
                    map.getTile(t.getTileX() + i, t.getTileY() + j).setTower(null);
                }
            }
            credits += t.getPrice() / 2;
            try {
                enemyHandler.recalculatePaths();
            } catch (Exception ex) {
            }
        }
    }

    public static void setMode(mode mode) {
        GameInstance.mode = mode;
    }
}
