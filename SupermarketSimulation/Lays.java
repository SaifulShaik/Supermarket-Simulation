import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A bag of Lays chips that can be bought in the store.
 *
 * Lays is a Product with a fixed name, price, and image.
 * It also keeps track of the total number of Lays items
 * created in the store using a static stock counter.
 *
 * Each time a Lays object is created, the stock count increases.
 *
 * @author Owen Kung
 * @version Nov 2025
 */
public class Lays extends Product
{
    // Tracks the total number of Lays items in the entire store
    private static int stock = 0;

    private GreenfootImage image;
    
    /**
     * Creates a new Lays product with name, price, and image set.
     *
     * The Lays:
     *  - has name "Lays"
     *  - has price $3.00
     *  - uses product/chips/Chips 3.PNG as its image
     * Each time a Lays is constructed, the static stock counter increases.
     */
    public Lays()
    {
        price = 3.0;
        name  = "Lays";

        image = new GreenfootImage("product/chips/Chips 3.PNG");
        image.scale(image.getWidth() / 2, image.getHeight() / 2);
        setImage(image);
        
        // increase total Lays stock
        stock++;
    }

    /**
     * Returns the total number of Lays items in the store.
     *
     * @return total Lays stock
     */
    public int getStock()
    {
        return stock;
    }

    /**
     * Sets the total number of Lays items in the store.
     *
     * @param newStockCount the new total Lays stock
     */
    public void setStock(int newStockCount)
    {
        stock = newStockCount;
    }
}

