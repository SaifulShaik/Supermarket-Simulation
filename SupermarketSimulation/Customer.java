import greenfoot.*; 
import java.util.*;

/**
 * Abstract Customer class
 *
 * @author Joe and saiful
 * @version November 2025
 */

public abstract class Customer extends SuperSmoothMover 
{
    private double movementSpeed;
    private double budget;
    
    protected List<Class<? extends Product>> shoppingList;
    protected List<Product> cart;
    
    protected List<Node> path;
    protected Cashier targetCashier;
    protected boolean hasCheckedOut;
    
    protected Node previousNode;
    protected Node currentNode;
    protected Node targetNode;
    
    protected int pauseTimer = 0;
    
    protected Store store;
    
    protected int currentActCycles;
    protected int maxActCycles;
    
    public Customer() {
        this(2.0, 100.0, null, 3, 2, 100);
    }
    
    /**
     * Customer constructor
     * 
     * @param movementSpeed movement speed of the customer
     * @param budget budget of the customer
     * @param currentNode default node the customer spawns at
     * @param minShoppingListItems minimum number of shopping list items
     * @param maxAdditionalRandomItems maximum number of additional shopping list items
     */
    public Customer(double movementSpeed, double budget, Node currentNode, int minShoppingListItems, int maxAdditionalRandomItems, int maxActCycles) {
        this.movementSpeed = movementSpeed;
        this.budget = budget;
        
        this.currentNode = currentNode;
        targetNode = null;
        previousNode = null;
        
        this.currentActCycles = 0;
        this.maxActCycles = maxActCycles;
        
        store = null;
        
        hasCheckedOut = false;
        
        shoppingList = new ArrayList<>();
        shoppingList = generateShoppingList(minShoppingListItems, maxAdditionalRandomItems);
        cart = new ArrayList();
    }
    
    /**
     * Main act loop
     * first chooses store if not done that
     * then moves around the store and browses products
     * finally checks out once all products are purchased
     */
    public void act() {
        currentActCycles++;
        
        // move to target node if already set
        if (targetNode != null) {
            moveToNode(targetNode);
            return;
        }
        
        // move to store entrance access node
        if (SimulationWorld.getStartNode().equals(currentNode)) {
            move(false);
            return;
        }
        
        // choose store
        if (store == null) {
            //System.out.println("[Customer] choosing store");
            chooseStore();
            return;
        }
        
        // take items while walking around if shopping list items aren't collected yet
        if (!shoppingList.isEmpty() && currentActCycles < maxActCycles) {
            //System.out.println("[Customer] walking around");
            retrieveProdcuts(); 
            move(false);
            return;
        }
        
        // check out if the shopping list items are all collected
        if (!hasCheckedOut) {
            // chooses cashier first
            if (targetCashier == null) {
                //System.out.println("[Customer] choosing cashier");
                chooseCashier();
            }
            // then moves to cashier
            else {
                //System.out.println("[Customer] moving to cashier");
                moveToCashier();
            }
            return;
        }
        
        
        // leaves the store if everything has been done
        //System.out.println("[Customer] leaving store");
        leaveStore();
    }
    
    /**
     * Method to choose a store to go into
     */
    protected void chooseStore() {
        // initializes shopping lists
        List<Class<? extends Product>> storeOneShoppingList = new ArrayList<>();
        List<Class<? extends Product>> storeTwoShoppingList = new ArrayList<>();
        
        // checks if product is in store one or store two
        for (Class<? extends Product> productClass : shoppingList) {
            boolean inStoreOne = false;
            
            // checks if the product class belongs in the store one's available products
            for (Class<? extends Product> c : SimulationWorld.storeOne.getAvailableProducts()) {
                if (c == productClass) {
                    inStoreOne = true;
                    break;
                }
            }
            
            // then checks if the product class belongs in the store two's available products
            boolean inStoreTwo = false;
            for (Class<? extends Product> p : SimulationWorld.storeTwo.getAvailableProducts()) {
                if (p == productClass) {
                    inStoreTwo = true;
                    break;
                }
            }
    
            // adds the product to the store based shopping lists
            if (inStoreOne) storeOneShoppingList.add(productClass);
            if (inStoreTwo) storeTwoShoppingList.add(productClass);
        }
        
        // compares store based shopping lists and goes to the store with more available items
        if (storeOneShoppingList.size() > storeTwoShoppingList.size()) {
            store = SimulationWorld.storeOne;
            shoppingList = storeOneShoppingList;
        }
        else if (storeOneShoppingList.size() < storeTwoShoppingList.size()){
            store = SimulationWorld.storeTwo;
            shoppingList = storeTwoShoppingList;
        }
        // if both lists are the same size or are both empty
        else {
            // both lists are empty
            if (storeOneShoppingList.isEmpty() && storeTwoShoppingList.isEmpty()) {
                store = SimulationWorld.storeOne; // defaults to store one 
            } 
            // both lists are the same size
            else {
                int chosenStore = Greenfoot.getRandomNumber(2);
                store = (chosenStore == 0) ? SimulationWorld.storeOne : SimulationWorld.storeTwo;
                shoppingList = store == SimulationWorld.storeOne ? storeOneShoppingList : storeTwoShoppingList;
            }
        }

        // updates store entrance
        targetNode = store.getEntranceNode();
        System.out.println("Chose Store");
    }
    
