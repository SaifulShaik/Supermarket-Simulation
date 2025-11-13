import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;

/**
 * Dispaly unit that display snacks
 * 
 * @author Owen Kung
 * @version Nov 6, 2025
 */
public class CupNoodleShelf extends MultiRowUnit
{
    public CupNoodleShelf() 
    {   stockedItems = new ArrayList<>();
        image = new GreenfootImage("furniture/shelf.png");
        image.scale(image.getWidth()/2, image.getHeight()/2);//make it smaller
        setImage(image);
        
        COLS = 5;        // how many per row
        ROWS = 3;        // how many rows
        LEFT_PAD = 10;    // distance from fridge’s left edge
        TOP_PAD  = 12;   // distance from fridge’s top
        COL_GAP  = 20;   // horizontal gap
        ROW_GAP  = 25;   // vertical gap
        stocked=false;

    }
    protected Product stockItemsByRow(int rowNum)
    {
            if(rowNum==0)
            {
                return new Nissin();
            }
            else if(rowNum==1)
            {
                 return new XingRamen();
            }
            else if(rowNum==2)
            {
                return new JinRamen();
            }
     
            return null;
    }
}
