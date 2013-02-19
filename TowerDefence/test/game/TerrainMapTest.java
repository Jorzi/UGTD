package game;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.LinkedList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import terrain.TerrainMap;

/**
 *
 * @author Göran
 */
public class TerrainMapTest {
    
    TerrainMap map;

    public TerrainMapTest() {
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
        map = new TerrainMap("map2");
    }

    @After
    public void tearDown() {
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //

    @Test
    public void properInitialization() {
        assertEquals(map.getPixels().length, map.getMapImage().getWidth());
        assertEquals(map.getPixels()[0].length, map.getMapImage().getHeight());
    }
    
    @Test
    public void testPathfindingSimple() throws Exception{
        assertEquals(1, map.generatePath(TerrainMap.target[0], TerrainMap.target[1]).size());
    }
    
    @Test
    public void testPathfindingComplex() throws Exception{
        assertEquals(TerrainMap.target[0], map.generatePath(47, 19).getLast().getX());
        assertEquals(TerrainMap.target[1], map.generatePath(47, 19).getLast().getY());
    }
}
