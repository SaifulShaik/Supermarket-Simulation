import greenfoot.*;
/**
 * An on-screen clock that displays the current in-game time.
 * 
 * This actor asks TimeOfDayManager for the time each frame
 * and updates its image to show it as text.
 * 
 * @author:Owen Kung
 * @version:Nov 2025
 */
public class ClockDisplay extends Actor
{
    public ClockDisplay() {
        updateImage();
    }

    public void act()
    {
        TimeOfDayManager.updateTime();
        updateImage();
    }

    private void updateImage()
    {
        String time = TimeOfDayManager.getTimeString();
        GreenfootImage img = new GreenfootImage("Time: " + time, 26, Color.WHITE, new Color(0,0,0,150));
        setImage(img);
    }
}



