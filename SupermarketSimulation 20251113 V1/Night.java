import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;

/**
 * This is the world wide effect that represents nighttime
 * By: Owen
 */
public class Night extends Effect
{
    private boolean firstAct;
    private int position, direction, duration;
    
    private double speed;
    private final int LOWEST_POSITION = -512;
    private final int HIGHEST_POSITION = 512;
    
    // Moon arc animation properties
    private double moonProgress; // 0.0 to 1.0, tracks progress across the sky
    private double moonSpeed;
    
    public Night () {
        actCount = 360;
        fadeTime = 90;
        firstAct = true;
        position = 0;
        direction = 1;
        duration = 50;
        speed = 1.5;
        
        // Initialize moon animation
        moonProgress = 0.0;
        moonSpeed = 0.003; // Adjust this to make moon move faster/slower 
        
        drawimage();
    }
    
    public void act () {
        if (getWorld() == null){
            return;
        }
        
        // Update moon progress
        moonProgress += moonSpeed;
        if (moonProgress > 1.0) {
            moonProgress = 1.0; // Stop at the end
        }
        
        // Redraw the image with moon at new position
        drawimage();
        
        // fade out
        actCount--;
        if (actCount > fadeTime) {
            image.setTransparency(255);
        } else {
            fadeOut(actCount, fadeTime);
        }
        
        if (actCount == 0){
            getWorld().removeObject(this);
            return;
        }
    }
    
    private void drawimage() {
        image = new GreenfootImage(1600, 600);
        image.setColor(new Color(0, 0, 0, 255)); // black background
        image.fill();
        
        // Calculate moon position in a V shape
        // X moves from left to right (0 to image width)
        int moonX = (int)(moonProgress * image.getWidth());
        
        // Y follows a V shape: goes up first half, then down second half
        int startY = 300; // Starting height (lower in sky)
        int peakY = 50;  // Peak height (higher in sky)
        int moonY;
        
        if (moonProgress < 0.5) {
            // First half: going up (Y decreases as we go up the screen)
            moonY = (int)(startY - (startY - peakY) * (moonProgress / 0.5));
        } else {
            // Second half: going down (Y increases)
            moonY = (int)(peakY + (startY - peakY) * ((moonProgress - 0.5) / 0.5));
        }
        
        int moonSize = 60;
        
        // Draw moon glow (the part on top)
        image.setColor(new Color(255, 255, 200, 80));
        image.fillOval(moonX - 15, moonY - 15, moonSize + 30, moonSize + 30);
        
        // Draw moon background
        image.setColor(new Color(255, 255, 220));
        image.fillOval(moonX, moonY, moonSize, moonSize);
        
        // Optional: Add stars
        drawStars();
        
        setImage(image);
    }
    
    private void drawStars() {
        image.setColor(new Color(255, 255, 255, 200));
        // Create random stars (use fixed seed for consistent positions)
        for (int i = 0; i < 50; i++) {
            int starX = (i * 127) % image.getWidth(); 
            int starY = (i * 73) % (image.getHeight() - 100);
            int starSize = 2 + (i % 3);
            image.fillOval(starX, starY, starSize, starSize);
        }
    }
}