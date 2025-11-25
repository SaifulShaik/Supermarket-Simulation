import greenfoot.*;

/**
 * A small shopping basket that can be carried by a customer.
 *
 * The Basket is drawn using simple lines and rectangles on a
 * transparent GreenfootImage. It does not handle any logic by
 * itself and is mainly a visual object.
 *
 * @author  Owen Kung
 * @version Nov 2025
 */
public class Basket extends SuperSmoothMover
{
    /**
     * Creates a new Basket image.
     *
     * The image is 30 x 25 pixels and includes:
     *  - an outer rectangle as the basket frame
     *  - a horizontal line near the top to show the basket opening
     *  - three vertical lines to show the basket slats
     */
    public Basket()
    {
        GreenfootImage img = new GreenfootImage(30, 25);

        // Draw basket outline
        img.setColor(Color.BLACK);
        img.drawRect(0, 0, 29, 24);

        // Draw horizontal divider (basket top)
        img.drawLine(3, 10, 27, 10);

        // Draw vertical slats
        img.drawLine(8, 10, 8, 24);
        img.drawLine(15, 10, 15, 24);
        img.drawLine(22, 10, 22, 24);

        setImage(img);
    }
}

