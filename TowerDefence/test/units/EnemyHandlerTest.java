/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package units;

import game.GameInstance;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import terrain.TerrainMap;
import static org.junit.Assert.*;

/**
 *
 * @author Göran
 */
public class EnemyHandlerTest {

    EnemyHandler handler;
    TerrainMap map;

    @BeforeClass
    public static void setUpClass() {
        resources.ImageLoader.loadImages();
    }

    @Before
    public void setUp() {
        map = new TerrainMap("map2");
        handler = new EnemyHandler(map);
    }

    @Test
    public void enemyGetsAdded() {
        int size = handler.enemyList.size();
        handler.addEnemy(47, 10);
        assertEquals(size + 1, handler.enemyList.size());
    }

    @Test
    public void enemyNotAddedOnRock() {
        int size = handler.enemyList.size();
        handler.addEnemy(0, 0);
        assertEquals(size, handler.enemyList.size());
    }
}
