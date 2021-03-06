/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package terrain;

import game.GlobalConstants;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import resources.ImageLoader;
import units.Enemy;

/**
 * Responsible for creating and drawing the map object, as well as accessing
 * MapTiles and pathfinding
 *
 * @author Göran Maconi
 */
public class TerrainMap {

    public static int[] target = {9, 26};
    public static Point spawn = new Point(47, 14);
    private BufferedImage mapImage;
    private BufferedImage tileset = ImageLoader.imageLibrary.get("tileset");
    private BufferedImage[] tiles = new BufferedImage[64];
    private BufferedImage bunker = ImageLoader.imageLibrary.get("hqBunker");
    private int[][] pixels; // tile index array
    private MapTile[][] navigationGraph;

    public TerrainMap(String mapName) {
        mapImage = ImageLoader.imageLibrary.get(mapName);

        pixels = new int[mapImage.getWidth()][mapImage.getHeight()];
        navigationGraph = new MapTile[mapImage.getWidth()][mapImage.getHeight()];

        fillTileArrays();
        generateConnectivity();
        loadTiles();
    }

    /**
     * Reads pixels and creates MapTile objects at coordinates with "passable"
     * values.
     */
    private void fillTileArrays() {
        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[0].length; j++) {
                int value = mapImage.getRGB(i, j);
                value = value & 255;
                pixels[i][j] = value > 0 ? 3 : 0;
                if (value == 0) {
                    navigationGraph[i][j] = new MapTile(i, j);
                }
                //System.out.print(pixels[i][j] + " ");
            }
            //System.out.println();
        }
        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[0].length; j++) {
                pixels[i][j] = determineTileType(i, j);
            }
        }
    }

    private void loadTiles() {
        for (int i = 0; i < 64; i++) {
            tiles[i] = tileset.getSubimage((i % 8) * GlobalConstants.tileSize, (i / 8) * GlobalConstants.tileSize,
                    GlobalConstants.tileSize, GlobalConstants.tileSize);
        }
    }
    
    private String getNeighbours(int x, int y){
        String s = "";
        if(y == 0 || x == pixels.length - 1 || pixels[x+1][y-1] != 0){
            s += "1";
        }else{
            s += "0";
        }if(x == pixels.length - 1 || pixels[x+1][y] != 0){
            s += "1";
        }else{
            s += "0";
        }if(y == pixels[0].length - 1 || x == pixels.length - 1 || pixels[x+1][y+1] != 0){
            s += "1";
        }else{
            s += "0";
        }if(y == pixels[0].length - 1 || pixels[x][y+1] != 0){
            s += "1";
        }else{
            s += "0";
        }if(y == pixels[0].length - 1 || x == 0 || pixels[x-1][y+1] != 0){
            s += "1";
        }else{
            s += "0";
        }if(x == 0 || pixels[x-1][y] != 0){
            s += "1";
        }else{
            s += "0";
        }if(y == 0 || x == 0 || pixels[x-1][y-1] != 0){
            s += "1";
        }else{
            s += "0";
        }if(y == 0 || pixels[x][y-1] != 0){
            s += "1";
        }else{
            s += "0";
        }
        return s;
    }

    private int determineTileType(int x, int y) {
        if(pixels[x][y] == 0){
            return 0;
        }
        String neighbours = getNeighbours(x, y);
        //fill
        if(neighbours.equals("11111111")){
            if((x + pixels.length * y)%11 == 0){
                return 7;
            }
            if((x + pixels.length * y)%5 == 0){
                return 6;
            }
            if((x + pixels.length * y)%2 == 0){
                return 5;
            }
            return 4;
        }
        //straight
        if(neighbours.charAt(1)=='0' && neighbours.charAt(3)=='1' && neighbours.charAt(5)=='1' && neighbours.charAt(7)=='1'){
            return 16;
        }
        if(neighbours.charAt(1)=='1' && neighbours.charAt(3)=='0' && neighbours.charAt(5)=='1' && neighbours.charAt(7)=='1'){
            return 17;
        }
        if(neighbours.charAt(1)=='1' && neighbours.charAt(3)=='1' && neighbours.charAt(5)=='0' && neighbours.charAt(7)=='1'){
            return 18;
        }
        if(neighbours.charAt(1)=='1' && neighbours.charAt(3)=='1' && neighbours.charAt(5)=='1' && neighbours.charAt(7)=='0'){
            return 19;
        }
        //outer L
        if(neighbours.charAt(1)=='0' && neighbours.charAt(3)=='1' && neighbours.charAt(5)=='1' && neighbours.charAt(7)=='0'){
            return 8;
        }
        if(neighbours.charAt(1)=='0' && neighbours.charAt(3)=='0' && neighbours.charAt(5)=='1' && neighbours.charAt(7)=='1'){
            return 9;
        }
        if(neighbours.charAt(1)=='1' && neighbours.charAt(3)=='0' && neighbours.charAt(5)=='0' && neighbours.charAt(7)=='1'){
            return 10;
        }
        if(neighbours.charAt(1)=='1' && neighbours.charAt(3)=='1' && neighbours.charAt(5)=='0' && neighbours.charAt(7)=='0'){
            return 11;
        }
        //inner L
        if(neighbours.charAt(1)=='1' && neighbours.charAt(3)=='1' && neighbours.charAt(5)=='1' && neighbours.charAt(7)=='1' && neighbours.charAt(0)=='0'){
            return 12;
        }
        if(neighbours.charAt(1)=='1' && neighbours.charAt(3)=='1' && neighbours.charAt(5)=='1' && neighbours.charAt(7)=='1' && neighbours.charAt(2)=='0'){
            return 13;
        }
        if(neighbours.charAt(1)=='1' && neighbours.charAt(3)=='1' && neighbours.charAt(5)=='1' && neighbours.charAt(7)=='1' && neighbours.charAt(4)=='0'){
            return 14;
        }
        if(neighbours.charAt(1)=='1' && neighbours.charAt(3)=='1' && neighbours.charAt(5)=='1' && neighbours.charAt(7)=='1' && neighbours.charAt(6)=='0'){
            return 15;
        }
        //point
        if(neighbours.charAt(1)=='0' && neighbours.charAt(3)=='0' && neighbours.charAt(5)=='1' && neighbours.charAt(7)=='0'){
            return 20;
        }
        if(neighbours.charAt(1)=='0' && neighbours.charAt(3)=='0' && neighbours.charAt(5)=='0' && neighbours.charAt(7)=='1'){
            return 21;
        }
        if(neighbours.charAt(1)=='1' && neighbours.charAt(3)=='0' && neighbours.charAt(5)=='0' && neighbours.charAt(7)=='0'){
            return 22;
        }
        if(neighbours.charAt(1)=='0' && neighbours.charAt(3)=='1' && neighbours.charAt(5)=='0' && neighbours.charAt(7)=='0'){
            return 23;
        }
        return 3;
    }

    /**
     * Loops through the navigationGraph and connects adjacent tiles, i.e. the 8
     * nearest neighbors.
     */
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

    public MapTile getTile(Point coords) {
        return getTile(coords.x, coords.y);
    }

    public void paint(Graphics g, ImageObserver imOb) {
        Graphics2D g2d = (Graphics2D) g;
        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[0].length; j++) {
                g2d.drawImage(tiles[pixels[i][j]], GlobalConstants.tileSize * i, GlobalConstants.tileSize * j, imOb);
//                    if(getTile(i, j).getEnemy() != null){
//                        g2d.setColor(new Color(200, 20, 20, 64));
//                        g2d.fillRect(i*GlobalConstants.tileSize, j*GlobalConstants.tileSize, GlobalConstants.tileSize, GlobalConstants.tileSize);
//                    }
            }
        }
        g2d.drawImage(bunker, GlobalConstants.tileSize * target[0] - 16, GlobalConstants.tileSize * target[1] - 16, imOb);
    }

    public int[][] getPixels() {
        return pixels;
    }

    public BufferedImage getMapImage() {
        return mapImage;
    }

    /**
     * A* pathfinding algorithm. Generates a path (a stack of tiles) from the
     * input coordinates to the static goal coordinates.
     *
     * @param startX the current x tile coordinate
     * @param startY the current y tile coordinate
     * @return a stack (LinkedList) of MapTiles representing the path.
     * @throws Exception if a path to the target cannot be found.
     */
    public LinkedList<MapTile> generatePath(int startX, int startY) throws Exception {
        HashSet<MapTile> closedSet = new HashSet<>();
        PriorityQueue<Node> openSet = new PriorityQueue<>();
        HashMap<MapTile, Node> openSet2 = new HashMap<>(); // same as openSet in a different data structure

        //manhattan heuristic
        openSet.add(new Node(navigationGraph[startX][startY], 0, Math.abs(startX - target[0]) + Math.abs(startY - target[1]), null));
        LinkedList<MapTile> route = new LinkedList<>();

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
        throw new Exception("Path blocked");

    }

    public LinkedList<MapTile> generatePath(Point tileCoords) throws Exception {
        return generatePath(tileCoords.x, tileCoords.y);
    }

    /**
     * Checks and clears all tiles from a specific enemy occupation. Called when
     * an enemy is destroyed in order to make sure that it is removed by the
     * garbage collector.
     *
     * @param e
     */
    public void clearEnemy(Enemy e) {
        for (MapTile[] tileRow : navigationGraph) {
            for (MapTile tile : tileRow) {
                if (tile != null && tile.getEnemy() == e) {
                    tile.setEnemy(null);
                }
            }
        }
    }

    /**
     * The node is a temporary wrapper for the MapTile, containing info required
     * for the pathfinder.
     */
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
