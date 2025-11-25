import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A Nissin cup noodle product that can be bought in the store.
 *
 * Nissin is a Product with a fixed name and price. It uses a
 * cup noodle sprite and keeps track of the total number of
 * Nissin items created in the store using a static stock counter.
 *
 * Image source reference (art only):
 * https://www.pngegg.com/en/search?q=Cup+noodle
 *
 * Each time a Nissin object is created, the stock count increases.
 *
 * @author Owen Kung
 * @version Nov 2025
 */
public class Nissin extends Product
{
    // Tracks the total number of Nissin items in the entire store
    private static int stock = 0;

    private GreenfootImage image;
    
    /**
     * Creates a new Nissin product with name, price, and image set.
     *
     * The Nissin:
     *  - has name "Nissin"
     *  - has price $3.75
     *  - uses product/cup noodle/Cup Noodle 2.png as its image
     * Each time a Nissin is constructed, the static stock counter increases.
     */
    public Nissin()
    {
        price = 3.75;
        name  = "Nissin";

        image = new GreenfootImage("product/cup noodle/Cup Noodle 2.png");
        image.scale(image.getWidth() / 15, image.getHeight() / 15);
        setImage(image);
        
        // increase total Nissin stock
        stock++;
    }

    /**
     * Returns the total number of Nissin items in the store.
     *
     * @return total Nissin stock
     */
    public int getStock()
    {
        return stock;
    }

    /**
     * Sets the total number of Nissin items in the store.
     *
     * @param newStockCount the new total Nissin stock
     */
    public void setStock(int newStockCount)
    {
        stock = newStockCount;
    }
}

