import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Water here.
 * 
 * @author Owen Kung 
 * @version Nov 6 2025
 */
public class Water extends Product
{
    private GreenfootImage image;
    private static int stock=0;//keep track of total stock ih the store
    
    public Water()
    {
        name=SimulationWorld.PRODUCT_WATER;
        image = new GreenfootImage("product/Water.PNG");
        image.scale(image.getWidth()/2, image.getHeight()/2);
        setImage(image);
        
        stock++;
    }
     //Return total number of stock in the store
    public  int getStock()
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


