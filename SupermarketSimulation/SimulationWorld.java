import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class MyWorld here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class SimulationWorld extends World
{
    //public static final int worldSizeX = 1200;
    //public static final int worldSizeY = 600;
    private static final GreenfootImage bg = new GreenfootImage("background.png");
    public SimulationWorld()
    {    
        //super(worldSizeX, worldSizeY, 1);
        super(bg.getWidth(), bg.getHeight(), 1);
        setBackground(new GreenfootImage(bg)); 
        // add the butcher
        //Butcher butcher = new Butcher();
        //addObject(butcher, getWidth()/2, getHeight()/2);
    }
}
