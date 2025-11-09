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
<<<<<<< HEAD
    
    private static final int COLS = 3;        // how many per row
    private static final int ROWS = 2;        // how many rows
    private static final int LEFT_PAD = 20;   // distance from fridge’s left edge
    private static final int TOP_PAD  = 65;   // distance from fridge’s top
    private static final int COL_GAP  = 5;      // horizontal gap
    private static final int ROW_GAP  = 5;      // vertical gap
    private boolean stocked=false;  // Instance variable, not static
    
    public SteakWarmer() 
    {
=======

    
    public SteakWarmer() 
    {
            
        COLS = 3;        // how many per row
        ROWS = 2;        // how many rows
        LEFT_PAD = 20;    // distance from fridge’s left edge
        TOP_PAD  = 65;   // distance from fridge’s top
        COL_GAP  = 20;   // horizontal gap
        ROW_GAP  = 30;   // vertical gap
        stocked=false;
        
>>>>>>> Owen-K
        stockedItems= new ArrayList<>();
        image = new GreenfootImage("furniture/displayWarmer.png");
        image.scale(image.getWidth()/4, image.getHeight()/4);//make it smaller
        setImage(image);
        stocked=false;
    }
    public Product retrieve()
    {
<<<<<<< HEAD
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
                Steak item=new Steak();
                //x and y co-ordinates
                int itemX = topLeftX + LEFT_PAD + c * COL_GAP + item.getImage().getWidth()/2;
                int itemY = topLeftY + TOP_PAD  + r * ROW_GAP + item.getImage().getHeight()/2;

                getWorld().addObject(item, itemX, itemY);
                stockedItems.add(item);
=======
        return retrieve(Steak.class);
    }  
    protected Product stockItemsByRow(int rowNum)
    {
            if(rowNum==0)
            {
                return new Steak();
>>>>>>> Owen-K
            }
            else if(rowNum==1)
            {
                return new DrumStick();
            }

     
            return null;
    }

}
