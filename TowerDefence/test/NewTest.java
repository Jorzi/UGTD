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
public class NewTest {
    
    public NewTest() {
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
        GameInstance game = new GameInstance();
        assertTrue(game.map != null);
        assertTrue(game.enemyList != null);
        assertTrue(game.towerList != null);
    }
}
