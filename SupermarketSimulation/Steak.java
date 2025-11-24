import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A cooked steak product that can be bought in the store.
 *
 * Steak is a Product with a fixed name, price, and image.
 * It also keeps track of the total number of Steak items
 * created in the store using a static stock counter.
 *
 * Each time a Steak object is created, the stock count increases.
 *
 * @author Owen Kung
 * @version Nov 2025
 */
public class Steak extends Product
{
    // Tracks the total number of Steak items in the entire store
    private static int stock = 0;

    private GreenfootImage image;
    
    /**
     * Creates a new Steak product with name, price, and image set.
     *
     * The Steak:
     *  - has name "Steak"
     *  - has price $10.50
     *  - uses product/Cooked Steak.PNG as its image
     * Each time a Steak is constructed, the static stock counter increases.
     */
    public Steak() 
    {
        price = 10.5;
        name  = "Steak";

        image = new GreenfootImage("product/Cooked Steak.PNG");
        image.scale(image.getWidth() / 3, image.getHeight() / 3);
        setImage(image);
        
        // increase total Steak stock
        stock++;
    }

    /**
     * Returns the total number of Steak items in the store.
     *
     * @return total Steak stock
     */
    public int getStock()
    {
        return stock;
    }

    /**
     * Sets the total number of Steak items in the store.
     *
     * @param newStockCount the new total Steak stock
     */
    public void setStock(int newStockCount)
    {
        stock = newStockCount;
    }
}

