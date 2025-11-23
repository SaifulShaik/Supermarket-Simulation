import java.util.LinkedList;
import java.util.List;

/**
 * Represents a purchasable item stored inside a DisplayUnit.
 * A Product has a base price, a stock count, and can belong to a DisplayUnit
 * and a Store. Some items may be discounted during a sale.
 *
 * Each product is expected to be a visual object placed by a DisplayUnit
 * and retrieved by customers.
 *
 * @author Joe Zhuo and Saiful Shaik
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

    /**
     * Creates a product with a random base price between two dollars
     * and seven dollars. The item begins with a stock count of one.
     */
    public Product() 
    {
        price = 2.0 + (int)(Math.random() * 6);
        stock = 1;
    }

    /**
     * Returns the name of the product.
     *
     * @return the product name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Returns the current price of the product. If the product was discounted,
     * the returned price reflects the reduced amount.
     *
     * @return the price of the product
     */
    public double getPrice() { 
        return price; 
    }

    /**
     * Applies a discount to the product price by the given percentage.
     * Once discounted, the product is marked so that it is not discounted twice.
     *
     * @param percent the discount percentage to apply
     */
    protected void applyDiscount(double percent) {
        price *= (1 - (percent / 100));
        isDiscounted = true;
    }

    /**
     * Returns the remaining stock for this product item.
     *
     * @return the current stock count
     */
    public int getStock() {
        return stock;
    }

    /**
     * Sets the stock count for this product.
     *
     * @param amount the new stock value
     */
    protected void setStock(int amount) {
        stock = amount;
    }

    /**
     * Assigns the DisplayUnit that this product belongs to.
     * The product will also inherit the parent store from the unit.
     *
     * @param unit the DisplayUnit placing the product
     */
    public void setDisplayUnit(DisplayUnit unit) {
        this.displayUnit = unit;

        if (unit != null) {
            this.parentStore = unit.getParentStore();
        }
    }

    /**
     * Returns the DisplayUnit that currently holds this product.
     *
     * @return the display unit
     */
    public DisplayUnit getDisplayUnit() {
        return displayUnit;
    }

    /**
     * Returns the Store that this product belongs to, as inherited
     * from the DisplayUnit.
     *
     * @return the parent store
     */
    public Store getParentStore() {
        return parentStore;
    }

    /**
     * Returns true if this product has already received a discount.
     *
     * @return whether the product is discounted
     */
    public boolean isDiscounted() {
        return isDiscounted;
    }
}