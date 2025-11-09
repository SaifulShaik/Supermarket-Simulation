import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Doritos here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Doritos extends Product
{
    private static int stock=0;//keep track of total stock ih the store
    private GreenfootImage image;
    
    public Doritos()
    {
        price=3.5;
        name=SimulationWorld.PRODUCT_DORITOS;
        image = new GreenfootImage("product/chips/Chips 1.PNG");
        image.scale(image.getWidth()/2, image.getHeight()/2);
        setImage(image);
        
        //whenever a Sprite is added to a shelve,
        //increase the stock#
        stock++;
    }
    //Return total number of stock in the store
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
