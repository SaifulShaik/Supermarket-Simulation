import greenfoot.*;

public class CutscenePerson2 extends Actor
{
    private int timer;
    private SpeechBubble bubble;
    private int dialogueStage;
    private int scale;
    private BigText bigText;
    
    public CutscenePerson2() {
        timer = 0;
        bubble = null;
        dialogueStage = 0;
        scale = 30; // Start small 
        bigText = null;
        
        updateImage();
    }
    
    private void updateImage() {
        GreenfootImage image = new GreenfootImage("baby2.png");
        int newWidth = scale;
        int newHeight = (image.getHeight() * newWidth) / image.getWidth();
        image.scale(newWidth, newHeight);
        setImage(image);
    }
    
    public void act()
    {
        timer++;
        
        // Walk towards camera (look like its walking down a street)
        if (timer < 180) {
            // Gradually increase size to simulate walking closer
            if (timer % 3 == 0 && scale < 100) {
                scale += 2;
                updateImage();
            }
            // Move down slightly to simulate coming down the street
            setLocation(getX(), getY() + 1);
        }
        
        // Stop and show dialogue
        if (timer == 300 && dialogueStage == 0) {
            showSpeechBubble("Wow! Two stores...\nWhich one should\nI choose?");
            dialogueStage = 1;
        }
        
        if (timer == 450 && dialogueStage == 1) {
            removeSpeechBubble();
            dialogueStage = 2;
        }
        
        // Big dramatic text
        if (timer == 500 && dialogueStage == 2) {
            showBigText();
            dialogueStage = 3;
        }
        
        if (timer == 600 && dialogueStage == 3) {
            removeBigText();
            dialogueStage = 4;
        }
    }
    
    private void showBigText() {
        bigText = new BigText("WHICH STORE\nWILL WIN?");
        getWorld().addObject(bigText, getWorld().getWidth() / 2, 150);
    }
    
    private void removeBigText() {
        if (bigText != null && getWorld() != null) {
            getWorld().removeObject(bigText);
            bigText = null;
        }
    }
    
    private void showSpeechBubble(String text) {
        removeSpeechBubble();
        bubble = new SpeechBubble(text);
        getWorld().addObject(bubble, getX(), getY() - 100);
    }
    
    private void removeSpeechBubble() {
        if (bubble != null && getWorld() != null) {
            getWorld().removeObject(bubble);
            bubble = null;
        }
    }
}
