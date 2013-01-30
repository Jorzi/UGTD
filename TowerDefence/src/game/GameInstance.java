/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.awt.Color;
import java.awt.Dimension;
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
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;

import terrain.MapTile;
import terrain.TerrainMap;
import units.Enemy;
import units.Tower;

/**
 *
 * @author Göran
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
    public TerrainMap map;
    public List<Tower> towerList;
    public List<Enemy> enemyList;

    public GameInstance(String mapName) {

        addKeyListener(new TAdapter());
        setFocusable(true);
        setDoubleBuffered(true);

        mode = mode.SELECT;
        map = new TerrainMap(mapName);
        random = new Random();
        towerList = new ArrayList<>();
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
            tower.paint(g, this);
        }
        for (Enemy enemy : enemyList) {
            enemy.paint(g, this);
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.RED);
        g2d.drawString(mouseCoords.x + " " + mouseCoords.y + " " + mode, 0, 15);
        if (mode == mode.SELECT) {
            g2d.drawRect(mouseCoords.x - mouseCoords.x % GlobalConstants.tileSize, mouseCoords.y - mouseCoords.y % GlobalConstants.tileSize, GlobalConstants.tileSize, GlobalConstants.tileSize);
            g2d.setColor(new Color(200, 20, 20, 96));
            g2d.fillRect(mouseCoords.x - mouseCoords.x % GlobalConstants.tileSize, mouseCoords.y - mouseCoords.y % GlobalConstants.tileSize, GlobalConstants.tileSize, GlobalConstants.tileSize);
        }

        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        mouseCoords = MouseInfo.getPointerInfo().getLocation();
        try {
            mouseCoords.x -= this.getLocationOnScreen().x;
            mouseCoords.y -= this.getLocationOnScreen().y;
        } catch (IllegalComponentStateException ex) {
        }

        for (Tower tower : towerList) {
            tower.update();
        }
        for (Enemy enemy : enemyList) {
            enemy.update();
        }
        
        credits++;
        repaint();
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

    public void addTower(String type, int x, int y) {
        towerList.add(new Tower(x, y));
    }

    public void addEnemy(int x, int y) {
        if (map.getTile(x, y) != null) {
            enemyList.add(new Enemy(map.generatePath(x, y)));
        } else {
            System.out.println("cannot place enemy");
        }
    }
}
