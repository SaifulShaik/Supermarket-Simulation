import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Lettuce here.
 * 
 * @author Owen Kung
 * @version Nov 2025
 */
public class DrumStick extends Product
{
    private static int stock=0;//keep track of total stock ih the store
    private GreenfootImage image;
    
    public DrumStick() 
    {
        price=4.5;
        name=SimulationWorld.PRODUCT_DRUM_STICK;
        image = new GreenfootImage("product/Drum Stick.PNG");
        image.scale(image.getWidth()/3, image.getHeight()/3);
        //image.rotate(270);
        setImage(image);
        
        //whenever a Sprite is added to a shelve,
        //increase the stock#
        stock++;
    }
    public int getStock()
    {
        return stock;
    }
    //Return total number of stock in the store
    public void setStock(int newStockCount)
    {
        stock=newStockCount;
    }
}
