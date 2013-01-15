/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rtype;

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
import terrain.TerrainMap;


public class Board extends JPanel implements ActionListener {

    private Timer timer;
    private Craft craft;
    private Random random;
    private terrain.TerrainMap map;

    public Board() {

        addKeyListener(new TAdapter());
        setFocusable(true);
        setBackground(new Color(0x100853));
        setDoubleBuffered(true);

        craft = new Craft();
        map = new TerrainMap("map1.png");

        timer = new Timer(5, this);
        timer.start();
        random = new Random();
    }


    @Override
    public void paint(Graphics g) {
        super.paint(g);
        map.paint(g, this);

        Graphics2D g2d = (Graphics2D)g;
//        for(int i = 0; i < 10000; i++) {
//            g2d.drawImage(craft.getImage(), random.nextInt(400), random.nextInt(300), this);
//        }
        g2d.drawImage(craft.getImage(), craft.getX(), craft.getY(), this);

        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        craft.move();
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

