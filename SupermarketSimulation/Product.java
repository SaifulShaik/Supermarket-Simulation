import java.util.LinkedList;
import java.util.List;

/**
 * Product class.
 * Represents any purchasable item inside the store.
 *
 * A Product has a price, stock count, a name, and may belong to a
 * DisplayUnit or Store. It also supports discounts applied either
 * by the store or by the SaleManager.
 *
 * This is an abstract class. Each specific item (Coke, Doritos, Steak, etc.)
 * extends Product and defines its own name and image.
 *
 * Touching fire removes this product from the world.
 *
 * @author  Joe Zhuo and Owen Kung
 * @version Nov 2025
 */
public abstract class Product extends SuperSmoothMover
{
    protected double price;          // original price before discounts
    protected int stock;             // quantity available
    protected String name;           // name of the item
    protected Node node;             // optional location node

    protected DisplayUnit displayUnit;
    protected Store parentStore;

    /**
     * Creates a Product with a random base price between $2 and $7
     * and a default stock of 1.
     */
    public Product() 
    {
        price = 2.0 + (int)(Math.random() * 6); // $2 - $7
        stock = 1;
    }

    /**
     * Called each act cycle.
     * Currently checks whether the product is touching fire.
     */
    public void act() {
        touchingFire();
    }

    /**
     * Returns the name of this product.
     *
     * @return the product name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the final price of this product after discounts.
     * 
     * Discount order:
     * 1. Store-wide discount (if the store sets one)
     * 2. SaleManager discount (if this specific product type is on sale)
     *
     * @return final price after all discounts
     */
    public double getPrice() { 
        double finalPrice = price;

        // Apply store-wide discount first
        if (parentStore != null && parentStore.getStoreDiscount() > 0) {
            finalPrice *= (1 - (parentStore.getStoreDiscount() / 100.0));
        }

        // Then apply product-specific sale discount
        if (SaleManager.isOnSale(this.getClass())) {
            finalPrice *= (1 - (SaleManager.getDiscountPercent() / 100.0));
        }

        return finalPrice;
    }

    /**
     * Returns true if this product has any discount applied
     * (either store-wide or SaleManager sale).
     */
    public boolean isDiscounted() {
        boolean storeDiscount = (parentStore != null && parentStore.getStoreDiscount() > 0);
        boolean saleDiscount = SaleManager.isOnSale(this.getClass());
        return storeDiscount || saleDiscount;
    }

    /**
     * Returns the current stock count of this product.
     *
     * @return number of units
     */
    public int getStock() {
        return stock;
    }

    /**
     * Sets how much stock this product has.
     *
     * @param amount new stock value
     */
    protected void setStock(int amount) {
        stock = amount;
    }

    /**
     * Assigns this product to a DisplayUnit.
     * Automatically updates its parentStore as well.
     *
     * @param unit the DisplayUnit holding this product
     */
    public void setDisplayUnit(DisplayUnit unit) {
        this.displayUnit = unit;
        if (unit != null) {
            this.parentStore = unit.getParentStore();
        }
    }

    /**
     * Returns the DisplayUnit that holds this product.
     */
    public DisplayUnit getDisplayUnit() {
        return displayUnit;
    }

    /**
     * Returns the Store this product belongs to.
     */
    public Store getParentStore() {
        return parentStore;
    }

    /**
     * Sets the Store this product belongs to.
     *
     * @param store the Store that owns this product
     */
    public void setParentStore(Store store) {
        this.parentStore = store;
    }

    /**
     * Removes this product if it touches a Fire object.
     */
    private void touchingFire() {
        if (getOneIntersectingObject(Fire.class) != null) {
            getWorld().removeObject(this);
        }
    }
}

