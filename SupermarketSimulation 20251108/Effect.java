import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The effect superclass provides the functionality of fading away and removing effects
 * This code was inspired from the lesson in class
 */
public class Effect extends SuperSmoothMover
{
    protected int actCount;
    protected int fadeTime;
    protected GreenfootImage image;
    
    protected void fadeOut (int timeLeft, int totalFadeTime){
        double percent = timeLeft / (double)fadeTime;
        if (percent > 1.00){
            return;
        }
        int newTransparency = (int)(percent * 255);
        image.setTransparency (newTransparency);
    }
    
    protected void fadeIn (int timeLeft, int totalFadeTime){
        double percent = timeLeft / (double)totalFadeTime;
        if (percent > 1.00){
            return;
        }
        int newTransparency = (int)(percent * 255);
        image.setTransparency (newTransparency);
    }

    public void act()
    {
        actCount--;
        fadeOut(actCount, fadeTime);
        // System.out.println(getX()); // for testing
        if (actCount == 0){
            getWorld().removeObject(this);
            return;
        }
    }
}
