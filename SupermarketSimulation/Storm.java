import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;

/**
 * Storm effect with lightning that can start fires in stores.
 * The storm appears as grey clouds that randomly spawn lightning
 * Lightning bolts can spawn fire objects when they strike within store boundaries. 
 * 
 * @author Owen L
 * @version Nov 2025
 */

public class Storm extends Effect {
    private boolean firstAct;
    private int position, direction, duration;
    private double speed;
    private final int LOWEST_POSITION = -512;
    private final int HIGHEST_POSITION = 512;
    
    private int lightningTimer;
    private int lightningCooldown;
    private boolean showLightning;
    private int flashDuration;
    private ArrayList<LightningBolt> lightningBolts;
    private int numberOfFires;
    
    /**
     * Class representing a single lightning bolt strike.
     * Tracks the start position, end position, and target height of each bolt.
     * Used to make lightning and determine location of strike spot (for fire)
     */
    private class LightningBolt {
        private int startX;
        private int startY;
        private int endX;
        private int endY;
        private int targetHeight; //how far down the screen the lightning goes
        
        LightningBolt(int startX, int startY) {
            this.startX = startX;
            this.startY = startY;
            this.endX = startX;
            this.endY = 0;
            // Random height: 40-100% of image height
            this.targetHeight = 40 + Greenfoot.getRandomNumber(50);
        }
    }
    
    /**
     * Constructs a new storm effect with default settings.
     * The storm is initialized and clouds move back and forth
     */
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
    
    /**
     * Main action method called each game cycle.
     * Handles storm movement, lightning timing, flash effects, and fire spawning.
     * The storm moves back and forth, periodically triggers lightning strikes,
     * and fades out when its duration expires.
     */
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
                    numberOfFires++;
                    drawimage(); // Redraw without lightning
                    lightningCooldown = 30;
                }
            }
        }
    }
    
    /**
     * Draws the base storm cloud image without lightning.
     * Creates a semi-transparent gray cloud effect using randomly placed
     * oval shapes to simulate smoke blobs. 
     */
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
    
    /**
     * Triggers a lightning strike event.
     * Generates 1-3 random lightning bolts at different horizontal positions
     * and initiates the flash display for 5 frames. Clears any previous lightning bolts.
     */
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
    
    /**
     * Draws the complete lightning effect including flash.
     * Redraws the base storm image with a bright white flash overlay,
     */
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
    
    /**
     * Draws an individual lightning bolt with jagged segments and glow effect.
     * The bolt is drawn in white with spikes (horizontal segments) sticking out. 
     * Includes random branching effects and tracks the final strike position for fire spawning.
     * 
     * @param bolt the LightningBolt object containing position and height data
     */
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
            
            if (y >= maxHeight) break;
        }
    }
    
    /**
     * Helper method to draw lines with variable thickness.
     * Creates thick lines by drawing multiple parallel lines offset from the center.
     * 
     * @param x1 starting X coordinate
     * @param y1 starting Y coordinate
     * @param x2 ending X coordinate
     * @param y2 ending Y coordinate
     * @param thickness the thickness of the line in pixels
     */
    private void drawThickLine(int x1, int y1, int x2, int y2, int thickness) {
        for (int i = -thickness/2; i <= thickness/2; i++) {
            for (int j = -thickness/2; j <= thickness/2; j++) {
                image.drawLine(x1 + i, y1 + j, x2 + i, y2 + j);
            }
        }
    }
    
    /**
     * Spawns fire objects at lightning strike locations within store boundaries.
     * Each lightning bolt has a 70% chance of starting a fire.
     */
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
                //(calculated based on testing with drawStoreBoundaries() method in SimulationWorld)
                boolean inStore1 = (worldX >= 25 && worldX <= 475);
                boolean inStore2 = (worldX >= 725 && worldX <= 1095);
                
                if (inStore1 || inStore2) {
                    // Spawn fire at lightning strike location
                    Fire fire = new Fire();
                    getWorld().addObject(fire, worldX, worldY);
                    
                    String storeName = inStore1 ? "Store 1" : "Store 2";
                }
            }
        }
    }
}