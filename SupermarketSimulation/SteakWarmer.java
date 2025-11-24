import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;

/**
 * A heated display unit used to keep cooked meat warm.
 *
 * SteakWarmer is a MultiRowUnit with two rows of hot food.
 * The top row holds Steak, and the bottom row holds DrumStick.
 *
 * @author  Owen Kung
 * @version Nov 6 2025
 */
public class SteakWarmer extends MultiRowUnit
{
    private GreenfootImage image;

    /**
     * Creates a new SteakWarmer with a warmer image and layout
     * values for rows and columns.
     *
     * The warmer uses:
     *  - COLS = 3  (items per row)
     *  - ROWS = 2  (rows of items)
     * plus padding and gap values to position the food inside
     * the warmer.
     */
    public SteakWarmer() 
    {
        COLS = 3;       // how many per row
        ROWS = 2;       // how many rows
        LEFT_PAD = 20;  // distance from warmer's left edge
        TOP_PAD  = 65;  // distance from warmer's top
        COL_GAP  = 20;  // horizontal gap
        ROW_GAP  = 30;  // vertical gap
        stocked = false;
        
        stockedItems = new ArrayList<>();

        image = new GreenfootImage("furniture/displayWarmer.png");
        image.scale(image.getWidth() / 4, image.getHeight() / 4); // make it smaller
        setImage(image);
        stocked = false;
    }

    /**
     * Retrieves one hot food item from the warmer, if available.
     *
     * This uses Product.class so any item inside this warmer
     * (Steak or DrumStick) can be returned.
     *
     * @return a Product from this warmer, or null if it is empty
     */
    public Product retrieve()
    {
        return retrieve(Product.class);
    }

    /**
     * Chooses which product type to place on a given row.
     *
     * Row mapping:
     *  - row 0: Steak
     *  - row 1: DrumStick
     *
     * @param rowNum the row number (0 at the top)
     * @return a new Product for that row, or null if the row is not used
     */
    protected Product stockItemsByRow(int rowNum)
    {
        if (rowNum == 0)
        {
            return new Steak();
        }
        else if (rowNum == 1)
        {
            return new DrumStick();
        }

        return null;
    }
}

