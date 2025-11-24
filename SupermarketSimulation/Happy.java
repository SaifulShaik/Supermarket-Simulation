import greenfoot.*;

/**
 * A small happy face emoji that appears briefly and then disappears.
 *
 * This class loads a smiley image, scales it to 40x40 pixels,
 * and starts fully visible. The timing and removal behavior is
 * handled by the Emoji superclass.
 *
 * @author Owen Kung
 * @version Nov 2025
 */
public class Happy extends Emoji
{
    /**
     * Creates a Happy emoji by loading the smiley image,
     * scaling it, and setting its transparency to fully visible.
     */
    public Happy() {
        setImage(new GreenfootImage("smiley1.png"));
        getImage().scale(40, 40);
        getImage().setTransparency(255);
    }
}

