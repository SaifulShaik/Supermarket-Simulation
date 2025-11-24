import greenfoot.*;

/**
 * A small skull that appears briefly when a shopped is eaten by a zombie
 * 
 * @author:Owen Kung
 * @version:Nov 2025
 */
public class Death extends Emoji
{

    public Death() {
        setImage(new GreenfootImage("skull.png"));
        getImage().scale(40,40);
        getImage().setTransparency(255);   // start fully visible
    }
}