    /**
     * Method to generate a shopping list
     * 
     * @param minShoppingListItems minimum number of shopping list items
     * @return list of products generated
     */
    protected List<Class<? extends Product>> generateShoppingList(int minShoppingListItems, int maxAdditionalRandomItems) {
        // initializes a list of items that are going to be in the cart
        List<Class<? extends Product>> items = new ArrayList<>();
        
        // initializes a list of available items
        List<Class<? extends Product>> availableItemTypes = new ArrayList<>();
        
        // adds available products from store one
        for (Class<? extends Product> p : SimulationWorld.storeOne.getAvailableProducts()) {
            availableItemTypes.add(p);
        }
        
        // adds available products from store two
        for (Class<? extends Product> p : SimulationWorld.storeTwo.getAvailableProducts()) {
            availableItemTypes.add(p);
        }
        
        // cannot have 0 or negative shopping lsit size
        if (minShoppingListItems <= 0) {
            minShoppingListItems = 1; 
        }
        
        // generate list size between 1 and maxShoppingListItems
        int numItems = minShoppingListItems + Greenfoot.getRandomNumber(maxAdditionalRandomItems + 1);
        
        // randomly selects items
        for (int i = 0; i < numItems; i++) {
            if (!availableItemTypes.isEmpty()) {
                Class<? extends Product> itemClass = availableItemTypes.get(Greenfoot.getRandomNumber(availableItemTypes.size()));
                items.add(itemClass);
            } 
        }
        
        // returns the list
        return items;
    }
    
    /**
     * Method for the customer to retrieve products
     */
    protected void retrieveProdcuts() {
        if (store == null || currentNode == null || shoppingList == null || shoppingList.isEmpty() || getWorld() == null) return;
        
        // gets the available display units in the store
        List<DisplayUnit> units = store.getAvailableDisplayUnits();
        
        // cannot get anyhting if there are no display units
        if (units == null || units.isEmpty()) return;
        
        // loops through display units
        for (DisplayUnit u : units) {
            if (u == null) continue;
            
            // gets the display unit's access nodes
            List<Node> accessNodes = u.getCustomerNodes();
            
            boolean accessMatch = false;
            
            // checks if the customer can access the display unit
            if (accessNodes != null) {
                for (Node an : accessNodes) {
                    if (an == currentNode) { 
                        accessMatch = true; 
                        break; 
                    }
                    if (currentNode.equals(an)) { 
                        accessMatch = true; 
                        break; 
                    }
                }
            }
            
            // cannot retrieve item if not in range
            if (!accessMatch) continue;
            
            // checks stocked items
            List<Product> stocked = u.getStockedItems();
            if (stocked == null || stocked.isEmpty()) continue;
            
            // take item if in shopping list
            for (Class<? extends Product> wantedClass : new ArrayList<>(shoppingList)) {
                Product retrieved = u.retrieve(wantedClass);
                if (retrieved != null) {
                    // adds to cart and removes from shopping list
                    cart.add(retrieved);
                    shoppingList.remove(wantedClass);
                    pauseTimer = 10 + Greenfoot.getRandomNumber(21); // 10-30 act delay
                    return;
                }
            }
        }
    }
    
