import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;

/**
 * @author:Owen Kung
 * @version: Nov 2025
 */
public abstract class DisplayUnit extends SuperSmoothMover
{
    protected ArrayList<Product> stockedItems;
    protected boolean stocked = false;  // Track if this unit has been stocked
    protected boolean reStocked=false;
    
    //Flag to control whether display units should stock items (false in editor, true in simulation)
    private static boolean enableStocking = true;
    
    // Node that customers navigate to (computed dynamically based on position)
    protected Node customerNode;
    protected Store parentStore;
    
    public DisplayUnit() {}
    protected abstract void stock();
/*
    public void act()
    {
        if(RestockingTruck.loading && !reStocked)
        {
            stocked=false; 
            //stock();
            reStocked=true;
            showText("restocked",Color.YELLOW,getX(),getY()+getImage().getHeight()/2);   
        }
        if(!RestockingTruck.loading)
        {
            reStocked=false; //ready for the next restock       
        }
        stock();
    }
 */

    public void act()
    {
        // Only stock if stocking is enabled (not in editor mode)
        if (isStockingEnabled()) {
            stock();
        }
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
        return customerNode;
    }

    /**
     * Allow external code (editor/world) to set which Node customers should
     * navigate to when shopping at this DisplayUnit.
     */
    public void setCustomerNode(Node n) {
        this.customerNode = n;
        // Debug: print assignment so editor/runtime shows which node is linked
        try {
            String name = this.getClass().getSimpleName();
            if (n != null) {
                System.out.println("DisplayUnit " + name + " at (" + getX() + "," + getY() + ") assigned Node(" + n.getX() + "," + n.getY() + ")");
            } else {
                System.out.println("DisplayUnit " + name + " at (" + getX() + "," + getY() + ") unassigned Node (null)");
            }
        } catch (Exception e) {
            // ignore printing errors in environments where getX/getY or System.out behave differently
        }
    }
    
    /**
     * Get the parent store this DisplayUnit belongs to.
     */
    public Store getParentStore() {
        return parentStore;
    }    /**
     * Recompute the customer node based on current world position.
     * Should be called when the DisplayUnit is added to world or moved.
     */
    /*public void updateCustomerNode() {
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
        
        if (closestStore == null) {
            System.out.println("WARNING: DisplayUnit at (" + getX() + ", " + getY() + ") could not find a parent Store");
            return;
        }
        
        parentStore = closestStore;
        
        // Try to get a node from the store's grid at this position
        Node node = parentStore.getNodeAtWorldPosition(getX(), getY());
        
        if (node != null) {
            // Great! DisplayUnit is on the store's grid
            customerNode = node;
            System.out.println("DisplayUnit " + getClass().getSimpleName() + " at (" + getX() + ", " + getY() + 
                             ") assigned to existing grid node (" + node.getX() + ", " + node.getY() + ")");
        } else {
            // DisplayUnit is outside the store grid - create a virtual node at this position
            // Convert world coordinates to grid coordinates
            int cellSize = parentStore.getCellSize();
            int storeX = parentStore.getStoreWorldX();
            int storeY = parentStore.getStoreWorldY();
            
            int gridX = (getX() - storeX) / cellSize;
            int gridY = (getY() - storeY) / cellSize;
            
            // Create a new node at this virtual position (not blocked, not entrance)
            customerNode = new Node(gridX, gridY, null, 0, 0, false, false, false);
            System.out.println("DisplayUnit " + getClass().getSimpleName() + " at (" + getX() + ", " + getY() + 
                             ") created virtual node at grid(" + gridX + ", " + gridY + ")");
        }
    }*/
}


