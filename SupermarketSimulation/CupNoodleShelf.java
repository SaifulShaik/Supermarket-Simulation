import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;

/**
 * Display unit that shows different brands of cup noodles.
 *
 * CupNoodleShelf is a MultiRowUnit set up with three rows of
 * instant noodles. Each row holds a different product:
 * row 0 = Nissin, row 1 = XingRamen, row 2 = JinRamen.
 *
 * @author  Owen Kung
 * @version Nov 6, 2025
 */
public class CupNoodleShelf extends MultiRowUnit
{
    /**
     * Creates a new CupNoodleShelf with a shelf image and layout
     * values for rows and columns.
     *
     * The shelf uses:
     *  - COLS = 5  (items per row)
     *  - ROWS = 3  (rows of items)
     * plus padding and gap values to position the noodles on the shelf.
     */
    public CupNoodleShelf() 
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
     * Chooses which product type to place on a given row.
     *
     * Row mapping:
     *  - row 0: Nissin
     *  - row 1: XingRamen
     *  - row 2: JinRamen
     *
     * @param rowNum the row number (0 at the top)
     * @return a new Product for that row, or null if the row is not used
     */
    protected Product stockItemsByRow(int rowNum)
    {
        if (rowNum == 0)
        {
            return new Nissin();
        }
        else if (rowNum == 1)
        {
            return new XingRamen();
        }
        else if (rowNum == 2)
        {
            return new JinRamen();
        }
     
        return null;
    }
}

