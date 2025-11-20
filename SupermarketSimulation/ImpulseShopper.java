import greenfoot.*;  
import java.util.ArrayList;

/**
 * The impulsive is impatient
 * Can't wait for a long time
 * 
 * @author Owen Kung 
 * @version Nov 2025
 */
public class ImpulseShopper extends Customer
{
    
    
    public ImpulseShopper(Node n) 
    {
         //moving speed, budget, node

        super(2, 100, n, 5, 7, 700); // 5-12 items, 0.5-3.0 spee
        
        rightImages = new GreenfootImage[] {new GreenfootImage("impulseShopper/right1.png"),new GreenfootImage("impulseShopper/right2.png")};
        leftImages = new GreenfootImage[] {new GreenfootImage("impulseShopper/left1.png"),new GreenfootImage("impulseShopper/left2.png")};
        upImages = new GreenfootImage[] {new GreenfootImage("impulseShopper/up1.png"),new GreenfootImage("impulseShopper/up2.png")};
        downImages = new GreenfootImage[] {new GreenfootImage("impulseShopper/down1.png"),new GreenfootImage("impulseShopper/down2.png")};
        
    }
    private boolean shoppingDone()
    {
        if(shoppingList.size()==0 && cart.size()>0)
        {
            return true;
        }
        
        return false;
    }
    private void checkingOutAtCashier()
    {
        if(shoppingDone() && isTouching(Cashier.class))
        {
            
        }
    }
}



