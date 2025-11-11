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
            
            // Add placeholder products to the store's product list
            // These represent the types of products available, but their nodes
            // will be assigned dynamically based on DisplayUnit positions
            availableProducts.add(new Coke());
            availableProducts.add(new Doritos());
            availableProducts.add(new Fanta());
            availableProducts.add(new Water());
            availableProducts.add(new Lays());
            availableProducts.add(new Sprite());
            availableProducts.add(new Ruffles());
        } else {
            // Right store (Store 2) - add entrance node
            nodes[9][3].setEntrance(true);
            
            // Add placeholder products to the store's product list
            availableProducts.add(new Apple());
            availableProducts.add(new Orange());
            availableProducts.add(new Carrot());
            availableProducts.add(new Lettuce());
            availableProducts.add(new Steak());
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
        
        // Get all display units in the world
        List<DisplayUnit> units = world.getObjects(DisplayUnit.class);
        
        System.out.println("Store.updateProductLocations: Found " + units.size() + " DisplayUnits in world");
        System.out.println("Store.updateProductLocations: Updating " + availableProducts.size() + " products");
        
        // For each product in our available products list, find the matching DisplayUnit
        // and assign the product's node to be the DisplayUnit's customer node
        for (Product product : availableProducts) {
            DisplayUnit matchingUnit = findDisplayUnitForProduct(product, units);
            if (matchingUnit != null) {
                // Set the DisplayUnit reference in the product
                product.setDisplayUnit(matchingUnit);
                
                // Get the node from the display unit (computed dynamically)
                Node unitNode = matchingUnit.getCustomerNode();
                if (unitNode != null) {
                    product.setNode(unitNode);
                    System.out.println("✓ Set " + product.getName() + " to DisplayUnit node at grid(" + 
                                     unitNode.getX() + ", " + unitNode.getY() + ")");
                } else {
                    System.out.println("✗ WARNING: " + product.getName() + " found matching DisplayUnit but it has no customerNode!");
                }
            } else {
                System.out.println("✗ WARNING: Could not find DisplayUnit for product: " + product.getName());
            }
        }
    }
    
    /**
     * Find the DisplayUnit that should contain this product type
     */
    private DisplayUnit findDisplayUnitForProduct(Product product, List<DisplayUnit> units) {
        String productName = product.getName();
        String targetUnitType = null;
        
        // Map product names to display unit types
        if (productName.equals("Coke") || productName.equals("Sprite") || 
            productName.equals("Fanta") || productName.equals("Water")) {
            targetUnitType = "Fridge";
        }
        else if (productName.equals("Doritos") || productName.equals("Lays") || productName.equals("Ruffles")) {
            targetUnitType = "SnackShelf";
        }
        else if (productName.equals("Apple")) targetUnitType = "AppleBin";
        else if (productName.equals("Orange")) targetUnitType = "OrangeBin";
        else if (productName.equals("Carrot")) targetUnitType = "CarrotBin";
        else if (productName.equals("Lettuce")) targetUnitType = "LettuceBin";
        else if (productName.equals("Steak")) targetUnitType = "SteakWarmer";
        else if (productName.equals("Raw Beef")) targetUnitType = "RawBeefHangers";
        
        if (targetUnitType == null) {
            System.out.println("  No target unit type for product: " + productName);
            return null;
        }
        
        System.out.println("  Looking for " + targetUnitType + " for product " + productName);
        
        // Find the closest matching unit in this store's area
        DisplayUnit closest = null;
        double minDistance = Double.MAX_VALUE;
        int matchCount = 0;
        
        for (DisplayUnit unit : units) {
            String unitType = unit.getClass().getSimpleName();
            if (unitType.equals(targetUnitType)) {
                matchCount++;
                System.out.println("    Found matching " + unitType + " at (" + unit.getX() + ", " + unit.getY() + ")");
                
                // Calculate distance from store center to find the closest unit
                // (Don't require units to be in store grid - they can be anywhere)
                double dx = unit.getX() - (worldX + width/2);
                double dy = unit.getY() - (worldY + height/2);
                double dist = Math.sqrt(dx*dx + dy*dy);
                
                System.out.println("      Distance from store center: " + (int)dist + " pixels");
                
                if (dist < minDistance) {
                    minDistance = dist;
                    closest = unit;
                }
            }
        }
        
        if (matchCount == 0) {
            System.out.println("    ✗ No units of type " + targetUnitType + " found in world");
        } else if (closest != null) {
            System.out.println("    ✓ Selected closest " + targetUnitType + " at distance " + (int)minDistance);
        }
        
        return closest;
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
