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
    protected double price; // Original price before discount
    protected int stock;
    
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
    
    // Returns price with store discount applied dynamically
    // In Product.java - replace getPrice() method
    public double getPrice() { 
        double finalPrice = price; 
        
        // Apply store-wide discount first
        if (parentStore != null && parentStore.getStoreDiscount() > 0) {
            finalPrice *= (1 - (parentStore.getStoreDiscount() / 100.0));
        }
        
        // Then apply sale discount if this product is on sale
        if (SaleManager.isOnSale(this.getClass())) {
            finalPrice *= (1 - (SaleManager.getDiscountPercent() / 100.0));
        }
        
        return finalPrice;
    }

    public boolean isDiscounted() {
        boolean storeDiscount = (parentStore != null && parentStore.getStoreDiscount() > 0);
        boolean saleDiscount = SaleManager.isOnSale(this.getClass());
        return storeDiscount || saleDiscount;
    }
    
    public int getStock() {
        return stock;
    }
    
    protected void setStock(int amount) {
        stock = amount;
    }
    
    public void setDisplayUnit(DisplayUnit unit) {
        this.displayUnit = unit;
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