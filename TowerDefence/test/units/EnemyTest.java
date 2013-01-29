/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package units;

import java.util.LinkedList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import terrain.MapTile;
import terrain.TerrainMap;

/**
 *
 * @author Göran
 */
public class EnemyTest {
    
    public EnemyTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
     @Test
     public void notArrivedByDefault() {
         TerrainMap map = new TerrainMap("map2.png");
         LinkedList<MapTile> path = new LinkedList<>();
         path.add(map.getTile(TerrainMap.target[0], TerrainMap.target[1]));
         path.push(map.getTile(TerrainMap.target[0] + 1, TerrainMap.target[1]));
         Enemy enemy = new Enemy(path);
         
         assertFalse(enemy.isArrived());
     }
     
     @Test
     public void waitsAtGoalAfterArriving(){
         TerrainMap map = new TerrainMap("map2.png");
         LinkedList<MapTile> path = new LinkedList<>();
         path.add(map.getTile(TerrainMap.target[0], TerrainMap.target[1]));
         path.push(map.getTile(TerrainMap.target[0] + 1, TerrainMap.target[1]));
         Enemy enemy = new Enemy(path);
         enemy.update();
         assertFalse(enemy.isArrived());
         
         for(int i = 0; i < 100; i++){
             enemy.update();
         }
         
         assertTrue(enemy.isArrived());
     }
}
