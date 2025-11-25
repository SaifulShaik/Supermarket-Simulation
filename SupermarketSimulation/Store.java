import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;
/**
 * Represents the stores in the simulation with nodes for customer navigation,
 * display units for products, and cashiers for checkout. 
 * Tracks profit, customer statistics, and handles store settings like discounts and ratings.
 * Manages both stores
 * 
 * @author Joe, Saiful, and Owen Lee
 * @version November 11, 2025
 */
public class Store {
    String name;
    
    private List<Class<? extends Product>> availableProductTypes;
    private List<DisplayUnit> availableDisplayUnits;
    private List<Cashier> cashiers;
    
    private List<Node> nodes;
    private boolean nodesVisible = false;
    private List<NodeMarker> nodeMarkers = new ArrayList<>();
    
    private double profit;
    private double storeDiscount = 0.0; // Percentage discount (0-100)
    private double baseRating = 3.0; // Starting rating (1.0-5.0)
    
    /**
     * Store constructor
     * 
     * @param name store name (Can be either "Store 1" for left store or "Store 2" for right store)
     */
    public Store(String name) {
        this.name = name;
        this.profit = 0;
        
        this.nodes = new ArrayList<>();
        this.cashiers = new ArrayList<>();
        this.availableDisplayUnits = new ArrayList<>();
        this.availableProductTypes = new ArrayList<>();
        
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
        // Left store
        if (name.equals("Store 1")) {
            Node n1 = new Node(425, 400, true, false, false); // entrance node
            Node n2 = new Node(425, 335); // 
            
            Node n3 = new Node(350, 335); // bottom right
            Node n4a = new Node(275, 335); // 2 right bottom
            Node n5a = new Node(200, 335); // mid bottom
            Node n6a = new Node(125, 335); // 2 bottom left
            Node n7a = new Node(50, 335); // bottom left
            
            Node n4b = new Node(350, 250); // upper right
            Node n5b = new Node(275, 250); // 2 upper right
            Node n6b = new Node(200, 250); // mid top
            Node n7b = new Node(125, 250); // 2 upper left
            Node n8b = new Node(50, 250); // upper left
            
            // exit nodes
            Node n1a = new Node(425, 450, false, true, false);
            Node n11 = new Node(200, 450, false, true, false);
            Node n12 = new Node(275, 450, false, true, false);
            Node n13 = new Node(550, 400, false, true, false);
            Node n14 = new Node(550, 500, false, true, true);
            
            // extra nodes to prevent placement of display units
            Node e0 = new Node(50, 425);
            
            // handles node neighbouring
            n11.addNeighbouringNode(n12);
            n12.addNeighbouringNode(n1a);
            n1a.addNeighbouringNode(n1);
            n1.addNeighbouringNode(n13);
            n13.addNeighbouringNode(n14);
            
            n1.addNeighbouringNode(n2);
            n2.addNeighbouringNode(n3);
            n3.addNeighbouringNode(n4a);
            n3.addNeighbouringNode(n4b);
            
            n4a.addNeighbouringNode(n5a);
            n4a.addNeighbouringNode(n3);
            n4a.addNeighbouringNode(n12);
            
            n5a.addNeighbouringNode(n6a);
            n5a.addNeighbouringNode(n6b);
            n5a.addNeighbouringNode(n4a);
            n5a.addNeighbouringNode(n11);
            
            n6a.addNeighbouringNode(n7a);
            n6a.addNeighbouringNode(n5a);
            
            n7a.addNeighbouringNode(n6a);
            n7a.addNeighbouringNode(n8b);
            
            n4b.addNeighbouringNode(n5b);
            n4b.addNeighbouringNode(n3);
            
            n5b.addNeighbouringNode(n6b);
            n5b.addNeighbouringNode(n4b);
            //n5b.addNeighbouringNode(n4a);
            
            n6b.addNeighbouringNode(n7b);
            n6b.addNeighbouringNode(n5b);
            n6b.addNeighbouringNode(n5a);
            
            n7b.addNeighbouringNode(n8b);
            n7b.addNeighbouringNode(n6b);
            //n7b.addNeighbouringNode(n6a);
            
            n8b.addNeighbouringNode(n7b);
            n8b.addNeighbouringNode(n7a);
            
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
            nodes.add(n13);
            nodes.add(n14);
            nodes.add(e0);
        } 
        else if (name.equals("Store 2")) {
            Node n1 = new Node(750, 400, true, false, false); // entrance node
            Node n1e = new Node(750, 240, false, true, false); // exit node
            Node n2a = new Node(825, 400);
            Node n2e = new Node(825, 240, false, true, false); // exit node
            Node n3a = new Node(925, 400);
            Node n4a = new Node(1000, 400);
            Node n5a = new Node(1075, 400);
            
            Node n1b = new Node(750, 325);
            Node n2b = new Node(825, 325);
            Node n3b = new Node(925, 325);
            Node n4b = new Node(1000,325);
            Node n5b = new Node(1075, 325);
            
            Node n6 = new Node(650, 240, false, true, false);
            Node n7 = new Node(650, 500, false, true, true); 
            
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
            n2b.addNeighbouringNode(n2e);
            n2b.addNeighbouringNode(n1b);
            n3b.addNeighbouringNode(n4b);
            n3b.addNeighbouringNode(n2b);
            n3b.addNeighbouringNode(n3a);
            n4b.addNeighbouringNode(n5b);
            n4b.addNeighbouringNode(n3b);
            n5b.addNeighbouringNode(n5a);
            n5b.addNeighbouringNode(n4b);
            
            n2e.addNeighbouringNode(n1e);
            n1e.addNeighbouringNode(n6);
            n6.addNeighbouringNode(n7);
            
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
            nodes.add(n6);
            nodes.add(n7);
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
    public List<Class<? extends Product>> getAvailableProducts() {
        List<Class<? extends Product>> availableProductTypes = new ArrayList<>();
        
        for (DisplayUnit u : availableDisplayUnits) {
            for (Product p : u.getStockedItems()) {
                availableProductTypes.add(p.getClass());
            }
        }
        
        return availableProductTypes;
    }
    
    /**
     * adds an available product class to the store's available products
     * 
     * @param c class of the product to be added
     */
    public void addAvailableProductTypes(Class c) {
        availableProductTypes.add(c);
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
    
    /**
     * gets a node at a specific x and y location
     */
    public Node getNode(int x, int y) {
        for (Node n : nodes) {
            if (n.getX() == x && n.getY() == y) {
                return n;
            }
        }
        return null;
    }
    
    public boolean ownsNode(Node n) {
        if (nodes.contains(n)) return true;
        return false;
    }
    
    public void addDisplayUnit(DisplayUnit d) {
        availableDisplayUnits.add(d);
    }
    
    public void addCashier(Cashier c) {
        if (cashiers == null) cashiers = new ArrayList<>();
        cashiers.add(c);
    }
    
    public List<Cashier> getCashiers() {
        return cashiers;
    }
    
    public List<DisplayUnit> getAvailableDisplayUnits() {
        return availableDisplayUnits;
    }
    
    public void addProfit(double amount) {
        profit += amount;
    }
    
    public double getProfit() {
        return profit;
    }

    public boolean isInStore(int x, int y) {
        if (name.equals("Store 1")) {
            // Left store boundaries (blue/gray store)
            // fillRect(25, 150, 450, 350) means x: 25 to 475, y: 150 to 500
            return x >= 25 && x <= 475 && y >= 150 && y <= 500;
        } else if (name.equals("Store 2")) {
            // Right store boundaries (wooden store)
            // fillRect(725, 150, 370, 300) means x: 725 to 1095, y: 150 to 450
            return x >= 725 && x <= 1095 && y >= 150 && y <= 450;
        }
        return false;
    }
    
    /**
     * Prepares the store for a new day of operation.
     * 
     * This method creates a list of all product types that can be selected
     * for a daily sale event and then calls the SaleManager to randomly
     * choose one of them. The world reference is passed so that the sale
     * visuals or effects can be displayed in the correct world.
     *
     * @param world the world in which the sale event should be created
     */
    public void startNewDay(World world) {
        List<Class<? extends Product>> allTypes = new ArrayList<>();
        allTypes.add(Coke.class);
        allTypes.add(Doritos.class);
        allTypes.add(XingRamen.class);
        allTypes.add(Lettuce.class);
        allTypes.add(Carrot.class);
        allTypes.add(Candy.class);
        allTypes.add(DrumStick.class);
        allTypes.add(Apple.class);
    
        // Pass the world reference to the SaleManager
        SaleManager.chooseRandomSale(allTypes, world);
    }
    
    /**
     * Returns the store number based on this store's name.
     * For example, "Store 1" returns 1 and "Store 2" returns 2.
     *
     * @return the store number if recognized, or -1 if the name is not a known format
     */
    public int getStoreNumber() {
        if ("Store 1".equals(name)) {
            return 1;
        } else if ("Store 2".equals(name)) {
            return 2;
        }
        return -1;
    }
    
    public void increaseCountNumOfBargainShoppers(){ numOfBargainShoppers++; };
    public void increaseCountNumOfBulkShoppers(){ numOfBulkShoppers++; };
    public void increaseCountNumOfImpulseShoppers(){ numOfImpulseShoppers++; };
    public void increaseCountNumOfRegularShoppers(){ numOfRegularShoppers++; };
    public void increaseCountNumOfProductsSold(){ numOfProductsSold++; };
    public void increaseCountNumOfZombies(){ numOfZombies++; };
    public void increaseCountNumOfFires(){ numOfFires++; };
    public void increaseCountNumOfStorms(){ numOfStorms++; };
    
    
    /**
     * Gets the current store discount percentage.
     * 
     * @return the discount percentage
     */
    public double getStoreDiscount() {
        return storeDiscount;
    }
    
    /**
     * Sets the store discount percentage.
     * Value is between 0 and 100.
     * 
     * @param discount the discount percentage to set
     */
    public void setStoreDiscount(double discount) {
        this.storeDiscount = Math.max(0, Math.min(100, discount));
    }
    
    /**
     * Gets the base rating for the store.
     * 
     * @return the base rating (1.0-5.0)
     */
    public double getBaseRating() {
        return baseRating;
    }
    
    /**
     * Sets the base rating for the store.
     * Value is between 1.0 and 5.0.
     * 
     * @param rating the base rating to set
     */
    public void setBaseRating(double rating) {
        this.baseRating = Math.max(1.0, Math.min(5.0, rating));
    }
    
    /**
     * Resets all store statistics and profit to their initial values.
     * Should be called when restarting the simulation.
     */
    public void resetStore() {
        this.profit = 0.0;
        this.numOfBargainShoppers = 0;
        this.numOfBulkShoppers = 0;
        this.numOfImpulseShoppers = 0;
        this.numOfRegularShoppers = 0;
        this.numOfProductsSold = 0;
        this.numOfZombies = 0;
        this.numOfFires = 0;
        this.numOfStorms = 0;
    }
}

