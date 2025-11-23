import greenfoot.*;
import java.util.*;

/**
 * BulkShopper buys MULTIPLE units of EVERY item in its shopping list.
 *
 * Example:
 *   shoppingList = [Milk, Bread]
 *   bulkAmount = 4
 *   → shopper will buy:
 *       Milk x4
 *       Bread x4
 *
 * Based only on overriding retrieveProdcuts().
 *
 * @author Owen Kung
 * @version Nov 2025
 */
public class BulkShopper extends Customer
{
    private static int BULK_AMOuNT=5;
    private int bulkAmount;
    
    public BulkShopper(Node n)
    {
        // movementSpeed, budget, startingNode, minList, maxExtraList, maxActCycles
        super(2.0, 200.0, n, 3, 2, 900);

        this.bulkAmount = Math.max(1, BULK_AMOuNT);
        
        rightImages = new GreenfootImage[] {
            padImage(new GreenfootImage("bulkShopper/right1.png")),
            padImage(new GreenfootImage("bulkShopper/right2.png"))
        };
        leftImages = new GreenfootImage[] {
            padImage(new GreenfootImage("bulkShopper/left1.png")),
            padImage(new GreenfootImage("bulkShopper/left2.png"))
        };
        upImages = new GreenfootImage[] {
            padImage(new GreenfootImage("bulkShopper/up1.png")),
            padImage(new GreenfootImage("bulkShopper/up2.png"))
        };
        downImages = new GreenfootImage[] {
            padImage(new GreenfootImage("bulkShopper/down1.png")),
            padImage(new GreenfootImage("bulkShopper/down2.png"))
        };
    }

    /**
     * Bulk shopper behaviour:
     * - For each item in shoppingList, tries to retrieve NOT just 1,
     *   but "bulkAmount" units from the display.
     * - Removes the item from shoppingList only after ALL units are collected.
     */
    @Override
    protected void retrieveProdcuts()
    {
        if (store == null || currentNode == null || shoppingList == null || shoppingList.isEmpty() || getWorld() == null)
            return;

        List<DisplayUnit> units = store.getAvailableDisplayUnits();
        if (units == null || units.isEmpty()) return;

        // Iterate through display units
        for (DisplayUnit u : units)
        {
            if (u == null) continue;

            List<Node> accessNodes = u.getCustomerNodes();
            boolean inRange = false;

            if (accessNodes != null)
            {
                for (Node an : accessNodes)
                {
                    if (an == currentNode || currentNode.equals(an)) {
                        inRange = true;
                        break;
                    }
                }
            }

            if (!inRange) continue;

            // Check stocked items
            List<Product> stocked = u.getStockedItems();
            if (stocked == null || stocked.isEmpty()) continue;

            // Try buying multiple units for each wanted item
            for (Class<? extends Product> wantedClass : new ArrayList<>(shoppingList))
            {
                int countTaken = 0;

                // Take up to bulkAmount units
                for (int i = 0; i < bulkAmount; i++)
                {
                    Product retrieved = u.retrieve(wantedClass);

                    if (retrieved == null) break;

                    cart.add(retrieved);
                    countTaken++;

                    // Visual basket
                    addItemToBasket(retrieved);

                    // Small picking delay
                    pauseTimer = 6 + Greenfoot.getRandomNumber(8);  // 6–13 acts
                    if (getWorld() != null) {
                    getWorld().addObject(new Explosion(), getX(), getY() - 40);
                }
                }

                // Remove item from shopping list ONLY if all bulk units taken
                if (countTaken == bulkAmount)
                {
                    shoppingList.remove(wantedClass);
                }

                // Even if partially taken, stop retrieving this tick
                if (countTaken > 0) return;
            }
        }
    }
}


