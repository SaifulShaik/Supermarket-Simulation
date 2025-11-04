import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;

/**
 * Write a description of class Customer here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public abstract class Customer extends SmoothMover
{
    private List<Product> shoppingList;
    private List<Product> cart;
    
    private double budget; 
    private double movementSpeed; 
    private double timeInLine;
    private double queuePosition;
    private int emotion; // 0: happy 1: neutral 2: angry 3: mad 4: raging
    
    public Customer() {}
    public void act() {}
    protected void chooseStore() {}
    protected void enterStore() {}
    protected void leaveStore() {}
    protected void move() {}
    protected void addItemToCart(Product item) {}
    protected void removeItemFromCart(Product item) {}
    protected List<Product> generateShoppingList() { return null; }
    protected void checkout() {}
    protected double calculatePriceOfCart() { return 0;}
}
