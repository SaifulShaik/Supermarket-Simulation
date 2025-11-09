import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;

/**
 * Bin with Apples
 * Has it's own stocking mechnism
 * 
 * @author Owen Kung
 * @version Nov 2025
 */
public class AppleBin extends DisplayUnit
{
    private GreenfootImage image;
    
    //Display parameters
    private static final int COLS = 10;         // how many per row
    private static final int ROWS = 3;          // how many rows
    private static final int LAYERS = 5;        // how many layers (higher = taller pile)
    private static final int LEFT_PAD = 10;     // distance from fridge’s left edge
    private static final int TOP_PAD  = 30;     // distance from fridge’s top
    private static final int COL_GAP  = 5;      // horizontal gap
    private static final int ROW_GAP  = 5;      // vertical gap
    private boolean stocked=false;  // Instance variable, not static
    
    public AppleBin() 
    {
        stockedItems= new ArrayList<>();
        image = new GreenfootImage("furniture/bin/woodenbin.png");
        image.scale(image.getWidth()/8, image.getHeight()/8);//make it smaller
        setImage(image);
        stocked=false;
    }
    public void act()
    {
        // Only stock if stocking is enabled (not in editor mode)
        if (isStockingEnabled()) {
            stock();
        }
    }
    protected void stock() {
        if (getWorld()== null) return;
        if(stocked) return;
        
        //clear old itemes first
        clear();
    
        // Bin geometry
        int binW = getImage().getWidth();
        int binH = getImage().getHeight();
    
        // Compute the top surface line of the bin (screen Y increases downward)
        int surfaceY = getY() - binH / 2 + TOP_PAD;
    
        // Center the columns over the bin
        int leftX = getX() - ((COLS - 1) * COL_GAP) / 2;
    
        // Build the pile: bottom layer first, last-added ends up on top
        for (int layer = 0; layer < LAYERS; layer++) {
            for (int c = 0; c < COLS; c++) {
                int randomX = Greenfoot.getRandomNumber(3) - 1; // -1..+1
                int randomY = Greenfoot.getRandomNumber(3) - 1; // -1..+1
    
                int x = leftX + c * COL_GAP + randomX;
                int y = surfaceY - layer * ROW_GAP + randomY;
    
                Apple head = new Apple();
                stockedItems.add(head);//add to stocked list
                getWorld().addObject(head, x, y);
    
                // Optional: tiny random rotation for variety
                // head.setRotation(Greenfoot.getRandomNumber(21) - 10); // -10..+10°
            }
        }
        
        stocked=true;
    }

}
