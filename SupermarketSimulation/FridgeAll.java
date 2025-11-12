import greenfoot.*;
import java.util.ArrayList;

public class FridgeAll extends MultiRowUnit 
{

    public FridgeAll() 
    {
        stockedItems = new ArrayList<>();
        image = new GreenfootImage("furniture/fridge.png");
        image.scale(image.getWidth()/5, image.getHeight()/5);//make it smaller
        setImage(image);
        
        COLS = 6;        // how many per row
        ROWS = 4;        // how many rows
        LEFT_PAD = 10;   // distance from fridge’s left edge
        TOP_PAD  = 12;   // distance from fridge’s top
        COL_GAP  = 10;   // horizontal gap
        ROW_GAP  = 25;   // vertical gap
        stocked=false;

    }
    protected Product stockItemsByRow(int rowNum)
    {
            if(rowNum==0)
            {
                return new Sprite();
            }
            else if(rowNum==1)
            {
                return new Fanta();
            }
            else if(rowNum==2)
            {
                return new Water();
            }
            else if(rowNum==3)
            {
                return new Coke();
            }
     
            return null;
    }
}


