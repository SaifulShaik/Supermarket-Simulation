import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Coke here.
 * 
 * @author Owen Kung 
 * @version Nov 6 2025
 */
public class Coke extends Product
{
    private GreenfootImage image;
    
    public Coke()
    {
        image = new GreenfootImage("product/pop/Pop 3.PNG");
        image.scale(image.getWidth()/2, image.getHeight()/2);
        setImage(image);
    }
    /**
     * Act - do whatever the Coke wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        // Add your action code here.
    }
}


