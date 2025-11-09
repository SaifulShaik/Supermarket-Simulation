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
    private int x;
    private int y;
    
    public Node previousNode;
    
    private int distanceFromStart;
    private int distanceToGoal;
    
    private boolean isBlocked;
    private boolean isEntrance;
    
    public Node(int x, int y, Node previousNode, int distanceFromStart, int distanceToGoal, boolean isBlocked, boolean isEntrance) {
        this.x = x;
        this.y = y;
        this.previousNode = previousNode;
        this.distanceFromStart = distanceFromStart;
        this.distanceToGoal = distanceToGoal;
        this.isBlocked = isBlocked;
        this.isEntrance = isEntrance;
    }
    
    public int getTotalDistance() {
        return distanceFromStart + distanceToGoal;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        
        if (!(obj instanceof Node)) return false;
        
        Node other = (Node) obj;
        return this.x == other.x && this.y == other.y;
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
        return x;
    }
    
    public int getY() {
        return y;
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
    
    public int getWorldX() {
        return x * 20 + 20 / 2;
    }
    
    public int getWorldY() {
        return y * 20 + 20 / 2;
    }
}
