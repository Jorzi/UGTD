/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package units;

import java.awt.geom.Point2D;
import java.util.Comparator;
import terrain.MapTile;

/**
 * Used for sorting the list in a tower's guard area according to its distance to the tower.
 * @author Göran Maconi
 */
public class TileRangeComparator implements Comparator<MapTile> {
    
    private double x;
    private double y;

    public TileRangeComparator(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int compare(MapTile o1, MapTile o2) {
        double a = Point2D.distance(x, y, o1.getX(), o1.getY()) - Point2D.distance(x, y, o2.getX(), o2.getY());
        if(a == 0) {
            return 0;
        }else if(a > 0){
            return 1;
        }else{
            return -1;
        }
        
        
    }
    
}
