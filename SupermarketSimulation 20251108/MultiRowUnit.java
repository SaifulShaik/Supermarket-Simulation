import greenfoot.*;
import java.util.ArrayList;

public abstract class MultiRowUnit extends DisplayUnit {
    protected GreenfootImage image;
    protected int COLS = 6;        // how many per row
    protected int ROWS = 4;        // how many rows
    protected int LEFT_PAD = 10;   // distance from fridge’s left edge
    protected int TOP_PAD  = 12;   // distance from fridge’s top
    protected int COL_GAP  = 10;   // horizontal gap
    protected int ROW_GAP  = 25;   // vertical gap
    protected boolean stocked=false;

    public MultiRowUnit() {
        stockedItems = new ArrayList<>();
        image = new GreenfootImage("furniture/fridge.png");
        image.scale(image.getWidth()/5, image.getHeight()/5);//make it smaller
        setImage(image);
        stocked=false;

    }
    public void act()
    {
        stock();      
    }
    protected void stock() {
        if (getWorld()== null) return;
        if(stocked) return;
        
        //clear old itemes first
        clear();

        //Fining top left corner of the display unit
        int fridgeW = getImage().getWidth();
        int fridgeH = getImage().getHeight();
        int topLeftX = getX() - fridgeW/2;
        int topLeftY = getY() - fridgeH/2;

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                Product item=stockItemsByRow(r);
                
                //x and y co-ordinates
                if(item!=null)
                {
                    int itemX = topLeftX + LEFT_PAD + c * COL_GAP + item.getImage().getWidth()/2;
                    int itemY = topLeftY + TOP_PAD  + r * ROW_GAP + item.getImage().getHeight()/2;

                    getWorld().addObject(item, itemX, itemY);
                    stockedItems.add(item);
                }

            }
        }
        stocked=true;
        
    }
    /*
     * subclass must implement this method based on the item to be stocked per row
     */
    abstract Product stockItemsByRow(int rowNum);

}


