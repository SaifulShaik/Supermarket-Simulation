import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;

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
    
    protected int facing; // 0: top, 1: right, 2: down, 3: left
    protected double[][] targetLocation;
    //protected double targetLocationoffset = UI.getHeight;
    protected double budget; 
    protected double movementSpeed; 
    protected double timeInLine;
    protected double queuePosition;
    protected boolean hasPaid;
    protected int emotion; // for now
    
    private Store currentStore;
    
    public Customer() {}
    
    public void act() {}
    protected void chooseStore() {}
    protected void enterStore() {}
    protected void leaveStore() {}
    protected void move() {
        
    }
    
    protected void moveTowards(double[][] targetLocation) {
        
    }
    
    protected void turnTowards(int direction) {
        
    }
    
    protected void addItemToCart(Product item) {
        cart.add(item);
    }
    
    protected void removeItemFromCart(Product item) {
        cart.remove(item);
    }
    
    protected List<Product> generateShoppingList() { 
        return null; 
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
