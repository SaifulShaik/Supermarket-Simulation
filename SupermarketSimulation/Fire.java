import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
/**
 * Animated fire effect that displays flickering flames and gradually burns out over time.
 * Each fire instance has a random size and duration and shrinks to indicate it is dissapearing
 * 
 * @author Owen L
 * @version Nov 2025
 */

public class Fire extends Actor
{
    private GreenfootImage[] fireFrames = new GreenfootImage[11];
    private int frame = 0;
    private int animationCounter = 0;
    private final int ANIMATION_SPEED = 5;
    private int fireSize = Greenfoot.getRandomNumber(5); 
    
    // Burnout variables
    private int burnoutTimer;
    private int maxBurnTime;
    
    /**
     * Constructs a new fire object with random size and burnout duration.
     * Loads and scales all animation frames
     * Sets up a burnout timer between 120-300 act cycles 
     */
    public Fire() {
        // creates random sized fire 
        for (int i = 0; i < fireFrames.length; i++) {
            fireFrames[i] = new GreenfootImage("Fire_" + i + ".png");
            if (fireSize == 0) {
                fireFrames[i].scale(20, 40);
            } else if (fireSize == 1) {
                fireFrames[i].scale(40, 60);
            } else if (fireSize == 2) {
                fireFrames[i].scale(70, 90);
            } else if (fireSize == 3) {
                fireFrames[i].scale(70, 110);
            } else {
                fireFrames[i].scale(150, 130);
            }
        }
        setImage(fireFrames[0]);
        
        // Quick burnout times (120-300 acts = about 2-5 seconds at 60 fps)
        maxBurnTime = 120 + Greenfoot.getRandomNumber(180);
        burnoutTimer = maxBurnTime;
    }
    
    /**
     * Main act loop for the fire.
     * Animates the fire by cycling through frames and handles the burnout process,
     */
    public void act() {
        animateFire();
        burnOut();
    }
    
    /**
     * Cycles through the fire animation frames to create a animation.
     * Changes to the next frame based on animation speed and cycles through the images
     */
    private void animateFire() {
        animationCounter++;
        if (animationCounter >= ANIMATION_SPEED) {
            frame = (frame + 1) % fireFrames.length;
            setImage(fireFrames[frame]);
            animationCounter = 0;
        }
    }
    
    /**
     * Manages the fire's lifecycle as it burns out over time.
     * The fire shrinks and fades until it is removed
     */
    private void burnOut() {
        burnoutTimer--;
        
        // Fade out in the last 30% of lifetime
        if (burnoutTimer < maxBurnTime * 0.3) {
            int transparency = (int)(255 * ((double)burnoutTimer / (maxBurnTime * 0.3)));
            getImage().setTransparency(Math.max(50, transparency));
        }
        
        // Shrink at 50% lifetime
        if (burnoutTimer == maxBurnTime / 2) {
            shrinkFire(0.7); // Shrink to 70%
        }
        
        // Shrink again at 25% lifetime
        if (burnoutTimer == maxBurnTime / 4) {
            shrinkFire(0.5); // Shrink to 50%
        }
        
        // Remove when burned out
        if (burnoutTimer <= 0) {
            getWorld().removeObject(this);
        }
    }
    
    /**
     * Scales all fire animation frames by the specified factor.
     * Used during the burnout process to make the fire appear smaller.
     * Updates the current displayed image after scaling all frames.
     * 
     * @param scale the scaling factor (ex. 0.7 means 70% of original size)
     */
    private void shrinkFire(double scale) {
        for (int i = 0; i < fireFrames.length; i++) {
            int newWidth = (int)(fireFrames[i].getWidth() * scale);
            int newHeight = (int)(fireFrames[i].getHeight() * scale);
            fireFrames[i].scale(newWidth, newHeight);
        }
        setImage(fireFrames[frame]);
    }
}