import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * An orange product that can be bought in the store.
 *
 * Orange is a Product with a fixed name and image.
 * It also keeps track of the total number of Orange items
 * created in the store using a static stock counter.
 *
 * Each time an Orange object is created, the stock count increases.
 *
 * @author Owen Kung
 * @version Nov 2025
 */
public class Orange extends Product
{
    // Tracks the total number of Orange items in the entire store
    private static int stock = 0;

    private GreenfootImage image;
    
    /**
     * Creates a new Orange product with name and image set.
     *
     * The Orange:
     *  - has name "Orange"
     *  - uses product/Orange.PNG as its image
     * Each time an Orange is constructed, the static stock counter increases.
     */
    public Orange()
    {
        name = "Orange";

        image = new GreenfootImage("product/Orange.PNG");
        image.scale(image.getWidth() / 2, image.getHeight() / 2);
        setImage(image);
        
        // increase total Orange stock
        stock++;
    }

    /**
     * Returns the total number of Orange items in the store.
     *
     * @return total Orange stock
     */
    public int getStock()
    {
        return stock;
    }

    /**
     * Sets the total number of Orange items in the store.
     *
     * @param newStockCount the new total Orange stock
     */
    public void setStock(int newStockCount)
    {
        stock = newStockCount;
    }
}

