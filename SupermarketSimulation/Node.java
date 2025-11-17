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
    private boolean isExit;
    private boolean isEnd;
    
    public Node(int x, int y) {
        this(x, y, false, false, false);
    }
    
    public Node(int x, int y, boolean isEntrance, boolean isExit, boolean isEnd) {
        worldX = x;
        worldY = y;
        this.isEntrance = isEntrance;
        this.hasCustomer = false;
        this.isExit = isExit;
        this.isEnd = isEnd;
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
    
    public boolean checkIsEnd() {
        return isEnd;
    }
    
    public boolean checkIsEntrance() {
        return isEntrance;
    }
    
    public boolean checkIsExit() {
        return isExit;
    }
    
    public boolean checkHasCustomer() {
        return hasCustomer;
    }
    
    public List<Node> getNeighbouringNodes() {
        return neighbouringNodes;
    }
}
