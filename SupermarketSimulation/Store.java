import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;
/**
 * @author Joe and Saiful
 * @version nov 11, 2025
 */
public class Store {
    String name;
    
    private List<Product> availableProducts;
    
    private List<Node> nodes;
    
    private boolean nodesVisible = true;
    
    private List<NodeMarker> nodeMarkers = new ArrayList<>();
    
    public Store(String name) {
        this.name = name;
        this.nodes = new ArrayList<>();
        initializeNodes();
    }

    public void showNodesInWorld(World world) {
        if (world == null) return;
        hideNodesFromWorld(world);

        if (!nodesVisible) return;

        for (Node n : nodes) {
            NodeMarker marker = new NodeMarker(n);
            world.addObject(marker, n.getX(), n.getY());
            nodeMarkers.add(marker);
        }
    }

    public void hideNodesFromWorld(World world) {
        if (world == null) return;
        for (NodeMarker m : new ArrayList<>(nodeMarkers)) {
            if (m.getWorld() != null) world.removeObject(m);
        }
        nodeMarkers.clear();
    }

    public void setNodesVisible(boolean visible, World world) {
        this.nodesVisible = visible;
        if (visible) showNodesInWorld(world);
        else hideNodesFromWorld(world);
    }
    
    public void initializeNodes() {
        if (name.equals("Store 1")) {
            Node n1 = new Node(425, 450);
            n1.setEntrance();
            Node n2 = new Node(425, 335);
            
            Node n3 = new Node(350, 335);
            Node n4a = new Node(275, 335);
            Node n5a = new Node(200, 335);
            Node n6a = new Node(125, 335);
            Node n7a = new Node(50, 335);
            
            Node n4b = new Node(350, 250);
            Node n5b = new Node(275, 250);
            Node n6b = new Node(200, 250);
            Node n7b = new Node(125, 250);
            Node n8b = new Node(50, 250);
            
            Node n9 = new Node(50, 450);
            
            Node n10 = new Node(125, 450);
            Node n11 = new Node(250, 450);
            Node n12 = new Node(350, 450);
            
            n1.addNeighbouringNode(n2);
            n2.addNeighbouringNode(n3);
            n3.addNeighbouringNode(n4a);
            n3.addNeighbouringNode(n4b);
            
            n4a.addNeighbouringNode(n5a);
            n4a.addNeighbouringNode(n3);
            
            n5a.addNeighbouringNode(n6a);
            n5a.addNeighbouringNode(n6b);
            n5a.addNeighbouringNode(n4a);
            
            n6a.addNeighbouringNode(n7a);
            n6a.addNeighbouringNode(n5a);
            
            n7a.addNeighbouringNode(n6a);
            n7a.addNeighbouringNode(n8b);
            
            n4b.addNeighbouringNode(n5b);
            n4b.addNeighbouringNode(n3);
            
            n5b.addNeighbouringNode(n6b);
            n5b.addNeighbouringNode(n4b);
            
            n6b.addNeighbouringNode(n7b);
            n6b.addNeighbouringNode(n5b);
            n6b.addNeighbouringNode(n5a);
            
            n7b.addNeighbouringNode(n8b);
            n7b.addNeighbouringNode(n6b);
            
            n8b.addNeighbouringNode(n7b);
            n8b.addNeighbouringNode(n7a);
            
            n9.addNeighbouringNode(n7a);
            n10.addNeighbouringNode(n6a);
            n11.addNeighbouringNode(n5a);
            n12.addNeighbouringNode(n4a);
            
            nodes.add(n1);
            nodes.add(n2);
            nodes.add(n3);
            nodes.add(n4a);
            nodes.add(n5a);
            nodes.add(n6a);
            nodes.add(n7a);
            
            nodes.add(n4b);
            nodes.add(n5b);
            nodes.add(n6b);
            nodes.add(n7b);
            nodes.add(n8b);
            
            nodes.add(n9);
            nodes.add(n10);
            nodes.add(n11);
            nodes.add(n12);
        } else if(name.equals("Store 2")) {
            Node n1 = new Node(750, 425);
            Node n2a = new Node(825, 425);
            Node n3a = new Node(925, 425);
            Node n4a = new Node(1000, 425);
            Node n5a = new Node(1075, 425);
            
            Node n1b = new Node(750, 325);
            Node n2b = new Node(825, 325);
            Node n3b = new Node(925, 325);
            Node n4b = new Node(1000,325);
            Node n5b = new Node(1075, 325);
            
            n1.setEntrance();
            
            n1.addNeighbouringNode(n2a);
            n1.addNeighbouringNode(n1b);
            n2a.addNeighbouringNode(n3a);
            n2a.addNeighbouringNode(n1);
            n3a.addNeighbouringNode(n4a);
            n3a.addNeighbouringNode(n2a);
            n3a.addNeighbouringNode(n3b);
            n4a.addNeighbouringNode(n5a);
            n4a.addNeighbouringNode(n3a);
            n5a.addNeighbouringNode(n5b);
            n5a.addNeighbouringNode(n4a);
            
            n1b.addNeighbouringNode(n2b);
            n1b.addNeighbouringNode(n1);
            n2b.addNeighbouringNode(n3b);
            n2b.addNeighbouringNode(n1b);
            n3b.addNeighbouringNode(n4b);
            n3b.addNeighbouringNode(n2b);
            n3b.addNeighbouringNode(n3a);
            n4b.addNeighbouringNode(n5b);
            n4b.addNeighbouringNode(n3b);
            n5b.addNeighbouringNode(n5a);
            n5b.addNeighbouringNode(n4b);
            
            nodes.add(n1);
            nodes.add(n2a);
            nodes.add(n3a);
            nodes.add(n4a);
            nodes.add(n5a);
            nodes.add(n1b);
            nodes.add(n2b);
            nodes.add(n3b);
            nodes.add(n4b);
            nodes.add(n5b);
        } else {
            System.out.println("Not Valid Store.");
        }
    }
    
    public Node getEntranceNode() { 
        for (Node n : nodes) {
            if (n.checkIsEntrance()) {
                return n;
            }
        }
        return null;
    }
    
    public Node getNode(int x, int y) {
        for (Node n : nodes) {
            if (n.getX() == x && n.getY() == y) {
                return n;
            }
        }
        return null;
    }

    /**
     * Return the list of nodes that belong to this store. Used by the editor
     * to prevent placing objects on customer paths.
     */
    public List<Node> getNodes() {
        return nodes;
    }
}