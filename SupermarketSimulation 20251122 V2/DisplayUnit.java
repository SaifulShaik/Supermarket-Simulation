import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;
import java.util.*;

/**
 * An abstract base class for all display units in the supermarket
 * (fridges, shelves, racks, etc.).
 *
 * Holds and manages a list of {@link Product} objects.
 * Can stock and restock items (implemented in subclasses via {@link #stock()}).
 * Provides customer nodes that shoppers navigate to.</li>
 * Plays sound and shows visual feedback when items are retrieved or restocked.
 * 
 *
 * Subclasses must implement stock() to place items into the world.
 *
 * @author Owen Kung and Joe Zhuo
 * @version Nov 2025
 */

public abstract class DisplayUnit extends SuperSmoothMover
{
    protected List<Product> stockedItems;
    protected boolean stocked = false;  // Track if this unit has been stocked
    protected boolean reStocked = false;
    
    //Flag to control whether display units should stock items (false in editor, true in simulation)
    protected static boolean enableStocking = true;
    
    // Node that customers navigate to (computed dynamically based on position)
    protected List<Node> customerNodes;
    protected Store parentStore;
protected SaleSign saleSign;   // current sale sign for this unit (if any)
    public DisplayUnit() {
        stockedItems = new ArrayList<>();
        customerNodes = new ArrayList<>();
    }
    
    protected abstract void stock();
    /**
     * Skipping stocking if {@link #enableStocking} is disabled.
     * Reacting to restocking events from RestockingTruck
     *     Marks stocked status for restocking.
     *     Plays a restock sound.
     *     Shows “restocked” text below the unit.
     *   
     * Delegates actual stocking logic to stock() in subclass
     *
     */

    public void act()
    {
        // Only stock if stocking is enabled (not in editor mode)
        if (!isStockingEnabled()) {
            //stock();
            return;
        }

        if(RestockingTruck.unloading && !reStocked)
        {
            stocked=false; 
            //stock();
            reStocked=true;
            
            //sound and visual effect indicating it's restocked
            SoundManager.playShelfRestocked();
            showText("restocked",Color.YELLOW,getX(),getY()+getImage().getHeight()/2);   
        }
        
        if(!RestockingTruck.unloading)
        {
            reStocked=false; //ready for the next restock       
        }
      
        stock();
        updateSaleStatus();   //after stocking check to see if there's item on the unit has sale
    }
    
    /**
     * Set whether display units should stock items
     */
    public static void setEnableStocking(boolean enabled) {
        enableStocking = enabled;
    }
    
    /**
     * Check if stocking is enabled
     */
    protected boolean isStockingEnabled() {
        return enableStocking;
    }
    
    /*
     * Clear whatever is in the display shelves
     */
    protected void clear()
    {  //remove old items
        for (Product p : stockedItems) {
            if (p.getWorld() != null) {
                p.setStock(0);//clean the stock count
                getWorld().removeObject(p);
            }
        }
        stockedItems.clear();
    } 
    
    /**
     * Retrieves an item of the given product class from this display unit.
     *   Searches {@link #stockedItems} for the first instance of {@code productClass}.
     *   Decreases its stock count by 1.</li>
     *   Makes its image fully transparent (instead of removing from the world).
     *   emoves it from {@link #stockedItems}.</li>
     *   Shows a “retrieved” text above the display unit.
     * 
     *
     * @param productClass the Class of the product type to retrieve
     * @return the  product instance if found; null otherwise
     */

    protected Product retrieve(Class productClass) {
        if (getWorld() == null) return null;

        // Use Iterator to avoid ConcurrentModificationException
        for(Product p: stockedItems)
        {
            if(productClass.isInstance(p))
            {
                //rest the stock ount
                p.setStock(p.getStock()-1);
                
                //Remove from world and stockedItems list
                getWorld().removeObject(p);
                //p.getImage().setTransparency(0);
                stockedItems.remove(p);
                //showText(p.getName()+"retrieved",Color.RED,getX(),getY());
                SoundManager.playItemRetrieved();

                return p;//only return the first found
               
            }
        }

        return null; // none found or out of stock
    }

    
    /**
     * Compute and return the node where customers should stand when shopping at this display unit.
     * The node is calculated based on the DisplayUnit's world position converted to grid coordinates.
     */
    public List<Node> getCustomerNodes() {
        // If node already cached and store hasn't changed, return it
        return customerNodes;
    }

    /**
     * Allow external code (editor/world) to set which Node customers should
     * navigate to when shopping at this DisplayUnit.
     */
    public void setCustomerNodes(List<Node> nodes) {
        this.customerNodes = nodes;
        try {
            StringBuilder sb = new StringBuilder();
            if (nodes != null) {
                for (Node n : nodes) sb.append("(").append(n.getX()).append(",").append(n.getY()).append(") ");
            }
            System.out.println("[DisplayUnit] " + getClass().getSimpleName() + " customerNodes set to: [" + (nodes == null ? "null" : sb.toString()) + "]");
        } catch (Exception ignore) {}
    }
    
    /**
     * Get the parent store this DisplayUnit belongs to.
     */
    public Store getParentStore() {
        return parentStore;
    }
    
    public void setParentStore(Store s) {
        parentStore = s;
    }
    
    /**
     * Return the list of stocked Product instances for this DisplayUnit.
     * Ensures a non-null list is returned so callers don't need to null-check.
     */
    public List<Product> getStockedItems() {
        if (stockedItems == null) stockedItems = new ArrayList<Product>();
        return stockedItems;
    }
    /**
     * Show or hide the SALE sign on this display unit based on the global sale.
     */
    protected void updateSaleStatus() {
        if (getWorld() == null) return;
    
        Class<? extends Product> saleType = SaleManager.getSaleProduct();
        boolean hasSaleProduct = false;
    
        // Check if this display unit stocks the sale product type
        if (saleType != null) {
            for (Product p : getStockedItems()) {
                if (p != null && saleType.equals(p.getClass())) {
                    hasSaleProduct = true;
                    break;
                }
            }
        }
    
        // CASE 1: This shelf DOES stock today's sale item
        if (hasSaleProduct) {
    
            // CASE 1A: No sign yet → create one
            if (saleSign == null || saleSign.getWorld() == null ||
                !saleSign.productType.equals(saleType)) 
            {
                // remove old sign
                if (saleSign != null && saleSign.getWorld() != null) {
                    getWorld().removeObject(saleSign);
                }
    
                // create new sale sign for today's sale item
                saleSign = new SaleSign(this, saleType);
    
                int shelfTopY = getY() - getImage().getHeight() / 2;
                getWorld().addObject(saleSign, getX(), shelfTopY - 30);
            }
    
        } else {
            // CASE 2: This shelf does NOT contain today's sale product
            if (saleSign != null && saleSign.getWorld() != null) {
                getWorld().removeObject(saleSign);
            }
            saleSign = null; // IMPORTANT: reset reference
        }
    }
}



