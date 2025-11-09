import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Lettuce here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Lettuce extends Product
{
    private static int stock=0;//keep track of total stock ih the store
    private GreenfootImage image;
    
    public Lettuce()
    {
<<<<<<< HEAD
=======
        price=2.5;
>>>>>>> Owen-K
        name=SimulationWorld.PRODUCT_LETTUCE;
        image = new GreenfootImage("product/Lettuce.PNG");
        image.scale(image.getWidth()/2, image.getHeight()/2);
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
