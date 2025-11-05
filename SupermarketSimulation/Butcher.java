import greenfoot.*;

public class Butcher extends Actor {
    private GreenfootImage[] frames;
    private int frame = 0;
    private int tick = 0;
    private int delay = 6; // lower = faster animation

    public Butcher() {
        frames = new GreenfootImage[] {
            new GreenfootImage("butcher/butcher0.png"),
            new GreenfootImage("butcher/butcher1.png"),
            new GreenfootImage("butcher/butcher2.png")
        };



        setImage(frames[0]);
    }

    public void act() {
        // advance frame every `delay` ticks
        if (++tick >= delay) {
            tick = 0;
            frame = (frame + 1) % frames.length;
            setImage(frames[frame]);
        }
    }
}
