import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;

/**
 * Display Warmer object in the store.  
 * It stock up the display warmer and show what's taken away.
 * 
 * @author Owen Kung 
 * @version Nov 6, 2025
 */
public class RawBeefHangers extends DisplayUnit
{
    private GreenfootImage image;
    
    private static final int COLS = 4;        // how many per row
    private static final int ROWS = 1;        // how many rows
    private static final int LEFT_PAD = 20;   // distance from fridge’s left edge
    private static final int TOP_PAD  = 45;   // distance from fridge’s top
    private static final int COL_GAP  = 5;      // horizontal gap
    private static final int ROW_GAP  = 5;      // vertical gap
    private boolean stocked=false;  // Instance variable, not static
    
    public RawBeefHangers() 
    {
        stockedItems= new ArrayList<>();
        image = new GreenfootImage("furniture/steakWarmer.png");
        image.scale(image.getWidth()/4, image.getHeight()/4);//make it smaller
        setImage(image);
        stocked=false;
    }
    /**
     * Act - do whatever the DisplayWarmer wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
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

        // Top-left corner of the fridge image
        int warmerW = getImage().getWidth();
        int warmerH = getImage().getHeight();
        int topLeftX = getX() - warmerW/2;
        int topLeftY = getY() - warmerH/2;

        // Place coke, sprite, water
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                RawBeef item=new RawBeef();
                //x and y co-ordinates
                int itemX = topLeftX + LEFT_PAD + c * COL_GAP + item.getImage().getWidth()/2;
                int itemY = topLeftY + TOP_PAD  + r * ROW_GAP + item.getImage().getHeight()/2;

                getWorld().addObject(item, itemX, itemY);
                stockedItems.add(item);
            }
        }
        stocked=true;
        
    }
}
