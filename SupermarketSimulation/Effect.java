import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The effect superclass provides the functionality of fading away and removing effects
 * This code was inspired from the lesson in class
 * 
 * @author Owen Lee
 * @version Nov 2025
 */
public class Effect extends SuperSmoothMover
{
    protected int actCount;
    protected int fadeTime;
    protected GreenfootImage image;
    
    /**
     * Gradually fades out the effect by decreasing its transparency over time
     * The transparency decreases based on time remaining
     * 
     * @param timeLeft the number of acts remaining in the fade effect
     * @param totalFadeTime the total duration (in acts) of the fade effect
     */
    protected void fadeOut (int timeLeft, int totalFadeTime){
        double percent = timeLeft / (double)fadeTime;
        if (percent > 1.00){
            return;
        }
        int newTransparency = (int)(percent * 255);
        image.setTransparency (newTransparency);
    }
    
    /**
     * Gradually fades in the effect by increasing its transparency over time.
     * The transparency increases based on time remaining
     * 
     * @param timeLeft the number of acts remaining in the fade effect
     * @param totalFadeTime the total duration (in acts) of the fade effect
     */
    protected void fadeIn (int timeLeft, int totalFadeTime){
        double percent = timeLeft / (double)totalFadeTime;
        if (percent > 1.00){
            return;
        }
        int newTransparency = (int)(percent * 255);
        image.setTransparency (newTransparency);
    }

     /**
     * Act count decreases, applies the fade-out effect,
     * and removes the effect from the world when the counter reaches zero.
     */
    public void act()
    {
        actCount--;
        fadeOut(actCount, fadeTime);
        if (actCount == 0){
            getWorld().removeObject(this);
            return;
        }
    }
}
