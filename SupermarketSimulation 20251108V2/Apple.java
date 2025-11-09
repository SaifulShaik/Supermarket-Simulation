import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * 
 * 
 * @author Owen Kung
 * @version Nov 2025
 */
public class Apple extends Product
{
    private static int stock=0;//keep track of total stock ih the store
    private GreenfootImage image;
    
    public Apple()
    {
        name=SimulationWorld.PRODUCT_APPLE;
        price=2.0;
        image = new GreenfootImage("product/Apple.PNG");
        image.scale(image.getWidth()/2, image.getHeight()/2);
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
