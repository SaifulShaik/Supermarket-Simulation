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
    
    public Customer() {
        cart = new ArrayList<>();
        budget = 20 + Greenfoot.getRandomNumber(81); //20 - 100 dollar budget
        movementSpeed = 1 + (Greenfoot.getRandomNumber(21) / 10); // 1-3 peed
        emotion = 0;
        currentStore = null;
    }
    
    public Customer(int budget, int speed) {
        cart = new ArrayList<>();
        this.budget = budget;
        movementSpeed = speed;
        emotion = 0;
        currentStore = null;
    }
    
    public void act() {
        if (currentStore == null) {
            chooseStore();
        }
        else if (currentNode == null) {
            moveToStore();
        }
        else if (!shoppingList.isEmpty()) {
            if (currentProductTarget == null) {
                chooseNextProduct();
            }
            move();
        } 
        else {
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
        // get first store
        // make it choose better store based on products later
        List<Store> stores = getWorld().getObjects(Store.class);
        if (!stores.isEmpty()) {
            currentStore = stores.get(0);
        }
        
        shoppingList = generateShoppingList();
        
        List<Node> entrances = currentStore.getEntranceNodes();
        if (!entrances.isEmpty()) {
            targetNode = entrances.get(Greenfoot.getRandomNumber(entrances.size()));
        }
    
        pathfinder = new Pathfinder(currentStore);
    }
    
    protected void leaveStore() {
        // we'll just remove it for now
        // later change it to walk towards bottom of the road
        getWorld().removeObject(this);
    }
    
    protected void move() {
        if (currentProductTarget == null && !shoppingList.isEmpty()) {
            chooseNextProduct();
        }
    
        if (currentProductTarget != null) {
            targetNode = currentProductTarget.getNode();
            moveTowards(targetNode);
    
            if (canAccessItem(currentProductTarget)) {
                addItemToCart(currentProductTarget);
                currentProductTarget = null;
            }
        }
    }
    
    protected void moveToStore() {
        double dx = targetNode.getWorldX() - getX();
        double dy = targetNode.getWorldY() - getY();
        
        if (dx < 1 && dy < 1) {
            currentNode = targetNode;
            
            currentPath = null;
            targetNode = null;
        }
        
        double angle = Math.atan2(dy, dx);
        
        double newX = getX() + Math.cos(angle) * movementSpeed;
        double newY = getY() + Math.sin(angle) * movementSpeed;
        
        setLocation(newX, newY);
    }
    
    protected boolean canAccessItem(Product p) {
        if (p == null || currentStore == null) return false;
        
        Node productNode = p.getNode();
        if (productNode == null) return false;
    
        int[][] directions = { {0, -1}, {0, 1}, {-1, 0}, {1, 0} };
        
        for (int[] dir : directions) {
            Node neighbor = currentStore.getNode(currentNode.getX() + dir[0], currentNode.getY() + dir[1]);
            
            if (neighbor != null && neighbor.equals(productNode)) {
                return true;
            }
        }
        return false;                           
    }
    
    protected void moveTowards(Node targetNode) {
        if (targetNode == null || currentNode == null || pathfinder == null) {
            return;
        }
    
        if (currentPath == null || currentPath.isEmpty()) {
            currentPath = pathfinder.findPath(currentNode.getX(), currentNode.getY(), targetNode.getX(), targetNode.getY());
            
            if (currentPath == null || currentPath.isEmpty()) return;
        }
        
        if (canAccessItem(currentProductTarget)) return;
        
        Node nextNode = currentPath.get(0);
    
        double[][] location = currentStore.getCellCenter(nextNode.getX(), nextNode.getY());
        
        if (location != null) {
            double dx = location[0][0] - getX();
            double dy = location[0][1] - getY();
            
            if (Math.hypot(dx, dy) < 1) {
                currentPath.remove(0);
                currentNode = nextNode;
                return;
            }
            
            double angle = Math.atan2(dy, dx);
            
            double newX = getX() + Math.cos(angle) * movementSpeed;
            double newY = getY() + Math.sin(angle) * movementSpeed;
    
            setLocation(newX, newY);
        }
    }
    
    // do proper turning later
    protected void turnTowards(int direction) {}
    
    protected void chooseNextProduct() {
        if (shoppingList == null || shoppingList.isEmpty()) {
            currentProductTarget = null;
            return; // call checkout when list is empty later
        }
        
        currentProductTarget = shoppingList.get(0);
        targetNode = currentProductTarget.getNode();
    }
    
    protected void addItemToCart(Product item) {
        if (item != null && !cart.contains(item)) {
            cart.add(item);
            shoppingList.remove(item);
        }
    }
    
    protected void removeItemFromCart(Product item) {
        cart.remove(item);
    }
    
    protected List<Product> generateShoppingList() { 
        // will use all products later
        List<Product> list = new ArrayList<>();
    
        if (currentStore == null) return list;
        
        List<Product> storeProducts = currentStore.getAvailableProducts();
        if (storeProducts == null || storeProducts.isEmpty()) return list;
        
        int numItems = 1 + Greenfoot.getRandomNumber(5);
        
        for (int i = 0; i < numItems; i++) {
            Product p = storeProducts.get(Greenfoot.getRandomNumber(storeProducts.size()));
            if (!list.contains(p)) { 
                list.add(p);
            }
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
