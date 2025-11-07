import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class House here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class House extends Actor
{
    /**
     * Act - do whatever the House wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    
    public House(){
        // Load image from your project's images folder
        GreenfootImage image = new GreenfootImage("house-03.png");
        
        // Or scale proportionally (keeps aspect ratio)
        int newWidth = 400;
        int newHeight = (image.getHeight() * newWidth) / image.getWidth();
        image.scale(newWidth, newHeight);
        
        // Set as actor's image
        setImage(image);
    }
    
    public void act()
    {
        // Add your action code here.
    }
}
