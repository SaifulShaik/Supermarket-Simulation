import greenfoot.*;
import java.util.ArrayList;

/**
 * A refrigerated display unit used to show packaged meat.
 *
 * MeatFridge is a MultiRowUnit with four rows of meat products.
 * Each row holds a different type of meat:
 * row 0 = Bacon, row 1 = RawSteak,
 * row 2 = RawChicken, row 3 = MeatOnTheBone.
 *
 * @author  Owen Kung
 * @version Nov 6, 2025
 */
public class MeatFridge extends MultiRowUnit 
{
    /**
     * Creates a new MeatFridge with a fridge image and layout
     * values for rows and columns.
     *
     * The fridge uses:
     *  - COLS = 3  (items per row)
     *  - ROWS = 4  (rows of items)
     * plus padding and gap values to position the meat on the shelves.
     */
    public MeatFridge() 
    {
        stockedItems = new ArrayList<>();

        image = new GreenfootImage("furniture/fridge.png");
        image.scale(image.getWidth() / 5, image.getHeight() / 5); // make it smaller
        setImage(image);
        
        COLS = 3;       // how many per row
        ROWS = 4;       // how many rows
        LEFT_PAD = 10;  // distance from fridge's left edge
        TOP_PAD  = 22;  // distance from fridge's top
        COL_GAP  = 15;  // horizontal gap
        ROW_GAP  = 25;  // vertical gap
        stocked = false;
    }

    /**
     * Chooses which product type to place on a given row.
     *
     * Row mapping:
     *  - row 0: Bacon
     *  - row 1: RawSteak
     *  - row 2: RawChicken
     *  - row 3: MeatOnTheBone
     *
     * @param rowNum the row number (0 at the top)
     * @return a new Product for that row, or null if the row is not used
     */
    protected Product stockItemsByRow(int rowNum)
    {
        if (rowNum == 0)
        {
            return new Bacon();
        }
        else if (rowNum == 1)
        {
            return new RawSteak();
        }
        else if (rowNum == 2)
        {
            return new RawChicken();
        }
        else if (rowNum == 3)
        {
            return new MeatOnTheBone();
        }
     
        return null;
    }
}

