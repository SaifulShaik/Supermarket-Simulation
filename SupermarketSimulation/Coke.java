import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A Coke soft drink product that can be bought in the store.
 *
 * Coke is a Product with a fixed name and price. It uses a single
 * Coke can sprite and keeps track of the total number of Coke items
 * created in the store using a static stock counter.
 *
 * Each time a Coke object is created, the stock count increases.
 *
 * @author Owen Kung 
 * @version Nov 6 2025
 */
public class Coke extends Product
{
    private GreenfootImage image;

    // Keeps track of total Coke stock in the store
    private static int stock = 0;
     
    /**
     * Creates a new Coke product with name, price, and image set.
     *
     * The Coke:
     *  - has name "Coke"
     *  - has price $2.50
     *  - uses product/pop/Pop 3.PNG as its image
     * Each time a Coke is constructed, the static stock counter increases.
     */
    public Coke()
    {
        price = 2.5;
        name = "Coke";

        image = new GreenfootImage("product/pop/Pop 3.PNG");
        image.scale(image.getWidth() / 2, image.getHeight() / 2);
        setImage(image);
        
        // increase total Coke stock
        stock++;
    }

    /**
     * Returns the total number of Coke items in the store.
     *
     * @return total Coke stock
     */
    public int getStock()
    {
        return stock;
    }

    /**
     * Returns the total number of Coke items in the store.
     * Same as getStock(), but static so it can be called without an object.
     *
     * @return total Coke stock
     */
    public static int getCurrentStock()
    {
        return stock;
    }

    /**
     * Sets the total number of Coke items in the store.
     *
     * @param newStockCount the new total Coke stock
     */
    public void setStock(int newStockCount)
    {
        stock = newStockCount;
    }
}

