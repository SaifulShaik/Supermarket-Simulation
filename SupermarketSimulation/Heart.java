import greenfoot.*;

/**
 * A small heart icon that appears above an actor to show a positive emotion,
 * such as happiness or satisfaction.
 *
 * The heart uses the image "heart.png", is scaled to 40x40 pixels,
 * and starts fully visible with transparency set to 255.
 *
 * Any movement or fading behavior is handled by the Emoji superclass.
 *
 * @author Owen
 * @version November 2025
 */
public class Heart extends Emoji
{
    /**
     * Creates a Heart emoji.
     * - Loads the heart.png image
     * - Scales it to 40x40 pixels
     * - Sets transparency to 255 (fully visible)
     */
    public Heart() {
        setImage(new GreenfootImage("heart.png"));
        getImage().scale(40, 40);
        getImage().setTransparency(255);
    }
}

