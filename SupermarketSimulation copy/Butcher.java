import greenfoot.*;

public class Butcher extends SuperSmoothMover
{
    private GreenfootImage[] frames;
    private int frame = 0;
    private int tick = 0;
    private int delay = 20; // lower = faster animation

    public Butcher() 
    {
        GreenfootImage frame0 = new GreenfootImage("butcher/butcher0.png");
        GreenfootImage frame1 = new GreenfootImage("butcher/butcher1.png");
        GreenfootImage frame2 = new GreenfootImage("butcher/butcher2.png");
        frame0.scale(frame0.getWidth()/7, frame0.getHeight()/7);
        frame1.scale(frame1.getWidth()/7, frame1.getHeight()/7);
        frame2.scale(frame2.getWidth()/7, frame2.getHeight()/7);

        frames=new GreenfootImage[]{frame0, frame1, frame2};
        setImage(frames[0]);
    }

    public void act() 
    {
        // advance frame every `delay` ticks
        if (++tick >= delay) 
        {
            tick = 0;
            frame = (frame + 1) % frames.length;
            setImage(frames[frame]);
        }
    }
}
