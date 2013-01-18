/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import javax.swing.JFrame;
import rtype.Board;
/**
 *
 * @author Göran
 */
public class Main extends JFrame{
    
    public Main() {

        add(new GameInstance());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(96*16 + 6, 64*16 + 6);
        setLocationRelativeTo(null);
        setTitle("Ultra Generic Tower Defence");
        setResizable(false);
        setVisible(true);
    }

    public static void main(String[] args) {
        new Main();
    }
    
}