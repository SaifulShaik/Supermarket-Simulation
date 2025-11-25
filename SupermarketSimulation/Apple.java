import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A single apple product that can be bought in the store.
 *
 * Apple is a concrete Product with a fixed name and price.
 * It also tracks how many Apple objects have been created in
 * the simulation using a static stock counter.
 *
 * @author  Owen Kung
 * @version Nov 2025
 */
public class Apple extends Product
{
    // Tracks how many Apple objects exist in the store
    private static int stock = 0;

    private GreenfootImage image;

    /**
     * Creates a new Apple with a set name, price, and image.
     *
     * The apple:
     *  - has name "Apple"
     *  - has price $2.00
     *  - uses product/Apple.PNG as its sprite
     * Each time an Apple is created, the static stock counter is increased.
     */
    public Apple()
    {
        name = "Apple";
        price = 2.0;

        image = new GreenfootImage("product/Apple.PNG");
        image.scale(image.getWidth() / 2, image.getHeight() / 2);
        setImage(image);
        
        // Whenever an Apple is created (e.g., added to a shelf),
        // increase the total Apple stock in the store.
        stock++;
    }
}

