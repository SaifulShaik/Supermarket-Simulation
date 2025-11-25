import greenfoot.*;
import java.util.ArrayList;

/**
 * A refrigerated display unit used to show cold drinks.
 *
 * Fridge is a MultiRowUnit with four rows of beverages.
 * Each row holds a different drink:
 * row 0 = Sprite, row 1 = Fanta, row 2 = Water, row 3 = Coke.
 *
 * @author  Owen Kung
 * @version Nov 6, 2025
 */
public class Fridge extends MultiRowUnit 
{
    /**
     * Creates a new Fridge with a fridge image and layout
     * values for rows and columns.
     *
     * The fridge uses:
     *  - COLS = 6  (items per row)
     *  - ROWS = 4  (rows of items)
     * plus padding and gap values to position the drinks on the shelves.
     */
    public Fridge() 
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
     *  - row 0: Sprite
     *  - row 1: Fanta
     *  - row 2: Water
     *  - row 3: Coke
     *
     * @param rowNum the row number (0 at the top)
     * @return a new Product for that row, or null if the row is not used
     */
    protected Product stockItemsByRow(int rowNum)
    {
        if (rowNum == 0)
        {
            return new Sprite();
        }
        else if (rowNum == 1)
        {
            return new Fanta();
        }
        else if (rowNum == 2)
        {
            return new Water();
        }
        else if (rowNum == 3)
        {
            return new Coke();
        }
     
        return null;
    }
}

