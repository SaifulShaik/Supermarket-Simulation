import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;

/**
 * Write a description of class Bin here.
 * 
 * @author Owen Kung
 * @version Nov 6 2025
 */
public class SteakWarmer extends MultiRowUnit
{
    private GreenfootImage image;
    
    private static final int COLS = 3;        // how many per row
    private static final int ROWS = 2;        // how many rows
    private static final int LEFT_PAD = 20;   // distance from fridge's left edge
    private static final int TOP_PAD  = 65;   // distance from fridge's top
    private static final int COL_GAP  = 5;      // horizontal gap
    private static final int ROW_GAP  = 5;      // vertical gap
    
    public SteakWarmer()
    {
        stockedItems= new ArrayList<>();
        image = new GreenfootImage("furniture/displayWarmer.png");
        image.scale(image.getWidth()/4, image.getHeight()/4);//make it smaller
        setImage(image);
        stocked=false;
    }
    
    protected void stock() {
        if (getWorld()== null) return;
        if(stocked) return;
        
        //clear old items first
        clear();

        // Top-left corner of the warmer image
        int warmerW = getImage().getWidth();
        int warmerH = getImage().getHeight();
        int topLeftX = getX() - warmerW/2;
        int topLeftY = getY() - warmerH/2;

        // Place steak items
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                Steak item = new Steak();
                //x and y co-ordinates
                int itemX = topLeftX + LEFT_PAD + c * COL_GAP + item.getImage().getWidth()/2;
                int itemY = topLeftY + TOP_PAD  + r * ROW_GAP + item.getImage().getHeight()/2;

                getWorld().addObject(item, itemX, itemY);
                stockedItems.add(item);
            }
        }
        
        stocked = true;
    }
    
    @Override
    protected Product stockItemsByRow(int rowNum) {
        // All rows stock Steak items
        return new Steak();
    }
}
