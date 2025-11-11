import greenfoot.*; 
import java.util.*;

/**
 * An abstract customer that uses A* pathfinding to navigate the store.
 * Each customer has a shopping list, cart, and can find the shortest
 * path to each product's location before collecting it.
 *
 * @author Joe
 * @version November 2025
 */
public abstract class Customer extends SuperSmoothMover
{
    // ====== Configurable parameters ======
    protected static final int MAX_SHOPPING_LIST_ITEMS = 3;
    protected static final int COLLECTING_DELAY = 30;   // frames pause when collecting

    // ====== Shopping data ======
    protected List<Product> shoppingList = new ArrayList<>();
    protected List<Product> cart = new ArrayList<>();
    protected Product currentProductTarget;

    // ====== Pathfinding data ======
    protected Pathfinder pathfinder;
    protected Node currentNode;
    protected List<Node> currentPath;
    protected Node nextNode;

    // ====== Store and movement ======
    protected Store currentStore;
    protected boolean storeChosen = false;
    private boolean isMoving = false;
    private boolean isCollecting = false;
    private int collectingTimer = 0;
    private double targetX, targetY;
    private boolean isExiting = false;
    private boolean hasExited = false;

    // ====== Customer stats ======
    protected double budget;
    protected double movementSpeed;
    protected boolean hasPaid;
    protected int emotion;

    public Customer() {
        budget = 20 + Greenfoot.getRandomNumber(81); // 20 to 100 dollar budget
        movementSpeed = 2;
    }

    public Customer(int budget, int speed) {
        this.budget = budget;
        this.movementSpeed = speed;
    }

    /**
     * Main Loop
     */
    public void act() {
        if (isCollecting) {
            handleCollecting();
            return;
        }

        if (!storeChosen) {
            chooseStoreAndEnter();
            return;
        }

        if (currentStore == null) return;

        if (shoppingList.isEmpty() && !isExiting) {
            System.out.println("Customer finished shopping with " + cart.size() + " items!");
            leaveStore();
            return;
        }
        
        // Continue moving toward the exit if leaving
        if (isExiting) {
            moveAlongExitPath();
            return;
        }

        if (currentProductTarget == null) {
            chooseNextProduct();
        }

        moveAlongPath();
    }

    /**
     * Choose Store and enter it
     */
    protected void chooseStoreAndEnter() {
        List<Store> stores = getWorld().getObjects(Store.class);
        
        if (stores.isEmpty()) {
            System.out.println("ERROR: No stores found!");
            getWorld().removeObject(this);
            return;
        }

        // Pick store with available products
        List<Store> viableStores = new ArrayList<>();
        List<List<Product>> storeLists = new ArrayList<>();

        for (Store s : stores) {
            List<Product> list = generateShoppingListFromStore(s);
            if (!list.isEmpty()) {
                viableStores.add(s);
                storeLists.add(list);
            }
        }

        if (viableStores.isEmpty()) {
            System.out.println("ERROR: No viable stores found!");
            getWorld().removeObject(this);
            return;
        }

        int randomIndex = Greenfoot.getRandomNumber(viableStores.size());
        currentStore = viableStores.get(randomIndex);
        shoppingList = storeLists.get(randomIndex);

        List<Node> entrances = currentStore.getEntranceNodes();
        if (entrances.isEmpty()) {
            System.out.println("ERROR: Store has no entrances!");
            getWorld().removeObject(this);
            return;
        }

        Node entrance = entrances.get(Greenfoot.getRandomNumber(entrances.size()));
        pathfinder = new Pathfinder(currentStore);
        currentNode = entrance;

        double[][] center = currentStore.getCellCenter(entrance.getX(), entrance.getY());
        setLocation(center[0][0], center[0][1]);

        storeChosen = true;
        System.out.println("Customer entered store at node (" + entrance.getX() + ", " + entrance.getY() + ")");
    }

    /**
     * Path based movement
     */
    protected void moveAlongPath() {
        if (currentProductTarget == null || currentStore == null || pathfinder == null) return;

        Node productNode = currentProductTarget.getNode();
        
        if (productNode == null) {
            System.out.println("ERROR: Product node is null, skipping item.");
            shoppingList.remove(currentProductTarget);
            currentProductTarget = null;
            return;
        }

        // If we can access the product directly, collect it
        if (canAccessItem(currentProductTarget)) {
            collectCurrentProduct();
            return;
        }

        // If no path or we reached the end of it, find a new one
        if (currentPath == null || currentPath.isEmpty()) {
            currentPath = pathfinder.findPath(currentNode.getX(), currentNode.getY(), productNode.getX(), productNode.getY());

            if (currentPath == null || currentPath.isEmpty()) {
                System.out.println("No path found to product, removing it.");
                shoppingList.remove(currentProductTarget);
                currentProductTarget = null;
                return;
            }

            System.out.println("Path found with " + currentPath.size() + " nodes.");
        }

        if (!isMoving) {
            // Move to the next node in the path
            nextNode = currentPath.get(0);
            double[][] targetCenter = currentStore.getCellCenter(nextNode.getX(), nextNode.getY());
            targetX = targetCenter[0][0];
            targetY = targetCenter[0][1];
            isMoving = true;
        }

        continueMovement();
    }

