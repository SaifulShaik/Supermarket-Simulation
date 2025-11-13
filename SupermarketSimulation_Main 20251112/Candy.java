import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * 
 * 
 * @author Owen Kung 
 * @version Nov 6 2025
 */
public class Candy extends Product
{
    private GreenfootImage image;
    private static int stock=0;//keep track of total stock ih the store
     
    public Candy()
    {
        name=SimulationWorld.PRODUCT_CANDY;
        
        //randomly assign different look to candy
        int candyChoice=Greenfoot.getRandomNumber(5);
        if(candyChoice==0)
        {
            image = new GreenfootImage("product/candy/Candy 1.PNG");
        }
        if(candyChoice==1)
        {
            image = new GreenfootImage("product/candy/Candy 2.PNG");
        }
        if(candyChoice==2)
        {
            image = new GreenfootImage("product/candy/Candy 3.PNG");
        }
        if(candyChoice==3)
        {
            image = new GreenfootImage("product/candy/Candy 4.PNG");
        }
        if(candyChoice==4)
        {
            image = new GreenfootImage("product/candy/Candy 5.PNG");
        }


        image.scale(image.getWidth()/2, image.getHeight()/2);
        setImage(image);
    
        //increase the stock#
        stock++;
        //set price
        price=1.5;
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


