import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;

/**
 * Storm effect with lightning that can start fires
 * By: Owen L
 */
public class Storm extends Effect {
    private boolean firstAct;
    private int position, direction, duration;
    private double speed;
    private final int LOWEST_POSITION = -512;
    private final int HIGHEST_POSITION = 512;
    
    // Lightning variables
    private int lightningTimer;
    private int lightningCooldown;
    private boolean showLightning;
    private int flashDuration;
    private ArrayList<LightningBolt> lightningBolts;
    
    // Track lightning strike positions for fire spawning
    private class LightningBolt {
        int startX;
        int startY;
        int endX;
        int endY;
        int targetHeight; // How far down the bolt should go (percentage)
        
        LightningBolt(int startX, int startY) {
            this.startX = startX;
            this.startY = startY;
            this.endX = startX;
            this.endY = 0;
            // Random height: 40-100% of image height
            this.targetHeight = 40 + Greenfoot.getRandomNumber(50);
        }
    }
    
    public Storm() {
        drawimage();
        actCount = 600;  // Storm lasts 600 acts before fading (about 10 seconds)
        fadeTime = 120;  // Takes 120 acts to fade out (about 2 seconds)
        firstAct = true;
        position = 0;
        direction = 1;
        duration = 50;
        speed = 1.5;
        
        // Initialize lightning
        lightningTimer = 60 + Greenfoot.getRandomNumber(120);
        lightningCooldown = 0;
        showLightning = false;
        flashDuration = 0;
        lightningBolts = new ArrayList<LightningBolt>();
    }
    
