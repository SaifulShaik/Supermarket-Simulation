import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Lettuce here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Orange extends Product
{
    private static int stock=0;//keep track of total stock ih the store
    private GreenfootImage image;
    
    public Orange()
    {
        image = new GreenfootImage("product/Orange.PNG");
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
