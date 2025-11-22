import greenfoot.*;

/**
 * A small heart that appears briefly and then disappears.
 */
public class Heart extends Emoji
{

    public Heart() {
        setImage(new GreenfootImage("heart.png"));
        getImage().scale(40,40);
        getImage().setTransparency(255);   // start fully visible
    }
}

