import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A Jin Ramen cup noodle product that can be bought in the store.
 *
 * JinRamen is a Product with a fixed name and price. It uses a
 * cup noodle sprite and keeps track of the total number of Jin Ramen
 * items created in the store using a static stock counter.
 *
 * Image source reference (art only): 
 * https://www.pngegg.com/en/search?q=Cup+noodle
 *
 * @author Owen Kung
 * @version Nov 2025
 */
public class JinRamen extends Product
{
    // Tracks the total number of Jin Ramen items in the entire store
    private static int stock = 0;

    private GreenfootImage image;
    
    /**
     * Creates a new JinRamen product with name, price, and image set.
     *
     * The Jin Ramen:
     *  - has name "Jin Ramen"
     *  - has price $3.50
     *  - uses product/cup noodle/Cup Noodle 3.png as its image
     * Each time a JinRamen is constructed, the static stock counter increases.
     */
    public JinRamen()
    {
        price = 3.5;
        name  = "Jin Ramen";

        image = new GreenfootImage("product/cup noodle/Cup Noodle 3.png");
        image.scale(image.getWidth() / 20, image.getHeight() / 20);
        setImage(image);
        
        // increase total Jin Ramen stock
        stock++;
    }

    /**
     * Returns the total number of Jin Ramen items in the store.
     *
     * @return total Jin Ramen stock
     */
    public int getStock()
    {
        return stock;
    }

    /**
     * Sets the total number of Jin Ramen items in the store.
     *
     * @param newStockCount the new total Jin Ramen stock
     */
    public void setStock(int newStockCount)
    {
        stock = newStockCount;
    }
}


