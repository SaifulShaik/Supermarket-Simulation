import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * An object that represents a customer inside a store
 * 
 * @author Joe
 * @version November 2025
 */
public abstract class Customer extends SuperSmoothMover
{
    // Configurable shopping list parameters
    protected static int MAX_SHOPPING_LIST_ITEMS = 3; // Easy to change maximum items
    
    protected List<Product> shoppingList;
    protected List<Product> cart;
    protected Product currentProductTarget;
    
    protected Node targetNode;
    protected List<Node> currentPath;
    protected Node currentNode;
    
    private Pathfinder pathfinder;
    
    protected int facing; // 0: top, 1: right, 2: down, 3: left
    //protected double targetLocationoffset = UI.getHeight;
    protected double budget; 
    protected double movementSpeed; 
    protected double timeInLine;
    protected double queuePosition;
    protected boolean hasPaid;
    protected int emotion; // handle this later
    
    private Store currentStore;
    private boolean storeChosen = false; // Flag to prevent re-choosing store
    
    // Grid-based movement variables
    private boolean isMoving = false;
    private double targetX;
    private double targetY;
    private static final int MOVE_SPEED = 2; // pixels per frame
    
    // Collecting animation
    private boolean isCollecting = false;
    private int collectingTimer = 0;
    private static final int COLLECTING_DELAY = 30; // frames to pause when collecting
    
    public Customer() {
        cart = new ArrayList<>();
        budget = 20 + Greenfoot.getRandomNumber(81); //20 - 100 dollar budget
        movementSpeed = MOVE_SPEED;
        emotion = 0;
        currentStore = null;
        storeChosen = false;
    }
    
    public Customer(int budget, int speed) {
        cart = new ArrayList<>();
        this.budget = budget;
        movementSpeed = MOVE_SPEED;
        emotion = 0;
        currentStore = null;
        storeChosen = false;
    }
    
    public void act() {
        // If currently collecting an item, wait
        if (isCollecting) {
            collectingTimer--;
            if (collectingTimer <= 0) {
                isCollecting = false;
                // Choose next product after collecting delay
                if (!shoppingList.isEmpty()) {
                    chooseNextProduct();
                }
            }
            return; // Don't move while collecting
        }
        
        // Choose store only once when first spawned
        if (!storeChosen) {
            chooseStore();
            storeChosen = true; // Mark as chosen to prevent re-choosing
            
            // After choosing a store, teleport to entrance
            if (currentStore != null && targetNode != null) {
                double[][] entrancePos = currentStore.getCellCenter(targetNode.getX(), targetNode.getY());
                if (entrancePos != null) {
                    setLocation(entrancePos[0][0], entrancePos[0][1]);
                    currentNode = targetNode;
                    targetNode = null;
                    System.out.println("Customer entered store at (" + getX() + ", " + getY() + ")");
                }
            } else {
                System.out.println("ERROR: Failed to choose store or entrance!");
                getWorld().removeObject(this);
                return;
            }
        }
        
        // Only proceed if store was chosen successfully
        if (currentStore == null) {
            return; // Wait for next frame
        }
        
        if (!shoppingList.isEmpty()) {
            // Inside store, shopping
            if (currentProductTarget == null) {
                chooseNextProduct();
            }
            
            // Simple movement - just move directly to the product
            if (currentProductTarget != null) {
                moveToProduct();
            }
        } 
        else {
            // Shopping complete
            System.out.println("Customer finished shopping with " + cart.size() + " items!");
            getWorld().removeObject(this);
        }
    }
    
