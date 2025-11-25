import greenfoot.*;

/**
 * A small mad face emoji that appears briefly and then disappears.
 *
 * This class loads a mad image, scales it to 40x40 pixels,
 * and starts fully visible. The timing and removal behavior is
 * handled by the Emoji superclass.
 *
 * @author Owen Kung
 * @version Nov 2025
 */
public class Mad extends Emoji
{
    /**
     * Creates a mad emoji by loading the mad image,
     * scaling it, and setting its transparency to fully visible.
     */
    public Mad() {
        setImage(new GreenfootImage("mad.png"));
        getImage().scale(60, 60);
        getImage().setTransparency(255);
    }
}

