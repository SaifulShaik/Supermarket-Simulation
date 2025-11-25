import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A cooked chicken drumstick product that can be bought in the store.
 *
 * DrumStick is a Product with a fixed name, price, and image.
 * It also keeps track of the total number of DrumStick items
 * created in the store using a static stock counter.
 *
 * Each time a DrumStick object is created, the stock count increases.
 *
 * @author Owen Kung
 * @version Nov 2025
 */
public class DrumStick extends Product
{
    // Tracks the total number of DrumStick items in the entire store
    private static int stock = 0;

    private GreenfootImage image;
    
    /**
     * Creates a new DrumStick product with name, price, and image set.
     *
     * The drumstick:
     *  - has name "Drumstick"
     *  - has price $4.50
     *  - uses product/Drumstick.PNG as its image
     * Each time a DrumStick is constructed, the static stock counter increases.
     */
    public DrumStick() 
    {
        price = 4.5;
        name = "Drumstick";

        image = new GreenfootImage("product/Drumstick.PNG");
        image.scale(image.getWidth() / 3, image.getHeight() / 3);
        setImage(image);
        
        // increase the total DrumStick stock
        stock++;
    }

    /**
     * Returns the total number of DrumStick items in the store.
     *
     * @return total DrumStick stock
     */
    public int getStock()
    {
        return stock;
    }

    /**
     * Sets the total number of DrumStick items in the store.
     *
     * @param newStockCount the new total DrumStick stock
     */
    public void setStock(int newStockCount)
    {
        stock = newStockCount;
    }
}

