import greenfoot.*;

public class Basket extends SuperSmoothMover
{
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