import greenfoot.*; 
import java.util.*;

/**
 * Abstract Customer class
 * 
 * This class represents a generic customer
 * Responsible for
 * - navigating nodes in the store
 * - choosing the best store to go to
 * - creating a shopping list
 * - taking items in the store
 * - selecting a cashier with the shortest queue
 * - leaving the store through the world exit once done
 * - handles animations in four directions
 * - pauses when collecting items and waiting in queue
 *
 * @author Saiful Shaik and Joe Zhuo
 * @version November 2025
 */

public abstract class Customer extends SuperSmoothMover 
{
    private double movementSpeed;
    protected double budget; //changed to proteced for bargainshopper 
    
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
    
    //imagea related variables
    protected GreenfootImage rightImages[]={new GreenfootImage("maleShopper/right1.png"),new GreenfootImage("maleShopper/right2.png")};
    protected GreenfootImage leftImages[]={new GreenfootImage("maleShopper/left1.png"),new GreenfootImage("maleShopper/left2.png")};
    protected GreenfootImage upImages[]={new GreenfootImage("maleShopper/up1.png"),new GreenfootImage("maleShopper/up2.png")};
    protected GreenfootImage downImages[]={new GreenfootImage("maleShopper/down1.png"),new GreenfootImage("maleShopper/down2.png")};
    protected int animCounter=0;
    protected int animSpeed=10; //the larger the slower
    protected int animIndex=0;
    protected double facingAngle=0; //for correctly display images with right direction

    // ===== Basket system variables =====
    protected Basket visualBasket; // visual basket
    protected List<Product> carriedItems;  // visual basket

    // rating related
    private int originalShoppingListSize = 0;
    
    public static int supermarketTotalProductsSold = 0;
    public static int butcherTotalProductsSold = 0;
    
    public static int supermarketTotalBargainShoppers = 0;
    public static int supermarketTotalBulkShoppers = 0;
    public static int supermarketTotalImpulseShoppers = 0;
    public static int supermarketTotalRegularShoppers = 0;
    
    public static int butcherTotalBargainShoppers = 0;
    public static int butcherTotalBulkShoppers = 0;
    public static int butcherTotalImpulseShoppers = 0;
    public static int butcherTotalRegularShoppers = 0;

    /**
     * Default customer constructor
     */
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
        //keep track of original shoppinglist
        //for rating
        originalShoppingListSize=shoppingList.size();
        cart = new ArrayList();
        
