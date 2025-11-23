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
    
    protected boolean isDiscounted = false;
    
    protected String name;
    protected Node node;
    
    protected DisplayUnit displayUnit;
    protected Store parentStore;
    
    public Product() 
    {
        price = 2.0 + (int)(Math.random() * 6); // $2 - $7
        stock = 1;
    }
    
    public void act(){
        touchingFire();
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
    
    public int getStock() {
        return stock;
    }
    
    protected void setStock(int amount) {
        stock = amount;
    }
    
    public void setDisplayUnit(DisplayUnit unit) {
        this.displayUnit = unit;
        // Inherit parent store from display unit when assigned
        if (unit != null) {
            this.parentStore = unit.getParentStore();
        }
    }
    
    public DisplayUnit getDisplayUnit() {
        return displayUnit;
    }
    
    public Store getParentStore() {
        return parentStore;
    }
    
    public void setParentStore(Store store) {
        this.parentStore = store;
    }
    
    private void touchingFire(){
        if (getOneIntersectingObject(Fire.class) != null){
            getWorld().removeObject(this);
        }
    }
}


