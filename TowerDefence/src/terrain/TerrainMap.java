/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package terrain;

import game.GlobalConstants;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import javax.imageio.ImageIO;

/**
 *
 * @author Göran
 */
public class TerrainMap {

    public static int[] target = {9, 26};
    private BufferedImage mapImage;
    private BufferedImage tile1;
    private BufferedImage tile2;
    private BufferedImage bunker;
    private int[][] pixels; // tile index array
    private MapTile[][] navigationGraph;

    public TerrainMap(String mapName) {
        loadImages(mapName);

        pixels = new int[mapImage.getWidth()][mapImage.getHeight()];
        navigationGraph = new MapTile[mapImage.getWidth()][mapImage.getHeight()];

        fillTileArrays();
        generateConnectivity();
    }

    private void loadImages(String mapName) {
        try {
            mapImage = ImageIO.read(new File(mapName));
            tile1 = ImageIO.read(new File("ground1.png")); //TODO: unhardcode tileset
            tile2 = ImageIO.read(new File("rock1.png"));
            bunker = ImageIO.read(new File("bunker1.png"));
        } catch (IOException e) {
            System.out.println("couldn't find image");
        }
    }

    private void fillTileArrays() {
        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[0].length; j++) {
                int value = mapImage.getRGB(i, j);
                value = value & 255;
                pixels[i][j] = value;
                if (value == 0) {
                    navigationGraph[i][j] = new MapTile(i, j);
                }
                //System.out.print(pixels[i][j] + " ");
            }
            //System.out.println();
        }
    }

    private void generateConnectivity() {
        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[0].length; j++) {
                if (navigationGraph[i][j] != null) {
                    if (i > 0 && navigationGraph[i - 1][j] != null) { // left tile
                        navigationGraph[i - 1][j].neighbours.put(navigationGraph[i][j], 1.0);
                        navigationGraph[i][j].neighbours.put(navigationGraph[i - 1][j], 1.0);
                    }
                    if (j > 0 && navigationGraph[i][j - 1] != null) { // top tile
                        navigationGraph[i][j - 1].neighbours.put(navigationGraph[i][j], 1.0);
                        navigationGraph[i][j].neighbours.put(navigationGraph[i][j - 1], 1.0);
                    }
                    if (i * j > 0 && navigationGraph[i - 1][j] != null && navigationGraph[i][j - 1] != null
                            && navigationGraph[i - 1][j - 1] != null) { // top left tile
                        navigationGraph[i - 1][j - 1].neighbours.put(navigationGraph[i][j], Math.sqrt(2));
                        navigationGraph[i][j].neighbours.put(navigationGraph[i - 1][j - 1], Math.sqrt(2));
                    }
                    if (j > 0 && i < navigationGraph.length - 1 && navigationGraph[i + 1][j] != null
                            && navigationGraph[i][j - 1] != null && navigationGraph[i + 1][j - 1] != null) { // top right tile
                        navigationGraph[i + 1][j - 1].neighbours.put(navigationGraph[i][j], Math.sqrt(2));
                        navigationGraph[i][j].neighbours.put(navigationGraph[i + 1][j - 1], Math.sqrt(2));
                    }
                }
            }
        }
    }

    public MapTile getTile(int x, int y) {
        return navigationGraph[x][y];
    }

    public void paint(Graphics g, ImageObserver imOb) {
        Graphics2D g2d = (Graphics2D) g;
        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[0].length; j++) {
                if (pixels[i][j] == 0) {
                    g2d.drawImage(tile1, GlobalConstants.tileSize * i, GlobalConstants.tileSize * j, imOb);
//                    if(getTile(i, j).getEnemy() != null){
//                        g2d.setColor(new Color(200, 20, 20, 64));
//                        g2d.fillRect(i*GlobalConstants.tileSize, j*GlobalConstants.tileSize, GlobalConstants.tileSize, GlobalConstants.tileSize);
//                    }
                } else {
                    g2d.drawImage(tile2, GlobalConstants.tileSize * i, GlobalConstants.tileSize * j, imOb);
                }
            }
        }
        g2d.drawImage(bunker, GlobalConstants.tileSize*target[0] - 16, GlobalConstants.tileSize*target[1] - 16, imOb);
    }

    public int[][] getPixels() {
        return pixels;
    }

    public BufferedImage getMapImage() {
        return mapImage;
    }

    public LinkedList<MapTile> generatePath(int startX, int startY) {
        HashSet<MapTile> closedSet = new HashSet<MapTile>();
        PriorityQueue<Node> openSet = new PriorityQueue<Node>();
        HashMap<MapTile, Node> openSet2 = new HashMap<MapTile, Node>(); // same as openSet in a different data structure

        //manhattan heuristic
        openSet.add(new Node(navigationGraph[startX][startY], 0, Math.abs(startX - target[0]) + Math.abs(startY - target[1]), null));
        LinkedList<MapTile> route = new LinkedList<MapTile>();

        while (!openSet.isEmpty()) {
            Node current = openSet.remove();
            if (current.tile.equals(navigationGraph[target[0]][target[1]])) {
                while (current != null) {
                    route.push(current.tile);
                    current = openSet2.get(current.prev);
                }
                return route;
            }
            //openSet2.remove(current.tile);

            closedSet.add(current.tile);
            for (MapTile tile : current.tile.neighbours.keySet()) {
                if (tile.isPassable()) {
                    if (!closedSet.contains(tile)) {
                        double tentativeDist = current.dist + current.tile.neighbours.get(tile);
                        double heur = Math.abs(tile.getX() - target[0]) + Math.abs(tile.getY() - target[1]);
                        if (!openSet2.containsKey(tile) || openSet2.get(tile).dist >= tentativeDist) {
                            if (!openSet2.containsKey(tile)) {
                                Node neighbour = new Node(tile, tentativeDist, heur, current.tile);
                                openSet.add(neighbour);
                                openSet2.put(tile, neighbour);
                                neighbour.prev = current.tile;
                            } else {
                                openSet2.get(tile).dist = tentativeDist;
                                openSet2.get(tile).prev = current.tile;
                            }
                        }
                    }
                }
            }
        }
        return null;

    }

    public class Node implements Comparable<Node> {

        public double dist;
        public double heur;
        public MapTile tile;
        public MapTile prev;

        public Node(MapTile tile, double dist, double heur, MapTile prev) {
            this.dist = dist;
            this.heur = heur;
            this.tile = tile;
            this.prev = prev;
        }

        @Override
        public int compareTo(Node n) {
            if (this.dist + this.heur > n.dist + n.heur) {
                return 1;
            }
            if (this.dist + this.heur < n.dist + n.heur) {
                return -1;
            }
            return 0;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj.getClass().equals(this.getClass())) {
                Node n = (Node) obj;
                return n.tile == this.tile;
            }
            return false;
        }
    }
}
