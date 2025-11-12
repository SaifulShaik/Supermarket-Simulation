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
    
    protected ArrayList<String> shoppingList = new ArrayList<>();
    protected ArrayList<String> shoppingListStore = new ArrayList<>();
    public final ArrayList<String> supermarketProductsList = new ArrayList<>(Arrays.asList(
        "Apple", "Candy", "Carrot", "Chips", "Cup Noodle", "Lettuce", "Orange", "Pop", "Water"
        ));
    public final ArrayList<String> butcherProductsList = new ArrayList<>(Arrays.asList(
        "Bacon", "Baked Chicken", "Cooked Steak", "Drumstick", "Meat On The Bone", "Raw Chicken", "Raw Steak"
        ));
    
    private List<Product> cart;
    
    private Node previousNode;
    private Node currentNode;
    private Node targetNode;
    
    private int pauseTimer = 0;
    
    private Store store;
    
    public Customer() {}
    
    public Customer(double movementSpeed, double budget, Node currentNode) {
        this.movementSpeed = movementSpeed;
        this.budget = budget;
        
        this.currentNode = currentNode;
        targetNode = null;
        previousNode = null;
        
        store = null;
    }
    
    public void act() {
        if (store == null) {
            chooseStore();
        }
        move();
    }
    
    protected void chooseStore() {
        List<Store> stores = getWorld().getObjects(Store.class);
        
        if (stores.isEmpty()) { return; }
        
        int chosenStore;
        if (shoppingListStore.get(0) == "butcher"){ chosenStore=0; }
        else{ chosenStore=1; }
        
        System.out.println("Hi");
        
        Store s = stores.get(chosenStore);

        targetNode = s.getEntranceNode();
        store = s;
        System.out.println("Heading to store entrance at: " 
        + targetNode.getX() + "," + targetNode.getY());
    }
    
    protected void createShoppingList(int listLength){
        for(int i = 0 ; i < listLength ; i++){
            int productStore = Greenfoot.getRandomNumber(2);
            int productListLength;
            String product;
            if (productStore==0){ 
                productListLength = supermarketProductsList.size();
                product = supermarketProductsList.get(Greenfoot.getRandomNumber(productListLength));
                shoppingListStore.add("supermarket");
            }
            else{
                productListLength = butcherProductsList.size();
                product = butcherProductsList.get(Greenfoot.getRandomNumber(productListLength));
                shoppingListStore.add("butcher");
            }
            shoppingList.add(product);
        }
        System.out.println("Going to " + shoppingListStore.get(0) + " for " + shoppingList.get(0));
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