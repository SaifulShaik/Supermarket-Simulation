import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Coke here.
 * 
 * @author Angelina Zhou
 * @version Nov 19 2025 
*/
public class MeatOnTheBone extends Product
{
    private GreenfootImage image;
    private static int stock=0;//keep track of total stock in the store
     
    public MeatOnTheBone()
    {
        price=7.5;
        name="MeatOnTheBone";
        image = new GreenfootImage("product/Meat On The Bone.PNG");
        image.scale(image.getWidth()/3, image.getHeight()/3);
        setImage(image);
        
        //whenever chicken is added to a shelve,
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