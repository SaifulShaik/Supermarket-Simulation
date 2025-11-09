import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
/**
 * Write a description of class Cutscene here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Cutscene extends World
{
    /**
     * Constructor for objects of class Cutscene.
     * 
     */
    public Cutscene()
    {    
        super(1200, 600, 1);
        addObject(new House(), 200, 400);
        addObject(new CutscenePerson(), 200, 400);
        drawBackground();
    }
    
    public void transitionToSimulation(){
        fadeOutAndTransition(new Cutscene2());
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
    
    private void drawBackground() {
        GreenfootImage bg = getBackground();
        
        int horizonLine = getHeight() / 2 + 100; // Where sky meets ground
        
        // Draw sky with gradient (lighter at horizon, darker at top)
        for (int y = 0; y < horizonLine; y++) {
            // Gradient from darker blue at top to lighter blue at horizon
            int blueShade = 150 + (int)((85.0 * y) / horizonLine);
            bg.setColor(new Color(135, 206, blueShade));
            bg.fillRect(0, y, getWidth(), 1);
        }
        
        // Draw ground with gradient (lighter at horizon, darker at bottom)
        for (int y = horizonLine; y < getHeight(); y++) {
            // Gradient from lighter green at horizon to darker at bottom
            int greenShade = 200 - (int)((100.0 * (y - horizonLine)) / horizonLine);
            bg.setColor(new Color(34, greenShade, 34));
            bg.fillRect(0, y, getWidth(), 1);
        }
        
        // Optional: Draw horizon line
        bg.setColor(new Color(100, 150, 100));
        bg.fillRect(0, horizonLine - 1, getWidth(), 2);
    }
}