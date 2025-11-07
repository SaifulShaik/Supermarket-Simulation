import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The Sprite drinks in the store
 * 
 * @author Owen Kung 
 * @version Nov 6 2025
 */
public class Sprite extends Product
{
    private GreenfootImage image;
    private static int stock=0;//keep track of total stock ih the store
    
    public Sprite()
    {
        image = new GreenfootImage("product/pop/Pop 2.PNG");
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
    /*
     * total number of stock currently in the store as a static method
     */
    public static int getCurrentStock()
    {
        return stock;
    }
}