    protected void chooseStore() {
        // Get all stores
        List<Store> stores = getWorld().getObjects(Store.class);
        
        if (stores.isEmpty()) {
            System.out.println("ERROR: No stores found!");
            return;
        }
        
        System.out.println("\n=== CUSTOMER SPAWNING ===");
        System.out.println("Budget: $" + String.format("%.2f", budget));
        System.out.println("Found " + stores.size() + " stores:");
        for (int i = 0; i < stores.size(); i++) {
            Store s = stores.get(i);
            System.out.println("  Store " + i + " at (" + s.getX() + ", " + s.getY() + ") with " + s.getAvailableProducts().size() + " products");
        }
        
        // Try to generate a shopping list from each store and pick one that has products
        List<Store> viableStores = new ArrayList<>();
        List<List<Product>> potentialLists = new ArrayList<>();
        
        for (Store store : stores) {
            List<Product> tempList = generateShoppingListFromStore(store);
            if (!tempList.isEmpty()) {
                viableStores.add(store);
                potentialLists.add(tempList);
            }
        }
        
        if (viableStores.isEmpty()) {
            System.out.println("ERROR: No stores have available products!");
            getWorld().removeObject(this);
            return;
        }
        
        // Randomly select one viable store
        int randomIndex = Greenfoot.getRandomNumber(viableStores.size());
        currentStore = viableStores.get(randomIndex);
        shoppingList = potentialLists.get(randomIndex);
        
        System.out.println("Selected Store at (" + currentStore.getX() + ", " + currentStore.getY() + ")");
        System.out.println("Shopping List (" + shoppingList.size() + " items):");
        double totalCost = 0;
        for (Product p : shoppingList) {
            double itemPrice = p.getPrice();
            System.out.println("  - " + p.getName() + " at node (" + p.getNode().getX() + ", " + p.getNode().getY() + ") ($" + String.format("%.2f", itemPrice) + ")");
            totalCost += itemPrice;
        }
        System.out.println("Total Cost: $" + String.format("%.2f", totalCost));
        System.out.println("Remaining: $" + String.format("%.2f", (budget - totalCost)));
        System.out.println("=========================\n");
        
        // Get entrance nodes and randomly select one
        List<Node> entrances = currentStore.getEntranceNodes();
        if (!entrances.isEmpty()) {
            targetNode = entrances.get(Greenfoot.getRandomNumber(entrances.size()));
            System.out.println("Target entrance node: (" + targetNode.getX() + ", " + targetNode.getY() + ")");
        } else {
            System.out.println("ERROR: Store has no entrance nodes!");
        }
    
        pathfinder = new Pathfinder(currentStore);
    }
    
    protected void leaveStore() {
        // we'll just remove it for now
        // later change it to walk towards bottom of the road
        getWorld().removeObject(this);
    }
    
