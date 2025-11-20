import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;
/*
 * 
 */
public abstract class DisplayUnit extends SuperSmoothMover
{
    protected ArrayList<Product> stockedItems;
    protected boolean stocked=false;
    protected boolean reStocked=false;
    
    public DisplayUnit() {}
    protected abstract void stock();
    
    public void act()
    {
        if(RestockingTruck.loading && !reStocked)
        {
            stocked=false; 
            //stock();
            reStocked=true;
            showText("restocked",Color.YELLOW,getX(),getY()+getImage().getHeight()/2);   
        }
        if(!RestockingTruck.loading)
        {
            reStocked=false; //ready for the next restock       
        }
        stock();
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
    protected Product retrieve(Class productClass) {
        if (getWorld() == null) return null;

        // Use Iterator to avoid ConcurrentModificationException
        for(Product p: stockedItems)
        {
            if(productClass.isInstance(p))
            {
                //rest the stock ount
                p.setStock(p.getStock()-1);
                p.getImage().setTransparency(0); //make it invisible
                stockedItems.remove(p);  //remoe from stockedItems
//showText(p.getName()+": "+ "taken."+p.getStock()+ "remained",Color.RED,getX(),getY());
                return p;//only return the first found
               
            }
        }

        return null; // none found
    }

}