    public void act() {
        if (getWorld() == null) {
            return;
        }
        
        // Call parent's fade out and removal logic
        actCount--;
        fadeOut(actCount, fadeTime);
        if (actCount == 0) {
            getWorld().removeObject(this);
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
        
        // Only spawn lightning if not in fade-out phase
        if (actCount > fadeTime) {
            // Handle lightning timing
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
                    spawnFireAtLightningStrike();
                    drawimage(); // Redraw without lightning
                    lightningCooldown = 30;
                }
            }
        }
    }
    
    private void drawimage() {
        image = new GreenfootImage(1600, 600);
        image.setColor(new Color(80, 80, 80, 100)); // gray background
        image.fill();
        
        // Create smoke blobs effect
        image.setColor(new Color(80, 80, 80, 40));
        for (int i = 0; i < 200; i++) {
            int randY = Greenfoot.getRandomNumber(image.getHeight());
            int randX = Greenfoot.getRandomNumber(image.getWidth());
            int randSize = 30 + Greenfoot.getRandomNumber(70);
            
            image.fillOval(randX, randY, randSize * 2, randSize);
        }
        
        setImage(image);
    }
    
    // Trigger lightning strike
    private void triggerLightning() {
        showLightning = true;
        flashDuration = 5; // Lightning visible for 5 frames
        
        // Generate 1-3 lightning bolts
        int numBolts = 1 + Greenfoot.getRandomNumber(3);
        lightningBolts.clear();
        
        for (int i = 0; i < numBolts; i++) {
            int startX = Greenfoot.getRandomNumber(image.getWidth());
            LightningBolt bolt = new LightningBolt(startX, 0);
            lightningBolts.add(bolt);
        }
        
        drawLightning();
    }
    
    // Draw the lightning effect
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
        
        // Draw lightning bolts and track end positions
        for (LightningBolt bolt : lightningBolts) {
            drawLightningBolt(bolt);
        }
        
        setImage(image);
    }
    
    // Draw individual lightning bolt and track where it hits
    private void drawLightningBolt(LightningBolt bolt) {
        int x = bolt.startX;
        int y = bolt.startY;
        int segments = 15 + Greenfoot.getRandomNumber(10);
        
        // Calculate max height this bolt should reach
        int maxHeight = (image.getHeight() * bolt.targetHeight) / 100;
        int segmentHeight = maxHeight / segments;
        
        // Draw main bolt (bright white)
        image.setColor(new Color(255, 255, 255, 255));
        
        for (int i = 0; i < segments; i++) {
            int nextX = x + (-20 + Greenfoot.getRandomNumber(40));
            int nextY = y + segmentHeight;
            
            if (nextX < 0) nextX = 0;
            if (nextX >= image.getWidth()) nextX = image.getWidth() - 1;
            if (nextY >= maxHeight) nextY = maxHeight; // Stop at target height
            
            drawThickLine(x, y, nextX, nextY, 4);
            
            // Sometimes add a branch
            if (Greenfoot.getRandomNumber(100) < 30 && i < segments - 3) {
                int branchX = nextX + (-40 + Greenfoot.getRandomNumber(80));
                int branchY = nextY + segmentHeight * 2;
                if (branchX >= 0 && branchX < image.getWidth() && branchY <= maxHeight) {
                    drawThickLine(nextX, nextY, branchX, branchY, 2);
                }
            }
            
            x = nextX;
            y = nextY;
            
            // Stop if we've reached target height
            if (y >= maxHeight) break;
        }
        
        // Store the final position where lightning ends
        bolt.endX = x;
        bolt.endY = y;
        
        // Add glow effect
        image.setColor(new Color(200, 220, 255, 100));
        x = bolt.startX;
        y = bolt.startY;
        
        for (int i = 0; i < segments; i++) {
            int nextX = x + (-20 + Greenfoot.getRandomNumber(40));
            int nextY = y + segmentHeight;
            
            if (nextX < 0) nextX = 0;
            if (nextX >= image.getWidth()) nextX = image.getWidth() - 1;
            if (nextY >= maxHeight) nextY = maxHeight;
            
            drawThickLine(x, y, nextX, nextY, 8);
            
            x = nextX;
            y = nextY;
            
            // Stop if we've reached target height
            if (y >= maxHeight) break;
        }
    }
    
    // Helper to draw thick lines
    private void drawThickLine(int x1, int y1, int x2, int y2, int thickness) {
        for (int i = -thickness/2; i <= thickness/2; i++) {
            for (int j = -thickness/2; j <= thickness/2; j++) {
                image.drawLine(x1 + i, y1 + j, x2 + i, y2 + j);
            }
        }
    }
    
    // Spawn fire where lightning struck
    private void spawnFireAtLightningStrike() {
        if (getWorld() == null) return;
        
        for (LightningBolt bolt : lightningBolts) {
            // 70% chance each lightning bolt starts a fire
            if (Greenfoot.getRandomNumber(100) < 70) {
                // Convert relative position to world coordinates
                int worldX = getX() - (image.getWidth() / 2) + bolt.endX;
                int worldY = getY() - (image.getHeight() / 2) + bolt.endY;
                
                // Keep within world bounds
                worldX = Math.max(0, Math.min(getWorld().getWidth() - 1, worldX));
                worldY = Math.max(0, Math.min(getWorld().getHeight() - 1, worldY));
                
                // Only spawn fire if within store boundaries
                // Store 1: x between 10-550
                // Store 2: x between 900-1500
                boolean inStore1 = (worldX >= 25 && worldX <= 475);
                boolean inStore2 = (worldX >= 725 && worldX <= 1095);
                
                if (inStore1 || inStore2) {
                    // Spawn fire at lightning strike location
                    Fire fire = new Fire();
                    getWorld().addObject(fire, worldX, worldY);
                    
                    String storeName = inStore1 ? "Store 1" : "Store 2";
                    System.out.println("[Storm] Lightning struck in " + storeName + " at (" + worldX + ", " + worldY + ") - Fire started!");
                } else {
                    System.out.println("[Storm] Lightning struck outside stores at (" + worldX + ", " + worldY + ") - No fire");
                }
            }
        }
    }
}