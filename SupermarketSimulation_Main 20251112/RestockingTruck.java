import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * 
 * 
 * @author Owen Kung
 * @version Nov 2025
 */
public class RestockingTruck extends SuperSmoothMover
{
    private int speed=2;
    private int loadingTime=120;
    public static boolean loading=false;
    
    public RestockingTruck()
    {
        loading=false;
        setRotation(180);
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
            loading=true;
        }
        
    }
    private void leave()
    {
        if(loadingTime<=0)
        {
           loading=false;
           setLocation(getX(),getY()+speed);
        }
        if(isAtEdge())
        {
            getWorld().removeObject(this);
        }
    }
}
