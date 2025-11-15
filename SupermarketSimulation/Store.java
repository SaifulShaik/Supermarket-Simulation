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
    
    /**
     * Store constructor
     * 
     * @param name store name (Can be either "Store 1" for left store or "Store 2" for right store)
     */
    public Store(String name) {
        this.name = name;
        this.nodes = new ArrayList<>();
        initializeNodes();
    }

    /**
     * Method to make all nodes visible 
     * 
     * @param world world to make all nodes visible in
     */
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

    /**
     * Method to hide nodes from a world
     * 
     * @param world world to be hidden from
     */
    public void hideNodesFromWorld(World world) {
        if (world == null) return;
        for (NodeMarker m : new ArrayList<>(nodeMarkers)) {
            if (m.getWorld() != null) world.removeObject(m);
        }
        nodeMarkers.clear();
    }

    /**
     * Method to make a node visible in a world
     * 
     * @param visible whether the node should be visible
     * @param world world the node should be added to
     */
    public void setNodesVisible(boolean visible, World world) {
        this.nodesVisible = visible;
        if (visible) showNodesInWorld(world);
        else hideNodesFromWorld(world);
    }
    
    /**
     * Method that adds all the hard-coded nodes to the world and initializes the neighbouring system used for pathfinding
     */
    public void initializeNodes() {
        if (name.equals("Store 1")) {
            Node n1 = new Node(425, 400); // entrance node
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
            
            // exit nodes
            Node n1a = new Node(425, 450);
            Node n11 = new Node(200, 450);
            Node n12 = new Node(275, 450);
            
            // handles node neighbouring
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
            
            n11.addNeighbouringNode(n5a);
            n12.addNeighbouringNode(n4a);
            n12.addNeighbouringNode(n1a);
            
            // adds nodes to list
            nodes.add(n1);
            nodes.add(n1a);
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
            
            nodes.add(n11);
            nodes.add(n12);
        } 
        else if(name.equals("Store 2")) {
            Node n1 = new Node(750, 400); // entrance node
            n1.setEntrance();
            Node n1e = new Node(750, 240); // exit node
            Node n2a = new Node(825, 400);
            Node n2e = new Node(825, 240); // exit node
            Node n3a = new Node(925, 400);
            Node n4a = new Node(1000, 400);
            Node n5a = new Node(1075, 400);
            
            Node n1b = new Node(750, 325);
            Node n2b = new Node(825, 325);
            Node n3b = new Node(925, 325);
            Node n4b = new Node(1000,325);
            Node n5b = new Node(1075, 325);
            
            // handles neighbouring nodes
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
            
            n2e.addNeighbouringNode(n1e);
            
            // adds nodes to list
            nodes.add(n2e);
            nodes.add(n1);
            nodes.add(n1e);
            nodes.add(n2a);
            nodes.add(n3a);
            nodes.add(n4a);
            nodes.add(n5a);
            nodes.add(n1b);
            nodes.add(n2b);
            nodes.add(n3b);
            nodes.add(n4b);
            nodes.add(n5b);
        }
    }
    
    /**
     * Method that gets all the nodes in the store
     * 
     * @return list of all ndoes
     */
    public List<Node> getNodes() {
        return nodes;
    }
    
    /**
     * Method that gets all the available products in the store
     * 
     * @return list of available products
     */
    public List<Product> getAvailableProducts() {
        return availableProducts;
    }
    
    /**
     * Method that gets the entrance node to the store
     * 
     * @return Node the entrance node
     */
    public Node getEntranceNode() { 
        for (Node n : nodes) {
            if (n.checkIsEntrance()) {
                return n;
            }
        }
        return null;
    }
}

