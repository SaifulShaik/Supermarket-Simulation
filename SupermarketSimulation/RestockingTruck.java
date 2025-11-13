import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import greenfoot.GreenfootImage;

/**
 * When the truck arrives
 * The DisplayUnit will restock
 * 
 * @author Owen Kung
 * @version Nov 2025
 */
public class RestockingTruck extends SuperSmoothMover
{
    private int speed=2;
    private int loadingTime=120;
    public static boolean unloading=false;
    
    public RestockingTruck()
    {
        GreenfootImage image=new GreenfootImage("truck.png");
        image.scale(image.getWidth()/4, image.getHeight()/4);
        setImage(image);
        unloading=false;
        
    }
    public void act()
    {
        moveToUnloadSpot();
        unLoad();
        leave();
    }
    private void moveToUnloadSpot()
    {
        if(getY()<300)
       {
           setLocation(getX(),getY()+speed);
       }
    }
    private void unLoad()
    {
        if(getY()==300 )
        {
            loadingTime--;
            showText("Unloading \n&\n Restocking",Color.RED,getX(),getY()+getImage().getHeight()/2+20);
            //System.out.println("Unloading and Restocking");
            unloading=true;
        }
        
    }
    private void leave()
    {
        if(loadingTime<=0)
        {
           unloading=false;
           setLocation(getX(),getY()+speed);
        }
        if(isAtEdge())
        {
            getWorld().removeObject(this);
        }
    }
}
