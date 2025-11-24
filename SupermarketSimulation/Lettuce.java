import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A lettuce product that can be bought in the store.
 *
 * Lettuce is a Product with a fixed name and image.
 * It also keeps track of the total number of Lettuce items
 * created in the store using a static stock counter.
 *
 * Each time a Lettuce object is created, the stock count increases.
 *
 * @author Owen Kung
 * @version Nov 2025
 */
public class Lettuce extends Product
{
    // Tracks the total number of Lettuce items in the entire store
    private static int stock = 0;

    private GreenfootImage image;
    
    /**
     * Creates a new Lettuce product with name and image set.
     *
     * The Lettuce:
     *  - has name "Lettuce"
     *  - uses product/Lettuce.PNG as its image
     * Each time a Lettuce is constructed, the static stock counter increases.
     */
    public Lettuce()
    {
        name = "Lettuce";

        image = new GreenfootImage("product/Lettuce.PNG");
        image.scale(image.getWidth() / 2, image.getHeight() / 2);
        setImage(image);
        
        // increase total Lettuce stock
        stock++;
    }

    /**
     * Returns the total number of Lettuce items in the store.
     *
     * @return total Lettuce stock
     */
    public int getStock()
    {
        return stock;
    }

    /**
     * Sets the total number of Lettuce items in the store.
     *
     * @param newStockCount the new total Lettuce stock
     */
    public void setStock(int newStockCount)
    {
        stock = newStockCount;
    }
}

