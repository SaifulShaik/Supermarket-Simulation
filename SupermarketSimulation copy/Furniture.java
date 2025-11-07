import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;
/*
 * 
 */
public abstract class Furniture extends SuperSmoothMover
{
    protected ArrayList<Product> stockedItems;
    
    public Furniture() {}
    protected abstract void stock();
    
            /*
     * Clear whatever is in the display shelves
     */
    protected void clear()
    {  // Remove old items
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
