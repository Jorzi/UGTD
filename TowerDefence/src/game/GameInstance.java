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
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.Timer;
import rtype.Craft;
import terrain.TerrainMap;
import units.Tower;

/**
 *
 * @author Göran
 */
public class GameInstance extends JPanel implements ActionListener{
    private Timer timer;
    private Craft craft;
    private Random random;
    private TerrainMap map;
    private units.Tower tower;

    public GameInstance() {

        addKeyListener(new TAdapter());
        setFocusable(true);
        setBackground(Color.BLACK);
        setDoubleBuffered(true);

        craft = new Craft();
        map = new TerrainMap("map1.png");
        random = new Random();
        tower = new Tower(10, 10);

        timer = new Timer(10, this);
        timer.start();
    }


    @Override
    public void paint(Graphics g) {
        super.paint(g);
        map.paint(g, this);
        tower.paint(g, this);

        Graphics2D g2d = (Graphics2D)g;
//        for(int i = 0; i < 1000; i++) {
//            g2d.drawImage(craft.getImage(), random.nextInt(400), random.nextInt(300), this);
//        }
        g2d.translate((int)craft.getX() + 8, (int)craft.getY() + 8); 
        g2d.rotate(craft.getAngle() + Math.PI/2);
        g2d.translate(-8, -8);
        g2d.drawImage(craft.getImage(), 0, 0, this);
        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        craft.move();
        tower.update();
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
}
