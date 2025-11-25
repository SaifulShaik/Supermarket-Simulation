import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A bag of Ruffles chips that can be bought in the store.
 *
 * Ruffles is a Product with a fixed name and image.
 * It also keeps track of the total number of Ruffles items
 * created in the store using a static stock counter.
 *
 * Each time a Ruffles object is created, the stock count increases.
 *
 * @author Owen Kung
 * @version Nov 2025
 */
public class Ruffles extends Product
{
    // Tracks the total number of Ruffles items in the entire store
    private static int stock = 0;

    private GreenfootImage image;
    
    /**
     * Creates a new Ruffles product with name and image set.
     *
     * The Ruffles:
     *  - has name "Ruffles"
     *  - uses product/chips/Chips 2.PNG as its image
     * Each time a Ruffles is constructed, the static stock counter increases.
     */
    public Ruffles()
    {
        name = "Ruffles";

        image = new GreenfootImage("product/chips/Chips 2.PNG");
        image.scale(image.getWidth() / 2, image.getHeight() / 2);
        setImage(image);

        // increase total Ruffles stock
        stock++;
    }

    /**
     * Returns the total number of Ruffles items in the store.
     *
     * @return total Ruffles stock
     */
    public int getStock()
    {
        return stock;
    }

    /**
     * Sets the total number of Ruffles items in the store.
     *
     * @param newStockCount the new total Ruffles stock
     */
    public void setStock(int newStockCount)
    {
        stock = newStockCount;
    }
}

