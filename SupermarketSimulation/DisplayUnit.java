import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;
/*
 * 
 */
public abstract class DisplayUnit extends SuperSmoothMover
{
    protected ArrayList<Product> stockedItems;
    
    // Flag to control whether display units should stock items (false in editor, true in simulation)
    private static boolean enableStocking = true;
    
    public DisplayUnit() {}
    protected abstract void stock();
    
    /**
     * Set whether display units should stock items
     */
    public static void setEnableStocking(boolean enabled) {
        enableStocking = enabled;
    }
    
    /**
     * Check if stocking is enabled
     */
    protected boolean isStockingEnabled() {
        return enableStocking;
    }
    
    /*
     * Clear whatever is in the display shelves
     */
    protected void clear()
    {  //remove old items
        for (Product p : stockedItems) {
            if (p.getWorld() != null) {
                p.setStock(0);//clean the stock count
                getWorld().removeObject(p);
            }
        }
        stockedItems.clear();
    } 
    /*
     * retrieve an item from display shelve
     */
    protected boolean retrieve(Class productClass) {
        if (getWorld() == null) return false;

        // Use Iterator to avoid ConcurrentModificationException
        for(Product p: stockedItems)
        {
            if(productClass.isInstance(p))
            {
                //rest the stock ount
                p.setStock(p.getStock()-1);
                
                //Remove from world and stockedItems list
                getWorld().removeObject(p);
                stockedItems.remove(p);
                
                return true;//only search the first found
               
            }
        }

        return false; // none found
    }

}


