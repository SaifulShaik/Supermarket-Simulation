import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;

/**
 * Base class for any bin-style display unit that stacks loose items in a pile.
 * 
 * A Bin:
 * - Uses a bin image as the container background.
 * - Arranges items in several layers, rows, and columns.
 * - Adds small random offsets so the pile looks more natural.
 * - Applies a discount to items if they match the current sale product.
 * 
 * Subclasses must implement itemToFill to decide which product
 * type is placed into the bin.
 * 
 * Adjustable layout fields:
 * COLS, ROWS, LAYERS, LEFT_PAD, TOP_PAD, COL_GAP, ROW_GAP.
 * 
 * @author Owen Kung (Edited by Saiful Shaik)
 * @version Nov 6 2025
 */
public abstract class Bin extends DisplayUnit
{
    // Background image for this bin.
    private GreenfootImage image;
    
    // Number of items per row.
    protected int COLS = 10;
    
    // Number of rows per layer. 
    protected int ROWS = 3;
    
    //Number of stacked layers (higher value gives a taller pile).
    protected int LAYERS = 5;
    
    //Horizontal padding from the left edge of the bin image.
    protected int LEFT_PAD = 10;
    
    //Vertical padding from the top of the bin image.
    protected int TOP_PAD  = 30;
    
    //Horizontal distance between items in the same row.
    protected int COL_GAP  = 5;
    
    //Vertical distance between stacked layers.
    protected int ROW_GAP  = 5;
    
    /**
     * Creates a new Bin display unit.
     * 
     * The constructor:
     * - Prepares an empty stockedItems list.
     * - Loads and scales the wooden bin image.
     * - Marks the bin as not yet stocked.
     */
    public Bin() 
    {
        stockedItems = new ArrayList<>();
        image = new GreenfootImage("furniture/bin/woodenbin.png");
        image.scale(image.getWidth() / 8, image.getHeight() / 8);
        setImage(image);
        stocked = false;
    }

    /**
     * Returns the product that should be placed into this bin.
     * 
     * Subclasses use this method to define which product type the bin holds.
     * For example, an AppleBin can return a new Apple object here.
     * 
     * @return a new Product instance to be stacked in the bin,
     *         or null if no product should be created
     */
    protected abstract Product itemToFill();

    /**
     * Stocks this bin with a stacked pile of products.
     * 
     * Behaviour:
     * - Does nothing if the world is not available or if the bin is already stocked.
     * - Clears old items from previous stocking.
     * - Calculates the visible top surface of the bin from its image.
     * - Stacks items in several layers, rows, and columns.
     * - Adds small random offsets in x and y so the pile does not look too perfect.
     * - Applies a sale discount if the product type matches the current sale product.
     * - Links each item back to this display unit and adds it to the world and to
     *   the stockedItems list.
     */
    protected void stock() {
        if (getWorld() == null) return;
        if (stocked) return;    
        
        // Remove old items first
        clear();
    
        // Bin geometry
        int binW = getImage().getWidth();
        int binH = getImage().getHeight();
    
        // Compute the top surface line of the bin (screen Y increases downward)
        int surfaceY = getY() - binH / 2 + TOP_PAD;
    
        // Center the columns over the bin
        int leftX = getX() - ((COLS - 1) * COL_GAP) / 2;
    
        // Build the pile: top layer first so the last-added items end up on the bottom.
        // When a product is retrieved, it will be taken from the visible top.
        for (int layer = LAYERS - 1; layer >= 0; layer--) {
            for (int c = COLS - 1; c >= 0; c--) {
                int randomX = Greenfoot.getRandomNumber(3) - 1; // -1..+1
                int randomY = Greenfoot.getRandomNumber(3) - 1; // -1..+1
    
                int x = leftX + c * COL_GAP + randomX;
                int y = surfaceY - layer * ROW_GAP + randomY;
    
                Product item = itemToFill();

                item.setRotation(Greenfoot.getRandomNumber(90) - 40);
                item.setDisplayUnit(this);
                stockedItems.add(item);
                getWorld().addObject(item, x, y);
            }
        }
        
        stocked = true;
    }  
}