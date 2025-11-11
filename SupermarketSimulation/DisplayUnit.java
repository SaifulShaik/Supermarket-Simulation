import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;
/**
 * Parent class of all DisplayUnits
 * the subclass must implement the stock() method
 * 
 * 
 * @author: Owen Kung
 * @verion: Nov 2025
 */
public abstract class DisplayUnit extends SuperSmoothMover
{
    protected ArrayList<Product> stockedItems;  
    protected boolean stocked=false;  //indicator whether the shelves were stocked, to avoid constantly stocking up in act
    protected boolean reStocked=false; //after restocking, mark this flag to false
    
    public DisplayUnit() {}
    protected abstract void stock();
    
    public void act()
    {
        //When Restocking trucking is unloading to store
        //The Dispaly Units needed to be stocked up again
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
                //Since it's taken by the customer
                //-1 from stockCount
                p.setStock(p.getStock()-1);
                p.getImage().setTransparency(0); //make it invisible
                stockedItems.remove(p);  //remoe from stockedItems
 showText(p.getName()+": "+ "taken.\n"+p.getStock()+ "remained",Color.RED,getX(),getY()+50);
                return p;//only return the first found
            }
        }

        return null; // none found
    }

}


