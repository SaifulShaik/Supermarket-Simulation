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
    protected boolean isDiscounted=false;
    protected String name;
    protected Node node;
    
    public Product() 
    {
        // Set default price (can be overridden by subclasses)
        price = 2.0 + (Greenfoot.getRandomNumber(6)); // $2 - $7
    }
    public String getName()
    {
        return name;
    }
    public double getPrice() { 
        return price; 
    }
    protected void applyDiscount(double percent) {
        price *= (1 - (percent / 100));
        isDiscounted=true;
    }
    public String toString()
    {
        String result=name+":" ;
        if(isDiscounted)
        {
            result=result+".  Discounted to-$"+price;
        }
        else
        {
            result=result+ " Price -$" + price;
        }
        return result;
    }
    protected abstract int getStock() ;    
    protected abstract void setStock(int amount);
    
    protected void unlock() {
        
    }
    
    public void setNode(Node node)
    {   
        this.node=node;
        
    }
    public Node getNode()
    {   
        return node;
        
    }
}


