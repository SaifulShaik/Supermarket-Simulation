import greenfoot.*;

public class Cutscene2 extends World
{
    private int worldTimer = 0;
    
    public Cutscene2()
    {    
        super(1200, 600, 1);
        drawBackground();
        
        // Add the cutscene person starting far away (top center)
        addObject(new CutscenePerson2(), 600, 150);
        
        // Add store owner 1 on the left
        addObject(new StoreOwner1(), 250, 400);
        
        // Add store owner 2 on the right
        addObject(new StoreOwner2(), 950, 400);
    }
    
    public void act() {
        worldTimer++;
        
        // Transition to SimulationWorld after cutscene is done
        if (worldTimer == 650) {
            Greenfoot.setWorld(new SimulationWorld());
        }
    }
    
    private void drawBackground() {
        GreenfootImage bg = getBackground();
        
        // Draw a street scene background
        int horizonLine = getHeight() / 2 + 100;
        
        // Sky gradient
        for (int y = 0; y < horizonLine; y++) {
            int blueShade = 150 + (int)((85.0 * y) / horizonLine);
            bg.setColor(new Color(135, 206, blueShade));
            bg.fillRect(0, y, getWidth(), 1);
        }
        
        // Ground (sidewalk)
        for (int y = horizonLine; y < getHeight(); y++) {
            int grayShade = 180 - (int)((30.0 * (y - horizonLine)) / horizonLine);
            bg.setColor(new Color(grayShade, grayShade, grayShade));
            bg.fillRect(0, y, getWidth(), 1);
        }
        
        // Draw two stores
        // Store 1 (left side)
        bg.setColor(new Color(200, 100, 100));
        bg.fillRect(50, 200, 300, 250);
        bg.setColor(new Color(100, 50, 50));
        bg.drawRect(50, 200, 300, 250);
        
        // Store 1 sign
        bg.setColor(new Color(255, 255, 255));
        bg.fillRect(100, 220, 200, 50);
        GreenfootImage store1Text = new GreenfootImage("STORE 1", 32, Color.BLACK, new Color(255, 255, 255));
        bg.drawImage(store1Text, 130, 230);
        
        // Store 2 (right side)
        bg.setColor(new Color(100, 150, 200));
        bg.fillRect(850, 200, 300, 250);
        bg.setColor(new Color(50, 75, 100));
        bg.drawRect(850, 200, 300, 250);
        
        // Store 2 sign
        bg.setColor(new Color(255, 255, 255));
        bg.fillRect(900, 220, 200, 50);
        GreenfootImage store2Text = new GreenfootImage("STORE 2", 32, Color.BLACK, new Color(255, 255, 255));
        bg.drawImage(store2Text, 930, 230);
    }
}
