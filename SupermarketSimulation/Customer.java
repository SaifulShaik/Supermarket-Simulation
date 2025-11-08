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
        shoppingList = generateShoppingList();
        cart = new ArrayList<>();
        budget = 20 + Greenfoot.getRandomNumber(81); //20 - 100 dollar budget
        movementSpeed = 1 + (Greenfoot.getRandomNumber(21) / 10); // 1-3 peed
        emotion = 0;
        currentStore = null;
    }
    
    public Customer(int budget, int speed) {
        shoppingList = generateShoppingList();
        cart = new ArrayList<>();
        this.budget = budget;
        movementSpeed = speed;
        emotion = 0;
        currentStore = null;
    }
    
    public void act() {
        if (currentStore == null) {
            chooseStore();
            enterStore();
        }
        else if (currentNode == null) {
            moveToStore();
        }
        else if (!shoppingList.isEmpty()) {
            move();
            if (currentProductTarget == null) {
                chooseNextProduct();
            }
        } else if (!hasPaid) {
            checkout();
        } else {
            leaveStore();
        }

    }
    
    protected void chooseStore() {
        // get first store
        // make it choose better store based on products later
        List<Store> stores = getWorld().getObjects(Store.class);
        if (!stores.isEmpty()) {
            currentStore = stores.get(0);
        }
    }
    
     protected void enterStore() {
        if (currentStore == null) return;

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
        
        if (dx < 1 && dy < 1) currentNode = targetNode;
        
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
        if (targetNode == null || currentNode == null || pathfinder == null) return;
    
        List<Node> path = pathfinder.findPath(currentNode.getX(), currentNode.getY(), targetNode.getX(), targetNode.getY());
        
        if (path.isEmpty()) return;
    
        Node nextNode = null;
    
        if (canAccessItem(currentProductTarget) && path.size() > 1) {
            nextNode = path.get(path.size() - 2); 
        } 
        else if (path.size() > 1) {
            nextNode = path.get(1); 
        } 
        else {
            nextNode = path.get(0); 
        }
    
        if (nextNode == null) return;
    
        double[][] loc = currentStore.getCellCenter(nextNode.getX(), nextNode.getY());
        
        if (loc != null) {
            double dx = loc[0][0] - getX();
            double dy = loc[0][1] - getY();
            double dist = Math.sqrt(dx*dx + dy*dy);
    
            if (dist <= movementSpeed) {
                setLocation((int)loc[0][0], (int)loc[0][1]);
                currentNode = nextNode;
            } else {
                double nx = getX() + dx / dist * movementSpeed;
                double ny = getY() + dy / dist * movementSpeed;
                setLocation((int)nx, (int)ny);
            }
        }
    }
    
    protected void turnTowards(int direction) {
        
    }
    
    protected void chooseNextProduct() {
        // call checkout when list is empty
        if (shoppingList == null || shoppingList.isEmpty()) {
            currentProductTarget = null;
            return;
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
