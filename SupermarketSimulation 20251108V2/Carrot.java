import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Lettuce here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Carrot extends Product
{
    private static int stock=0;//keep track of total stock ih the store
    private GreenfootImage image;
    
    public Carrot()
    {
        price=1.5;
        name=SimulationWorld.PRODUCT_CARROT;
        image = new GreenfootImage("product/carrot.PNG");
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
