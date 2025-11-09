import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class RegularShopper here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class RegularShopper extends Customer
{
    GreenfootImage shopper = new GreenfootImage("maleShopper/left3.png");
    public RegularShopper() {
        super(100, 2);
        shopper.scale(shopper.getWidth() / 8, shopper.getHeight() / 8);
        setImage(shopper);
    }
}
