import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)



/**
 * Product class
 * represents a purchasable item inside a store
 * 
 * @author Owen Kung
 * @version Nov 2025
 */

public abstract class Product extends SuperSmoothMover
{
    protected double price;
    protected boolean isLocked;
    
    protected Node node;
    
    public Product() 
    {
        
    }
    
    protected double getPrice() { 
        return price; 
    }
    protected void applyDiscount(double percent) {
        price *= (1 - (percent / 100));
    }
 
    protected abstract int getStock() ;    
    protected abstract void setStock(int amount);
    
    protected void unlock() {
        
    }
    
    public Node getNode() {
        return node;
    }
}


