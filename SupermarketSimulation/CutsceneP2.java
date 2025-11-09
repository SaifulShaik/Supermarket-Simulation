import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class CutsceneP2 here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class CutsceneP2 extends World
{

    /**
     * Constructor for objects of class CutsceneP2.
     * 
     */
    public CutsceneP2()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(600, 400, 1); 
    }
    
    public void transitionToSimulation(){
        fadeOutAndTransition(new SimulationWorld());
    }
    
    private void fadeOutAndTransition(World nextWorld)
    {
        GreenfootImage overlay = new GreenfootImage(getWidth(), getHeight());
        overlay.setColor(new Color(0, 0, 0, 50));
        overlay.fill();
        
        GreenfootImage bg = getBackground();
        bg.drawImage(overlay, 0, 0);
        Greenfoot.delay(5);
        
        Greenfoot.setWorld(nextWorld);
    }
}
