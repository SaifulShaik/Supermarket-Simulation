import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/*
 * 
 */
public abstract class DisplayUnit extends SuperSmoothMover
{
    protected ArrayList<Product> stockedItems;
    protected boolean stocked = false;  // Track if this unit has been stocked
    
    // Flag to control whether display units should stock items (false in editor, true in simulation)
    protected static boolean enableStocking = true;
    
    // Node that customers navigate to (computed dynamically based on position)
    protected List<Node> customerNodes;
    protected Store parentStore;
    
    public DisplayUnit() {
        stockedItems = new ArrayList<>();
        customerNodes = new ArrayList<>();
    }
    
    protected abstract void stock();
    
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
    /*
     * retrieve an item from display shelve
     */
    protected Product retrieve(Class productClass) {
        if (getWorld() == null) return null;

        // Use Iterator to avoid ConcurrentModificationException
        for (Product p : stockedItems)
        {
            if (productClass.isInstance(p))
            {
                //rest the stock ount
                p.setStock(p.getStock()-1);
                
                //Remove from world and stockedItems list
                getWorld().removeObject(p);
                //p.getImage().setTransparency(0);
                stockedItems.remove(p);
                
                return p;//only return the first found
               
            }
        }

        return null; // none found
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
        customerNodes.clear();
        if (nodes != null) customerNodes.addAll(nodes);
    }
    
    /**
     * Get the parent store this DisplayUnit belongs to.
     */
    public Store getParentStore() {
        return parentStore;
    }
    
    /**
     * Set the parent store for this display unit. Intended for editor/world wiring.
     */
    public void setParentStore(Store s) {
        this.parentStore = s;
    }

    /**
     * Return the list of stocked Product instances for this DisplayUnit.
     * Ensures a non-null list is returned so callers don't need to null-check.
     */
    public List<Product> getStockedItems() {
        if (stockedItems == null) stockedItems = new ArrayList<Product>();
        return stockedItems;
    }
}


