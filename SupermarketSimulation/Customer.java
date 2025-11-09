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
    
    // Grid-based movement variables
    private boolean isMoving = false;
    private double targetX;
    private double targetY;
    private static final int MOVE_SPEED = 2; // pixels per frame
    
    public Customer() {
        cart = new ArrayList<>();
        budget = 20 + Greenfoot.getRandomNumber(81); //20 - 100 dollar budget
        movementSpeed = MOVE_SPEED;
        emotion = 0;
        currentStore = null;
    }
    
    public Customer(int budget, int speed) {
        cart = new ArrayList<>();
        this.budget = budget;
        movementSpeed = MOVE_SPEED;
        emotion = 0;
        currentStore = null;
    }
    
    public void act() {
        if (currentStore == null) {
            chooseStore();
        }
        else if (currentNode == null) {
            // Moving to store entrance
            moveToStore();
        }
        else if (!shoppingList.isEmpty()) {
            // Inside store, shopping
            if (currentProductTarget == null) {
                chooseNextProduct();
            }
            move();
        } 
        else {
            // Shopping complete
            System.out.println("Customer finished shopping with " + cart.size() + " items!");
            getWorld().removeObject(this);
        }
        /*else if (!hasPaid) {
            checkout();
        } 
        else {
            leaveStore();
        }*/

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
    
    protected void move() {
        // Check if we can collect current product
        if (currentProductTarget != null && canAccessItem(currentProductTarget)) {
            System.out.println("Collecting: " + currentProductTarget.getName());
            addItemToCart(currentProductTarget);
            currentProductTarget = null;
            targetNode = null;
            currentPath = null;
            
            // Choose next product if list not empty
            if (!shoppingList.isEmpty()) {
                chooseNextProduct();
            }
            return;
        }
        
        // If no current target, choose next product
        if (currentProductTarget == null && !shoppingList.isEmpty()) {
            chooseNextProduct();
        }
    
        // Move towards current product target
        if (currentProductTarget != null && targetNode != null) {
            moveTowards(targetNode);
        }
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
            return;
        }
        
        // If already moving to a cell, continue that movement
        if (isMoving) {
            continueMovement();
            return;
        }
    
        // Generate path if we don't have one
        if (currentPath == null || currentPath.isEmpty()) {
            currentPath = pathfinder.findPath(currentNode.getX(), currentNode.getY(), targetNode.getX(), targetNode.getY());
            
            if (currentPath == null || currentPath.isEmpty()) {
                System.out.println("No path found from (" + currentNode.getX() + ", " + currentNode.getY() + ") to (" + targetNode.getX() + ", " + targetNode.getY() + ")");
                return;
            }
        }
        
        // Check if we can access the product
        if (canAccessItem(currentProductTarget)) {
            System.out.println("Can access product, collecting it");
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
        }
    }
    
    // do proper turning later
    protected void turnTowards(int direction) {}
    
    protected void chooseNextProduct() {
        if (shoppingList == null || shoppingList.isEmpty()) {
            currentProductTarget = null;
            targetNode = null;
            currentPath = null;
            System.out.println("Shopping complete! All items collected.");
            return;
        }
        
        currentProductTarget = shoppingList.get(0);
        
        if (currentProductTarget == null || currentProductTarget.getNode() == null) {
            System.out.println("ERROR: Product has no node! Removing from list.");
            shoppingList.remove(0);
            chooseNextProduct(); // Try next product
            return;
        }
        
        targetNode = currentProductTarget.getNode();
        currentPath = null; // Clear old path to force new pathfinding
        
        System.out.println("New target: " + currentProductTarget.getName() + " at node (" + targetNode.getX() + ", " + targetNode.getY() + ")");
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
