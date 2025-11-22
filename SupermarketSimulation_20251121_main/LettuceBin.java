import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;

/**
 * Write a description of class Bin here.
 * 
 * @author Owen Kung
 * @version Nov 6 2025
 */
public class LettuceBin extends Bin
{
    private GreenfootImage image;
    
    public LettuceBin()
    {
        COLS = 5;          // how many per row
        ROWS = 4;           // how many rows
        LAYERS = 5;         // how many layers (higher = taller pile)
        LEFT_PAD = 10;      // distance from fridge’s left edge
        TOP_PAD  = 30;      // distance from fridge’s top
        COL_GAP  = 8;       // horizontal gap
        ROW_GAP  = 8;       // vertical gap
        stocked=false;
    
        stockedItems= new ArrayList<>();
        image = new GreenfootImage("furniture/bin/woodenbin.png");
        image.scale(image.getWidth()/8, image.getHeight()/8);//make it smaller
        setImage(image);
        stocked=false;
    }
    protected Product itemToFill()
    {
       return new Lettuce();
    }
    public Product retrieve()
    {
        return retrieve(Lettuce.class);
    } 
}
