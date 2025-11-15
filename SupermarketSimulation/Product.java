import java.util.LinkedList;
import java.util.List;

/**
 * Product class
 * represents a purchasable item inside a store
 * 
 * @author Joe Zhuo and Owen Kung
 * @version Nov 2025
 */

public abstract class Product extends SuperSmoothMover
{
    protected double price;
    protected int stock;
    
    protected boolean isLocked;
    protected boolean isDiscounted=false;
    
    protected String name;
    protected Node node;
    
    protected DisplayUnit displayUnit; 
    
    public Product() 
    {
        price = 2.0 + (int)(Math.random() * 6); // $2 - $7
        stock = 1;
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
        isDiscounted = true;
    }
    
    protected int getStock() {
        return stock;
    }
    
    protected void setStock(int amount) {
        stock = amount;
    }
    
    public void setDisplayUnit(DisplayUnit unit) {
        this.displayUnit = unit;
    }
    
    public DisplayUnit getDisplayUnit() {
        return displayUnit;
    }
}


