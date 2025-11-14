import greenfoot.*;

public class StoreOwner1 extends Actor
{
    private int timer;
    private SpeechBubble bubble;
    private int dialogueStage;
    
    public StoreOwner1() {
        timer = 0;
        bubble = null;
        dialogueStage = 0;
        
        // Create a simple shopkeeper image (you can replace with actual image)
        GreenfootImage image = new GreenfootImage(80, 100);
        image.setColor(new Color(150, 100, 50));
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
        if (timer == 30 && dialogueStage == 0) {
            showSpeechBubble("Hey Saiful! Come to\nmy store!");
            dialogueStage = 1;
        }
        
        if (timer == 180 && dialogueStage == 1) {
            removeSpeechBubble();
            showSpeechBubble("We have the best\ndeals in town!");
            dialogueStage = 2;
        }
        
        if (timer == 330 && dialogueStage == 2) {
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