import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;

public class Store extends Actor
{
    public final int width = 450;
    public final int height = 500;
    public final int cellSize = 50;
    
    public static final int maxCustomers = 8;
    private int currentCustomers;
    
    private List<Product> availableProducts;
    
    private boolean[][] blockedCells;
    private int gridWidth;
    private int gridHeight;
    
    public Store() {
        gridWidth = width / cellSize;
        gridHeight = height / cellSize;
        blockedCells = new boolean[gridWidth][gridHeight];
    }
    
    public void setBlockedCells(int x, int y, boolean value) {
        if (isInBounds(x, y)) {
            blockedCells[x][y] = value;
        }
    }
    
    public boolean isInBounds(int x, int y) {
        return x >= 0 && y >= 0 && x < gridWidth && y < gridHeight;
    }
    
    public boolean isBlocked(int x, int y) {
        return blockedCells[x][y] && isInBounds(x, y);
    }
    
    public int getGridWidth() { 
        return gridWidth; 
    }
    
    public int getGridHeight() { 
        return gridHeight; 
    }
}
