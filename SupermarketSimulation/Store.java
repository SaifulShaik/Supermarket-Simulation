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
    private boolean showGrid = false; // World grid is now drawn by SimulationWorld
    private boolean showStoreInfo = true; // Show store boundaries and special cells
    
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
     * Create the store image - completely invisible
     */
    private void createStoreImage() {
        GreenfootImage img = new GreenfootImage(width, height);
        
        // Clear the image to make it completely transparent
        img.clear();
        
        setImage(img);
    }
    
    @Override
    protected void addedToWorld(World world) {
        // Capture the store's world position when added to the world
        // Store position is center, calculate top-left corner
        worldX = getX() - width / 2;
        worldY = getY() - height / 2;
        
        // Find actual DisplayUnit positions and update product nodes
        // Note: display units may be added after the store (loaded later),
        // so this initial call may be a no-op; we also expose a public
        // refresh method so the world can force a re-scan after units are loaded.
        updateProductLocations();
    }
    
    /**
     * Update product node positions based on actual DisplayUnit world positions
     */
    /**
     * Re-scan display units in the world and update product node positions
     * to match the actual display unit locations. Public so the world can
     * call it after loading display units from saved layout.
     */
    public void updateProductLocations() {
        World world = getWorld();
        if (world == null) return;
        
        for (Product product : availableProducts) {
            // Find the DisplayUnit for this product in the world
            List<DisplayUnit> units = world.getObjects(DisplayUnit.class);
            for (DisplayUnit unit : units) {
                // Check if this display unit contains our product type
                if (unitHasProduct(unit, product)) {
                    // Convert world position to grid coordinates
                    int gridX = (unit.getX() - worldX) / cellSize;
                    int gridY = (unit.getY() - worldY) / cellSize;
                    
                    if (isInBounds(gridX, gridY)) {
                        product.setNode(nodes[gridX][gridY]);
                        System.out.println("Set " + product.getName() + " to actual position: node(" + gridX + ", " + gridY + ") world(" + unit.getX() + ", " + unit.getY() + ")");
                    }
                    break;
                }
            }
        }
    }
    
    /**
     * Check if a DisplayUnit contains a specific product type
     */
    private boolean unitHasProduct(DisplayUnit unit, Product product) {
        String productName = product.getName();
        String unitClass = unit.getClass().getSimpleName();
        
        // Map product names to display unit types
        if (productName.equals("Coke") || productName.equals("Sprite") || 
            productName.equals("Fanta") || productName.equals("Water")) {
            return unitClass.equals("Fridge");
        }
        if (productName.equals("Doritos") || productName.equals("Lays") || productName.equals("Ruffles")) {
            return unitClass.equals("SnackShelf");
        }
        if (productName.equals("Apple")) return unitClass.equals("AppleBin");
        if (productName.equals("Orange")) return unitClass.equals("OrangeBin");
        if (productName.equals("Carrot")) return unitClass.equals("CarrotBin");
        if (productName.equals("Lettuce")) return unitClass.equals("LettuceBin");
        if (productName.equals("Steak")) return unitClass.equals("SteakWarmer");
        if (productName.equals("Raw Beef")) return unitClass.equals("RawBeefHangers");
        
        return false;
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
    
    /**
     * Convert world coordinates to grid cell coordinates
     * Returns null if the position is outside this store
     */
    public Node getNodeAtWorldPosition(double worldPosX, double worldPosY) {
        // Convert world position to grid coordinates
        int gridX = (int)((worldPosX - worldX) / cellSize);
        int gridY = (int)((worldPosY - worldY) / cellSize);
        
        if (isInBounds(gridX, gridY)) {
            return nodes[gridX][gridY];
        }
        return null;
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
