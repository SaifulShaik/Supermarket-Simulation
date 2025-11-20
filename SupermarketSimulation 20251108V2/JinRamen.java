import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * 
 * https://www.pngegg.com/en/search?q=Cup+noodle
 * @author Owen Kung
 * @version Nov 2025
 */
public class JinRamen extends Product
{
    private static int stock=0;//keep track of total stock ih the store
    private GreenfootImage image;
    
    public JinRamen()
    {
        price=3.5;
        name=SimulationWorld.PRODUCT_JIN_RAMEN;
        image = new GreenfootImage("product/cup noodle/Cup Noodle 3.png");
        image.scale(image.getWidth()/15, image.getHeight()/15);
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
