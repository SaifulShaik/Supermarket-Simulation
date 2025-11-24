import greenfoot.*;

/**
 * 
 * The explosion floats upward, gradually fades out, and
 * removes itself from the world when its lifetime ends.
 * 
 * @author:Owen Kung
 * @version:Nov 2025
 */
public class Explosion extends Emoji
{
    /**
     * Creates a new Explosion using a single PNG image.
     * The image file should be named "explosion.png" and
     * placed in the Greenfoot project folder.
     */
    public Explosion()
    {
        setImage(new GreenfootImage("bomb.png"));
        getImage().scale(30,30);
        getImage().setTransparency(255);   // start fully visible
    }
}


