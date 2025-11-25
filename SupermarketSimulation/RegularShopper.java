import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * 
 * 
 * @author Owen Kung
 * @version Nov 2025
 */
public class RegularShopper extends Customer
{
    //GreenfootImage shopper = new GreenfootImage("maleShopper/left3.png");
    
    public RegularShopper(Node n) {
        super(2, 100, n, 5, 7, 700); // 5-12 items, 0.5-3.0 speed

        rightImages = new GreenfootImage[] {
            padImage(new GreenfootImage("regularShopper/right1.png")),
            padImage(new GreenfootImage("regularShopper/right2.png"))
        };
        leftImages = new GreenfootImage[] {
            padImage(new GreenfootImage("regularShopper/left1.png")),
            padImage(new GreenfootImage("regularShopper/left2.png"))
        };
        upImages = new GreenfootImage[] {
            padImage(new GreenfootImage("regularShopper/up1.png")),
            padImage(new GreenfootImage("regularShopper/up2.png"))
        };
        downImages = new GreenfootImage[] {
            padImage(new GreenfootImage("regularShopper/down1.png")),
            padImage(new GreenfootImage("regularShopper/down2.png"))
        };
    }
    
    protected int getType(){
        return 3;
    }
}


