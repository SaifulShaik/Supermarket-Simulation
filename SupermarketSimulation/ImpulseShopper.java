import greenfoot.*;  
import java.util.ArrayList;
import java.util.List;

/**
 * The impulsive shopper is impatient
 * and often buys extra items they did not plan for.
 * 
 * @author Owen Kung 
 * @version Nov 2025
 */
public class ImpulseShopper extends Customer
{
    private static final int IMPUSLE_INDEX=5; //the larger the number the more impulse the buyer, buy more stuff
    
    public ImpulseShopper(Node n) 
    {
        //movement speed, budget, starting node, min list items, max extra random items, max act cycles
        super(2, 100, n, 5, 7, 700); 
        
        rightImages = new GreenfootImage[] {
            padImage(new GreenfootImage("impulseShopper/right1.png")),
            padImage(new GreenfootImage("impulseShopper/right2.png"))
        };
        leftImages = new GreenfootImage[] {
            padImage(new GreenfootImage("impulseShopper/left1.png")),
            padImage(new GreenfootImage("impulseShopper/left2.png"))
        };
        upImages = new GreenfootImage[] {
            padImage(new GreenfootImage("impulseShopper/up1.png")),
            padImage(new GreenfootImage("impulseShopper/up2.png"))
        };
        downImages = new GreenfootImage[] {
            padImage(new GreenfootImage("impulseShopper/down1.png")),
            padImage(new GreenfootImage("impulseShopper/down2.png"))
        };
    }

    /**
     * Impulse shopper behaviour:
     * - First, do normal product retrieval (based on shopping list).
     * - Then, sometimes grabs a random extra item from any nearby shelf,
     *   even if it is not on the shopping list. 
     * - A small heart will merge, indicating it's an impulse purchase
     */
    protected void retrieveProdcuts() 
    {
        //Normal behaviour from Customer: takes needed items from shopping list
        super.retrieveProdcuts();

        //If not in a store or no world, nothing more to do
        if (getStore() == null || getWorld() == null) return;

        //IMPUSLE_INDEX% chance each call to impulse-buy something extra
        if (Greenfoot.getRandomNumber(100) >= IMPUSLE_INDEX) return;

        impulsePurchase();
    }
    /**
     * Attempts to perform an impulse purchase when the shopper is standing
     * beside a display unit.
     *
     * The method checks all display units in the current store and finds one
     * that the shopper is close enough to access. If the unit has stocked
     * items, one item is chosen at random. The shopper retrieves that item,
     * adds it to the cart, and the item is also added visually to the basket.
     *
     * A short pause is applied to simulate picking up the item. A small heart
     * is displayed above the shopper to show that an impulse purchase was made.
     *
     * Only one impulse item is purchased per call. Once an item is taken, the
     * method stops checking other display units.
     */
    private void impulsePurchase()
    {
        List<DisplayUnit> units = getStore().getAvailableDisplayUnits();
        if (units == null || units.isEmpty()) return;

        for (DisplayUnit u : units) {
            if (u == null) continue;

            //Check if this shopper is standing at one of the unit's customer nodes
            List<Node> accessNodes = u.getCustomerNodes();
            if (accessNodes == null || accessNodes.isEmpty()) continue;

            boolean inRange = false;
            for (Node an : accessNodes) {
                if (an == currentNode || currentNode.equals(an)) {
                    inRange = true;
                    break;
                }
            }
            if (!inRange) continue;

            //Get stocked items and pick a random one
            List<Product> stocked = u.getStockedItems();
            if (stocked == null || stocked.isEmpty()) continue;

            Product randomProd = stocked.get(Greenfoot.getRandomNumber(stocked.size()));
            if (randomProd == null) return;

            // Ask display unit to give us one of that type
            Product extra = u.retrieve(randomProd.getClass());
            if (extra != null) {
                cart.add(extra);                     //logically in cart
                addItemToBasket(extra);              //visually in basket (from Customer)
                pauseTimer = 5 + Greenfoot.getRandomNumber(11); //short pause 5–15 acts
                
                if (getWorld() != null) {
                    getWorld().addObject(new Heart(), getX(), getY() - 40);
                }
            }
           
            //Only one impulse item per retrieval attempt
            break;
        }
    }
    
    protected int getType(){
        return 2;
    }
}