    /**
     * Method to choose a cashier
     */
    private void chooseCashier() {
        // gets the store's cashiers
        List<Cashier> cashiers = store.getCashiers();
        
        // cannot choose if no cashiers
        if (cashiers == null || cashiers.isEmpty()) {
            return;
        }
        
        // defaults to first cashier
        Cashier best = cashiers.get(0);
        
        // loops through cashiers
        for (Cashier c : cashiers) {
            // compares queue size to the current best queue size
            if (c.getQueueSize() < best.getQueueSize()) {
                best = c;
            }
        }
        
        // updates target cashier
        targetCashier = best;
        
        // finds a path to the cashier's node
        if (path == null || path.isEmpty()) {
            path = findPath(best.getCustomerNode());
        }
    }
    
    /**
     * Method to move to cashier
     */
    private void moveToCashier() {
        Node cashierNode = targetCashier.getCustomerNode();
        if (cashierNode == null) return;
        
        if (path == null || path.isEmpty()) {
            path = findPath(cashierNode);
        }

        if (path != null && !path.isEmpty()) {
            targetNode = path.get(0);
    
            if (moveToNode(targetNode)) {
                path.remove(0);
                targetNode = null;
            }
        }

        if (path == null || path.isEmpty()) {
            targetCashier.addCustomerToQueue(this);
            targetNode = null;
        }
    }
    
    private List<Node> findPath(Node goal) {
        List<Node> result = new ArrayList<>();
    
        if (currentNode == null || goal == null) {
            return result;
        }
    
        Queue<Node> queue = new LinkedList<>();
        Map<Node, Node> cameFrom = new HashMap<>();
        Set<Node> visited = new HashSet<>();
    
        queue.add(currentNode);
        visited.add(currentNode);
        cameFrom.put(currentNode, null);
    
        while (!queue.isEmpty()) {
            Node node = queue.poll();
    
            if (node.equals(goal)) {
                Node cur = node;
                while (cur != null) {
                    result.add(0, cur); 
                    cur = cameFrom.get(cur);
                }
                return result;
            }
    
            List<Node> neighbours = node.getNeighbouringNodes();
            if (neighbours == null) continue;
    
            for (Node next : neighbours) {
                if (visited.contains(next)) continue;
                visited.add(next);
                cameFrom.put(next, node);
                queue.add(next);
            }
        }
    
        return result;
    }
    
    /**
     * Method for the customer to leave the store
     */
    public void leaveStore() {
        // cannot leave if didn't check out yet
        if (!hasCheckedOut) return;
        
        Node worldExit = SimulationWorld.getExitNode();
        
        if (currentNode.checkIsEnd()) {
            getWorld().removeObject(this);
            return;
        }
        
        move(true);
    }
    
