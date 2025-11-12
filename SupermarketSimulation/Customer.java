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
    private double movementSpeed;
    private double budget;
    
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
        
        if (stores.isEmpty()) {
            return;
        }
        
        Store s = stores.get(Greenfoot.getRandomNumber(stores.size()));

        targetNode = s.getEntranceNode();
        store = s;
        System.out.println("Heading to store entrance at: " 
        + targetNode.getX() + "," + targetNode.getY());
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