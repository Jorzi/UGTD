/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package resources;

import java.util.HashMap;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import resources.ImageLoader;

/**
 *
 * @author Göran
 */
public class ImageLoaderTest {
    
    public ImageLoaderTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        ImageLoader.imageLibrary = new HashMap<>();
    }
    
    @After
    public void tearDown() {
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
     @Test
     public void addImagesToLibrary() {
         assertTrue(ImageLoader.imageLibrary.isEmpty());
         ImageLoader.loadImages();
         assertFalse(ImageLoader.imageLibrary.isEmpty());
     }
}
