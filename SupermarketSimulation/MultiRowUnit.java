import greenfoot.*;
import java.util.ArrayList;

/**
 * Base class for any display unit that arranges products in several rows,
 * such as fridges or shelves with multiple levels.
 * 
 * A MultiRowUnit:
 * - Uses a single background image for the unit (for example, a fridge).
 * - Lays out items in a grid with a fixed number of rows and columns.
 * - Positions each product using padding and gaps so items line up neatly.
 * - Applies sale discounts to items if they match the current sale product.
 * 
 * Subclasses must decide which product to place on each row by
 * implementing the stockItemsByRow method.
 * 
 * @author Owen Kung
 * @version Nov 2025
 */
public abstract class MultiRowUnit extends DisplayUnit {
    protected GreenfootImage image;

    // Number of items per row
    protected int COLS = 6;

    // Number of rows in this unit.
    protected int ROWS = 4;

    //Horizontal padding from the left edge of the unit image.
    protected int LEFT_PAD = 10;

    // Vertical padding from the top edge of the unit image. 
    protected int TOP_PAD  = 12;

    //Horizontal distance between items in the same row.
    protected int COL_GAP  = 10;

    //Vertical distance between rows of items.
    protected int ROW_GAP  = 25;

    /**
     * Creates a new MultiRowUnit with a default fridge image and
     * prepares an empty stockedItems list.
     * The fridge image is scaled down before being used.
     */
    public MultiRowUnit() {
        stockedItems = new ArrayList<>();
        image = new GreenfootImage("furniture/fridge.png");
        image.scale(image.getWidth() / 5, image.getHeight() / 5);
        setImage(image);
        stocked = false;
    }

    /**
     * Stocks this unit with items arranged in several rows and columns.
     * 
     * Behaviour:
     * - Does nothing if the world is not ready or if the unit is already stocked.
     * - Clears old items from the unit.
     * - Calculates the top-left corner of the unit based on its image.
     * - For each row and column, asks the subclass which item to place
     *   on that row by calling stockItemsByRow.
     * - If the item matches the current sale product and is not yet discounted,
     *   applies a sale discount.
     * - Sets the item's DisplayUnit, adds it to the world, and records it
     *   in the stockedItems list.
     */
    protected void stock() {
        if (getWorld() == null) return;
        if (stocked) return;

        // Remove any items from a previous stocking
        clear();

        // Find the top-left corner of this unit's image
        int w = getImage().getWidth();
        int h = getImage().getHeight();
        int topLeftX = getX() - w / 2;
        int topLeftY = getY() - h / 2;

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                Product item = stockItemsByRow(r);

                if (item != null) {
                    // Compute the item position inside the unit
                    int itemX = topLeftX + LEFT_PAD + c * COL_GAP + item.getImage().getWidth() / 2;
                    int itemY = topLeftY + TOP_PAD  + r * ROW_GAP + item.getImage().getHeight() / 2;

                    item.setDisplayUnit(this);
                    getWorld().addObject(item, itemX, itemY);
                    stockedItems.add(item);
                }
            }
        }

        stocked = true;
        System.out.println("[MultiRowUnit] " + getClass().getSimpleName()
                           + " stocked " + stockedItems.size()
                           + " items, parent store: "
                           + (parentStore == null ? "<null>" : parentStore.name));
    }

    /**
     * Returns the product that should be placed on a given row.
     * This lets different rows hold different product types.
     * 
     * For example, a subclass might put drinks on row 0 and snacks on row 1.
     * 
     * @param rowNum the row index starting from zero at the top
     * @return a new Product instance for that row,
     *         or null if nothing should be placed on that row
     */
    abstract Product stockItemsByRow(int rowNum);
}