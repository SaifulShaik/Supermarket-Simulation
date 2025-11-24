import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A Xing Ramen cup noodle product that can be bought in the store.
 *
 * XingRamen is a Product with a fixed name and price. It uses a
 * cup noodle sprite and keeps track of the total number of
 * Xing Ramen items created in the store using a static stock counter.
 *
 * Image source reference (art only):
 * https://www.pngegg.com/en/search?q=Cup+noodle
 *
 * Each time a XingRamen object is created, the stock count increases.
 *
 * @author Owen Kung
 * @version Nov 2025
 */
public class XingRamen extends Product
{
    // Tracks the total number of Xing Ramen items in the entire store
    private static int stock = 0;

    private GreenfootImage image;
    
    /**
     * Creates a new XingRamen product with name, price, and image set.
     *
     * The Xing Ramen:
     *  - has name "Xing Ramen"
     *  - has price $3.50
     *  - uses product/cup noodle/Cup Noodle 1.png as its image
     * Each time a XingRamen is constructed, the static stock counter increases.
     */
    public XingRamen()
    {
        price = 3.5;
        name  = "Xing Ramen";

        image = new GreenfootImage("product/cup noodle/Cup Noodle 1.png");
        image.scale(image.getWidth() / 20, image.getHeight() / 20);
        setImage(image);
        
        // increase total Xing Ramen stock
        stock++;
    }

    /**
     * Returns the total number of Xing Ramen items in the store.
     *
     * @return total Xing Ramen stock
     */
    public int getStock()
    {
        return stock;
    }

    /**
     * Sets the total number of Xing Ramen items in the store.
     *
     * @param newStockCount the new total Xing Ramen stock
     */
    public void setStock(int newStockCount)
    {
        stock = newStockCount;
    }
}

