import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;

/**
 * Write a description of class Bin here.
 * 
 * @author Owen Kung
 * @version Nov 6 2025
 */
public class RawBeefHangers extends MultiRowUnit
{
    private GreenfootImage image;

    
    public RawBeefHangers() 
    {
            
        COLS = 4;        // how many per row
        ROWS = 1;        // how many rows
        LEFT_PAD = - 5;    // distance from fridge’s left edge
        TOP_PAD  = 10;   // distance from fridge’s top
        COL_GAP  = 30;   // horizontal gap
        ROW_GAP  = 35;   // vertical gap
        stocked=false;
        
        stockedItems= new ArrayList<>();
        image = new GreenfootImage("furniture/hangers.jpeg");
        image.scale(image.getWidth()/6, image.getHeight()/6);//make it smaller
        setImage(image);
        stocked=false;
    }
    public Product retrieve()
    {
        return retrieve(RawBeef.class);
    }  
    protected Product stockItemsByRow(int rowNum)
    {
            return new RawBeef();
    }

}
