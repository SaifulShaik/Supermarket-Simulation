import greenfoot.*;

/**
 * A small happy face  that appears briefly and then disappears.
 */
public class Happy extends Emoji
{

    public Happy() {
        setImage(new GreenfootImage("smiley1.png"));
        getImage().scale(40,40);
        getImage().setTransparency(255);   // start fully visible
    }
}

