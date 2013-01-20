/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package units;

import java.awt.Graphics;
import java.awt.image.ImageObserver;

/**
 *
 * @author Göran
 */
public interface Drawable {
    public void paint(Graphics g, ImageObserver imOb);
}
