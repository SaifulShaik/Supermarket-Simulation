import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;

/**
 * A wooden bin used to display lettuce in the store.
 *
 * LettuceBin is a type of Bin that is set up to store Lettuce products.
 * It configures the layout (rows, columns, layers) and uses a wooden
 * bin image for display in the world.
 *
 * @author  Owen Kung
 * @version Nov 6 2025
 */
public class LettuceBin extends Bin
{
    private GreenfootImage image;

    /**
     * Creates a new LettuceBin with a wooden bin image and layout
     * values for rows, columns, and layers of lettuce.
     *
     * The bin uses:
     *  - COLS  = 5  (items per row)
     *  - ROWS  = 4  (rows of items)
     *  - LAYERS = 5 (stack height)
     * plus padding and gap values to position lettuce inside the bin.
     */
    public LettuceBin()
    {
        COLS = 5;        // how many per row
        ROWS = 4;        // how many rows
        LAYERS = 5;      // how many layers (higher = taller pile)
        LEFT_PAD = 10;   // distance from bin's left edge
        TOP_PAD  = 30;   // distance from bin's top
        COL_GAP  = 8;    // horizontal gap
        ROW_GAP  = 8;    // vertical gap
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
     * @return a new Lettuce object to place into the bin
     */
    protected Product itemToFill()
    {
        return new Lettuce();
    }

    /**
     * Retrieves one lettuce item from the bin, if available.
     *
     * @return a Product that is a Lettuce, or null if the bin is empty
     */
    public Product retrieve()
    {
        return retrieve(Lettuce.class);
    } 
}

