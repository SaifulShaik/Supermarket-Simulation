import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A candy product that can be bought in the store.
 *
 * Candy is a Product with a fixed name and price, but a random
 * visual sprite. Each Candy object chooses one of several candy
 * images when it is created. This class also keeps track of the
 * total number of Candy items in the store using a static counter.
 *
 * @author  Owen Kung 
 * @version Nov 6 2025
 */
public class Candy extends Product
{
    private GreenfootImage image;

    // Keeps track of total number of Candy objects in the store
    private static int stock = 0;
     
    /**
     * Creates a new Candy object with a random look.
     *
     * The candy:
     *  - has name "Candy"
     *  - has price $1.50
     *  - randomly selects one of five candy images
     *  - scales the image to half size
     * Each time a Candy is created, the static stock counter increases.
     */
    public Candy()
    {
        name = "Candy";
        
        // randomly assign different look to candy
        int candyChoice = Greenfoot.getRandomNumber(5);
        if (candyChoice == 0)
        {
            image = new GreenfootImage("product/candy/Candy 1.PNG");
        }
        else if (candyChoice == 1)
        {
            image = new GreenfootImage("product/candy/Candy 2.PNG");
        }
        else if (candyChoice == 2)
        {
            image = new GreenfootImage("product/candy/Candy 3.PNG");
        }
        else if (candyChoice == 3)
        {
            image = new GreenfootImage("product/candy/Candy 4.PNG");
        }
        else if (candyChoice == 4)
        {
            image = new GreenfootImage("product/candy/Candy 5.PNG");
        }

        image.scale(image.getWidth() / 2, image.getHeight() / 2);
        setImage(image);
    
        // increase the total Candy stock count
        stock++;

        // set price
        price = 1.5;
    }

    /**
     * Returns the total number of Candy items in the store.
     * This overrides the Product version to use the static counter.
     *
     * @return total Candy stock
     */
    public int getStock()
    {
        return stock;
    }

    /**
     * Returns the total number of Candy items in the store.
     * Same as getStock(), but static so it can be called without an object.
     *
     * @return total Candy stock
     */
    public static int getCurrentStock()
    {
        return stock;
    }

    /**
     * Sets the total number of Candy items in the store.
     *
     * @param newStockCount the new total Candy stock
     */
    public void setStock(int newStockCount)
    {
        stock = newStockCount;
    }
}

