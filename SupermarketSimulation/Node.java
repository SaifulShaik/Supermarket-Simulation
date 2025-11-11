import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

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
    
    public Node previousNode;
    
    private int distanceFromStart;
    private int distanceToGoal;
    
    private boolean isBlocked;
    private boolean isEntrance;
    private boolean hasCustomer;
    
    public Node(int x, int y, Node previousNode, int distanceFromStart, int distanceToGoal, boolean isBlocked, boolean isEntrance, boolean hasCustomer) {
        worldX = x;
        worldY = y;
        this.previousNode = previousNode;
        this.distanceFromStart = distanceFromStart;
        this.distanceToGoal = distanceToGoal;
        this.isBlocked = isBlocked;
        this.isEntrance = isEntrance;
    }
    
    public Node(int x, int y) {
        worldX = x;
        worldY = y;
        previousNode = null;
        distanceFromStart = 0;
        distanceToGoal = 0;
        isBlocked = false;
        isEntrance = false;
        hasCustomer = false;
    }
    
    public int getTotalDistance() {
        return distanceFromStart + distanceToGoal;
    }
    
    public int getDistanceFromGoal() {
        return distanceToGoal;
    }
    
    public void setDistanceToGoal(int amount) {
        distanceToGoal = amount;
    }
    
    public int getDistanceFromStart() {
        return distanceFromStart;
    }
    
    public void setDistanceFromStart(int amount) {
        distanceFromStart = amount;
    }
    
    public Node getPreviousNode() {
        return previousNode;
    }
    
    public int getX() {
        return worldX;
    }
    
    public int getY() {
        return worldY;
    }
    
    public boolean checkIsBlocked() {
        return isBlocked;
    }
    
    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }
    
    public boolean checkIsEntrance() {
        return isEntrance;
    }
    
    public void setEntrance(boolean entrance) {
        isEntrance = entrance;
    }
    
    public boolean checkHasCustomer() {
        return hasCustomer;
    }
}
