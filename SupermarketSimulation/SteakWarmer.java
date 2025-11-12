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

    
    public SteakWarmer() 
    {
            
        COLS = 3;        // how many per row
        ROWS = 2;        // how many rows
        LEFT_PAD = 20;    // distance from fridge’s left edge
        TOP_PAD  = 65;   // distance from fridge’s top
        COL_GAP  = 20;   // horizontal gap
        ROW_GAP  = 30;   // vertical gap
        stocked=false;
        
        stockedItems= new ArrayList<>();
        image = new GreenfootImage("furniture/displayWarmer.png");
        image.scale(image.getWidth()/4, image.getHeight()/4);//make it smaller
        setImage(image);
        stocked=false;
    }
    public Product retrieve()
    {
        return retrieve(Steak.class);
    }  
    protected Product stockItemsByRow(int rowNum)
    {
            if(rowNum==0)
            {
                return new Steak();
            }
            else if(rowNum==1)
            {
                return new DrumStick();
            }

     
            return null;
    }

}
