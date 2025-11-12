import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
/**
 * Write a description of class Cutscene here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Cutscene extends World
{
    private int timer;
    private CutsceneImage saiful;
    private CutsceneImage textBubble;
    private boolean walkingDiagonal;
    private boolean walkingForward;
    /**
     * Constructor for objects of class Cutscene.
     * 
     */
    public Cutscene()
    {    
        super(1200, 600, 1);
        timer = 0;
        walkingDiagonal = false;
        walkingForward = false;
        
        saiful = new CutsceneImage(new GreenfootImage("Cutscene/Saiful/Saiful Think.PNG"));
        textBubble = new CutsceneImage(new GreenfootImage("Cutscene/Text/SaiHungry Text.PNG"));
        
        addObject(saiful, 650, 325);
        addObject(textBubble, 650, 330);
        setBackground(new GreenfootImage("Cutscene/Cutscene Background 1.PNG"));
    }
    
    public void act(){
        timer++;
        
        if (timer==90){
            saiful.setImage(new GreenfootImage("Cutscene/Saiful/Saiful Aha.PNG"));
            textBubble.setImage(new GreenfootImage("Cutscene/Text/SaiFull Text.PNG"));
        }
        
        if (timer==180){
            saiful.setImage(new GreenfootImage("Cutscene/Saiful/Saiful Walk.PNG"));
            removeObject(textBubble);
            
            walkingDiagonal = true;
        }
        
        if (timer==260){
            walkingDiagonal = false;
            walkingForward = true;
        }
        
        if (timer==450){
            transitionToSimulation();
        }
        
        if (walkingDiagonal){
            int x=saiful.getX();
            int y=saiful.getY();
            saiful.setLocation(x+2,y+1);
        }
        
        if (walkingForward){
            int x=saiful.getX();
            int y=saiful.getY();
            saiful.setLocation(x+2,y);
        }
    }
    
    public void transitionToSimulation(){
        fadeOutAndTransition(new CutsceneP2());
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