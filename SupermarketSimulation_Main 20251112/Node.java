import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * Node system 
 * represents one tile that is part of a path
 * has a link to the previous nodes of the path
 * 
 * @author Joe Zhuo
 * @version November 2025
 */
public class Node
{
    private int worldX;
    private int worldY;
    
    private List<Node> neighbouringNodes;

    private boolean isEntrance;
    private boolean hasCustomer;
    
    public Node(int x, int y) {
        worldX = x;
        worldY = y;
        isEntrance = false;
        hasCustomer = false;
        neighbouringNodes = new ArrayList<>();
    }
    
    public void addNeighbouringNode(Node n) {
        neighbouringNodes.add(n);
    }
    
    public int getX() {
        return worldX;
    }
    
    public int getY() {
        return worldY;
    }
    
    public boolean checkIsEntrance() {
        return isEntrance;
    }
    
    public void setEntrance() {
        isEntrance = true;
    }
    
    public boolean checkHasCustomer() {
        return hasCustomer;
    }
    
    public List<Node> getNeighbouringNodes() {
        return neighbouringNodes;
    }
}
