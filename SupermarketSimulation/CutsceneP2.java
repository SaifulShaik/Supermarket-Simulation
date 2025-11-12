import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class CutsceneP2 here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class CutsceneP2 extends World
{
    private int timer;
    private CutsceneImage text = new CutsceneImage(new GreenfootImage("Cutscene/Text/Important Customer Text.PNG"));
    private int textNumber;
    
    /**
     * Constructor for objects of class CutsceneP2.
     * 
     */
    public CutsceneP2()
    {    
        super(1200, 600, 1);
        timer = 0;
        textNumber = 0;
        
        drawBackground();
        addObject(text,600,300);
    }
    
    public void act(){
        timer++;
        
        if (timer==60){ setText("THE Saiful Text.PNG"); }
        else if (timer==120){ setText("Yes Text.PNG"); }
        else if (timer==200){ setText("He's Here Text.PNG"); }
        else if (timer==260){
            setText("Saiful Wonders Text.PNG");
            getBackground().drawImage(new GreenfootImage("Cutscene/Saiful/Saiful Think 2.PNG"),0,0);
        }
        else if (timer==380){ setText("My Butcher Text.PNG"); }
        else if (timer==430){ setText("My Supermarket Text.PNG"); }
        else if (timer==520){ transitionToSimulation(); }
    }
    
    private void setText(String imageName){
        text.setImage(new GreenfootImage("Cutscene/Text/" + imageName));
    }

    private void drawBackground(){
        GreenfootImage background = new GreenfootImage("Cutscene/Cutscene Background 2.PNG");
        background.drawImage(new GreenfootImage("Cutscene/Butcher Owner.PNG"),0,0);
        background.drawImage(new GreenfootImage("Cutscene/Supermarket Owner.PNG"),0,0);
        setBackground(background);
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
