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
    private CutsceneImage text1;
    private CutsceneImage text2;
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
        text1 = new CutsceneImage(new GreenfootImage("Cutscene/Text/SaiHungry Text.PNG"));
        text2 = new CutsceneImage(new GreenfootImage("Cutscene/Text/Important Customer Text.PNG"));
        
        addObject(saiful, 650, 325);
        addObject(text1, 650, 330);
        setBackground(new GreenfootImage("Cutscene/Cutscene Background 1.PNG"));
    }
    
    public void act(){
        timer++;
        
        if (timer==90){
            saiful.setImage(new GreenfootImage("Cutscene/Saiful/Saiful Aha.PNG"));
            text1.setImage(new GreenfootImage("Cutscene/Text/SaiFull Text.PNG"));
        }
        
        if (timer==180){
            saiful.setImage(new GreenfootImage("Cutscene/Saiful/Saiful Walk.PNG"));
            removeObject(text1);
            
            walkingDiagonal = true;
        }
        
        if (timer==260){
            walkingDiagonal = false;
            walkingForward = true;
        }
        
        if (timer==450){
            walkingForward = false;
            removeObject(saiful);
            drawP2Background();
            addObject(text2, 600,300);
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
        
        if (timer==510){ 
            setText("THE Saiful Text.PNG");
        }
        else if (timer==570){ setText("Yes Text.PNG"); }
        else if (timer==630){ setText("He's Here Text.PNG"); }
        else if (timer==680){
            setText("Saiful Wonders Text.PNG");
            getBackground().drawImage(new GreenfootImage("Cutscene/Saiful/Saiful Think 2.PNG"),0,0);
        }
        else if (timer==800){ setText("My Butcher Text.PNG"); }
        else if (timer==850){ setText("My Supermarket Text.PNG"); }
        else if (timer==960){ transitionToSimulation(); }
    }
    
    private void setText(String imageName){
        text2.setImage(new GreenfootImage("Cutscene/Text/" + imageName));
    }
    
    private void drawP2Background(){
        GreenfootImage background = new GreenfootImage("Cutscene/Cutscene Background 2.PNG");
        background.drawImage(new GreenfootImage("Cutscene/Butcher Owner.PNG"),0,0);
        background.drawImage(new GreenfootImage("Cutscene/Supermarket Owner.PNG"),0,0);
        setBackground(background);
    }
    
    public void transitionToSimulation(){
        fadeOutAndTransition(new SettingWorld());
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