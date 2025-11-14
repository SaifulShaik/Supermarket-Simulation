import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * 
 * https://www.pngegg.com/en/search?q=Cup+noodle
 * @author Owen Kung
 * @version Nov 2025
 */
public class XingRamen extends Product
{
    private static int stock=0;//keep track of total stock ih the store
    private GreenfootImage image;
    
    public XingRamen()
    {
        price=3.5;
        name=SimulationWorld.PRODUCT_XING_RAMEN;;
        image = new GreenfootImage("product/cup noodle/Cup Noodle 1.png");
        image.scale(image.getWidth()/20, image.getHeight()/20);
        setImage(image);
        
        //increase the stock#
        stock++;
    }
}
