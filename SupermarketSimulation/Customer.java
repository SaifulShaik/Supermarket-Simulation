import greenfoot.*; 
import java.util.*;

/**
 * An abstract customer that uses A* pathfinding to navigate the store.
 * Each customer has a shopping list, cart, and can find the shortest
 * path to each product's location before collecting it.
 *
 * @author Joe and saiful
 * @version November 2025
 */

public abstract class Customer extends SuperSmoothMover 
{
    private double movementSpeed;
    private double budget;
    
    /*protected ArrayList<String> shoppingList = new ArrayList<>();
    protected ArrayList<String> shoppingListStore = new ArrayList<>();
    public final ArrayList<String> supermarketProductsList = new ArrayList<>(Arrays.asList(
        "Apple", "Candy", "Carrot", "Chips", "Cup Noodle", "Lettuce", "Orange", "Pop", "Water"
        ));
    public final ArrayList<String> butcherProductsList = new ArrayList<>(Arrays.asList(
        "Bacon", "Baked Chicken", "Cooked Steak", "Drumstick", "Meat On The Bone", "Raw Chicken", "Raw Steak"
        ));*/
    private List<Product> shoppingList;
    private int maxShoppingListItems;
    private List<Product> cart;
    
    private Node previousNode;
    private Node currentNode;
    private Node targetNode;
    
    private int pauseTimer = 0;
    
    private Store store;
    
    public Customer() {}
    
    public Customer(double movementSpeed, double budget, Node currentNode, int maxShoppingListItems) {
        this.movementSpeed = movementSpeed;
        this.budget = budget;
        
        this.maxShoppingListItems = maxShoppingListItems;
        shoppingList = new ArrayList<>();
        
        this.currentNode = currentNode;
        targetNode = null;
        previousNode = null;
        
        store = null;
        
        shoppingList = generateShoppingList(maxShoppingListItems);
    }
    
    public void act() {
        if (store == null) {
            chooseStore();
        }
        move();
    }
    
    protected void chooseStore() {
        List<Store> stores = getWorld().getObjects(Store.class);
        
        if (stores.isEmpty()) { 
            return; 
        }
        
        List<Product> storeOneShoppingList = new ArrayList<>();
        List<Product> storeTwoShoppingList = new ArrayList<>();
        
        for (Product p : shoppingList) {
            if (SimulationWorld.storeOne.getAvailableProducts().contains(p)) {
                storeOneShoppingList.add(p);
            }
            if (SimulationWorld.storeTwo.getAvailableProducts().contains(p)) {
                storeTwoShoppingList.add(p);
            }
        }
        
        if (storeOneShoppingList.size() > storeTwoShoppingList.size()) {
            store = SimulationWorld.storeOne;
            shoppingList = storeOneShoppingList;
        }
        else if (storeOneShoppingList.size() < storeTwoShoppingList.size()){
            store = SimulationWorld.storeTwo;
            shoppingList = storeTwoShoppingList;
        }
        else {
            int chosenStore = Greenfoot.getRandomNumber(stores.size());
            store = stores.get(chosenStore);
            shoppingList = store == SimulationWorld.storeOne ? storeOneShoppingList : storeTwoShoppingList;
        }
        
        targetNode = store.getEntranceNode();
    }
    
    protected List<Product> generateShoppingList(int maxShoppingListItems) {
        List<Product> items = new ArrayList<>();
        
        List<Product> availableItems = new ArrayList<>();
        availableItems.addAll(SimulationWorld.storeOne.getAvailableProducts());
        availableItems.addAll(SimulationWorld.storeTwo.getAvailableProducts());
        
        if (maxShoppingListItems <= 0) {
            maxShoppingListItems = 1; 
        }
        
        int numItems = 1 + Greenfoot.getRandomNumber(maxShoppingListItems);
        
        for (int i = 0 ; i < numItems ; i++) {
            if (availableItems.size() > 0) {
                Product item = availableItems.get(Greenfoot.getRandomNumber(availableItems.size()));
                items.add(item);
            }
        }
        
        return items;
    }
    
    protected void move() {
        if (pauseTimer > 0) {
            pauseTimer--;
            return;
        }

        if (targetNode != null) {
            moveToNode(targetNode); 
            return;
        }

        List<Node> neighbouringNodes = currentNode.getNeighbouringNodes();
        
        if (neighbouringNodes.isEmpty() || neighbouringNodes == null) {
            return;
        }
        
        List<Node> availableNodes = new ArrayList<>();
        
        for (Node n : neighbouringNodes) {
            if (n == previousNode) continue;
            else {
                availableNodes.add(n);
            }
        }
        
        if (availableNodes.isEmpty() || availableNodes == null) {
            return;
        }
        
        Node nextNode = availableNodes.get(Greenfoot.getRandomNumber(availableNodes.size()));
        previousNode = currentNode;
        targetNode = nextNode;
        
        moveToNode(nextNode);
    }
    
    protected void moveToNode(Node n) {
        int dx = n.getX() - getX();
        int dy = n.getY() - getY();
        
        double distance = Math.sqrt(dx * dx + dy * dy);
        
        if (distance < movementSpeed) {
            setLocation(n.getX(), n.getY());
            
            currentNode = n;
            targetNode = null;
            
            pauseTimer = 5 + Greenfoot.getRandomNumber(5);
            return;
        }
        
        double angle = Math.atan2(dy, dx);
        
        double newX = getX() + Math.cos(angle) * movementSpeed;
        double newY = getY() + Math.sin(angle) * movementSpeed;
        
        setLocation(newX, newY);
    }
    
    public void leaveStore() {
        getWorld().removeObject(this);
    }
    
    public double calculatePriceOfCart() {
        double total = 0;
        
        for (Product p : cart) {
            total += p.getPrice();
        }
        
        return total;
    }
}