    /**
     * Move method
     * Handles selection of target nodes
     */
    protected void move(boolean moveToExitNodes) {
        // cannot move if paused
        if (pauseTimer > 0) {
            pauseTimer--;
            return;
        }
        
        /**
        // DEBUG: print movement state
        try {
            String cur = (currentNode == null) ? "null" : currentNode.getX() + "," + currentNode.getY();
            String prev = (previousNode == null) ? "null" : previousNode.getX() + "," + previousNode.getY();
            String targ = (targetNode == null) ? "null" : targetNode.getX() + "," + targetNode.getY();
            System.out.println("[Customer Debug] move() cur=" + cur + " prev=" + prev + " targ=" + targ + " moveToExitNodes=" + moveToExitNodes);
        } catch (Exception e) {
            System.out.println("[Customer Debug] move() printing failed: " + e.getMessage());
        }
        */
        
        // no need to reselect a new target node if already moving to one
        if (targetNode != null) {
            moveToNode(targetNode); 
            return;
        }

        // gets neighbouring nodes
        List<Node> neighbouringNodes = currentNode.getNeighbouringNodes();
        
        // cannot move if no neighbouring nodes
        if (neighbouringNodes == null || neighbouringNodes.isEmpty()) {
            //System.out.println("[Customer Debug] neighbouringNodes is null/empty for currentNode=" + (currentNode==null?"null":currentNode.getX()+","+currentNode.getY()));
            return;
        }
        
        // makes available nodes
        List<Node> availableNodes = new ArrayList<>();
        
        // loops through all neighbouring nodes
        for (Node n : neighbouringNodes) {
            if (n.equals(previousNode)) continue;
            // a node is 'available' if it wasn't the previous node (prevent back and forth movement)
            if (moveToExitNodes) {
                if (n.checkIsExit()) {
                    availableNodes.add(n);
                }
            }
            else if (!n.checkIsExit()) {
                availableNodes.add(n);
            }
        }
        
        // cannot move if no available nodes
        if (availableNodes == null || availableNodes.isEmpty()) {
            // DEBUG: list neighbouring nodes and their exit flags
            try {
                //System.out.print("[Customer Debug] No available nodes. neighbouring: ");
                for (Node nn : neighbouringNodes) {
                    //System.out.print("(" + nn.getX() + "," + nn.getY() + ",exit=" + nn.checkIsExit() + ") ");
                }
                //System.out.println();
            } catch (Exception e) {}

            // Fallback: allow moving back to previous node or any neighbour to avoid deadlock
            try {
                for (Node nn : neighbouringNodes) {
                    if (nn != null) availableNodes.add(nn);
                }
                if (!availableNodes.isEmpty()) {
                    //System.out.println("[Customer Debug] Fallback: using all neighbouring nodes to continue movement.");
                } else {
                    //System.out.println("[Customer Debug] Fallback failed: still no neighbouring nodes.");
                    return;
                }
            } catch (Exception e) {
                return;
            }
        }
        
        // randomly chooses next node
        Node nextNode = availableNodes.get(Greenfoot.getRandomNumber(availableNodes.size()));
        
        // updates previous node
        previousNode = currentNode;
        
        // sets target node
        targetNode = nextNode;
        
        // start moving to next node
        moveToNode(nextNode);
    }
    
    /**
     * Method to move towards a Node
     * 
     * @param n node to move to
     * @return whether the customer has arrived at the target node
     */
    protected boolean moveToNode(Node n) {
        // Use precise coordinates (double) to avoid rounding oscillation with Actor.getX()/getY()
        double curX = (this instanceof SuperSmoothMover) ? ((SuperSmoothMover)this).getPreciseX() : getX();
        double curY = (this instanceof SuperSmoothMover) ? ((SuperSmoothMover)this).getPreciseY() : getY();
        double dx = n.getX() - curX;
        double dy = n.getY() - curY;
        double distance = Math.sqrt(dx * dx + dy * dy);
        
        // DEBUG: print distance to target
        try {
            //System.out.println("[Customer Debug] moveToNode target=" + n.getX() + "," + n.getY() + " current=" + getX() + "," + getY() + " distance=" + distance + " speed=" + movementSpeed);
        } catch (Exception e) {}
        
        // finishes movement if almost there (1 act cycle would move too much to the distance)
        // snap when within one step (use <= to be robust)
        if (distance <= movementSpeed) {
            // snaps to the target location
            setLocation(n.getX(), n.getY());
            
            // updates current and target node
            currentNode = n;
            targetNode = null;
            
            // DEBUG: arrived at node
            //System.out.println("[Customer Debug] Arrived at node " + n.getX() + "," + n.getY());
            // don't move any further
            return true;
        }
        
        // calculates angle using precise deltas
        double angle = Math.atan2(dy, dx);

        // updates x and y values using precise positions
        double newX = curX + Math.cos(angle) * movementSpeed;
        double newY = curY + Math.sin(angle) * movementSpeed;
        
        // updates location
        setLocation(newX, newY);
        
        // DEBUG: moved to intermediate pos
        //try { System.out.println("[Customer Debug] Moved to " + newX + "," + newY); } catch (Exception e) {}
        return false;
    }
    
    /**
     * Method to set the customer's check out flag to true
     */
    public void checkOut() {
        this.hasCheckedOut = true;
    }
    
    /**
     * Methods to calculate the total price of the customer's cart
     * 
     * @return a double representing the total price of the cart
     */
    public double calculatePriceOfCart() {
        double total = 0;
        
        for (Product p : cart) {
            total += p.getPrice();
        }
        
        return total;
    }
    
    public int getCartSize() {
        return cart.size();
    }
    
    public Store getStore() {
        return store;
    }
}

