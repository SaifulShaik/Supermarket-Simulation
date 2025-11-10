import greenfoot.*; 
import java.util.*;

/**
 * Pathfinding system for customers
 * Can be used in any way
 * Has one main role: finds the shortest distance from point a to b inside a store
 *
 * @author Joe Zhuo
 * @version November 2025
 */
public class Pathfinder
{
    private Store store;
    
    public Pathfinder(Store store) {
        this.store = store;
    }
    
    /**
     * Finds the shortest path from start point (sx, sy) to target point (tx, ty)
     * 
     * @return a list of nodes representing the path or empty if no paths
     */
    public List<Node> findPath(int sx, int sy, int tx, int ty) {
        PriorityQueue<Node> nodesToExplore = new PriorityQueue<>(Comparator.comparingInt(Node::getTotalDistance));
        List<Node> exploredNodes = new ArrayList<>();
        
        Node startingNode = store.getNode(sx, sy);
        
        startingNode.previousNode = null;
        startingNode.setDistanceFromStart(0);
        startingNode.setDistanceToGoal(estimateDistance(sx, sy, tx, ty));
        
        nodesToExplore.add(startingNode);
        
        while (!nodesToExplore.isEmpty()) {
            Node currentNode = nodesToExplore.poll();
            
            if (currentNode.getX() == tx && currentNode.getY() == ty) {
                return reconstructPath(currentNode);
            }
            
            exploredNodes.add(currentNode);
            
            for (int[] dir : directions()) {
                // neighbouring node coordinates
                int nx = currentNode.getX() + dir[0];
                int ny = currentNode.getY() + dir[1];
                
                // cannot continue if the neighbouring node's location is not in the store
                if (!store.isInBounds(nx, ny)) continue;
                
                // creates neighbouring node
                Node neighbour = store.getNode(nx, ny);
                
                // Don't explore blocked nodes, but DO allow entrance nodes for pathfinding
                if (neighbour == null || exploredNodes.contains(neighbour) || neighbour.checkIsBlocked()) {
                    continue;
                }
                
                int tentativeDist = currentNode.getDistanceFromStart() + 1;
                
                // skip this neighbouring node if a better/equal path already exists
                if (!nodesToExplore.contains(neighbour) || tentativeDist < neighbour.getDistanceFromStart()) {
                    neighbour.previousNode = currentNode;
                    neighbour.setDistanceFromStart(tentativeDist);
                    neighbour.setDistanceToGoal(estimateDistance(nx, ny, tx, ty));
                    
                    if (!nodesToExplore.contains(neighbour)) {
                        nodesToExplore.add(neighbour);
                    }
                }

            }
        }

        return new ArrayList<>();
    }
    
    private List<Node> reconstructPath(Node targetNode) {
        List<Node> path = new ArrayList<>();
        
        Node current = targetNode;
        while (current != null && current.getPreviousNode() != null) {
            path.add(current);
            current = current.getPreviousNode();
        }
        Collections.reverse(path);
        
        // Remove the first node (starting position) if it exists
        if (!path.isEmpty() && path.get(0).getPreviousNode() == null) {
            path.remove(0);
        }
        
        return path;
    }
    
    private int[][] directions() {
        return new int[][] { {0, -1}, {0, 1}, {-1, 0}, {1, 0} };
    }
    
    private int estimateDistance(int x, int y, int targetX, int targetY) {
        return Math.abs(targetX - x) + Math.abs(targetY - y);
    }
}
