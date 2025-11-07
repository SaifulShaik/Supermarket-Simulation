import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Coke here.
 * 
 * @author Owen Kung 
 * @version Nov 6 2025
 */
public class Fanta extends Product
{
    private GreenfootImage image;
    private static int stock=0;//keep track of total stock ih the store
     
    public Fanta()
    {
        image = new GreenfootImage("product/pop/Pop 1.PNG");
        image.scale(image.getWidth()/2, image.getHeight()/2);
        setImage(image);
        
        //whenever a coke is added to a shelve,
        //increase the stock#
        stock++;
    }
    //Return total number of stock in the store
    public static int getStock()
    {
        return stock;
    }
    //Return total number of stock in the store
    public static void setStock(int newStockCount)
    {
        stock=newStockCount;
    }
}


