import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import greenfoot.GreenfootImage;

/**
 * Write a description of class Fridge here.
 * 
 * @author Owen Kung
 * @version Nov 5, 2025
 */
public class Fridge extends Furniture
{
    private GreenfootImage image;
    
    public Fridge()
    {
        image=new GreenfootImage("furniture/fridge.png");
        int width=image.getWidth()/5;
        int height=image.getHeight()/5;
        image.scale(width,height);
        setImage(image);
    }
}
