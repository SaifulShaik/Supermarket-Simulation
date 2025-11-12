import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class BulkShopper here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class BulkShopper extends Customer
{
    private final int shopperTypeListLength = 6;
    
    /**
     * Act - do whatever the BulkShopper wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    
    public BulkShopper(){
        super.createShoppingList(shopperTypeListLength);
    }
    
    public void act()
    {
        // Add your action code here.
    }
}
