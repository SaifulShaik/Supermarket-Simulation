import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;

/**
 * Display unit that shows different snack chips.
 *
 * SnackShelf is a MultiRowUnit with three rows of chips.
 * Each row holds a different product:
 * row 0 = Doritos, row 1 = Lays, row 2 = Ruffles.
 *
 * @author  Owen Kung
 * @version Nov 6, 2025
 */
public class SnackShelf extends MultiRowUnit
{
    /**
     * Creates a new SnackShelf with a shelf image and layout
     * values for rows and columns.
     *
     * The shelf uses:
     *  - COLS = 5  (items per row)
     *  - ROWS = 3  (rows of items)
     * plus padding and gap values to position the snacks on the shelf.
     */
    public SnackShelf() 
    {
        stockedItems = new ArrayList<>();

        image = new GreenfootImage("furniture/shelf.png");
        image.scale(image.getWidth() / 2, image.getHeight() / 2); // make it smaller
        setImage(image);
        
        COLS = 5;       // how many per row
        ROWS = 3;       // how many rows
        LEFT_PAD = 10;  // distance from shelf's left edge
        TOP_PAD  = 12;  // distance from shelf's top
        COL_GAP  = 20;  // horizontal gap
        ROW_GAP  = 25;  // vertical gap
        stocked = false;
    }

    /**
     * Retrieves one snack item from the shelf, if available.
     *
     * This uses Product.class so any chip on this shelf
     * (Doritos, Lays, or Ruffles) can be returned.
     *
     * @return a Product from this shelf, or null if it is empty
     */
    public Product retrieve()
    {
        // SnackShelf.class was incorrect here (it is not a Product type)
        return retrieve(Product.class);
    }

    /**
     * Chooses which product type to place on a given row.
     *
     * Row mapping:
     *  - row 0: Doritos
     *  - row 1: Lays
     *  - row 2: Ruffles
     *
     * @param rowNum the row number (0 at the top)
     * @return a new Product for that row, or null if the row is not used
     */
    protected Product stockItemsByRow(int rowNum)
    {
        if (rowNum == 0)
        {
            return new Doritos();
        }
        else if (rowNum == 1)
        {
            return new Lays();
        }
        else if (rowNum == 2)
        {
            return new Ruffles();
        }
     
        return null;
    }
}

