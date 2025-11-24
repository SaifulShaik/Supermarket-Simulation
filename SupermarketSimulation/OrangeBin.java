import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;

/**
 * A wooden bin used to display oranges in the store.
 *
 * OrangeBin is a type of Bin that is set up to store Orange products.
 * It configures the layout (rows, columns, layers) and uses a wooden
 * bin image for display in the world.
 *
 * @author  Owen Kung
 * @version Nov 6 2025
 */
public class OrangeBin extends Bin
{
    private GreenfootImage image;

    /**
     * Creates a new OrangeBin with a wooden bin image and layout
     * values for rows, columns, and layers of oranges.
     *
     * The bin uses:
     *  - COLS  = 10 (items per row)
     *  - ROWS  = 3  (rows of items)
     *  - LAYERS = 5 (stack height)
     * plus padding and gap values to position oranges inside the bin.
     */
    public OrangeBin() 
    {
        COLS = 10;      // how many per row
        ROWS = 3;       // how many rows
        LAYERS = 5;     // how many layers (higher = taller pile)
        LEFT_PAD = 10;  // distance from bin's left edge
        TOP_PAD  = 30;  // distance from bin's top
        COL_GAP  = 5;   // horizontal gap
        ROW_GAP  = 5;   // vertical gap
        stocked = false;

        stockedItems = new ArrayList<>();

        image = new GreenfootImage("furniture/bin/woodenbin.png");
        image.scale(image.getWidth() / 8, image.getHeight() / 8); // make it smaller
        setImage(image);
        stocked = false;
    }

    /**
     * Returns the type of Product this bin should be filled with.
     *
     * @return a new Orange object to place into the bin
     */
    protected Product itemToFill()
    {
        return new Orange();
    }

    /**
     * Retrieves one orange item from the bin, if available.
     *
     * @return a Product that is an Orange, or null if the bin is empty
     */
    public Product retrieve()
    {
        return retrieve(Orange.class);
    }  
}

