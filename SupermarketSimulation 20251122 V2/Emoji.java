import greenfoot.*;

/**
 * 
 * The explosion floats upward, gradually fades out, and
 * removes itself from the world when its lifetime ends.
 */
public class Emoji extends Actor
{
    private int life = 120;
    private int fadeSpeed = 6;
    private int floatSpeed = 1;

    /**
     * Creates a new Explosion using a single PNG image.
     * The image file should be named "explosion.png" and
     * placed in the Greenfoot project folder.
     */
    public Emoji()
    {
        setImage(new GreenfootImage("bomb.png"));
        getImage().scale(30,30);
        getImage().setTransparency(255);   // start fully visible
    }

    /**
     * Called once per act. Handles the explosion behaviour:
     * it moves upward, fades out, and removes itself when done.
     */
    public void act()
    {
        floatUp();
        fadeOut();
        checkRemove();
    }

    /**
     * Moves the explosion slightly upward each frame.
     * This gives the effect of the explosion rising.
     */
    private void floatUp()
    {
        if (getWorld() != null) {
            setLocation(getX(), getY() - floatSpeed);
        }
    }

    /**
     * Reduces the transparency of the explosion image over time.
     * The life counter is also reduced each call.
     */
    private void fadeOut()
    {
        GreenfootImage img = getImage();
        int transparentcy = img.getTransparency();

        transparentcy -= fadeSpeed;
        if (transparentcy < 0) {
            transparentcy = 0;
        }

        img.setTransparency(transparentcy);
        life--;
    }

    /**
     * Checks whether the explosion should be removed.
     * The explosion is removed if its life has ended or
     * if it is fully transparent.
     */
    private void checkRemove()
    {
        if (life <= 0 || getImage().getTransparency() == 0) {
            if (getWorld() != null) {
                getWorld().removeObject(this);
            }
        }
    }
}

