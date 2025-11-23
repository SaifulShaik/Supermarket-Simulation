import greenfoot.*;

/**
 * BargainShopper:
 * - ONLY buys the sale item
 * - If nothing is on sale → leaves immediately
 * - Buys MANY units of the sale item until stock or budget runs out
 * - Shows a ❤️ emoji every time she picks up a sale item
 */
public class BargainShopper extends Customer
{
    private Class<? extends Product> saleType;
    private static int SALE_PURCHASE_AMOUNT=10;  //buy more when things on sale
    private boolean msgShown=false;
    
    public BargainShopper(Node startNode)
    {
        // movementSpeed, budget, startingNode, minList, extraRandomItems, max acts
        super(2, 100, startNode, 5, 7, 700);

        // Load female shopper animations
        rightImages = new GreenfootImage[] {
            padImage(new GreenfootImage("femaleShopper/right1.png")),
            padImage(new GreenfootImage("femaleShopper/right2.png"))
        };
        leftImages = new GreenfootImage[] {
            padImage(new GreenfootImage("femaleShopper/left1.png")),
            padImage(new GreenfootImage("femaleShopper/left2.png"))
        };
        upImages = new GreenfootImage[] {
            padImage(new GreenfootImage("femaleShopper/up1.png")),
            padImage(new GreenfootImage("femaleShopper/up2.png"))
        };
        downImages = new GreenfootImage[] {
            padImage(new GreenfootImage("femaleShopper/down1.png")),
            padImage(new GreenfootImage("femaleShopper/down2.png"))
        };

        //Identify today's sale item
        saleType = SaleManager.getSaleProduct();

        shoppingList.clear();

        if (saleType != null)
        {
            //ONLY buy sale item
            for(int i=0;i<=SALE_PURCHASE_AMOUNT;i++)
            {
                shoppingList.add(saleType);
            }
        }
        else
        {
            // No sale today → skip shopping and leave
            hasCheckedOut = true;
        }
    }
    
    /**
     * Method for the customer to leave the store
     */
    public void leaveStore() {

        //show msg and emoji
        if(!msgShown)
        {
            //Emoji
            getWorld().addObject(new Happy(), getX(), getY() - 50);
            //showText("Happy! Saved so much!", Color.YELLOW, getX(), getY() - 30);
            msgShown=true;   
        }

        super.leaveStore();
       
    }

}