import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * 
 * https://www.pngegg.com/en/search?q=Cup+noodle
 * @author Owen Kung
 * @version Nov 2025
 */
public class Nissin extends Product
{
    private static int stock=0;//keep track of total stock ih the store
    private GreenfootImage image;
    
    public Nissin()
    {
        price=3.75;
        name=SimulationWorld.PRODUCT_NISSIN;
        image = new GreenfootImage("product/cup noodle/Cup Noodle 2.png");
        image.scale(image.getWidth()/15, image.getHeight()/15);
        //image.rotate(270);
        setImage(image);
        
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
