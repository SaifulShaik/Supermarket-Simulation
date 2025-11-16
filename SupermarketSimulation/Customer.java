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
    
    protected Node previousNode;
    protected Node currentNode;
    protected Node targetNode;
    // Debug: last node coords printed to avoid redundant logs
    protected int _dbg_lastNodeX = Integer.MIN_VALUE;
    protected int _dbg_lastNodeY = Integer.MIN_VALUE;
    
    protected int pauseTimer = 0;
    
    protected Store store;
    // Debug: when true the customer will stop at the first product node after entering the store
    protected boolean debugStoppedAtFirst = false;
    protected Node debugTargetNode = null;
    
    public Customer() {
        this(2.0, 100.0, null, 5);
    }
    
    /**
     * Customer constructor
     * 
     * @param movementSpeed movement speed of the customer
     * @param budget budget of the customer
     * @param currentNode default node the customer spawns at
     */
    public Customer(double movementSpeed, double budget, Node currentNode, int maxShoppingListItems) {
        this.movementSpeed = movementSpeed;
        this.budget = budget;
        
        this.currentNode = currentNode;
        targetNode = null;
        previousNode = null;
        
        store = null;
        
        shoppingList = new ArrayList<>();
        shoppingList = generateShoppingList(maxShoppingListItems);
        cart = new ArrayList();    
        // Debug: print the generated shopping list for this customer
        try {
            StringBuilder sb = new StringBuilder();
            for (Class<? extends Product> c : shoppingList) {
                sb.append(c.getSimpleName()).append(", ");
            }
            String listStr = sb.length() > 0 ? sb.substring(0, sb.length()-2) : "<empty>";
            System.out.println("[Customer] spawned with shopping list: " + listStr);
        } catch (Exception ignore) {}
    }
    
    /**
     * Main act loop
     * first chooses store if not done that
     * then moves around the store and browses products
     * finally checks out once all products are purchased
     */
    public void act() {
        if (store == null) {
            chooseStore();
        }
        
        // Debug behavior: if customer is at the store entrance, route to the first product node and stop once
        if (!shoppingList.isEmpty() && store != null && currentNode != null && currentNode.checkIsEntrance() && !debugStoppedAtFirst) {
            // Determine first wanted product and find a display unit that provides it
            try {
                Class<? extends Product> firstWanted = shoppingList.get(0);
                for (DisplayUnit u : store.getAvailableDisplayUnits()) {
                    if (u == null) continue;
                    for (Product p : u.getStockedItems()) {
                        if (p != null && firstWanted.isInstance(p)) {
                            List<Node> accessNodes = u.getCustomerNodes();
                            if (accessNodes != null && !accessNodes.isEmpty()) {
                                debugTargetNode = accessNodes.get(0);
                                targetNode = debugTargetNode;
                                System.out.println("[Customer DEBUG] routing to first product node " + debugTargetNode.getX() + "," + debugTargetNode.getY());
                                break;
                            }
                        }
                    }
                    if (debugTargetNode != null) break;
                }
            } catch (Exception e) {
                // ignore debug failures
            }
        }

        if (!shoppingList.isEmpty()) {
            retrieveProdcuts(); 
            move();

            // After moving, if we reached the debug target node, pause and mark debug stop done
            if (debugTargetNode != null && currentNode != null) {
                if (currentNode.getX() == debugTargetNode.getX() && currentNode.getY() == debugTargetNode.getY()) {
                    debugStoppedAtFirst = true;
                    pauseTimer = 50; // pause for a while for debugging
                    System.out.println("[Customer DEBUG] stopped at first product node for inspection");
                    debugTargetNode = null;
                }
            }
        }
        else {
            moveToExit();
        }
    }
    
    /**
     * Method to choose a store to go into
     */
    protected void chooseStore() {
        // Debug: print store product lists to diagnose empty-store issue
        try {
            System.out.println("[Customer] chooseStore(): storeOne has " + SimulationWorld.storeOne.getAvailableProducts().size() + " products, storeTwo has " + SimulationWorld.storeTwo.getAvailableProducts().size() + " products");
        } catch (Exception ignore) {}

        List<Store> stores = getWorld().getObjects(Store.class);
        
        if (stores.isEmpty()) {
            System.out.println("[Customer] chooseStore(): no Store actors in world, using static references");
        }
        
        List<Class<? extends Product>> storeOneShoppingList = new ArrayList<>();
        List<Class<? extends Product>> storeTwoShoppingList = new ArrayList<>();
        
        System.out.println("[Customer] chooseStore(): original shopping list has " + shoppingList.size() + " items");
        
        for (Class<? extends Product> productClass : shoppingList) {
            boolean inStoreOne = false;
            for (Class<? extends Product> c : SimulationWorld.storeOne.getAvailableProducts()) {
                if (c == productClass) {
                    inStoreOne = true;
                    break;
                }
            }
    
            boolean inStoreTwo = false;
            for (Class<? extends Product> p : SimulationWorld.storeTwo.getAvailableProducts()) {
                if (p == productClass) {
                    inStoreTwo = true;
                    break;
                }
            }
    
            if (inStoreOne) storeOneShoppingList.add(productClass);
            if (inStoreTwo) storeTwoShoppingList.add(productClass);
        }
        
        System.out.println("[Customer] chooseStore(): after filtering - store1=" + storeOneShoppingList.size() + " items, store2=" + storeTwoShoppingList.size() + " items");
        
        if (storeOneShoppingList.size() > storeTwoShoppingList.size()) {
            store = SimulationWorld.storeOne;
            shoppingList = storeOneShoppingList;
        }
        else if (storeOneShoppingList.size() < storeTwoShoppingList.size()){
            store = SimulationWorld.storeTwo;
            shoppingList = storeTwoShoppingList;
        }
        else {
            // Both lists same size (including both empty)
            if (storeOneShoppingList.isEmpty() && storeTwoShoppingList.isEmpty()) {
                System.out.println("[Customer] chooseStore(): BOTH stores have empty shopping lists - stores not populated! Picking store 1 anyway.");
                store = SimulationWorld.storeOne; // fallback
            } else {
                int chosenStore = Greenfoot.getRandomNumber(2); // use 2 not stores.size() for static refs
                store = (chosenStore == 0) ? SimulationWorld.storeOne : SimulationWorld.storeTwo;
                shoppingList = store == SimulationWorld.storeOne ? storeOneShoppingList : storeTwoShoppingList;
            }
        }

        targetNode = store.getEntranceNode();
        // Debug: print chosen store and final shopping list for this customer
        try {
            StringBuilder sb2 = new StringBuilder();
            for (Class<? extends Product> c : shoppingList) sb2.append(c.getSimpleName()).append(", ");
            String listStr2 = sb2.length() > 0 ? sb2.substring(0, sb2.length()-2) : "<empty>";
            System.out.println("[Customer] chose " + (store == SimulationWorld.storeOne ? "Store 1" : "Store 2") + " with shopping list: " + listStr2);
        } catch (Exception ignore) {}
    }
    
    protected List<Class<? extends Product>> generateShoppingList(int maxShoppingListItems) {
        List<Class<? extends Product>> items = new ArrayList<>();
        
        List<Class<? extends Product>> availableItemTypes = new ArrayList<>();
        
        for (Class<? extends Product> p : SimulationWorld.storeOne.getAvailableProducts()) {
            availableItemTypes.add(p);
        }
        
        for (Class<? extends Product> p : SimulationWorld.storeTwo.getAvailableProducts()) {
            availableItemTypes.add(p);
        }
        
        if (maxShoppingListItems <= 0) {
            maxShoppingListItems = 1; 
        }
        
        // Generate between 1 and maxShoppingListItems (inclusive)
        int numItems = 1 + Greenfoot.getRandomNumber(maxShoppingListItems);
        
        System.out.println("[Customer] generateShoppingList: maxItems=" + maxShoppingListItems + ", generating " + numItems + " items from " + availableItemTypes.size() + " available types");
        
        for (int i = 0; i < numItems; i++) {
            if (!availableItemTypes.isEmpty()) {
                Class<? extends Product> itemClass = availableItemTypes.get(Greenfoot.getRandomNumber(availableItemTypes.size()));
                items.add(itemClass);
            } else {
                System.out.println("[Customer] generateShoppingList: availableItemTypes is empty - stores not populated yet!");
            }
        }
        
        System.out.println("[Customer] generateShoppingList: generated " + items.size() + " items total");
        return items;
    }
    
    protected void retrieveProdcuts() {
        // Debug: print quick state to help investigate why customers aren't picking items
        try {
            System.out.println("[Customer] retrieveProdcuts() state: store=" + (store==null?"<null>":store.name) + 
                ", currentNode=" + (currentNode==null?"<null>":"("+currentNode.getX()+","+currentNode.getY()+")") +
                ", shoppingListSize=" + (shoppingList==null?0:shoppingList.size()) +
                ", world=" + (getWorld()==null?"<null>":"present") );
        } catch (Exception ignore) {}

        if (store == null || currentNode == null || shoppingList == null || shoppingList.isEmpty() || getWorld() == null) return;
        
        List<DisplayUnit> units = store.getAvailableDisplayUnits();
        try {
            System.out.println("[Customer] store has " + (units==null?0:units.size()) + " display units");
        } catch (Exception ignore) {}
        if (units == null || units.isEmpty()) return;
        
        for (DisplayUnit u : units) {
            if (u == null) continue;
            
            List<Node> accessNodes = u.getCustomerNodes();
            try {
                StringBuilder sb = new StringBuilder();
                if (accessNodes != null) {
                    for (Node an : accessNodes) sb.append("(").append(an.getX()).append(",").append(an.getY()).append(") ");
                }
                System.out.println("[Customer] unit=" + u.getClass().getSimpleName() + " accessNodes=[" + sb.toString() + "] stocked=" + (u.getStockedItems()==null?0:u.getStockedItems().size()));
            } catch (Exception ignore) {}
            
            // Accept match either by object identity or by coordinates (robust against different Node instances)
            boolean accessMatch = false;
            if (accessNodes != null) {
                for (Node an : accessNodes) {
                    if (an == currentNode) { accessMatch = true; break; }
                    if (currentNode != null && an.getX() == currentNode.getX() && an.getY() == currentNode.getY()) { accessMatch = true; break; }
                }
            }
            if (!accessMatch) continue;
            
            List<Product> stocked = u.getStockedItems();
            if (stocked == null || stocked.isEmpty()) continue;
            
            for (Class<? extends Product> wantedClass : new ArrayList<>(shoppingList)) {
                Product retrieved = u.retrieve(wantedClass);
                if (retrieved != null) {
                    cart.add(retrieved);
                    shoppingList.remove(wantedClass);
                    pauseTimer = 5 + Greenfoot.getRandomNumber(11);
                    // Debug: print what was picked and the remaining shopping list / cart
                    try {
                        StringBuilder rem = new StringBuilder();
                        for (Class<? extends Product> c : shoppingList) rem.append(c.getSimpleName()).append(", ");
                        String remStr = rem.length() > 0 ? rem.substring(0, rem.length()-2) : "<empty>";
                        StringBuilder cartSb = new StringBuilder();
                        for (Product cp : cart) cartSb.append(cp.getClass().getSimpleName()).append(", ");
                        String cartStr = cartSb.length() > 0 ? cartSb.substring(0, cartSb.length()-2) : "<empty>";
                        System.out.println("[Customer] picked " + retrieved.getClass().getSimpleName() + "; remaining: " + remStr + "; cart: " + cartStr);
                    } catch (Exception ignore) {}
                    // Stop after picking one item so customer pauses and doesn't pick multiple items in one tick
                    return;
                }
            }
        }
    }
    
    private void moveToExit() {
        
    }
    
    /*
     * Retrieve products in the shopping list
     * Add the product to the basket  - can later be used for checking out.
     * 
     * @author: Owen Kung
     * @version: Nov 2025
     */
    /*protected void retrieveProdcuts()
    {
        //ArrayList<DisplayUnit> units= (ArrayList<DisplayUnit>)getIntersectingObjects(DisplayUnit.class);
        DisplayUnit unit=(DisplayUnit) getOneIntersectingObject(DisplayUnit.class);
        if(isTouching(Butcher.class))
        {
            //go to butcher for RawBeef
            unit=getDisplayUnit(RawBeefHangers.class);
        }
        if(unit==null)
        {
            return;
        }

        //Retrieve what's in fridge
        if(unit.getClass().getSimpleName().equals("Fridge"))
        {
            if(shoppingList.contains(SimulationWorld.PRODUCT_COKE))
            {
                cart.add(unit.retrieve(Coke.class));
                shoppingList.remove(SimulationWorld.PRODUCT_COKE);
            }
            if(shoppingList.contains(SimulationWorld.PRODUCT_SPRITE))
            {
                cart.add(unit.retrieve(Sprite.class));
                shoppingList.remove(SimulationWorld.PRODUCT_SPRITE);
            }
            if(shoppingList.contains(SimulationWorld.PRODUCT_FANTA))
            {
                cart.add(unit.retrieve(Fanta.class));
                shoppingList.remove(SimulationWorld.PRODUCT_FANTA);
            }
            if(shoppingList.contains(SimulationWorld.PRODUCT_WATER))
            {
                cart.add(unit.retrieve(Water.class));
                shoppingList.remove(SimulationWorld.PRODUCT_WATER);
            }
           
        }
        //Retrieve what's in snack shelf
        if(unit.getClass().getSimpleName().equals("SnackShelf"))
        {
            if(shoppingList.contains(SimulationWorld.PRODUCT_LAYS))
            {
                cart.add(unit.retrieve(Lays.class));
                shoppingList.remove(SimulationWorld.PRODUCT_LAYS);
            }
            if(shoppingList.contains(SimulationWorld.PRODUCT_RUFFLES))
            {
                cart.add(unit.retrieve(Ruffles.class));
                shoppingList.remove(SimulationWorld.PRODUCT_RUFFLES);
            }
            if(shoppingList.contains(SimulationWorld.PRODUCT_DORITOS))
            {
                cart.add(unit.retrieve(Doritos.class));
                shoppingList.remove(SimulationWorld.PRODUCT_DORITOS);
            }
        }
        //Retrieve what's in cup noodle shelf
        if(unit.getClass().getSimpleName().equals("CupNoodleShelf"))
        {
            if(shoppingList.contains(SimulationWorld.PRODUCT_XING_RAMEN))
            {
                cart.add(unit.retrieve(XingRamen.class));
                shoppingList.remove(SimulationWorld.PRODUCT_XING_RAMEN);
            }
            if(shoppingList.contains(SimulationWorld.PRODUCT_NISSIN))
            {
                cart.add(unit.retrieve(Nissin.class));
                shoppingList.remove(SimulationWorld.PRODUCT_NISSIN);
            }
            if(shoppingList.contains(SimulationWorld.PRODUCT_JIN_RAMEN))
            {
                cart.add(unit.retrieve(JinRamen.class));
                shoppingList.remove(SimulationWorld.PRODUCT_JIN_RAMEN);
            }
        }
        //Retrieve what's in applebin
        if(unit.getClass().getSimpleName().equals("CandyBin"))
        {
            if(shoppingList.contains(SimulationWorld.PRODUCT_CANDY))
            {
                cart.add(unit.retrieve(Candy.class));
                shoppingList.remove(SimulationWorld.PRODUCT_CANDY);
            }
        }
        if(unit.getClass().getSimpleName().equals("AppleBin"))
        {
            if(shoppingList.contains(SimulationWorld.PRODUCT_APPLE))
            {
                cart.add(unit.retrieve(Apple.class));
                shoppingList.remove(SimulationWorld.PRODUCT_APPLE);
            }
        }
        if(unit.getClass().getSimpleName().equals("OrangeBin"))
        {
            if(shoppingList.contains(SimulationWorld.PRODUCT_ORANGE))
            {
                cart.add(unit.retrieve(Orange.class));
                shoppingList.remove(SimulationWorld.PRODUCT_ORANGE);
            }
        }
        if(unit.getClass().getSimpleName().equals("LettuceBin"))
        {
            if(shoppingList.contains(SimulationWorld.PRODUCT_LETTUCE))
            {
                cart.add(unit.retrieve(Lettuce.class));
                shoppingList.remove(SimulationWorld.PRODUCT_LETTUCE);
            }
        }
        if(unit.getClass().getSimpleName().equals("CarrotBin"))
        {
            if(shoppingList.contains(SimulationWorld.PRODUCT_CARROT))
            {
                cart.add(unit.retrieve(Carrot.class));
                shoppingList.remove(SimulationWorld.PRODUCT_CARROT);
            }
        }
        if(unit.getClass().getSimpleName().equals("SteakWarmer"))
        {
            if(shoppingList.contains(SimulationWorld.PRODUCT_STEAK))
            {
                cart.add(unit.retrieve(Steak.class));
                shoppingList.remove(SimulationWorld.PRODUCT_STEAK);
            }
            if(shoppingList.contains(SimulationWorld.PRODUCT_DRUM_STICK))
            {
                cart.add(unit.retrieve(DrumStick.class));
                shoppingList.remove(SimulationWorld.PRODUCT_DRUM_STICK);
            }
        }
        if(unit.getClass().getSimpleName().equals("RawBeefHangers"))
        {
            if(shoppingList.contains(SimulationWorld.PRODUCT_RAW_BEEF))
            {
                cart.add(unit.retrieve(RawBeef.class));
                shoppingList.remove(SimulationWorld.PRODUCT_RAW_BEEF);
            }
        }
    }*/
    
    /*
     * Get the requested DisplayUnit with class type
     * 
     * @author:Owen Kung
     * @version: Nov 2025
     */
    /*private DisplayUnit getDisplayUnit(Class displayClass)
    {
        ArrayList<DisplayUnit> units = (ArrayList<DisplayUnit>) getWorld().getObjects(DisplayUnit.class);
        for(DisplayUnit u:units)
        {
            //return the requested disply unit
            if (u.getClass()==displayClass) 
            {
                return u;
            }
        }
        return null;
    }*/
    
    protected void move() {
        if (pauseTimer > 0) {
            pauseTimer--;
            return;
        }

        if (targetNode != null) {
            moveToNode(targetNode); 
            return;
        }

        List<Node> neighbouringNodes = currentNode.getNeighbouringNodes();
        
        if (neighbouringNodes.isEmpty() || neighbouringNodes == null) {
            return;
        }
        
        List<Node> availableNodes = new ArrayList<>();
        
        for (Node n : neighbouringNodes) {
            if (n == previousNode) continue;
            else {
                availableNodes.add(n);
            }
        }
        
        if (availableNodes.isEmpty() || availableNodes == null) {
            return;
        }
        
        Node nextNode = availableNodes.get(Greenfoot.getRandomNumber(availableNodes.size()));
        previousNode = currentNode;
        targetNode = nextNode;
        
        moveToNode(nextNode);
    }
    
    protected void moveToNode(Node n) {
        int dx = n.getX() - getX();
        int dy = n.getY() - getY();
        
        double distance = Math.sqrt(dx * dx + dy * dy);
        
        if (distance < movementSpeed) {
            setLocation(n.getX(), n.getY());
            
            currentNode = n;
            targetNode = null;

            // Debug: when node changes, print which display units expose this node
            try {
                if (currentNode != null && (currentNode.getX() != _dbg_lastNodeX || currentNode.getY() != _dbg_lastNodeY)) {
                    _dbg_lastNodeX = currentNode.getX();
                    _dbg_lastNodeY = currentNode.getY();
                    debugPrintMatchingUnitsAtCurrentNode();
                }
            } catch (Exception ignore) {}
            
            return;
        }
        
        double angle = Math.atan2(dy, dx);
        
        double newX = getX() + Math.cos(angle) * movementSpeed;
        double newY = getY() + Math.sin(angle) * movementSpeed;
        
        setLocation(newX, newY);
    }

    /** Debug helper: prints display units in the current store whose access nodes match currentNode */
    private void debugPrintMatchingUnitsAtCurrentNode() {
        try {
            if (store == null || currentNode == null) {
                System.out.println("[Customer DEBUG] no store or currentNode to match");
                return;
            }
            StringBuilder sb = new StringBuilder();
            for (DisplayUnit u : store.getAvailableDisplayUnits()) {
                if (u == null) continue;
                List<Node> an = u.getCustomerNodes();
                if (an == null) continue;
                for (Node node : an) {
                    if (node == currentNode || (node.getX() == currentNode.getX() && node.getY() == currentNode.getY())) {
                        sb.append(u.getClass().getSimpleName()).append("(stocked=").append(u.getStockedItems()==null?0:u.getStockedItems().size()).append("), ");
                        break;
                    }
                }
            }
            if (sb.length() == 0) {
                System.out.println("[Customer DEBUG] arrived at node (" + currentNode.getX() + "," + currentNode.getY() + ") - no matching display units expose this node");
            } else {
                System.out.println("[Customer DEBUG] arrived at node (" + currentNode.getX() + "," + currentNode.getY() + ") - matching units: " + sb.toString());
            }
        } catch (Exception e) {
            System.err.println("[Customer DEBUG] error in debugPrintMatchingUnitsAtCurrentNode: " + e.getMessage());
        }
    }
    
    public void leaveStore() {
        getWorld().removeObject(this);
    }
    
    public double calculatePriceOfCart() {
        double total = 0;
        
        for (Product p : cart) {
            total += p.getPrice();
        }
        
        return total;
    }
}

