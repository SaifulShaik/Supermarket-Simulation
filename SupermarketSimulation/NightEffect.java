import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;

/**
 * An overlay effect that simulates night in the supermarket simulation.
 *
 * It gradually darkens the screen and then brightens it again by changing the
 * transparency of a dark image drawn over the world. A simple moon animation
 * moves across the sky during the night. The night cycle starts after 21:00
 * (9:00 PM) and runs for a fixed duration each day.
 *
 * NightEffect also:
 * - Switches between ambience and night sounds.
 * - Resets the in-game time to a fixed morning hour when full darkness is reached.
 * - Removes all Customer objects while it is totally dark.
 *
 * @author Owen Kung, Owen Lee
 * @version Nov 2025
 */
public class NightEffect extends Effect
{
    //private static final int DAY_TIME_LENGTH=600;
    private static final int DARKER_LENGTH=200;
    private static final int BRIGHTER_LENGTH=200;
    private static final int RESET_HOUR=6;
    private boolean hasResetTime = false;
    private static int totalDuration=DARKER_LENGTH+BRIGHTER_LENGTH;
    private static int timer;
    
    //private int dayTime=0;
    private int gettingDark=0;
    private int gettingBrighter=BRIGHTER_LENGTH;
    public static boolean isDayTime=true;
    private static boolean isTotalDark=false;
    
    //Moon arc animation properties
    private double moonProgress; // 0.0 to 1.0, tracks progress across the sky
    private double moonSpeed;
    /**
     * Creates the NightEffect overlay and draws the initial night image.
     * The image starts fully transparent so that daytime is visible.
     */
    public NightEffect () 
    {
        moonSpeed = 0.006;   //moon moving speed
        moonProgress = 0.0;
        drawImage();
        getImage().setTransparency(0);
    }
    /**
     * Main act method for the night effect.
     *
     * Behaviour:
     * - At 21:00, the night cycle is started.
     * - While it is night, the effect runs darken() until the full cycle
     *   (darken + brighten) is complete.
     * - When fully dark, customers may be removed by cleanUp().
     */
    public void act()
    {
        
        if(TimeOfDayManager.getHour()==21 && TimeOfDayManager.getMinute()==0)
        {  
            timer=0;
            isDayTime=false;
            hasResetTime=false;
            
            SoundManager.stopAmbienceSound();
        }
        //if it's still night time, let the darkening mechanism work till completion
        if (!isDayTime && timer <= totalDuration)
        {
            timer++;
            darken();   
        }
        cleanUp();
    }
    /**
     * Runs a single step of the night cycle.
     *
     * This method:
     * - Updates the moon position.
     * - Redraws the night sky (moon and stars).
     * - Fades the overlay darker until DARKER_LENGTH is reached.
     * - Then fades the overlay brighter until BRIGHTER_LENGTH is finished.
     * - Resets state back to daytime at the end of the brightening phase.
     */
    private void darken()
    {
        // Update moon progress
        moonProgress += moonSpeed;
        if (moonProgress > 1.0) 
        {
            moonProgress = 1.0; // Stop at the end
        }
        drawImage();
        
        if(gettingDark<=DARKER_LENGTH)
        {
            gettingDark++;
            fade(gettingDark,DARKER_LENGTH);;
        }
        if(gettingDark>=DARKER_LENGTH  && gettingBrighter>=0)
        { 
            gettingBrighter--;
            fade(gettingBrighter,BRIGHTER_LENGTH);
        }
        if(gettingDark>=DARKER_LENGTH && gettingBrighter<=0)
        {
            isDayTime=true;
            isTotalDark=false;
            gettingDark=0;
            gettingBrighter=BRIGHTER_LENGTH;
            moonProgress = 0.0;

        } 

    }
    /**
     * Adjusts the transparency of the overlay image based on the current
     * progress of the darkening or brightening phase.
     *
     * @param timeLeft      current step in the darken or brighten phase
     * @param totalFadeTime total number of steps for that phase
     */
    private void fade (int timeLeft, int totalFadeTime){
        double percent = timeLeft / (double)totalFadeTime;
        
        if (percent > 1.00) return;
        
        int newTransparency = (int)(percent * 255);
        image.setTransparency (newTransparency);
        
        // When it is dark enough and we are in the darkening phase, start night sound
        if(newTransparency>30 && gettingDark>0)
        {       
            SoundManager.startNightSound();
        }
        // When we are well into the brightening phase, stop night sound and resume ambience
         if(gettingBrighter<BRIGHTER_LENGTH-60)
         {       
            SoundManager.stopNightSound();
            SoundManager.startAmbienceSound();           
        }
        //Reset in-game clock exactly when night reaches full darkness
        if (newTransparency >= 255 && !hasResetTime)
        {
            TimeOfDayManager.setSecond(RESET_HOUR * 3600); // 6:00 AM
            hasResetTime = true;
        }
        if(newTransparency >= 255)
        {
            isTotalDark=true;
        }
        if(newTransparency < 200)
        {
            isTotalDark=false;
        }
    }
    /**
     * Removes all Customer objects from the world while the overlay is
     * considered totally dark. This simulates the supermarket being closed
     * during the darkest part of the night.
     */
    private void cleanUp()
    {
        if(!isTotalDark)
        {
            return;
        }
        
        ArrayList<Customer> customers=(ArrayList<Customer>)getWorld().getObjects(Customer.class);
        
        for(Customer c: customers)
        {
            //remove all the carried items.
            c.removeAllCarriedItems(); //visual basket
            getWorld().removeObject(c);
        }
    }
    /**
     * Draws the night sky image for the overlay.
     * This includes:
     * - A black background.
     * - The moon at its current position along an arc.
     * - A simple star field.
     *
     * The previous transparency value is preserved, so the fade effect
     * remains smooth across frames.
     */
    private void drawImage() 
    {
        int prevAlpha = 0;
        if (image != null) prevAlpha = image.getTransparency();
    
        image = new GreenfootImage(1600, 600);
    
        // background
        image.setColor(Color.BLACK);
        image.fill();
    
        // Calculate moon position
        int moonX = (int)(moonProgress * image.getWidth());
        int startY = 300;
        int peakY = 50;
        int moonY;
    
        if (moonProgress < 0.5) {
            moonY = (int)(startY - (startY - peakY) * (moonProgress / 0.5));
        } else {
            moonY = (int)(peakY + (startY - peakY) * ((moonProgress - 0.5) / 0.5));
        }
    
        int moonSize = 60;
    
        image.setColor(new Color(255, 255, 200, 80));
        image.fillOval(moonX - 15, moonY - 15, moonSize + 30, moonSize + 30);
    
        image.setColor(new Color(255, 255, 220));
        image.fillOval(moonX, moonY, moonSize, moonSize);
    
        drawStars();
    
        setImage(image);
        image.setTransparency(prevAlpha);   //RESTORE the transparency
    }
    /**
     * Draws a simple star field on the current overlay image.
     * Stars are placed in repeatable pseudo-random positions so the pattern
     * looks scattered but does not change every frame.
     */
    private void drawStars() {
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



