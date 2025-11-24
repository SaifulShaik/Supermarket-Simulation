import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A raw beef product that can be bought in the store.
 *
 * RawBeef is a Product with a fixed name and image.
 * It also keeps track of the total number of RawBeef items
 * created in the store using a static stock counter.
 *
 * Each time a RawBeef object is created, the stock count increases.
 *
 * @author Owen Kung
 * @version Nov 2025
 */
public class RawBeef extends Product
{
    // Tracks the total number of RawBeef items in the entire store
    private static int stock = 0;

    private GreenfootImage image;
    
    /**
     * Creates a new RawBeef product with name and image set.
     *
     * The RawBeef:
     *  - has name "Raw Beef"
     *  - uses product/Raw Beef.PNG as its image
     * Each time a RawBeef is constructed, the static stock counter increases.
     */
    public RawBeef() 
    {
        name = "Raw Beef";

        image = new GreenfootImage("product/Raw Beef.PNG");
        image.scale(image.getWidth() / 4, image.getHeight() / 4);
        setImage(image);
        
        // increase total RawBeef stock
        stock++;
    }

    /**
     * Returns the total number of RawBeef items in the store.
     *
     * @return total RawBeef stock
     */
    public int getStock()
    {
        return stock;
    }

    /**
     * Sets the total number of RawBeef items in the store.
     *
     * @param newStockCount the new total RawBeef stock
     */
    public void setStock(int newStockCount)
    {
        stock = newStockCount;
    }
}

