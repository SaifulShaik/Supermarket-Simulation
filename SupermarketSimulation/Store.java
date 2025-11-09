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
    
    private boolean isLeftStore;
    
    // Store's world position (top-left corner)
    private int worldX;
    private int worldY;
    
    // Grid visualization
    private boolean showGrid = true; // Set to false to hide grid
    
    public Store(int width, int height, int cellSize, boolean isLeftStore) {
        this.width = width;
        this.height = height;
        this.cellSize = cellSize;
        this.isLeftStore = isLeftStore;
        gridWidth = width / cellSize;
        gridHeight = height / cellSize;
        availableProducts = new ArrayList<Product>();
        
        nodes = new Node[gridWidth][gridHeight];
        
        for (int x = 0; x < gridWidth; x++) {
            for (int y = 0; y < gridHeight; y++) {
                nodes[x][y] = new Node(x, y, null, 0, 0, false, false);
            }
        }
        
        if (isLeftStore) {
            // block nodes that aren't in the store (deals with offset for the store frame)
            for (int x = 0; x < gridWidth; x++) {
                for (int y = 0; y < gridHeight; y++) {
                    if (x < 2) nodes[x][y].setBlocked(true);  // Only block leftmost edge
                    if (y < 1) nodes[x][y].setBlocked(true);
                }
            }
            
            // entrance node
            nodes[23][21].setEntrance(true);
            
            // Add products to the store - moved to nodes that aren't blocked
            Coke coke = new Coke();
            availableProducts.add(coke);
            coke.setNode(nodes[13][13]);
            
            Doritos doritos = new Doritos();
            availableProducts.add(doritos);
            doritos.setNode(nodes[14][13]);
            
            Fanta fanta = new Fanta();
            availableProducts.add(fanta);
            fanta.setNode(nodes[15][13]);
            
            Water water = new Water();
            availableProducts.add(water);
            water.setNode(nodes[16][13]);
            
            Lays lays = new Lays();
            availableProducts.add(lays);
            lays.setNode(nodes[17][13]);
            
            Sprite sprite = new Sprite();
            availableProducts.add(sprite);
            sprite.setNode(nodes[18][13]);
            
            Ruffles ruffles = new Ruffles();
            availableProducts.add(ruffles);
            ruffles.setNode(nodes[19][13]);
        } else {
            // Right store (Store 2) - add entrance node
            nodes[9][3].setEntrance(true);
            
            // Add products to the store
            Apple apple = new Apple();
            availableProducts.add(apple);
            apple.setNode(nodes[2][3]);
            
            Orange orange = new Orange();
            availableProducts.add(orange);
            orange.setNode(nodes[3][3]);
            
            Carrot carrot = new Carrot();
            availableProducts.add(carrot);
            carrot.setNode(nodes[4][3]);
            
            Lettuce lettuce = new Lettuce();
            availableProducts.add(lettuce);
            lettuce.setNode(nodes[5][3]);
            
            Steak steak = new Steak();
            availableProducts.add(steak);
            steak.setNode(nodes[6][3]);
        }
        
        // Create the store visual with grid after all nodes are set up
        createStoreImage();
    }
    
    /**
     * Create the store image with visible grid
     */
    private void createStoreImage() {
        GreenfootImage img = new GreenfootImage(width, height);
        
        // Fill with semi-transparent background
        img.setColor(new Color(255, 255, 255));
        img.setTransparency(50);
        img.fill();
        
        if (showGrid) {
            // First, mark blocked nodes with a darker color
            img.setColor(new Color(150, 150, 150));
            img.setTransparency(100);
            for (int x = 0; x < gridWidth; x++) {
                for (int y = 0; y < gridHeight; y++) {
                    if (nodes[x][y].checkIsBlocked()) {
                        img.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
                    }
                }
            }
            
            // Draw grid lines
            img.setColor(new Color(0, 0, 0));
            img.setTransparency(80);
            
            // Vertical lines
            for (int x = 0; x <= width; x += cellSize) {
                img.drawLine(x, 0, x, height);
            }
            
            // Horizontal lines
            for (int y = 0; y <= height; y += cellSize) {
                img.drawLine(0, y, width, y);
            }
            
            // Highlight entrance nodes
            img.setColor(new Color(0, 255, 0));
            img.setTransparency(150);
            for (int x = 0; x < gridWidth; x++) {
                for (int y = 0; y < gridHeight; y++) {
                    if (nodes[x][y].checkIsEntrance()) {
                        img.fillRect(x * cellSize + 2, y * cellSize + 2, cellSize - 4, cellSize - 4);
                    }
                }
            }
            
            // Draw thicker border
            img.setColor(Color.BLACK);
            img.setTransparency(255);
            img.drawRect(0, 0, width - 1, height - 1);
            img.drawRect(1, 1, width - 3, height - 3);
        }
        
        setImage(img);
    }
    
    @Override
    protected void addedToWorld(World world) {
        // Capture the store's world position when added to the world
        // Store position is center, calculate top-left corner
        worldX = getX() - width / 2;
        worldY = getY() - height / 2;
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
    
        // Calculate world position based on store's position
        double cellWorldX = worldX + (x * cellSize) + (cellSize / 2.0);
        double cellWorldY = worldY + (y * cellSize) + (cellSize / 2.0);
    
        return new double[][] { { cellWorldX, cellWorldY } };
    }
    
    public int getCellSize() {
        return cellSize;
    }
    
    public int getStoreWorldX() {
        return worldX;
    }
    
    public int getStoreWorldY() {
        return worldY;
    }
    
    public List<Product> getAvailableProducts() {
        return availableProducts;
    }
}
