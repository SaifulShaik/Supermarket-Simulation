import greenfoot.*;

public class StoreOwner2 extends Actor
{
    private int timer;
    private SpeechBubble bubble;
    private int dialogueStage;
    
    public StoreOwner2() {
        timer = 0;
        bubble = null;
        dialogueStage = 0;
        
        GreenfootImage image = new GreenfootImage(80, 100);
        image.setColor(new Color(100, 120, 180));
        image.fillOval(20, 10, 40, 40); // Head
        image.fillRect(25, 50, 30, 50); // Body
        image.setColor(Color.BLACK);
        image.drawOval(20, 10, 40, 40);
        image.drawRect(25, 50, 30, 50);
        setImage(image);
    }
    
    public void act()
    {
        timer++;
        
        // Dialogue sequence
        if (timer == 90 && dialogueStage == 0) {
            showSpeechBubble("No, no! Visit\nmy store instead!");
            dialogueStage = 1;
        }
        
        if (timer == 240 && dialogueStage == 1) {
            removeSpeechBubble();
            showSpeechBubble("My store has fresher \n food Saiful!");
            dialogueStage = 2;
        }
        
        if (timer == 390 && dialogueStage == 2) {
            removeSpeechBubble();
            dialogueStage = 3;
        }
    }
    
    private void showSpeechBubble(String text) {
        removeSpeechBubble();
        bubble = new SpeechBubble(text);
        getWorld().addObject(bubble, getX(), getY() - 120);
    }
    
    private void removeSpeechBubble() {
        if (bubble != null && getWorld() != null) {
            getWorld().removeObject(bubble);
            bubble = null;
        }
    }
}