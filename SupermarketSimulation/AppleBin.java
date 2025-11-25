import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;

/**
 * A wooden bin that holds apples in the produce section.
 *
 * AppleBin is a type of Bin that is preconfigured to store Apple
 * objects. It sets up the layout (rows, columns, layers) and the
 * wooden bin image used in the world.
 *
 * @author  Owen Kung
 * @version Nov 6 2025
 */
public class AppleBin extends Bin
{
    private GreenfootImage image;

    /**
     * Creates a new AppleBin with a wooden bin image and layout
     * values for rows, columns, and layers of apples.
     *
     * The bin uses:
     *  - COLS  = 5  (items per row)
     *  - ROWS  = 3  (rows of items)
     *  - LAYERS = 5 (stack height)
     * and padding values to position apples inside the bin image.
     */
    public AppleBin() 
    {
        COLS = 5;         // how many per row
        ROWS = 3;         // how many rows
        LAYERS = 5;       // how many layers (higher = taller pile)
        LEFT_PAD = 10;    // distance from bin's left edge
        TOP_PAD  = 30;    // distance from bin's top
        COL_GAP  = 10;    // horizontal gap
        ROW_GAP  = 10;    // vertical gap
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
     * @return a new Apple object to place into the bin
     */
    protected Product itemToFill()
    {
        return new Apple();
    }

    /**
     * Retrieves one apple from the bin, if available.
     *
     * This calls the Bin retrieve method using Apple.class so that
     * only Apple items are removed from this bin.
     *
     * @return a Product that is an Apple, or null if the bin is empty
     */
    public Product retrieve()
    {
        // This used to be Lettuce.class, which did not match AppleBin
        return retrieve(Apple.class);
    }
}

