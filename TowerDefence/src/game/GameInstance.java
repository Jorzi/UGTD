/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.Timer;
import rtype.Craft;
import terrain.TerrainMap;
import units.Enemy;
import units.Tower;

/**
 *
 * @author Göran
 */
public class GameInstance extends JPanel implements ActionListener {

    private Timer timer;
    private Craft craft;
    private Random random;
    private TerrainMap map;
    
    private List<Tower> towerList;
    private List<Enemy> enemyList;

    public GameInstance() {

        addKeyListener(new TAdapter());
        setFocusable(true);
        setBackground(Color.BLACK);
        setDoubleBuffered(true);

        craft = new Craft();
        map = new TerrainMap("map1.png");
        random = new Random();
        towerList = new ArrayList<Tower>();
        towerList.add(new Tower(10, 10));
        this.addEnemy(87, 45);

        timer = new Timer(10, this);
        timer.start();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        map.paint(g, this);
        for (Tower tower : towerList) {
            tower.paint(g, this);
        }

        Graphics2D g2d = (Graphics2D) g;
//        for(int i = 0; i < 1000; i++) {
//            g2d.drawImage(craft.getImage(), random.nextInt(400), random.nextInt(300), this);
//        }
        g2d.translate((int) craft.getX() + 8, (int) craft.getY() + 8);
        g2d.rotate(craft.getAngle() + Math.PI / 2);
        g2d.translate(-8, -8);
        g2d.drawImage(craft.getImage(), 0, 0, this);
        
        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        craft.move();
        for (Tower tower : towerList) {
            tower.update();
        }
        repaint();
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            craft.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            craft.keyPressed(e);
        }
    }

    public void addTower(String type, int x, int y) {
    }
    
    public void addEnemy(int x, int y){
        if(map.getTile(x, y) != null){
        enemyList.add(new Enemy(map.generatePath(x, y)));
        }else{
            System.out.println("cannot place enemy");
        }
    }
}
