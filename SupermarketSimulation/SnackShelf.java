import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;

/**
 * Write a description of class Shelf here.
 * 
 * @author Owen Kung
 * @version Nov 6, 2025
 */
public class SnackShelf extends DisplayUnit
{
    private GreenfootImage image;
    
    private static final int COLS = 10;        // how many per row
    private static final int ROWS = 3;        // how many rows
    private static final int LEFT_PAD = 10;   // distance from fridge’s left edge
    private static final int TOP_PAD  = 12;   // distance from fridge’s top
    private static final int COL_GAP  = 10;   // horizontal gap
    private static final int ROW_GAP  = 25;   // vertical gap
    private boolean stocked=false;  // Instance variable, not static
    
    public SnackShelf() 
    {
        stockedItems = new ArrayList<>();
        image = new GreenfootImage("furniture/shelf.png");
        image.scale(image.getWidth()/2, image.getHeight()/2);//make it smaller
        setImage(image);
        stocked=false;
    }
    /**
     * Act - do whatever the Shelf wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
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
        int fridgeW = getImage().getWidth();
        int fridgeH = getImage().getHeight();
        int topLeftX = getX() - fridgeW/2;
        int topLeftY = getY() - fridgeH/2;

        
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                Product item=null;
                if(r==0)
                {
                    item=new Doritos();
                }
                else if(r==1)
                {
                    item=new Lays();
                }
                else if(r==2)
                {
                    item=new Ruffles();
                }
                else
                {
                    item=new Doritos();//default
                }
                //x and y co-ordinates
                int itemX = topLeftX + LEFT_PAD + c * COL_GAP + item.getImage().getWidth()/2;
                int itemY = topLeftY + TOP_PAD  + r * ROW_GAP + item.getImage().getHeight()/2;

                getWorld().addObject(item, itemX, itemY);
                stockedItems.add(item);
            }
        }
        stocked=true;
        
    }
}
