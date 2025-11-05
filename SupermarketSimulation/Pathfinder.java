import greenfoot.*; 
import java.util.*;

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
        
        Node startingNode = new Node(sx, sy, null, 0, estimateDistance(sx, sy, tx, ty));
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
                
                // cannot continue if the neighbouring node's location is not in the store or is blocked
                if (!store.isInBounds(nx, ny) || store.isBlocked(nx, ny)) continue;
                
                // creates neighbouring node
                Node neighbour = new Node(nx, ny, currentNode, currentNode.getDistanceFromStart() + 1, estimateDistance(nx, ny, tx, ty));
                
                // dont explore the node if it has been explored already
                if (exploredNodes.contains(neighbour)) continue;
                
                boolean skip = false;
                
                // skip this neighbouring node if a better/equal path already exists
                for (Node n : nodesToExplore) {
                    if (n.equals(neighbour) && n.getDistanceFromStart() <= neighbour.getDistanceFromStart()) {
                        skip = true;
                        break;
                    }
                }
                
                // add neighbour if not skipped
                if (!skip) {
                    nodesToExplore.add(neighbour);
                }
            }
        }

        return new ArrayList<>();
    }
    
    private List<Node> reconstructPath(Node targetNode) {
        List<Node> path = new ArrayList<>();
        
        Node current = targetNode;
        while (current != null) {
            path.add(current);
            current = current.getPreviousNode();
        }
        Collections.reverse(path);
        return path;
    }
    
    private int[][] directions() {
        return new int[][] { {0, -1}, {0, 1}, {-1, 0}, {1, 0} };
    }
    
    private int estimateDistance(int x, int y, int targetX, int targetY) {
        return Math.abs(targetX - x) + Math.abs(targetY - y);
    }
}