    /**
     * Simple direct movement to product - no pathfinding
     */
    protected void moveToProduct() {
        if (currentProductTarget == null) return;
        
        // Find the actual DisplayUnit for this product in the world
        // Must be in the same store area as the customer
        List<DisplayUnit> allDisplayUnits = getWorld().getObjects(DisplayUnit.class);
        DisplayUnit targetUnit = null;
        
        String productName = currentProductTarget.getName();
        
        // Find the closest matching DisplayUnit (should be in our store)
        double closestDistance = Double.MAX_VALUE;
        
        for (DisplayUnit unit : allDisplayUnits) {
            String unitClass = unit.getClass().getSimpleName();
            boolean matches = false;
            
            // Match product to its display unit type
            if ((productName.equals("Coke") || productName.equals("Sprite") || 
                 productName.equals("Fanta") || productName.equals("Water")) && unitClass.equals("Fridge")) {
                matches = true;
            }
            else if ((productName.equals("Doritos") || productName.equals("Lays") || 
                 productName.equals("Ruffles")) && unitClass.equals("SnackShelf")) {
                matches = true;
            }
            else if (productName.equals("Apple") && unitClass.equals("AppleBin")) {
                matches = true;
            }
            else if (productName.equals("Orange") && unitClass.equals("OrangeBin")) {
                matches = true;
            }
            else if (productName.equals("Carrot") && unitClass.equals("CarrotBin")) {
                matches = true;
            }
            else if (productName.equals("Lettuce") && unitClass.equals("LettuceBin")) {
                matches = true;
            }
            else if (productName.equals("Steak") && unitClass.equals("SteakWarmer")) {
                matches = true;
            }
            else if (productName.equals("Raw Beef") && unitClass.equals("RawBeefHangers")) {
                matches = true;
            }
            
            // If it matches, check if it's closer than previous matches
            if (matches) {
                double dx = unit.getX() - getX();
                double dy = unit.getY() - getY();
                double distance = Math.sqrt(dx * dx + dy * dy);
                
                if (distance < closestDistance) {
                    closestDistance = distance;
                    targetUnit = unit;
                }
            }
        }
        
        if (targetUnit == null) {
            System.out.println("ERROR: Could not find DisplayUnit for " + currentProductTarget.getName());
            // Remove this product and try the next one
            shoppingList.remove(currentProductTarget);
            currentProductTarget = null;
            return;
        }
        
        int targetX = targetUnit.getX();
        int targetY = targetUnit.getY();
        
        int dx = targetX - (int)getX();
        int dy = targetY - (int)getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        
        // If we're close enough, collect the item
        if (distance < 50) {
            System.out.println("Reached " + currentProductTarget.getName() + "!");
            
            // Remove one item from the display unit visually
            if (targetUnit instanceof DisplayUnit) {
                DisplayUnit displayUnit = (DisplayUnit) targetUnit;
                
                // Find the product class and retrieve one item
                try {
                    Class<?> productClass = currentProductTarget.getClass();
                    displayUnit.retrieve(productClass);
                    System.out.println("Removed 1 " + currentProductTarget.getName() + " from shelf");
                } catch (Exception e) {
                    System.out.println("Could not remove item from display");
                }
            }
            
            // Add to cart and start collecting animation
            addItemToCart(currentProductTarget);
            currentProductTarget = null;
            
            // Start collecting pause
            isCollecting = true;
            collectingTimer = COLLECTING_DELAY;
            
            return;
        }
        
        // Calculate movement direction and new position
        double angle = Math.atan2(dy, dx);
        double moveX = Math.cos(angle) * MOVE_SPEED;
        double moveY = Math.sin(angle) * MOVE_SPEED;
        
        // Calculate new position
        double newX = getX() + moveX;
        double newY = getY() + moveY;
        
        // Check if new position would collide with DisplayUnit
        if (!wouldCollideWithDisplayUnit(newX, newY)) {
            // Safe to move
            setLocation(newX, newY);
        } else {
            // Try moving only horizontally
            if (!wouldCollideWithDisplayUnit(getX() + moveX, getY())) {
                setLocation(getX() + moveX, getY());
            }
            // Try moving only vertically
            else if (!wouldCollideWithDisplayUnit(getX(), getY() + moveY)) {
                setLocation(getX(), getY() + moveY);
            }
            // Otherwise don't move (blocked)
        }
        
        // Keep customer upright
        setRotation(0);
    }
    
    /**
     * Check if moving to a position would collide with a DisplayUnit
     */
    private boolean wouldCollideWithDisplayUnit(double x, double y) {
        // Temporarily move to check collision
        double oldX = getX();
        double oldY = getY();
        setLocation(x, y);
        
        // Check for collision with DisplayUnits
        List<DisplayUnit> displayUnits = getIntersectingObjects(DisplayUnit.class);
        boolean collision = !displayUnits.isEmpty();
        
        // Move back to original position
        setLocation(oldX, oldY);
        
        return collision;
    }
    
    /**
     * Turn to face a specific point
     */
    private void turnTowards(double x, double y) {
        double dx = x - getX();
        double dy = y - getY();
        double angle = Math.atan2(dy, dx);
        setRotation((int)Math.toDegrees(angle));
    }
    
    protected void moveToStore() {
        if (targetNode == null || currentStore == null) return;
        
        // If already moving, continue the current movement
        if (isMoving) {
            continueMovement();
            return;
        }
        
        // Get the world position of the target entrance node
        double[][] cellCenter = currentStore.getCellCenter(targetNode.getX(), targetNode.getY());
        if (cellCenter == null) return;
        
        targetX = cellCenter[0][0];
        targetY = cellCenter[0][1];
        
        double dx = targetX - getX();
        double dy = targetY - getY();
        
        // If close enough to the entrance, enter the store
        if (Math.abs(dx) < MOVE_SPEED && Math.abs(dy) < MOVE_SPEED) {
            setLocation(targetX, targetY);
            currentNode = targetNode;
            currentPath = null;
            targetNode = null;
            isMoving = false;
            System.out.println("Customer entered store at node (" + currentNode.getX() + ", " + currentNode.getY() + ")");
            return;
        }
        
        // Start new movement - move one axis at a time (Manhattan style)
        if (Math.abs(dx) > Math.abs(dy)) {
            // Move horizontally
            if (dx > 0) {
                targetX = getX() + currentStore.getCellSize();
                targetY = getY();
                setRotation(0);  // Face right
            } else {
                targetX = getX() - currentStore.getCellSize();
                targetY = getY();
                setRotation(180);  // Face left
            }
        } else {
            // Move vertically
            if (dy > 0) {
                targetX = getX();
                targetY = getY() + currentStore.getCellSize();
                setRotation(90);  // Face down
            } else {
                targetX = getX();
                targetY = getY() - currentStore.getCellSize();
                setRotation(270);  // Face up
            }
        }
        
        isMoving = true;
    }
    
