import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class BulkShopper here.
 * 
 * @author: Owen Kung ?
 * @version Nov 2025
 */
public class BulkShopper extends Customer
{

    public BulkShopper(Node n)
    {
        //movement speed, budget, starting node, min list items, max extra random items, max act cycles
        super(2, 100, n, 5, 7, 700); 
        
        rightImages = new GreenfootImage[] 
        {
            new GreenfootImage("bulkShopper/right1.png"),
            new GreenfootImage("bulkShopper/right2.png")
        };
        leftImages = new GreenfootImage[] 
        {
            new GreenfootImage("bulkShopper/left1.png"),
            new GreenfootImage("bulkShopper/left2.png")
        };
        upImages = new GreenfootImage[] 
        {
            new GreenfootImage("bulkShopper/up1.png"),
            new GreenfootImage("bulkShopper/up2.png")
        };
        downImages = new GreenfootImage[] 
        {
            new GreenfootImage("bulkShopper/down1.png"),
            new GreenfootImage("bulkShopper/down2.png")
        };

    }

}
