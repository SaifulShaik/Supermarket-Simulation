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

    public MultiRowUnit() {
        stockedItems = new ArrayList<>();
        image = new GreenfootImage("furniture/fridge.png");
        image.scale(image.getWidth()/5, image.getHeight()/5);//make it smaller
        setImage(image);
        stocked=false;

    }
    /**
     * subclass must implement this method based on the item to be stocked per row
     */
    abstract Product stockItemsByRow(int rowNum);
    protected void stock() {
        if (getWorld()== null) return;
        if(stocked) return;
        
        //clear old itemes first
        clear();
        
        int imageW = getImage().getWidth();
        int imageH = getImage().getHeight();
        int topLeftX = getX() - imageW/2;
        int topLeftY = getY() - imageH/2;

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


}