    /**
     * Continue moving towards the current target position
     */
    protected void continueMovement() {
        double dx = targetX - getX();
        double dy = targetY - getY();
        
        // Check if we've reached the target
        if (Math.abs(dx) < MOVE_SPEED && Math.abs(dy) < MOVE_SPEED) {
            setLocation(targetX, targetY);
            isMoving = false;
            // Note: currentNode is updated in moveTowards(), not here
            return;
        }
        
        // Move towards target (only one axis should have distance)
        if (Math.abs(dx) > 0.1) {
            // Moving horizontally
            if (dx > 0) {
                setLocation(getX() + MOVE_SPEED, getY());
            } else {
                setLocation(getX() - MOVE_SPEED, getY());
            }
        } else if (Math.abs(dy) > 0.1) {
            // Moving vertically
            if (dy > 0) {
                setLocation(getX(), getY() + MOVE_SPEED);
            } else {
                setLocation(getX(), getY() - MOVE_SPEED);
            }
        }
    }
    
    protected boolean canAccessItem(Product p) {
        if (p == null || currentStore == null || currentNode == null) return false;
        
        Node productNode = p.getNode();
        if (productNode == null) return false;
    
        // Check if we're at the product's node
        if (currentNode.getX() == productNode.getX() && currentNode.getY() == productNode.getY()) {
            return true;
        }
        
        // Check if we're adjacent to the product node (can reach it)
        int[][] directions = { {0, -1}, {0, 1}, {-1, 0}, {1, 0} };
        
        for (int[] dir : directions) {
            int checkX = currentNode.getX() + dir[0];
            int checkY = currentNode.getY() + dir[1];
            
            if (checkX == productNode.getX() && checkY == productNode.getY()) {
                return true;
            }
        }
        return false;                           
    }
    
    protected void moveTowards(Node targetNode) {
        if (targetNode == null || currentNode == null || pathfinder == null) {
            System.out.println("DEBUG: moveTowards failed - targetNode=" + (targetNode != null) + ", currentNode=" + (currentNode != null) + ", pathfinder=" + (pathfinder != null));
            return;
        }
        
        // If already moving to a cell, continue that movement
        if (isMoving) {
            continueMovement();
            return;
        }
    
        // Generate path if we don't have one
        if (currentPath == null || currentPath.isEmpty()) {
            System.out.println("Finding path from (" + currentNode.getX() + ", " + currentNode.getY() + ") to (" + targetNode.getX() + ", " + targetNode.getY() + ")");
            currentPath = pathfinder.findPath(currentNode.getX(), currentNode.getY(), targetNode.getX(), targetNode.getY());
            
            if (currentPath == null || currentPath.isEmpty()) {
                System.out.println("ERROR: No path found from (" + currentNode.getX() + ", " + currentNode.getY() + ") to (" + targetNode.getX() + ", " + targetNode.getY() + ")");
                System.out.println("  Current node blocked: " + currentNode.checkIsBlocked());
                System.out.println("  Target node blocked: " + targetNode.checkIsBlocked());
                return;
            }
            System.out.println("Path found with " + currentPath.size() + " nodes");
        }
        
        // Check if we can access the product
        if (canAccessItem(currentProductTarget)) {
            System.out.println("Can access product from current position, collecting it");
            return;
        }
        
        // Get next node in path
        Node nextNode = currentPath.get(0);
        double[][] location = currentStore.getCellCenter(nextNode.getX(), nextNode.getY());
        
        if (location != null) {
            double dx = location[0][0] - getX();
            double dy = location[0][1] - getY();
            
            // If we're at the next node, remove it from path and update current node
            if (Math.abs(dx) < MOVE_SPEED && Math.abs(dy) < MOVE_SPEED) {
                setLocation(location[0][0], location[0][1]);
                currentPath.remove(0);
                currentNode = nextNode;
                System.out.println("Reached node (" + nextNode.getX() + ", " + nextNode.getY() + "), " + currentPath.size() + " nodes remaining");
                return;
            }
            
            // Start moving to the next cell
            this.targetX = location[0][0];
            this.targetY = location[0][1];
            
            // Set rotation based on direction
            if (Math.abs(dx) > Math.abs(dy)) {
                if (dx > 0) setRotation(0);   // Right
                else setRotation(180);         // Left
            } else {
                if (dy > 0) setRotation(90);   // Down
                else setRotation(270);          // Up
            }
            
            isMoving = true;
        } else {
            System.out.println("ERROR: Could not get cell center for node (" + nextNode.getX() + ", " + nextNode.getY() + ")");
        }
    }
    
