
import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A bag of Doritos chips that can be bought in the store.
 *
 * Doritos is a Product with a fixed name and image. It also keeps
 * track of the total number of Doritos items created in the store
 * using a static stock counter.
 *
 * Each time a Doritos object is created, the stock count increases.
 *
 * @author Owen Kung
 * @version Nov 2025
 */
public class Doritos extends Product
{
    // Tracks the total number of Doritos items in the entire store
    private static int stock = 0;

    private GreenfootImage image;
    
    /**
     * Creates a new Doritos product with name and image set.
     *
     * The Doritos:
     *  - has name "Doritos"
     *  - uses product/chips/Chips 1.PNG as its image
     * Each time a Doritos is constructed, the static stock counter increases.
     */
    public Doritos()
    {
        name = "Doritos";

        image = new GreenfootImage("product/chips/Chips 1.PNG");
        image.scale(image.getWidth() / 2, image.getHeight() / 2);
        setImage(image);
        
        // increase total Doritos stock
        stock++;
    }

    /**
     * Returns the total number of Doritos items in the store.
     *
     * @return total Doritos stock
     */
    public int getStock()
    {
        return stock;
    }

    /**
     * Sets the total number of Doritos items in the store.
     *
     * @param newStockCount the new total Doritos stock
     */
    public void setStock(int newStockCount)
    {
        stock = newStockCount;
    }
}

