/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

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

        

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //this.setExtendedState(this.getExtendedState() | this.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setTitle("Ultra Generic Tower Defence");
        //setResizable(true);
        setVisible(true);


    }

    public static void main(String[] args) {
        GameFrame.main(args);
    }
}
