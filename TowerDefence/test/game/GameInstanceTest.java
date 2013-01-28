package game;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import game.GameInstance;
import game.Main;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Göran
 */
public class GameInstanceTest {
    
    public GameInstanceTest() {
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
    public void gameInstanceInitialisation() {
        GameInstance game = new GameInstance("map2.png");
        assertTrue(game.map != null);
        assertTrue(game.enemyList != null);
        assertTrue(game.towerList != null);
    }
    
    @Test
    public void enemyGetsAdded() {
        GameInstance game = new GameInstance("map2.png");
        int size = game.enemyList.size();
        game.addEnemy(47, 10);
        assertEquals(size + 1, game.enemyList.size());
    }
    
    @Test
    public void towerGetsAdded() {
        GameInstance game = new GameInstance("map2.png");
        int size = game.towerList.size();
        game.addTower("", 16, 16);
        assertEquals(size + 1, game.towerList.size());
    }
}