    // do proper turning later
    protected void turnTowards(int direction) {}
    
    protected void chooseNextProduct() {
        if (shoppingList == null || shoppingList.isEmpty()) {
            currentProductTarget = null;
            System.out.println("Shopping list empty!");
            return;
        }
        
        currentProductTarget = shoppingList.get(0);
        
        if (currentProductTarget == null || currentProductTarget.getNode() == null) {
            System.out.println("ERROR: Product has no node! Removing from list.");
            shoppingList.remove(0);
            chooseNextProduct(); // Try next product
            return;
        }
        
        Node productNode = currentProductTarget.getNode();
        double[][] productPos = currentStore.getCellCenter(productNode.getX(), productNode.getY());
        
        System.out.println("Next target: " + currentProductTarget.getName() + 
                         " at node (" + productNode.getX() + ", " + productNode.getY() + ")" +
                         " world pos: (" + (int)productPos[0][0] + ", " + (int)productPos[0][1] + ")");
    }
    
    protected void addItemToCart(Product item) {
        if (item != null && !cart.contains(item)) {
            cart.add(item);
            shoppingList.remove(item);
            System.out.println("Added to cart: " + item.getName() + " | Items remaining: " + shoppingList.size());
        }
    }
    
    protected void removeItemFromCart(Product item) {
        cart.remove(item);
    }
    
    protected List<Product> generateShoppingList() { 
        return generateShoppingListFromStore(currentStore);
    }
    
    /**
     * Generate a shopping list from a specific store
     */
    protected List<Product> generateShoppingListFromStore(Store store) { 
        List<Product> list = new ArrayList<>();
    
        if (store == null) return list;
        
        List<Product> storeProducts = store.getAvailableProducts();
        if (storeProducts == null || storeProducts.isEmpty()) return list;
        
        // Filter out products that don't have valid nodes
        List<Product> validProducts = new ArrayList<>();
        for (Product p : storeProducts) {
            if (p.getNode() != null) {
                validProducts.add(p);
            }
        }
        
        if (validProducts.isEmpty()) return list;
        
        // Pick MAX_SHOPPING_LIST_ITEMS random items
        int numItems = Math.min(MAX_SHOPPING_LIST_ITEMS, validProducts.size());
        
        // Create a copy to pick from
        List<Product> availableProducts = new ArrayList<>(validProducts);
        
        // Randomly pick items without duplicates
        for (int i = 0; i < numItems && !availableProducts.isEmpty(); i++) {
            int randomIndex = Greenfoot.getRandomNumber(availableProducts.size());
            Product p = availableProducts.get(randomIndex);
            list.add(p);
            availableProducts.remove(randomIndex); // Remove to avoid duplicates
        }
        
        return list;
    }
    
    protected void checkout() {}
    protected void chooseCashier() {}
    
    protected double calculatePriceOfCart() { 
        double totalCost = 0;
        
        for (Product p : cart) {
            totalCost += p.getPrice();
        }
        
        return totalCost;
    }
}
