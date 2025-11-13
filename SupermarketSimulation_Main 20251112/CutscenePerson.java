import greenfoot.*;
/**
 * CutscenePerson that shows a speech bubble and walks off screen
 */
public class CutscenePerson extends Actor
{
    private int timer;
    private boolean hasSpoken;
    private boolean isWalking;
    private SpeechBubble bubble;
    
    public CutscenePerson() {
        timer = 0;
        hasSpoken = false;
        isWalking = false;
        bubble = null;
        GreenfootImage image = new GreenfootImage("baby2.png");
        
        // Or scale proportionally (keeps aspect ratio)
        int newWidth = 100;
        int newHeight = (image.getHeight() * newWidth) / image.getWidth();
        image.scale(newWidth, newHeight);
        
        // Set as actor's image
        setImage(image);
    }
    
    public void act()
    {
        timer++;
        
        // Show speech bubble after 30 frames (half second)
        if (timer == 30 && !hasSpoken) {
            showSpeechBubble();
            hasSpoken = true;
        }
        
        // Start walking after speech is shown for 120 frames (2 seconds)
        if (timer == 180 && !isWalking) {
            removeSpeechBubble();
            isWalking = true;
        }
        
        // Walk to the right
        if (isWalking) {
            setLocation(getX() + 4, getY()); // Move right by 2 pixels
            
            // Check if reached rightmost edge and trigger world transition
            if (getX() >= getWorld().getWidth() - 50) {
                // Cast world to Cutscene and call transition method
                Cutscene cutsceneWorld = (Cutscene) getWorld();
                cutsceneWorld.transitionToSimulation();
            }
        }
    }
    
    private void showSpeechBubble() {
        bubble = new SpeechBubble("I am SaifHungry time to\nget food and\nbecome SaiFull");
        getWorld().addObject(bubble, getX(), getY() - 100); // Position above head
    }
    
    private void removeSpeechBubble() {
        if (bubble != null && getWorld() != null) {
            getWorld().removeObject(bubble);
            bubble = null;
        }
    }
}