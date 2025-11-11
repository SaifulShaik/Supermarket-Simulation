import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;

/**
 * Display Warmer object in the store.  
 * It stock up the display warmer and show what's taken away.
 * 
 * @author Owen Kung 
 * @version Nov 6, 2025
 */
public class RawBeefHangers extends MultiRowUnit
{
    private GreenfootImage image;
    
    
    public RawBeefHangers() 
    {
        stockedItems = new ArrayList<>();
        //image = new GreenfootImage("furniture/displayWarmer.png");
        image = new GreenfootImage("furniture/hangers.jpeg");
        image.scale(image.getWidth()/5, image.getHeight()/5);//make it smaller
        setImage(image);
        stocked=false;
        
        COLS = 4;        // how many per row
        ROWS = 1;        // how many rows
        LEFT_PAD = -5;   // distance from fridge’s left edge
        TOP_PAD  = 25;   // distance from fridge’s top
        COL_GAP  = 35;   // horizontal gap
        ROW_GAP  = 30;   // vertical gap
    }
    protected Product stockItemsByRow(int rowNum)
    {
            if(rowNum==0)
            {
                return new RawBeef();
            }
            return null;
    }
}
