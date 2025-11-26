import greenfoot.*;

/**
 * Centralized manager for all sound effects and background music
 * used in the supermarket simulation.
 *
 *
 * @author Owen Kung
 * @version Nov 2025
 */
public class SoundManager
{
    //Display units sounds
    private static int itemRetrieveIndex;  //play sounds in turns
    private static GreenfootSound itemRetrieveSounds[]={new GreenfootSound("itemRetrieved.mp3"),
                                                        new GreenfootSound("itemRetrieved.mp3"),
                                                        new GreenfootSound("itemRetrieved.mp3"),
                                                        new GreenfootSound("itemRetrieved.mp3")};
    
    //when truck reloads the shself
    private static GreenfootSound shelfRestocked = new GreenfootSound("loaded.mp3");

    //Truck sound
    private static GreenfootSound truckSound  = new GreenfootSound("truckResume.mp3");   

    //Cashier sound
    private static GreenfootSound cashierSound = new GreenfootSound("beep.wav");
    
    //Customer walking
    // private static GreenfootSound footsteps = new GreenfootSound("walking.mp3");
    //Display units sounds
    private static int walkingIndex;  //play sounds in turns


    //super market ambience
    private static GreenfootSound ambience = new GreenfootSound("supermarketAmbience.mp3");
    //private static GreenfootSound ambience = new GreenfootSound("tokyo-music-walker-sunset-drive-chosic.com_.mp3"); 
    private static GreenfootSound ambienceNight = new GreenfootSound("midnight.mp3");
    
    //butcher sound effect
    //private static GreenfootSound butcherSound = new GreenfootSound("choppingMeat.mp3");
    //butcher sound has problem
    private static GreenfootSound butcherSound=null;
    
    //zombie sound
    private static int zombieSoundIndex;
    private static GreenfootSound zombieSounds[]= {new GreenfootSound("zombieEat.mp3"),
                                                   new GreenfootSound("zombieEat.mp3"),
                                                   new GreenfootSound("zombieEat.mp3"),
                                                   new GreenfootSound("zombieEat.mp3")}; 
    //bullet sound
    private static int bulletSoundIndex;
    private static GreenfootSound bulletSounds[]= {new GreenfootSound("bullet.mp3"),
                                                   new GreenfootSound("bullet.mp3"),
                                                   new GreenfootSound("bullet.mp3"),
                                                   new GreenfootSound("bullet.mp3")}; 

    /**
     * Private constructor
     * prevents instantiation of this class.
     * 
     * All access must be through static methods.
     */
    private SoundManager() 
    { 
    
    }
    //Helper methods inside SoundManager
    private static void playOnce(GreenfootSound s, int volume)
    {
        if (s == null) return;
        s.setVolume(volume);
        s.play();
    }
     //Helper methods inside SoundManager
    private static void playLoop(GreenfootSound s, int volume)
    {
        if (s == null) return;
        s.setVolume(volume);
        if (!s.isPlaying()) {
            s.playLoop();
        }
    }
    //Helper methods inside SoundManager
    private static void stop(GreenfootSound s)
    {
        if (s == null) return;
        if (s.isPlaying()) {
            s.stop();
        }
    }
    

    /**
     * Plays one of the item retrieval sounds when a customer takes a product.
     * Sounds are cycled to avoid overlap or repetition.
     */
    public static void playItemRetrieved()
    {
        itemRetrieveSounds[itemRetrieveIndex].setVolume(30);
        itemRetrieveSounds[itemRetrieveIndex].play();    
        itemRetrieveIndex++; 
        if(itemRetrieveIndex==itemRetrieveSounds.length)
        {
            itemRetrieveIndex=0;
        }
    }
    /**
     * Plays the sound for restocking a shelf once.
     */ 
    public static void playShelfRestocked()
    {
        playOnce(shelfRestocked, 20);
    }
    // --------------------------------------------------------
    // Truck sound events
    // --------------------------------------------------------
    /**
     * Starts looping the truck engine/resume sound.
     */
    // Truck events
    public static void playTruckSound()
    {
        playLoop(truckSound, 20);
    }
    /**
     * Stops the truck sound if it is playing.
     */
    public static void stopTruckSound()
    {
        stop(truckSound);
    }
    // --------------------------------------------------------
    // Butcher sound events
    // --------------------------------------------------------
    /**
     * Starts looping the butcher chopping sound.
     */
    public static void playButcherSound()
    {
        playLoop(butcherSound, 9);
    }
    /**
     * Stops the butcher sound.
     */
    public static void stopButcherSound()
    {
        stop(butcherSound);
    }
    /**
     * Play the cashier check out sound
     */
    public static void playCashierSound()
    {
         playOnce(cashierSound, 35);
    }
    /**
     * Stops the cashier sound.
     */
    public static void stopCashierSound()
    {
        stop(cashierSound);
    }
    // --------------------------------------------------------
    // Footsepts sounds
    // --------------------------------------------------------
    /**
     * Plays a footstep sound and cycles to the next footstep clip.
     * Used to avoid repetition and create more realistic walking audio.
     */
    public static void playFootsteps()
    {
        {
            walkingIndex=0;
        }
    }
    /**
     * Stops the most recently played footstep sound.
     */
    public static void stopFootsteps()
    {
        //walkingIndex--;
         //walkingSounds[walkingIndex].stop();
    }
    // --------------------------------------------------------
    // Ambience sounds
    // --------------------------------------------------------
    /**
     * Starts the daytime supermarket ambience on loop.
     */
    public static void startAmbienceSound()
    {
        playLoop(ambience,30);
    }
    /**
     * Stops the daytime ambience sound.
     */
    public static void stopAmbienceSound()
    {
        stop(ambience);
    }
    /**
     * Starts the night ambience on loop.
     */
    public static void startNightSound()
    {
        playLoop(ambienceNight,40);
    }
    /**
     * Stops the night ambience.
     */
    public static void stopNightSound()
    {
        //playLoop(ambience,30);
        stop(ambienceNight);
    }
    
    /**
     * Adjusts night ambience volume.
     *
     * @param volume positive or negative change
     */
    public static void adjustNightSound(int volume)
    {
        int totalVolume=ambienceNight.getVolume()+volume;      
        if(totalVolume>40 || totalVolume<5)
        {
            return;
        }

        ambienceNight.setVolume(totalVolume);
    }
    /**
     * Adjusts the daytime ambience volume.
     *
     * @param volume positive or negative change
     */
    public static void adjustAmbience(int volume) {
        int totalVolume=ambience.getVolume()+volume;
        
        if(totalVolume>40 || totalVolume<5)
        {
            return;
        }
        ambience.setVolume(totalVolume);
    }
    
    /**
     * Plays one of the zombie sounds when a zombie eats a customer.
     * Sounds are cycled to avoid overlap or repetition.
     */
    public static void playZombieSound()
    {
        zombieSounds[zombieSoundIndex].setVolume(30);
        zombieSounds[zombieSoundIndex].play();    
        zombieSoundIndex++; 
        if(zombieSoundIndex==zombieSounds.length)
        {
            zombieSoundIndex=0;
        }
    }
    /**
     * Plays one of the bullet sounds when a soldier shoot zombie
     * Sounds are cycled to avoid overlap or repetition.
     */
    public static void playBulletSound()
    {
        bulletSounds[bulletSoundIndex].setVolume(20);
        bulletSounds[bulletSoundIndex].play();    
        bulletSoundIndex++; 
        if(bulletSoundIndex==bulletSounds.length)
        {
            bulletSoundIndex=0;
        }
    }

}






