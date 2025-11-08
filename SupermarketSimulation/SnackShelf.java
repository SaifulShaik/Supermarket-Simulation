import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;

/**
 * Dispaly unit that display snacks
 * 
 * @author Owen Kung
 * @version Nov 6, 2025
 */
public class SnackShelf extends MultiRowUnit
{
    public SnackShelf() 
    {   stockedItems = new ArrayList<>();
        image = new GreenfootImage("furniture/shelf.png");
        image.scale(image.getWidth()/2, image.getHeight()/2);//make it smaller
        setImage(image);
        
        COLS = 6;        // how many per row
        ROWS = 3;        // how many rows
        LEFT_PAD = 6;    // distance from fridge’s left edge
        TOP_PAD  = 12;   // distance from fridge’s top
        COL_GAP  = 18;   // horizontal gap
        ROW_GAP  = 25;   // vertical gap
        stocked=false;

    }
    /**
     * Act - do whatever the Shelf wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        stock();
    }
    public Product retrieve()
    {
        return retrieve(SnackShelf.class);
    }
    protected Product stockItemsByRow(int rowNum)
    {
            if(rowNum==0)
            {
                return new Doritos();
            }
            else if(rowNum==1)
            {
                return new Lays();
            }
            else if(rowNum==2)
            {
                return new Ruffles();
            }
     
            return null;
    }
}
