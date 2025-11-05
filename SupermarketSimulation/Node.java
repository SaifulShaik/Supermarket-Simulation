import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

public class Node
{
    private int x;
    private int y;
    
    public Node previousNode;
    
    public int distanceFromStart;
    public int distanceToGoal;
    
    public Node(int x, int y, Node previousNode, int distanceFromStart, int distanceToGoal) {
        this.x = x;
        this.y = y;
        this.previousNode = previousNode;
        this.distanceFromStart = distanceFromStart;
        this.distanceToGoal = distanceToGoal;
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
    
    public int getDistanceFromStart() {
        return distanceFromStart;
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
}
