import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * Node system 
 * represents one tile that is part of a path
 * has a link to the previous nodes of the path
 * 
 * @author Saiful Shaik & Joe Zhuo
 * @version November 2025
 */
public class Node
{
    private int worldX;
    private int worldY;
    
    private List<Node> neighbouringNodes;

    // types of nodes
    private boolean isEntrance;
    private boolean isExit;
    private boolean isEnd;
    
    /**
     * Default constructor to make a new node at a certain location
     * 
     * @param x - x locaiton
     * @param y - y location
     */
    public Node(int x, int y) {
        this(x, y, false, false, false);
    }
    
    /**
     * more flexible constructor to allow more advanced node creation
     * 
     * @param x - x locaiton
     * @param y - y location
     * @param isEntrance - whether the node is a store entrance node or not
     * @param isExit - whether the node is a store exit or not
     * @param isEnd - whether the node is a world exit or not
     */
    public Node(int x, int y, boolean isEntrance, boolean isExit, boolean isEnd) {
        worldX = x;
        worldY = y;
        
        this.isEntrance = isEntrance;
        this.isExit = isExit;
        this.isEnd = isEnd;
        
        neighbouringNodes = new ArrayList<>();
    }
    
    @Override
    /**
     * Overrides default comparison
     * now compares x and y location and uses a tolerance value
     * 
     * @param obj - object to compare to
     */
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
    
        // converts object to node
        Node other = (Node) obj;
        
        // tolerance
        int tolerance = 1;
    
        return Math.abs(this.worldX - other.worldX) <= tolerance &&
               Math.abs(this.worldY - other.worldY) <= tolerance;
    }
    
    /**
     * adds a node ot the list of neihgbouring nodes
     * 
     * @param n - node to add
     */
    public void addNeighbouringNode(Node n) {
        neighbouringNodes.add(n);
    }
    
    /**
     * gets x location of the node
     * 
     * @return x locaiton
     */
    public int getX() {
        return worldX;
    }
    
    /**
     * gets y location of the node
     * 
     * @return y locaiton
     */
    public int getY() {
        return worldY;
    }
    
    /**
     * checks if the node is an end node
     * 
     * @return whether the node is an end node
     */
    public boolean checkIsEnd() {
        return isEnd;
    }
    
    /**
     * checks if the node is an entrance node
     * 
     * @return whether the node is an entrance node
     */
    public boolean checkIsEntrance() {
        return isEntrance;
    }
    
    /**
     * checks if the node is an exit node
     * 
     * @return whether the node is an exit node
     */
    public boolean checkIsExit() {
        return isExit;
    }
    
    /**
     * gets all neighbouring nodes
     * 
     * @return list of neighbouring nodes
     */
    public List<Node> getNeighbouringNodes() {
        return neighbouringNodes;
    }
}
