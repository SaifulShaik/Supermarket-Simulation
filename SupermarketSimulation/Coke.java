import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Coke here.
 * 
 * @author Owen Kung 
 * @version Nov 6 2025
 */
public class Coke extends Product
{
    private GreenfootImage image;
    private static int stock=0;//keep track of total stock ih the store
     
    public Coke()
    {
        image = new GreenfootImage("product/pop/Pop 3.PNG");
        image.scale(image.getWidth()/2, image.getHeight()/2);
        setImage(image);
    }
    /**
     * Act - do whatever the Coke wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        image = new GreenfootImage("product/pop/Pop 3.PNG");
        image.scale(image.getWidth()/2, image.getHeight()/2);
        setImage(image);
        
        //whenever a coke is added to a shelve,
        //increase the stock#
        stock++;
    }
    //Return total number of stock in the store
    public int getStock()
    {
        return stock;
    }
    public static int getCurrentStock()
    {
        return stock;
    }
    //Return total number of stock in the store
    public void setStock(int newStockCount)
    {
        stock=newStockCount;
    }
}


