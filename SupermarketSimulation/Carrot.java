import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A carrot product that can be bought in the store.
 *
 * Carrot is a Product with a fixed name and price. It uses a single
 * carrot sprite and keeps track of the total number of Carrot items
 * created in the store using a static stock counter.
 *
 * Each time a Carrot object is created, the stock count increases.
 * This is useful for tracking inventory across all bins or shelves.
 *
 * @author Owen Kung
 * @version Nov 2025
 */
public class Carrot extends Product
{
    // Tracks the total number of Carrot items in the entire store
    private static int stock = 0;

    private GreenfootImage image;

    /**
     * Creates a new Carrot product with name, price, and image set.
     *
     * The carrot:
     *  - has name "Carrot"
     *  - has price $1.50
     *  - uses product/carrot.PNG as its image
     * Each time a Carrot is constructed, the static stock counter increases.
     */
    public Carrot()
    {
        price = 1.5;
        name = "Carrot";

        image = new GreenfootImage("product/carrot.PNG");
        image.scale(image.getWidth() / 2, image.getHeight() / 2);
        setImage(image);

        // Increase total Carrot stock
        stock++;
    }

    /**
     * Returns the total number of Carrot items in the store.
     */
    public int getStock()
    {
        return stock;
    }

    /**
     * Sets the total number of Carrot items in the store.
     *
     * @param newStockCount the new total Carrot stock
     */
    public void setStock(int newStockCount)
    {
        stock = newStockCount;
    }
}

