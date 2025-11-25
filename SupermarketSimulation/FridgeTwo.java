import greenfoot.*;
import java.util.ArrayList;

/**
 * A refrigerated display unit used to show water and Coke.
 *
 * FridgeTwo is a MultiRowUnit with four rows of beverages.
 * Rows 0 and 1 hold Water, and rows 2 and 3 hold Coke.
 *
 * @author  Owen Kung
 * @version Nov 6, 2025
 */
public class FridgeTwo extends MultiRowUnit 
{
    /**
     * Creates a new FridgeTwo with a fridge image and layout
     * values for rows and columns.
     *
     * The fridge uses:
     *  - COLS = 6  (items per row)
     *  - ROWS = 4  (rows of items)
     * plus padding and gap values to position the drinks on the shelves.
     */
    public FridgeTwo() 
    {
        stockedItems = new ArrayList<>();

        image = new GreenfootImage("furniture/fridge.png");
        image.scale(image.getWidth() / 5, image.getHeight() / 5); // make it smaller
        setImage(image);
        
        COLS = 6;       // how many per row
        ROWS = 4;       // how many rows
        LEFT_PAD = 10;  // distance from fridge's left edge
        TOP_PAD  = 12;  // distance from fridge's top
        COL_GAP  = 10;  // horizontal gap
        ROW_GAP  = 25;  // vertical gap
        stocked = false;
    }

    /**
     * Chooses which product type to place on a given row.
     *
     * Row mapping:
     *  - row 0: Water
     *  - row 1: Water
     *  - row 2: Coke
     *  - row 3: Coke
     *
     * @param rowNum the row number (0 at the top)
     * @return a new Product for that row, or null if the row is not used
     */
    protected Product stockItemsByRow(int rowNum)
    {
        if (rowNum == 0)
        {
            return new Water();
        }
        else if (rowNum == 1)
        {
            return new Water();
        }
        else if (rowNum == 2)
        {
            return new Coke();
        }
        else if (rowNum == 3)
        {
            return new Coke();
        }
     
        return null;
    }
}

