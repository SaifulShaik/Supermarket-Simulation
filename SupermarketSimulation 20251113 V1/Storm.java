import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;

/**
 * This is the world wide effect that appears when the world is on fire
 */
public class Storm extends Effect {
    private boolean firstAct;
    private int position, direction, duration;
    private double speed;
    private final int LOWEST_POSITION = -512;
    private final int HIGHEST_POSITION = 512;
    
    // ADD THESE LIGHTNING VARIABLES
    private int lightningTimer;
    private int lightningCooldown;
    private boolean showLightning;
    private int flashDuration;
    private ArrayList<int[]> lightningBolts;
    
    public Storm() {
        drawimage();
        actCount = 240;
        fadeTime = 90;
        firstAct = true;
        position = 0;
        direction = 1;
        duration = 50;
        speed = 1.5;
        
        // ADD THIS: Initialize lightning
        lightningTimer = 60 + Greenfoot.getRandomNumber(120);
        lightningCooldown = 0;
        showLightning = false;
        flashDuration = 0;
        lightningBolts = new ArrayList<int[]>();
    }
    
    public void act() {
        if (getWorld() == null) {
            return;
        }
        
        // Move around back and forth
        if (duration > 0) {
            setLocation(getPreciseX() + (speed * direction), getPreciseY());
            duration--;
        } else { // duration has run out
            direction *= -1;
            duration = 50;
        }
        
        // ADD THIS: Handle lightning timing
        if (lightningCooldown > 0) {
            lightningCooldown--;
        } else {
            lightningTimer--;
            if (lightningTimer <= 0) {
                triggerLightning();
                lightningTimer = 80 + Greenfoot.getRandomNumber(150);
            }
        }
        
        // Handle lightning display
        if (showLightning) {
            flashDuration--;
            if (flashDuration <= 0) {
                showLightning = false;
                drawimage(); // Redraw without lightning
                lightningCooldown = 30;
            }
        }
    }
    
    private void drawimage() {
        image = new GreenfootImage(1600, 600);
        image.setColor(new Color(80, 80, 80, 100)); // gray background
        image.fill();
        
        // Create smoke blobs effect
        image.setColor(new Color(80, 80, 80, 40));
        for (int i = 0; i < 200; i++) { // fewer, larger wisps than rain
            int randY = Greenfoot.getRandomNumber(image.getHeight());
            int randX = Greenfoot.getRandomNumber(image.getWidth());
            int randSize = 30 + Greenfoot.getRandomNumber(70);
            
            image.fillOval(randX, randY, randSize * 2, randSize);
        }
        
        setImage(image);
    }
    
    // ADD THIS METHOD: Trigger lightning strike
    private void triggerLightning() {
        showLightning = true;
        flashDuration = 5; // Lightning visible for 5 frames
        
        // Generate 1-3 lightning bolts
        int numBolts = 1 + Greenfoot.getRandomNumber(3);
        lightningBolts.clear();
        
        for (int i = 0; i < numBolts; i++) {
            int startX = Greenfoot.getRandomNumber(image.getWidth());
            lightningBolts.add(new int[]{startX, 0}); // Start at top
        }
        
        drawLightning();
    }
    
    // ADD THIS METHOD: Draw the lightning effect
    private void drawLightning() {
        // Redraw base image first
        drawimage();
        
        // Add bright flash overlay
        image.setColor(new Color(255, 255, 255, 80));
        image.fill();
        
        // Redraw smoke with lighter tint
        image.setColor(new Color(200, 200, 200, 30));
        for (int i = 0; i < 200; i++) {
            int randY = Greenfoot.getRandomNumber(image.getHeight());
            int randX = Greenfoot.getRandomNumber(image.getWidth());
            int randSize = 30 + Greenfoot.getRandomNumber(70);
            image.fillOval(randX, randY, randSize * 2, randSize);
        }
        
        // Draw lightning bolts
        for (int[] bolt : lightningBolts) {
            drawLightningBolt(bolt[0], bolt[1]);
        }
        
        setImage(image);
    }
    
    // ADD THIS METHOD: Draw individual lightning bolt
    private void drawLightningBolt(int startX, int startY) {
        int x = startX;
        int y = startY;
        int segments = 15 + Greenfoot.getRandomNumber(10);
        int segmentHeight = image.getHeight() / segments;
        
        // Draw main bolt (bright white)
        image.setColor(new Color(255, 255, 255, 255));
        
        for (int i = 0; i < segments; i++) {
            int nextX = x + (-20 + Greenfoot.getRandomNumber(40));
            int nextY = y + segmentHeight;
            
            if (nextX < 0) nextX = 0;
            if (nextX >= image.getWidth()) nextX = image.getWidth() - 1;
            if (nextY >= image.getHeight()) nextY = image.getHeight() - 1;
            
            drawThickLine(x, y, nextX, nextY, 4);
            
            // Sometimes add a branch
            if (Greenfoot.getRandomNumber(100) < 30 && i < segments - 3) {
                int branchX = nextX + (-40 + Greenfoot.getRandomNumber(80));
                int branchY = nextY + segmentHeight * 2;
                if (branchX >= 0 && branchX < image.getWidth()) {
                    drawThickLine(nextX, nextY, branchX, branchY, 2);
                }
            }
            
            x = nextX;
            y = nextY;
        }
        
        // Add glow effect
        image.setColor(new Color(200, 220, 255, 100));
        x = startX;
        y = startY;
        
        for (int i = 0; i < segments; i++) {
            int nextX = x + (-20 + Greenfoot.getRandomNumber(40));
            int nextY = y + segmentHeight;
            
            if (nextX < 0) nextX = 0;
            if (nextX >= image.getWidth()) nextX = image.getWidth() - 1;
            if (nextY >= image.getHeight()) nextY = image.getHeight() - 1;
            
            drawThickLine(x, y, nextX, nextY, 8);
            
            x = nextX;
            y = nextY;
        }
    }
    
    // ADD THIS METHOD: Helper to draw thick lines
    private void drawThickLine(int x1, int y1, int x2, int y2, int thickness) {
        for (int i = -thickness/2; i <= thickness/2; i++) {
            for (int j = -thickness/2; j <= thickness/2; j++) {
                image.drawLine(x1 + i, y1 + j, x2 + i, y2 + j);
            }
        }
    }
}