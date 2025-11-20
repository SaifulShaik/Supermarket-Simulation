import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;

/**
 * An overlay effect that simulates night in the supermarket simulation.
 *
 * It gradually darkens the screen and then brightens it
 * again by changing the transparency of a dark rectangle drawn over the world.
 * The night cycle starts after 9:00 and runs for a fixed
 * duration each day.
 *
 * @author Owen Lee & Owen Kung
 * @version Oct 2025
 */
public class NightEffect extends Effect
{
    private static final int DARKER_LENGTH = 200;
    private static final int BRIGHTER_LENGTH = 200;
    private static int totalDuration = DARKER_LENGTH + BRIGHTER_LENGTH;
    private static int timer;
    
    private int gettingDark = 0;
    private int gettingBrighter = BRIGHTER_LENGTH;
    public static boolean isDayTime = true;
    private static boolean isTotalDark = false;
    
    // Moon arc animation properties
    private double moonProgress; // 0.0 to 1.0, tracks progress across the sky
    private double moonSpeed;
    
    public NightEffect () 
    {
        moonProgress = 0.0;
        moonSpeed = 1.0 / totalDuration; // Complete arc over the full night duration
        drawImage();
    }
    
    /**
     * At specified time, the timer is reset
     * Starting from 9:00, and while timer <= totalDuration
     * the night effect progresses by calling darken()
     */
    public void act()
    {
        if(TimeOfDayManager.getHour() == 23 && TimeOfDayManager.getMinute() == 0)
        {  
            timer = 0;
            isDayTime = false;
            moonProgress = 0.0; // Reset moon to start position
            
            SoundManager.stopAmbienceSound();
        }
        
        // Run the darkening mechanism with given duration
        // Make sure it has enough time to get back to day time
        if(TimeOfDayManager.getHour() >= 23 || TimeOfDayManager.getHour() <= 2 && timer <= totalDuration)
        {   
            timer++;
            darken();          
        }
        
        cleanUp();
    }
    
    private void darken()
    {
        // Update moon progress
        moonProgress += moonSpeed;
        if (moonProgress > 1.0) {
            moonProgress = 1.0; // Stop at the end
        }
        
        // Redraw the image with updated moon position
        drawImage();
        
        if(gettingDark <= DARKER_LENGTH)
        {
            gettingDark++;
            fade(gettingDark, DARKER_LENGTH);
        }
        
        if(gettingBrighter == BRIGHTER_LENGTH && gettingDark >= DARKER_LENGTH)
        {
            isTotalDark = true;
        }
        
        if(gettingDark >= DARKER_LENGTH && gettingBrighter >= 0)
        { 
            gettingBrighter--;
            fade(gettingBrighter, BRIGHTER_LENGTH);
        }
        
        if(gettingBrighter == BRIGHTER_LENGTH - 30)
        {
            isTotalDark = false;
        }
        
        if(gettingDark >= DARKER_LENGTH && gettingBrighter <= 0)
        {
            isDayTime = true;
            isTotalDark = false;
            gettingDark = 0;
            gettingBrighter = BRIGHTER_LENGTH;
        } 
    }
    
    /*
     * Use transparency to control the darkness of effect
     */
    private void fade(int timeLeft, int totalFadeTime)
    {
        double percent = timeLeft / (double)totalFadeTime;
        
        if (percent > 1.00) return;
        
        int newTransparency = (int)(percent * 255);
        image.setTransparency(newTransparency);
        
        // When it is dark enough and we are in the darkening phase, start night sound
        if(newTransparency > 30 && gettingDark > 0)
        {       
            SoundManager.startNightSound();
        }
        
        // When we are well into the brightening phase, stop night sound and resume ambience
        if(gettingBrighter < BRIGHTER_LENGTH - 60)
        {       
            SoundManager.stopNightSound();
            SoundManager.startAmbienceSound();
        }
    }
    
    private void cleanUp()
    {
        if(!isTotalDark)
        {
            return;
        }
        
        ArrayList<Customer> customers = (ArrayList<Customer>)getWorld().getObjects(Customer.class);
        
        for(Customer c : customers)
        {
            getWorld().removeObject(c);
        }
    }
    
    private void drawImage() 
    {
        image = new GreenfootImage(1600, 600);
        image.setColor(new Color(0, 0, 0, 255)); // Black background
        image.fill();
        
        // Calculate moon position in a V shape
        // X moves from left to right (0 to image width)
        int moonX = (int)(moonProgress * image.getWidth());
        
        // Y follows a V shape: goes up first half, then down second half
        int startY = 300; // Starting height (lower in sky)
        int peakY = 50;   // Peak height (higher in sky)
        int moonY;
        
        if (moonProgress < 0.5) {
            // First half: going up 
            moonY = (int)(startY - (startY - peakY) * (moonProgress / 0.5));
        } else {
            // Second half: going down 
            moonY = (int)(peakY + (startY - peakY) * ((moonProgress - 0.5) / 0.5));
        }
        
        int moonSize = 60;
        
        // Draw moon glow (the part on top)
        image.setColor(new Color(255, 255, 200, 80));
        image.fillOval(moonX - 15, moonY - 15, moonSize + 30, moonSize + 30);
        
        // Draw moon
        image.setColor(new Color(255, 255, 220));
        image.fillOval(moonX, moonY, moonSize, moonSize);
        
        // Draw stars
        drawStars();
        
        setImage(image);
        image.setTransparency(0);  // Start with daytime
    }
    
    private void drawStars() 
    {
        image.setColor(new Color(255, 255, 255, 200));
        // Create random stars 
        for (int i = 0; i < 50; i++) {
            int starX = (i * 127) % image.getWidth(); 
            int starY = (i * 73) % (image.getHeight() - 100);
            int starSize = 2 + (i % 3);
            image.fillOval(starX, starY, starSize, starSize);
        }
    }
}