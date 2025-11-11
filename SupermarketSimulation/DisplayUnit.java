import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;
/*
 * 
 */
public abstract class DisplayUnit extends SuperSmoothMover
{
    protected ArrayList<Product> stockedItems;
    protected boolean stocked = false;  // Track if this unit has been stocked
    
    // Flag to control whether display units should stock items (false in editor, true in simulation)
    private static boolean enableStocking = true;
    
    // Node that customers navigate to (computed dynamically based on position)
    protected Node customerNode;
    protected Store parentStore;
    
    public DisplayUnit() {}
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
                
                return p;//only return the first found
               
            }
        }

        return null; // none found
    }
    
    /**
     * Compute and return the node where customers should stand when shopping at this display unit.
     * The node is calculated based on the DisplayUnit's world position converted to grid coordinates.
     */
    public Node getCustomerNode() {
        // If node already cached and store hasn't changed, return it
        if (customerNode != null && parentStore != null) {
            return customerNode;
        }
        
        // Otherwise, compute the node based on current position
        updateCustomerNode();
        return customerNode;
    }
    
    /**
     * Recompute the customer node based on current world position.
     * Should be called when the DisplayUnit is added to world or moved.
     */
    public void updateCustomerNode() {
        if (getWorld() == null) return;
        
        // Find the Store this DisplayUnit belongs to
        java.util.List<Store> stores = getWorld().getObjects(Store.class);
        Store closestStore = null;
        double minDistance = Double.MAX_VALUE;
        
        for (Store store : stores) {
            double dx = getX() - store.getX();
            double dy = getY() - store.getY();
            double dist = Math.sqrt(dx * dx + dy * dy);
            if (dist < minDistance) {
                minDistance = dist;
                closestStore = store;
            }
        }
        
        if (closestStore == null) return;
        
        parentStore = closestStore;
        
        // Convert world position to grid node
        Node node = parentStore.getNodeAtWorldPosition(getX(), getY());
        if (node != null) {
            customerNode = node;
        }
    }
    
    @Override
    protected void addedToWorld(World world) {
        super.addedToWorld(world);
        // Compute the customer node when added to world
        updateCustomerNode();
    }

}


