import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A delivery truck that restocks the store when it arrives.
 *
 * RestockingTruck drives into the world, stops at an unload position,
 * spends some time "unloading" and restocking shelves, and then leaves.
 * While unloading, it sets the static flag to true so
 * DisplayUnits know they should restock.
 *
 * A truck sound plays while the truck is in the world.
 *
 * @author Owen Kung
 * @version Nov 2025
 */
public class RestockingTruck extends SuperSmoothMover
{
    private int speed = 2;
    private int loadingTime = 120;
    private boolean textShown = false;

    // True while the truck is unloading so shelves know to restock
    public static boolean unloading = false;
    
    /**
     * Creates a RestockingTruck with a scaled truck image.
     *
     * The truck:
     *  - starts with unloading set to false
     *  - uses "truck.png" scaled to one quarter of its size
     *  - starts the truck sound using SoundManager
     */
    public RestockingTruck()
    {
        GreenfootImage image = new GreenfootImage("truck.png");
        image.scale(image.getWidth() / 4, image.getHeight() / 4);
        setImage(image);

        unloading = false;
        SoundManager.playTruckSound();
    }

    /**
     * Called each act cycle.
     * The truck:
     *  1. Moves down to its unload spot
     *  2. Unloads and restocks for a set time
     *  3. Leaves the world when done
     */
    public void act()
    {
        moveToUnloadSpot();
        unLoad();
        leave();
    }

    /**
     * Moves the truck downward until it reaches the unload Y position.
     */
    private void moveToUnloadSpot()
    {
        if (getY() < 300)
        {
            setLocation(getX(), getY() + speed);
        }
    }

    /**
     * Handles the unloading phase once the truck is at the unload spot.
     * Shows a message once, counts down loadingTime, and sets unloading
     * to true while unloading is in progress.
     */
    private void unLoad()
    {
        if (getY() == 300)
        {
            loadingTime--;
            if (!textShown)
            {
                showText("Unloading \n&\n Restocking", Color.RED,
                         getX(), getY() + getImage().getHeight() / 2 + 20);
                textShown = true;
            }
            
            unloading = true;
        }
    }

    /**
     * After unloading finishes, the truck leaves the world.
     * When loadingTime reaches zero, unloading is set to false,
     * the truck drives off the screen, the truck sound stops,
     * and the truck object is removed.
     */
    private void leave()
    {
        if (loadingTime <= 0)
        {
           unloading = false;
           setLocation(getX(), getY() + speed);
        }
        if (isAtEdge())
        {
            SoundManager.stopTruckSound();
            getWorld().removeObject(this);
        }
    }
}

