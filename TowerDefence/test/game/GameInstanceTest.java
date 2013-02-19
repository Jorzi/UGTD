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
    
    GameInstance game;
    
    public GameInstanceTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        resources.ImageLoader.loadImages();
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        game = new GameInstance("map2", 100);
    }
    
    @After
    public void tearDown() {
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void gameInstanceInitialisation() {
        
        assertTrue(game.map != null);
        assertTrue(game.towerList != null);
    }
    
    @Test
    public void enemyGetsAdded() {
        int size = game.enemyList.size();
        game.addEnemy(47, 10);
        assertEquals(size + 1, game.enemyList.size());
    }
    
    @Test
    public void enemyNotAddedOnRock() {
        int size = game.enemyList.size();
        game.addEnemy(0, 0);
        assertEquals(size, game.enemyList.size());
    }
    
    @Test
    public void towerGetsAdded() {
        int size = game.towerList.size();
        game.addTower("", 7, 7);
        assertEquals(size + 1, game.towerList.size());
    }
    
        
}
