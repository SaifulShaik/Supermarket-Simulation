import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Ruffles here.
 * 
 * @author Owen Kung
 * @version Nov 2025
 */
public class Ruffles extends Product
{
    private static int stock=0;//keep track of total stock ih the store
    private GreenfootImage image;
    
    public Ruffles()
    {
<<<<<<< HEAD
=======
        price=2.75;
>>>>>>> Owen-K
        name=SimulationWorld.PRODUCT_RUFFLES;
        image = new GreenfootImage("product/chips/Chips 2.PNG");
        image.scale(image.getWidth()/2, image.getHeight()/2);
        setImage(image);

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
