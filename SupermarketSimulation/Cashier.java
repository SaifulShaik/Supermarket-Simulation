import greenfoot.*;

public class Cashier extends Actor 
{
    private GreenfootImage[] frames;
    private int frame = 0;
    private int tick = 0;
    private int delay = 50; // lower = faster animation

    public Cashier() 
    {
        GreenfootImage frame0 = new GreenfootImage("cashier/cashier1.png");
        GreenfootImage frame1 = new GreenfootImage("cashier/cashier2.png");
        frame0.scale(frame0.getWidth()/6, frame0.getHeight()/6);
        frame1.scale(frame1.getWidth()/6, frame1.getHeight()/6);

        frames=new GreenfootImage[]{frame0, frame1};
        setImage(frames[0]);
    }

    public void act() 
    {
        // advance frame every `delay` ticks
        if (++tick >= delay) {
            tick = 0;
            frame = (frame + 1) % frames.length;
            setImage(frames[frame]);
        }
    }
}
