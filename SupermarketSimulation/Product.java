import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Product class
 * represents a purchasable item inside a store
 * 
 * @author Joe
 * @version November 2025
 */
public abstract class Product extends Actor
{
    protected double price;
    protected int stock;
    protected boolean isLocked;
    
    public Product() {
        
    }
    
    public void act() {
        
    }
    
    protected double getPrice() { 
        return price; 
    }
    
    protected void applyDiscount(double percent) {
        price *= (1 - (percent / 100));
    }
    
    protected int getStock() { 
        return stock; 
    }
    
    protected void setStock(int amount) {
        stock = amount;
    }
    
    protected void unlock() {
        
    }
}
