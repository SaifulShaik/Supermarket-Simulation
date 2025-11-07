import greenfoot.*;
import java.util.ArrayList;
import java.util.List;

public class Fridge extends Furniture {
    private GreenfootImage image;
    private final List<Actor> stockedItems = new ArrayList<>();

    // Customize layout depending on the fridge image
    private static final int COLS = 6;        // how many per row
    private static final int ROWS = 4;        // how many rows
    private static final int LEFT_PAD = 10;   // distance from fridge’s left edge
    private static final int TOP_PAD  = 12;   // distance from fridge’s top
    private static final int COL_GAP  = 10;   // horizontal gap
    private static final int ROW_GAP  = 25;   // vertical gap
    private static boolean stocked=false;
    private int addedToWorldCounter=0;
    public Fridge() {
        image = new GreenfootImage("furniture/fridge.png");
        image.scale(image.getWidth()/5, image.getHeight()/5);//make it smaller
        setImage(image);
        stocked=false;
    }
    /*
     * After Fridge is successfully added to world
     * Set the paint order
     * can stock the products
     */
    protected void addedToWorld(World w) {
        // Make sure Coke and Water are drawn in front of the fridge
        if (w instanceof SimulationWorld) {
            ((SimulationWorld) w).setPaintOrder(Coke.class, Water.class, Sprite.class, Fanta.class,Fridge.class);
        }
        
        //showText("coke counter"+Coke.getStock(),Color.RED, 200,300);
        //showText("sprite counter"+Sprite.getStock(),Color.RED, 200,350);
        //showText("water counter"+Water.getStock(),Color.RED, 200,400);
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

        // Top-left corner of the fridge image
        int fridgeW = getImage().getWidth();
        int fridgeH = getImage().getHeight();
        int topLeftX = getX() - fridgeW/2;
        int topLeftY = getY() - fridgeH/2;

        // Place coke, sprite, water
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                Actor item=null;
                //if((r+c)%3==0)
                if(r==0)
                {
                    item=new Coke();
                }
                //else if((r+c)%3==1)
                else if(r==1)
                {
                    item=new Water();
                }
               //else if((r+c)%3==2)
               else if(r==2)
                {
                    item=new Sprite();
                }
                else if(r==3)
                {
                    item=new Fanta();
                }
                else
                {
                    item=new Coke();//default
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
    private void clear()
    {  // Remove old items
        for (Actor a : stockedItems) {
            if (a.getWorld() != null) {
                getWorld().removeObject(a);
            }
        }
        stockedItems.clear();
        
    }

    protected void removedFromWorld(World w) {
        for (Actor a : stockedItems) {
            if (a.getWorld() != null) w.removeObject(a);
        }
        stockedItems.clear();
    }
}


