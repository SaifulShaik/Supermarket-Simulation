import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;

/**
 * Parent class for bin type of display units
 * 
 * It mainly definee how items in a bin can be stacked
 * 
 * The subclass can set the following values in the contructor. 
 * COLS
 * ROWS
 * LAYERS
 * LEFT_PAD
 * TOP_PAD
 * COL_GAP
 * ROW_GAP
 * 
 * @author Owen Kung
 * @version Nov 6 2025
 */
public abstract class Bin extends DisplayUnit
{
    private GreenfootImage image;
    
    protected int COLS = 10;        // how many per row
    protected int ROWS = 3;        // how many rows
    protected int LAYERS = 5;   // how many layers (higher = taller pile)
    protected int LEFT_PAD = 10;   // distance from fridge’s left edge
    protected int TOP_PAD  = 30;   // distance from fridge’s top
    protected int COL_GAP  = 5;   // horizontal gap
    protected int ROW_GAP  = 5;   // vertical gap
    
    
    public Bin() 
    {
        stockedItems= new ArrayList<>();
        image = new GreenfootImage("furniture/bin/woodenbin.png");
        image.scale(image.getWidth()/8, image.getHeight()/8);//make it smaller
        setImage(image);
        stocked=false;
    }
    /**
     * The subclass must rewrite this, so the parent class can stack it
     */
    protected abstract Product itemToFill();
    protected void stock() {
        if (getWorld()== null) return;
        if(stocked) return;    
        
        //clear old itemes first
        clear();
    
        // Bin geometry
        int binW = getImage().getWidth();
        int binH = getImage().getHeight();
    
        // Compute the top surface line of the bin (screen Y increases downward)
        int surfaceY = getY() - binH / 2 + TOP_PAD;
    
        // Center the columns over the bin
        int leftX = getX() - ((COLS - 1) * COL_GAP) / 2;
    
        //Build the pile: toplayer first, last-added ends up on bottom
        //so when item is retrieved, it will retrieve the top one
        for (int layer = LAYERS-1; layer >=0; layer--) 
        {
            for (int c = COLS-1; c >= 0; c--) {
                int randomX = Greenfoot.getRandomNumber(3) - 1; // -1..+1
                int randomY = Greenfoot.getRandomNumber(3) - 1; // -1..+1
    
                int x = leftX + c * COL_GAP + randomX;
                int y = surfaceY - layer * ROW_GAP + randomY;
    
                Product item = itemToFill();
                item.setRotation(Greenfoot.getRandomNumber(90) - 40); //randomized the rotation
                stockedItems.add(item);//add to stocked list
                getWorld().addObject(item, x, y);
            }
        }
        
        stocked=true;
    }  

}