    protected void continueMovement() {
        if (!isMoving) return;

        double dx = targetX - getX();
        double dy = targetY - getY();

        if (Math.abs(dx) < movementSpeed && Math.abs(dy) < movementSpeed) {
            // Snap to grid position
            setLocation(targetX, targetY);
            currentNode = nextNode;
            currentPath.remove(0);
            isMoving = false;
            return;
        }

        double angle = Math.atan2(dy, dx);
        
        double moveX = Math.cos(angle) * movementSpeed;
        double moveY = Math.sin(angle) * movementSpeed;
        
        setLocation(getX() + moveX, getY() + moveY);
    }

    /**
     * Collecting items
     */
    protected boolean canAccessItem(Product p) {
        if (p == null || currentNode == null) return false;
        Node productNode = p.getNode();

        if (currentNode.equals(productNode)) return true;

        int[][] dirs = { {0, -1}, {0, 1}, {-1, 0}, {1, 0} };
        for (int[] d : dirs) {
            if (currentNode.getX() + d[0] == productNode.getX() &&
                currentNode.getY() + d[1] == productNode.getY()) {
                return true;
            }
        }
        return false;
    }

    protected void collectCurrentProduct() {
        System.out.println("Collecting " + currentProductTarget.getName());
        addItemToCart(currentProductTarget);
        currentProductTarget = null;
        isCollecting = true;
        collectingTimer = COLLECTING_DELAY;
    }

    protected void handleCollecting() {
        collectingTimer--;
        if (collectingTimer <= 0) {
            isCollecting = false;
            chooseNextProduct();
        }
    }

    // ====== SHOPPING LOGIC ======
    protected void chooseNextProduct() {
        if (shoppingList == null || shoppingList.isEmpty()) {
            currentProductTarget = null;
            System.out.println("Shopping list empty!");
            return;
        }

        currentProductTarget = shoppingList.get(0);
        Node node = currentProductTarget.getNode();

        if (node == null) {
            shoppingList.remove(0);
            chooseNextProduct();
            return;
        }

        double[][] pos = currentStore.getCellCenter(node.getX(), node.getY());
        System.out.println("Next target: " + currentProductTarget.getName() +
                           " at (" + node.getX() + ", " + node.getY() + ") => (" +
                           (int)pos[0][0] + ", " + (int)pos[0][1] + ")");
    }

    protected void addItemToCart(Product item) {
        if (item != null && !cart.contains(item)) {
            cart.add(item);
            shoppingList.remove(item);
            System.out.println("Added to cart: " + item.getName() + " | Remaining: " + shoppingList.size());
        }
    }

    // ====== STORE EXIT ======
    protected void leaveStore() {
        if (isExiting) return;
        
        if (currentStore == null || pathfinder == null) {
            getWorld().removeObject(this);
            return;
        }
    
        isExiting = true;
        System.out.println("Customer finished shopping — heading to exit.");
    
        int exitX = currentStore.getGridWidth() / 2 + 3;
        int exitY = currentStore.getGridHeight() - 1; 
        if (!currentStore.isInBounds(exitX, exitY)) {
            System.out.println("Exit position out of bounds!");
            getWorld().removeObject(this);
            return;
        }
    
        currentPath = pathfinder.findPath(
            currentNode.getX(), currentNode.getY(),
            exitX, exitY
        );
    
        if (currentPath == null || currentPath.isEmpty()) {
            System.out.println("No path to exit found — removing customer.");
            getWorld().removeObject(this);
            return;
        }
    
        System.out.println("Exit path found with " + currentPath.size() + " nodes.");
    }

    // ====== SHOPPING LIST CREATION ======
    protected List<Product> generateShoppingListFromStore(Store store) {
        List<Product> list = new ArrayList<>();
        if (store == null) return list;

        List<Product> products = store.getAvailableProducts();
        if (products == null || products.isEmpty()) return list;

        List<Product> valid = new ArrayList<>();
        for (Product p : products) {
            if (p.getNode() != null) valid.add(p);
        }

        int count = Math.min(MAX_SHOPPING_LIST_ITEMS, valid.size());
        for (int i = 0; i < count; i++) {
            Product p = valid.remove(Greenfoot.getRandomNumber(valid.size()));
            list.add(p);
        }
        return list;
    }
    
    protected void moveAlongExitPath() {
        if (currentPath == null || currentPath.isEmpty()) {
            if (!hasExited) {
                System.out.println("Customer reached exit — leaving store!");
                hasExited = true;
                getWorld().removeObject(this);
            }
            return;
        }
    
        if (!isMoving) {
            nextNode = currentPath.get(0);
            double[][] targetCenter = currentStore.getCellCenter(nextNode.getX(), nextNode.getY());
            targetX = targetCenter[0][0];
            targetY = targetCenter[0][1];
            isMoving = true;
        }
    
        double dx = targetX - getX();
        double dy = targetY - getY();
    
        if (Math.abs(dx) < movementSpeed && Math.abs(dy) < movementSpeed) {
            setLocation(targetX, targetY);
            currentNode = nextNode;
            currentPath.remove(0);
            isMoving = false;
            return;
        }
    
        double angle = Math.atan2(dy, dx);
        double moveX = Math.cos(angle) * movementSpeed;
        double moveY = Math.sin(angle) * movementSpeed;
        setLocation(getX() + moveX, getY() + moveY);
    }
    
    public double calculatePriceOfCart() {
        double total = 0;
        
        for (Product p : cart) {
            total += p.getPrice();
        }
        
        return total;
    }
}
