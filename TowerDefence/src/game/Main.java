/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.awt.LayoutManager;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Göran
 */
public class Main extends JFrame {

    private JButton b;
    private JPanel game;

    public Main() {
        game = new GameInterface();
        add(game);


        

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(game.getPreferredSize().width, game.getPreferredSize().height + 32);
        //this.setExtendedState(this.getExtendedState() | this.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setTitle("Ultra Generic Tower Defence");
        //setResizable(true);
        setVisible(true);


    }

    public static void main(String[] args) {
        new Main();
    }
}