        //initialize visual basket related variables
        visualBasket = null;  //visual basket
        carriedItems = new ArrayList<>();//visual basket
    }
    
    /**
     * Main act loop
     * first chooses store if not done that
     * then moves around the store and browses products
     * finally checks out once all products are purchased
     */
    public void act() {
        // updates act cycles
        currentActCycles++;
        
        // animation when not checking out or moving to checkout
        boolean isInQueueAndNotMoving = (!hasCheckedOut && targetCashier != null && targetNode == null && (path.isEmpty() || path == null));
        if (!(pauseTimer > 0) || !isInQueueAndNotMoving) {
            animateImages();
        }
        
        // updates visual basket
        updateBasketAndItems(); 
        
        // move to target node if already set
        if (targetNode != null) {
            moveToNode(targetNode, 0, 0);
            return;
        }
        
        // move to store entrance access node
        if (SimulationWorld.getStartNode().equals(currentNode)) {
            move(false);
            return;
        }
        
        // choose store
        if (store == null) {
            chooseStore();
            return;
        }
        
        // take items while walking around if shopping list items aren't collected yet
        if (!shoppingList.isEmpty() && currentActCycles < maxActCycles) {
            retrieveProdcuts(); 
            move(false);
            return;
        }
        
        // check out if the shopping list items are all collected
        if (!hasCheckedOut) {
            // chooses cashier first
            if (targetCashier == null) {
                chooseCashier();
            }
            // then moves to cashier
            else {
                moveToCashier();
            }
            return;
        }
        
        // leaves the store if everything has been done
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
                    
                    // 10-30 act delay after getting an item
                    pauseTimer = 10 + Greenfoot.getRandomNumber(21); 
                    
                    //visually add item into basket
                    addItemToBasket(retrieved);

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
        Cashier best = cashiers.get(cashiers.size() - 1);
        
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
        // gets cashier node
        Node cashierNode = targetCashier.getCustomerNode();
        
        // cannot move to cashier if the cashier does not have an access node
        if (cashierNode == null) return;

        // follows path if it isn't empty
        if (path != null && !path.isEmpty()) {
            // target node becomes the first node in the path
            targetNode = path.get(0);
    
            // arrived at the cashier node
            if (targetNode.equals(cashierNode)) {
                path.remove(0);
                targetNode = null;
            }
            // moved to target node
            else if (moveToNode(targetNode, 0, 0)) {
                path.remove(0);
                targetNode = null;
            }
            return;
        }

        // ===== has now arrived at cashier =====
        
        // adds this customer to queue
        targetCashier.addCustomerToQueue(this);
        
        // gets position in queue
        int pos = targetCashier.getPositionInQueue(this);
        
        // calculates spacing depending on store
        int spacing = store == SimulationWorld.storeOne ? -20 : 20;
        
        // offsets to simulate lining up
        double offsetX = 0;
        double offsetY = spacing * (pos + 1);
        
        // move to the offset
        boolean atOffset = moveToNode(cashierNode, offsetX, offsetY);
    }
    
    /**
     * Method to find a path from the current node to a goal node
     * Uses BFS (Breadth-First Search) algorithm
     * 
     * @param target node
     * @return a list of nodes representing the path, empty if no path 
     */
    private List<Node> findPath(Node goal) {
        // result path
        List<Node> result = new ArrayList<>();
    
        // return empty path if not reachable
        if (currentNode == null || goal == null) {
            return result;
        }
    
        // nodes to explore
        Queue<Node> queue = new LinkedList<>();
        // track previous nodes
        Map<Node, Node> cameFrom = new HashMap<>();
        // visited nodes
        Set<Node> visited = new HashSet<>();
    
        // start at the current node
        queue.add(currentNode);
        visited.add(currentNode);
        cameFrom.put(currentNode, null);
    
        // perform bfs
        while (!queue.isEmpty()) {
            Node node = queue.poll();
    
            // goal found
            if (node.equals(goal)) {
                Node cur = node;
                while (cur != null) {
                    // adds nodes to list
                    result.add(0, cur); 
                    cur = cameFrom.get(cur);
                }
                return result;
            }
    
            // get neighbour nodes
            List<Node> neighbours = node.getNeighbouringNodes();
            if (neighbours == null) continue;
    
            // explroe neighbours
            for (Node next : neighbours) {
                if (visited.contains(next)) continue; // skip visited nodes
                visited.add(next);
                cameFrom.put(next, node); // record path
                queue.add(next); // add for further exploration
            }
        }
    
        // return empty if no path exists
        return result;
    }
    
    /**
     * Method for the customer to leave the store
     */
    public void leaveStore() {
        // gets exit node
        Node worldExit = SimulationWorld.getExitNode();
        
        // arrived at exit
        if (currentNode.checkIsEnd()) {
            // removes items in basket
            removeAllCarriedItems(); 
            
            //calculates rating
            calculateRating();
            
            // remove from world
            getWorld().removeObject(this);
            
            return;
        }
        
        // move to exit
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
        
        // no need to reselect a new target node if already moving to one
        if (targetNode != null) {
            moveToNode(targetNode, 0, 0); 
            return;
        }

        // gets neighbouring nodes
        List<Node> neighbouringNodes = currentNode.getNeighbouringNodes();
        
        // cannot move if no neighbouring nodes
        if (neighbouringNodes == null || neighbouringNodes.isEmpty()) {
            return;
        }
        
        // makes available nodes
        List<Node> availableNodes = new ArrayList<>();
        
        // loops through all neighbouring nodes
        for (Node n : neighbouringNodes) {
            if (n.equals(previousNode)) {
                continue;
            }
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
            for (Node nn : neighbouringNodes) {
                if (nn != null) {
                    availableNodes.add(nn);
                }
            }
        }
        
        // randomly chooses next node
        Node nextNode = availableNodes.get(Greenfoot.getRandomNumber(availableNodes.size()));
        
        // updates previous node
        previousNode = currentNode;
        
        // sets target node
        targetNode = nextNode;
        
        // start moving to next node
        moveToNode(nextNode, 0, 0);
    }
    
    /**
     * Method to move towards a Node
     * 
     * @param n node to move to
     * @param offsetX x offset from node locaiton
     * @param offsetY y offset from node location
     * @return whether the customer has arrived at the target node
     */
    protected boolean moveToNode(Node n, double offsetX, double offsetY) {
        // target location
        double targetX = n.getX() + offsetX;
        double targetY = n.getY() + offsetY;
        
        // Use precise coordinates (double) to avoid rounding oscillation with Actor.getX()/getY()
        double curX = (this instanceof SuperSmoothMover) ? ((SuperSmoothMover)this).getPreciseX() : getX();
        double curY = (this instanceof SuperSmoothMover) ? ((SuperSmoothMover)this).getPreciseY() : getY();
        
        // calculates change in x and y
        double dx = targetX - curX;
        double dy = targetY - curY;
        double distance = Math.sqrt(dx * dx + dy * dy);
        
        // finishes movement if almost there (1 act cycle would move too much to the distance)
        // snap when within one step (use <= to be robust)
        if (distance <= movementSpeed) {
            // snaps to the target location
            setLocation(targetX, targetY);
            
            // updates current and target node
            currentNode = n;
            targetNode = null;
            
            // has arrived
            return true;
        }
        
        // calculates angle using precise deltas
        double angle = Math.atan2(dy, dx);
        facingAngle = Math.atan2(dy, dx);  //store facing angel

        // updates x and y values using precise positions
        double newX = curX + Math.cos(angle) * movementSpeed;
        double newY = curY + Math.sin(angle) * movementSpeed;
        
        // updates location
        setLocation(newX, newY);
        
        // has not arrived yet
        return false;
    }
    
    /**
     * Method to set the customer's check out flag to true
     */
    public void checkOut() {
        if (this.getStore() == SimulationWorld.storeOne){
            supermarketTotalProductsSold += this.getCartSize();
            if (this.getType() == 0){ supermarketTotalBargainShoppers++; }
            else if (this.getType() == 1){ supermarketTotalBulkShoppers++; }
            else if (this.getType() == 2){ supermarketTotalImpulseShoppers++; }
            else if (this.getType() == 3){ supermarketTotalRegularShoppers++; }
        }
        else{
            butcherTotalProductsSold += this.getCartSize();
            if (this.getType() == 0){ butcherTotalBargainShoppers++; }
            else if (this.getType() == 1){ butcherTotalBulkShoppers++; }
            else if (this.getType() == 2){ butcherTotalImpulseShoppers++; }
            else if (this.getType() == 3){ butcherTotalRegularShoppers++; }
        }
    
        this.hasCheckedOut = true;
        SoundManager.playCashierSound();
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
    
    /**
     * Method to get the size of the cart
     * 
     * @return an integer representing the size of the cart
     */
    public int getCartSize() {
        return cart.size();
    }
    
    /**
     * Method to get the store the customer is in
     * 
     * @return the store the customer is in
     */
    public Store getStore() {
        return store;
    }
    
    /**
     * Method to animate the images
     */
    private void animateImages()
    {
        // updates image
        if (animCounter++ >= animSpeed) {
            animCounter = 0;
            animIndex = (animIndex + 1) % 2;
        }
                
        // gets facing direction
        String dir = getFacingDirection();
        
        // changes image based on direction
        if (dir.equals("RIGHT")) {
            setImage(rightImages[animIndex]);
        }
        else if (dir.equals("LEFT")) { 
            setImage(leftImages[animIndex]);
        }
        else if (dir.equals("UP")) {
            setImage(upImages[animIndex]);
        }
        else if (dir.equals("DOWN")) { 
             setImage(downImages[animIndex]);
        }
    }
    
    /**
     * Method to get the customer facing direction
     * 
     * @return a string representing the facing direction
     */
    private String getFacingDirection() 
    {
        double x = Math.cos(facingAngle);
        double y = Math.sin(facingAngle);
    
        if (Math.abs(x) > Math.abs(y)) {
            return x > 0 ? "RIGHT" : "LEFT";
        } else {
            return y > 0 ? "DOWN" : "UP";
        }
    }
    
    /**
     * Add a retrieved product visually into the customer's basket.
     */
    protected void addItemToBasket(Product p)
    {
        if (getWorld() == null) return;

        // Create basket if this is the first carried item
        if (visualBasket == null) {
            visualBasket = new Basket();
            getWorld().addObject(visualBasket, getX(), getY());
        }

        // add items into currently held itmes
        carriedItems.add(p);

        // Add product actor to world if not already there
        if (p.getWorld() == null) {
            getWorld().addObject(p, getX(), getY());
        }

        //shrink the item so it fits better inside the basket
        GreenfootImage img = p.getImage();
        img.scale(20, 20);
        p.setImage(img);
    }

    /**
     * Update the position of the basket and all carried items
     * so they follow the customer.
     */
    private void updateBasketAndItems()
    {
        if (visualBasket == null) return;
        if (getWorld() == null) return;

        String dir = getFacingDirection();

        int bx = getX();
        int by = getY()-40;  //40 id the padding factor

        // place basket based on facing direction
        if (dir.equals("RIGHT")) {
            bx += 15;
            by += 5;
        } else if (dir.equals("LEFT")) {
            bx -= 15;
            by += 5;
        } else if (dir.equals("UP")) {
            by -= 15;
        } else if (dir.equals("DOWN")) {
            by += 15;
        }

        visualBasket.setLocation(bx, by);

        // place items stacked inside basket
        int index = 0;
        for (Product p : carriedItems) {
            if (p == null || p.getWorld() == null) continue;

            int px = bx;
            int py = by - 5 - index * 5; // stack upward inside the basket

            p.setLocation(px, py);
            index++;
        }
    }

    /**
     * Removes this customer's basket and any carried items from the world.
     * Called by NightEffect when customers are force-removed at night
     * and also by leaveStore() when customer exits normally.
     */
    public void removeAllCarriedItems()
    {
        if (getWorld() == null) return;

        // remove basket
        if (visualBasket != null && visualBasket.getWorld() != null) {
            getWorld().removeObject(visualBasket);
        }
        visualBasket = null;

        // remove all carried products
        for (Product p : carriedItems) {
            if (p != null && p.getWorld() != null) {
                getWorld().removeObject(p);
            }
        }
        carriedItems.clear();
    }
    
    /**
     * Creates a vertically padded image so the customer's feet align on the node.
     * The padding places the sprite on top of a taller transparent canvas.
     */
    protected GreenfootImage padImage(GreenfootImage original) {
        //original.scale(original.getWidth()/4,original.getHeight()/4);
        original.scale(40,80);
        int w = original.getWidth();
        int h = original.getHeight();
    
        //New canvas 2x taller (you can adjust factor later)
        GreenfootImage padded = new GreenfootImage(w, h * 2);

        // Draw original at the TOP (node will align with bottom)
        padded.drawImage(original, 0, 0);
        
        return padded;
    }
    
    /**
     * Calculates a star rating for this customer based on how many items
     * they successfully collected compared to their original shopping list,
     * and sends that rating to the StoreUI.
     * 
     * The rating rules are:
     * - 5 stars if the customer found 100% of their items
     * - 4 stars if they found at least 75%
     * - 3 stars if they found at least 50%
     * - 2 stars if they found more than 0% but less than 50%
     * - 1 star if they did not find any items
     * 
     * If the original shopping list size is 0, a default rating of 5 is used
     * as a safety value.
     */
    protected void calculateRating()
    {
        int found=cart.size();
        int rating=0;
        
        if (originalShoppingListSize == 0) 
        {
            rating=3+Greenfoot.getRandomNumber(3); //3-5
        }
    
        // compares number of items collected
        if (found > originalShoppingListSize)
        {
           rating = 5;
        }
        else if (found == originalShoppingListSize)
        {
           rating = 3 + Greenfoot.getRandomNumber(3);
        }
        else if (found < originalShoppingListSize)
        {
           rating = 1 + Greenfoot.getRandomNumber(2);
        }
        
        //display rating
        showText("Rating:"+rating,Color.GREEN,getX(),getY()-100);
        
        //update rating
        SimulationWorld.storeUI.addStar( rating, store.getStoreNumber());
    }
    
    protected abstract int getType();
}
