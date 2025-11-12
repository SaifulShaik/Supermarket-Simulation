import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Lettuce here.
 * 
 * @author Owen Kung
 * @version Nov 2025
 */
public class RawBeef extends Product
{
    private static int stock=0;//keep track of total stock ih the store
    private GreenfootImage image;
    
    public RawBeef() 
    {
        name=SimulationWorld.PRODUCT_RAWBEEF;
        image = new GreenfootImage("product/Raw Steak.PNG");
        image.scale(image.getWidth()/2, image.getHeight()/2);
        image.rotate(90);
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
