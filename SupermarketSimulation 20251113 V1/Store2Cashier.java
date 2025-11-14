import greenfoot.*;
/**
 * Cashies for store 2
 * Basically an animated objects in the store.
 * 
 * @author: Owen Kung
 * @version:Nov 4, 2025
 */
public class Store2Cashier extends Cashier
{
    private GreenfootImage[] frames;
    private int frame = 0;
    private int tick = 0;
    private int delay = 50; // lower = faster animation

    public Store2Cashier() 
    {
        super("cashier/cashier3.png","cashier/cashier4.png");

    }
}


