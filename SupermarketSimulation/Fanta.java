import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A Fanta soft drink product that can be bought in the store.
 *
 * Fanta is a Product with a fixed name and price. It uses a
 * single Fanta can sprite and keeps track of the total number
 * of Fanta items created in the store using a static stock counter.
 *
 * Each time a Fanta object is created, the stock count increases.
 *
 * @author Owen Kung 
 * @version Nov 6 2025
 */
public class Fanta extends Product
{
    private GreenfootImage image;

    // Keeps track of total Fanta stock in the store
    private static int stock = 0;
     
    /**
     * Creates a new Fanta product with name, price, and image set.
     *
     * The Fanta:
     *  - has name "Fanta"
     *  - has price $2.50
     *  - uses product/pop/Pop 1.PNG as its image
     * Each time a Fanta is constructed, the static stock counter increases.
     */
    public Fanta()
    {
        price = 2.5;
        name  = "Fanta";

        image = new GreenfootImage("product/pop/Pop 1.PNG");
        image.scale(image.getWidth() / 2, image.getHeight() / 2);
        setImage(image);
        
        // increase total Fanta stock
        stock++;
    }

    /**
     * Returns the total number of Fanta items in the store.
     *
     * @return total Fanta stock
     */
    public int getStock()
    {
        return stock;
    }

    /**
     * Sets the total number of Fanta items in the store.
     *
     * @param newStockCount the new total Fanta stock
     */
    public void setStock(int newStockCount)
    {
        stock = newStockCount;
    }

    /**
     * Returns the total number of Fanta items in the store.
     * Same as getStock(), but static so it can be called without an object.
     *
     * @return total Fanta stock
     */
    public static int getCurrentStock()
    {
        return stock;
    }
}

