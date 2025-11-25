import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;

/**
 * A hanging display used to show raw beef cuts.
 *
 * RawBeefHangers is a MultiRowUnit with one row of RawBeef products.
 * It uses a hanger image and places several pieces of raw beef
 * along the hanging rail.
 *
 * @author  Owen Kung
 * @version Nov 2025
 */
public class RawBeefHangers extends MultiRowUnit
{
    private GreenfootImage image;

    /**
     * Creates a new RawBeefHangers unit with a hanger image and
     * layout values for columns and padding.
     *
     * The display uses:
     *  - COLS = 4  (items per row)
     *  - ROWS = 1  (one row of items)
     * plus padding and gap values to position the meat along the hangers.
     */
    public RawBeefHangers() 
    {
        COLS = 4;        // how many per row
        ROWS = 1;        // how many rows
        LEFT_PAD = -5;   // distance from hanger's left edge
        TOP_PAD  = 10;   // distance from hanger's top
        COL_GAP  = 30;   // horizontal gap
        ROW_GAP  = 35;   // vertical gap
        stocked = false;
        
        stockedItems = new ArrayList<>();

        image = new GreenfootImage("furniture/hangers.jpeg");
        image.scale(image.getWidth() / 6, image.getHeight() / 6); // make it smaller
        setImage(image);
        stocked = false;
    }

    /**
     * Retrieves one RawBeef item from the hangers, if available.
     *
     * @return a Product that is a RawBeef, or null if the display is empty
     */
    public Product retrieve()
    {
        return retrieve(RawBeef.class);
    }

    /**
     * Returns the type of Product this display should be filled with.
     * Since there is only one row, all positions use RawBeef.
     *
     * @param rowNum the row number (ignored here, always RawBeef)
     * @return a new RawBeef object to place on the hangers
     */
    protected Product stockItemsByRow(int rowNum)
    {
        return new RawBeef();
    }
}

