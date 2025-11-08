import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * Represents a store
 * 
 * @author Joe
 * @version November 2025
 */
public class Store extends Actor
{
    private int width;
    private int height;
    private int cellSize;
    
    public static final int maxCustomers = 8;
    private int currentCustomers;
    
    private List<Product> availableProducts;
    
    private Node[][] nodes;
    
    private int gridWidth;
    private int gridHeight;
    
    public Store(int width, int height, int cellSize) {
        this.width = width;
        this.height = height;
        this.cellSize = cellSize;
        gridWidth = width / cellSize;
        gridHeight = height / cellSize;
        
        nodes = new Node[gridWidth][gridHeight];
        
        for (int x = 0; x < gridWidth; x++) {
            for (int y = 0; y < gridHeight; y++) {
                nodes[x][y] = new Node(x, y, null, 0, 0, false, false);
            }
        }

    }
    
    public void setBlockedNode(int x, int y, boolean blocked) {
        if (isInBounds(x, y)) {
            nodes[x][y].setBlocked(blocked);
        }
    }

    public void setEntranceNode(int x, int y, boolean entrance) {
        if (isInBounds(x, y)) {
            nodes[x][y].setEntrance(entrance);
        }
    }
    
    public boolean isInBounds(int x, int y) {
        return x >= 0 && y >= 0 && x < gridWidth && y < gridHeight;
    }
    
    public boolean isBlocked(int x, int y) {
        return isInBounds(x, y) && nodes[x][y].checkIsBlocked();
    }
    
    public boolean isEntrance(int x, int y) {
        return isInBounds(x, y) && nodes[x][y].checkIsEntrance();
    }
    
    public List<Node> getEntranceNodes() {
        List<Node> entrances = new ArrayList<>();
        for (int x = 0; x < gridWidth; x++) {
            for (int y = 0; y < gridHeight; y++) {
                Node n = nodes[x][y];
                if (n.checkIsEntrance()) entrances.add(n);
            }
        }
        return entrances;
    }
    
    public int getGridWidth() { 
        return gridWidth; 
    }
    
    public int getGridHeight() { 
        return gridHeight; 
    }
    
    public Node getNode(int x, int y) {
        if (!isInBounds(x, y)) {
            return null;
        }
        return nodes[x][y];
    }
    
    public double[][] getCellCenter(int x, int y) {
        if (!isInBounds(x, y)) return null;
    
        double worldX = x * cellSize + cellSize / 2.0;
        double worldY = y * cellSize + cellSize / 2.0;
    
        return new double[][] { { worldX, worldY } };
    }
    
    public List<Product> getAvailableProducts() {
        return availableProducts;
    }
}
