import java.util.LinkedList;
import java.util.List;

/**
 * Product class
 * represents a purchasable item inside a store
 * 
 * @author Owen Kung
 * @version Nov 2025
 */

public abstract class Product extends SuperSmoothMover
{
    protected double price;
    protected boolean isLocked;
    protected boolean isDiscounted=false;
    protected String name;
    protected Node node;
    protected DisplayUnit displayUnit; // Reference to the DisplayUnit this product belongs to
    /**
     * Linked list to store Node objects that should be considered "in front" of
     * this product's image (for example, overlay nodes or markers). Subclasses
     * such as `Orange` inherit this storage and helper APIs.
     */
    protected LinkedList<Node> nodeList;
    
    public Product() 
    {
    // Set default price (can be overridden by subclasses)
    // Use Math.random() to avoid a hard dependency on Greenfoot during compilation
    price = 2.0 + (int)(Math.random() * 6); // $2 - $7
    // initialize the node list used to store nodes that will be rendered
    // or tracked in front of this product's image
    nodeList = new LinkedList<>();
    }
    public String getName()
    {
        return name;
    }
    public double getPrice() { 
        return price; 
    }
    protected void applyDiscount(double percent) {
        price *= (1 - (percent / 100));
        isDiscounted=true;
    }
    @Override
    public String toString()
    {
        String result=name+":" ;
        if(isDiscounted)
        {
            result=result+".  Discounted to-$"+price;
        }
        else
        {
            result=result+ " Price -$" + price;
        }
        return result;
    }
    protected abstract int getStock() ;    
    protected abstract void setStock(int amount);
    
    protected void unlock() {
        
    }
    
    public void setNode(Node node)
    {   
        this.node=node;
        
    }
    public Node getNode()
    {   
        return node;
        
    }
    
    /**
     * Set the DisplayUnit this product belongs to.
     */
    public void setDisplayUnit(DisplayUnit unit) {
        this.displayUnit = unit;
    }
    
    /**
     * Get the DisplayUnit this product belongs to.
     */
    public DisplayUnit getDisplayUnit() {
        return displayUnit;
    }

    /* LinkedList helper methods */
    /** Add a node to the end of the list. */
    public void addNode(Node n) {
        if (n == null) return;
        nodeList.add(n);
    }

    /** Add a node to the front of the list (first position). */
    public void addNodeToFront(Node n) {
        if (n == null) return;
        nodeList.addFirst(n);
    }

    /** Remove a node from the list. Returns true if removed. */
    public boolean removeNode(Node n) {
        if (n == null) return false;
        return nodeList.remove(n);
    }

    /** Clear all stored nodes. */
    public void clearNodes() {
        nodeList.clear();
    }

    /** Return the list of nodes (modifiable). */
    public List<Node> getNodes() {
        return nodeList;
    }
}


