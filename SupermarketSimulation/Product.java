import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)



/**
 * Product class
 * represents a purchasable item inside a store
 * 
 * @author Joe
 * @version November 2025
 */

public abstract class Product extends SuperSmoothMover
{
    protected double price;
    //Owen Kung makes this static variable in subclass, 
    //so easily keep track of stock avaialbe on the shelf
    //protected int stock;  
    protected boolean isLocked;
    
    public Product() {
        
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
}


