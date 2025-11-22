import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class BargainShopper here.
 * 
 * @author Owen Kung 
 * @version (a version number or a date)
 */
public class BargainShopper extends Customer
{
    public BargainShopper(Node n)
    {
        //movement speed, budget, starting node, min list items, max extra random items, max act cycles
        super(2, 100, n, 5, 7, 700); 

        rightImages = new GreenfootImage[] {
            padImage(new GreenfootImage("femaleShopper/right1.png")),
            padImage(new GreenfootImage("femaleShopper/right2.png"))
        };
        leftImages = new GreenfootImage[] {
            padImage(new GreenfootImage("femaleShopper/left1.png")),
            padImage(new GreenfootImage("femaleShopper/left2.png"))
        };
        upImages = new GreenfootImage[] {
            padImage(new GreenfootImage("femaleShopper/up1.png")),
            padImage(new GreenfootImage("femaleShopper/up2.png"))
        };
        downImages = new GreenfootImage[] {
            padImage(new GreenfootImage("femaleShopper/down1.png")),
            padImage(new GreenfootImage("femaleShopper/down2.png"))
        };
    }
}
