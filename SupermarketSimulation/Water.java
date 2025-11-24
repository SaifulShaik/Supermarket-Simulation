import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A bottled water product that can be bought in the store.
 *
 * Water is a Product with a fixed name and image.
 * It also keeps track of the total number of Water items
 * created in the store using a static stock counter.
 *
 * Each time a Water object is created, the stock count increases.
 *
 * @author Owen Kung 
 * @version Nov 6 2025
 */
public class Water extends Product
{
    private GreenfootImage image;

    // Keeps track of total Water stock in the store
    private static int stock = 0;
    
    /**
     * Creates a new Water product with name and image set.
     *
     * The Water:
     *  - has name "Water"
     *  - uses product/Water.PNG as its image
     * Each time a Water is constructed, the static stock counter increases.
     */
    public Water()
    {
        name = "Water";

        image = new GreenfootImage("product/Water.PNG");
        image.scale(image.getWidth() / 2, image.getHeight() / 2);
        setImage(image);
        
        // increase total Water stock
        stock++;
    }

    /**
     * Returns the total number of Water items in the store.
     *
     * @return total Water stock
     */
    public int getStock()
    {
        return stock;
    }

    /**
     * Sets the total number of Water items in the store.
     *
     * @param newStockCount the new total Water stock
     */
    public void setStock(int newStockCount)
    {
        stock = newStockCount;
    }

    /**
     * Returns the total number of Water items in the store.
     * Same as getStock(), but static so it can be called without an object.
     *
     * @return total Water stock
     */
    public static int getCurrentStock()
    {
        return stock;
    }
}

