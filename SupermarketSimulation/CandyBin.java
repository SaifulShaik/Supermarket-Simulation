import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;

/**
 * A small bin used to display candy in the store.
 *
 * CandyBin is a type of Bin that is set up to store Candy products.
 * It configures the layout (rows, columns, layers) and uses a blue
 * bin image for display in the world.
 *
 * @author  Owen Kung
 * @version Nov 6 2025
 */
public class CandyBin extends Bin
{
    private GreenfootImage image;

    /**
     * Creates a new CandyBin with a blue bin image and layout
     * values for rows, columns, and layers of candy.
     *
     * The bin uses:
     *  - COLS  = 5  (items per row)
     *  - ROWS  = 3  (rows of items)
     *  - LAYERS = 5 (stack height)
     * plus padding and gap values to position candy inside the bin.
     */
    public CandyBin() 
    {
        COLS = 5;        // how many per row
        ROWS = 3;        // how many rows
        LAYERS = 5;      // how many layers (higher = taller pile)
        LEFT_PAD = 10;   // distance from bin's left edge
        TOP_PAD  = 30;   // distance from bin's top
        COL_GAP  = 10;   // horizontal gap
        ROW_GAP  = 10;   // vertical gap
        stocked = false;

        stockedItems = new ArrayList<>();

        image = new GreenfootImage("furniture/bin/blueBin.png");
        image.scale(image.getWidth() / 8, image.getHeight() / 8); // make it smaller
        setImage(image);
        stocked = false;
    }

    /**
     * Returns the type of Product this bin should be filled with.
     *
     * @return a new Candy object to place into the bin
     */
    protected Product itemToFill()
    {
        return new Candy();
    }

    /**
     * Retrieves one candy item from the bin, if available.
     *
     * @return a Product that is a Candy, or null if the bin is empty
     */
    public Product retrieve()
    {
        return retrieve(Candy.class);
    }  
}

