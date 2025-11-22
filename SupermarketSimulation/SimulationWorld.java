import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)  
import java.util.ArrayList;
import java.util.List;

/**
 * Write a description of class MyWorld here.
 * 
 * @author Saiful Shaik, Owen Kung, Joe Zhuo
 * @version Modified: Nov, 8, 2025
 */
public class SimulationWorld extends World
{
    
    //for spawning truck
    private int truckDelay;
    private int actCount;
    
    // Grid settings
    public static final int GRID_CELL_SIZE = 20; // pixels per cell
    public static final int GRID_START_Y = 100;  // Grid starts at y=100
    
    private static final GreenfootImage bg = new GreenfootImage("background.png");
    
    public static Store storeOne = new Store("Store 1"); 
    public static Store storeTwo = new Store("Store 2");
    
    private static List<Node> roadNodes;
    
    private static final boolean showNodes = true;
    
    //sale related variables
    private boolean saleChosenToday = false;
    private boolean saleUpdatedToday = false;
    private int lastHour = -1;
    public SimulationWorld(){
        super(bg.getWidth(), bg.getHeight(), 1);
        setBackground(bg); 
        
        roadNodes = new ArrayList<>();
        
        Node roadSpawn = new Node(600, 100);
        Node entranceAccess = new Node(600, 400);
        Node storeOneEntranceNode = storeOne.getEntranceNode();
        Node storeTwoEntranceNode = storeTwo.getEntranceNode();
        
        roadSpawn.addNeighbouringNode(entranceAccess);
        entranceAccess.addNeighbouringNode(storeOneEntranceNode);
        
        entranceAccess.addNeighbouringNode(storeTwoEntranceNode);
        
        roadNodes.add(roadSpawn);
        
        addObject(new CustomerSpawner(), 0, 0);
        addObject(new StoreUI(), getWidth()/2, 50);
        
        // Enable stocking in simulation mode
        DisplayUnit.setEnableStocking(true);
        
        // add the Cashier to right store
        Cashier rsCashier = new Cashier();
        addObject(rsCashier, getWidth()/2 + 200, getHeight()/2);
        rsCashier.setCustomerNode(storeTwo.getNode(825, 240));
        storeTwo.addCashier(rsCashier);
        
        // add cashier to left store
        Cashier lsLeftCashier = new Cashier();
        Cashier lsRightCashier = new Cashier();
        lsLeftCashier.setCustomerNode(storeOne.getNode(275, 450));
        lsRightCashier.setCustomerNode(storeOne.getNode(200, 450));
        addObject(lsLeftCashier, getWidth()/2 - 250, getHeight() / 2 + 130);
        addObject(lsRightCashier, getWidth()/2 - 425, getHeight() / 2 + 130);
        
        // adds cashiers to store
        storeOne.addCashier(lsLeftCashier);
        storeOne.addCashier(lsRightCashier);
        
        // add the butcher
        addObject(new Butcher(), 975, 260);
        
        // Load display units from saved layout, or use default if no saved layout exists
        loadDisplayUnits();

        // Add visual markers for stores' nodes (stores manage their own node markers)
        if (showNodes) {
            storeOne.showNodesInWorld(this);
            storeTwo.showNodesInWorld(this);
        }
       
        //Set Paint order
        //So customer, Product and Display units can present properly
        //setPaintOrder(Effect.class, Customer.class,FloatingText.class, Product.class,DisplayUnit.class    );
        setPaintOrder(FloatingText.class,Emoji.class,NightEffect.class,Customer.class, SaleSign.class,Product.class,DisplayUnit.class);  

         //Start ambienceSound
        SoundManager.startAmbienceSound();
        //Add NightEffect to the world
        addObject(new NightEffect(),400,300);
        
        //Add a clock in the centre of the screen
        //The customer should be spawned at certain periods in the day
        //The ReloadingTruck will also come only after store hours       
        addObject(new ClockDisplay(), 605, 45); // centre to

                
        //starts the day with 8:00 AM
        TimeOfDayManager.setSecond(8 * 3600); 
        //pick today's sale immediately
        storeOne.startNewDay(this);
        storeTwo.startNewDay(this);
        saleChosenToday = true;

    }
    public static Node getStartNode() {
        return roadNodes.get(0);
    }
    
    public static Node getExitNode() {
        return roadNodes.get(roadNodes.size() - 1);
    }
    
    /** Add a NodeMarker that follows each DisplayUnit so nodes in front of
     * display units are visible while the simulation runs.
     */
    private void addNodeMarkersForDisplayUnits() {
        java.util.List<DisplayUnit> units = getObjects(DisplayUnit.class);
        for (DisplayUnit unit : units) {
            // create a marker that follows the unit; position it initially on the unit
            NodeMarker marker = new NodeMarker(unit);
            addObject(marker, unit.getX(), unit.getY() - 8);
        }
    }
    
    /**
     * Load display units from saved layout or create default layout
     */
    private void loadDisplayUnits() {
        List<DisplayUnitData> savedLayout = DisplayUnitData.loadLayout();
        
        if (!savedLayout.isEmpty()) {
            // Load from saved file
            for (DisplayUnitData data : savedLayout) {
                DisplayUnit unit = data.createDisplayUnit();
                if (unit != null) {
                    addObject(unit, data.getX(), data.getY());
                    
                    // register and report available products for debugging
                    try {
                        java.util.List<Node> nearby = SettingWorld.findNodesInRange(data.getX(), data.getY(), 100);
                        unit.setCustomerNodes(nearby);
                        Store parent = null;
                        for (Node n : nearby) {
                            if (storeOne.ownsNode(n)) { storeOne.addDisplayUnit(unit); unit.setParentStore(storeOne); parent = storeOne; break; }
                            if (storeTwo.ownsNode(n)) { storeTwo.addDisplayUnit(unit); unit.setParentStore(storeTwo); parent = storeTwo; break; }
                        }
                        
                        // Stock AFTER parent store is assigned so products inherit store reference
                        try { 
                            unit.stock(); 
                        } catch (Exception stockEx) {
                            System.err.println("Error stocking " + unit.getClass().getSimpleName() + ": " + stockEx.getMessage());
                        }
                        
                        if (parent != null) {
                            // Derive provided product classes from any already-stocked Product instances
                            java.util.Set<Class<? extends Product>> provided = new java.util.HashSet<Class<? extends Product>>();
                            for (Product p : unit.getStockedItems()) {
                                if (p != null) provided.add((Class<? extends Product>) p.getClass());
                            }
                            if (!provided.isEmpty()) {
                                for (Class<? extends Product> pc : provided) parent.addAvailableProductTypes(pc);
                            }
                            System.out.println("Added " + unit.getClass().getSimpleName() + " to " + (parent == storeOne ? "Store 1" : "Store 2") + " -> products: " + (provided.isEmpty() ? "<none yet>" : provided));
                        } else {
                            System.out.println("Added " + unit.getClass().getSimpleName() + " at (" + data.getX() + "," + data.getY() + ") but could not assign to a store");
                        }
                    } catch (Exception e) {
                        System.err.println("Error registering display unit: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        } else {
            // Use default layout if no saved layout exists
            createDefaultLayout();
        }
    }
    
    /**
     * Create the default display unit layout (original hardcoded positions)
     */
    private void createDefaultLayout() {
        /**
        // add fridge to store 2
        addObject(new Fridge(),75,225);
        // add shelve next to fridge
        addObject(new SnackShelf(),175,240);
        
        // add Lettuce Bin in store 1
        addObject(new LettuceBin(),1010,460);
        // add Carrot Bin in store 1
        addObject(new CarrotBin(),940,460);
        // add Apple Bin in store 1
        addObject(new AppleBin(),870,460);
        // add Orange Bin in store 1
        addObject(new OrangeBin(),800,460);
        // add SteakHangers to store 1
        addObject(new RawBeefHangers(),935,147);
        */

        // After adding the default layout units, assign them to stores and print their available products
        for (DisplayUnit unit : getObjects(DisplayUnit.class)) {
            try {
                int ux = unit.getX();
                int uy = unit.getY();
                // attempt to stock default units so stockedItems is ready
                try { unit.stock(); } catch (Exception ignore) {}
                java.util.List<Node> nearby = SettingWorld.findNodesInRange(ux, uy, 100);
                unit.setCustomerNodes(nearby);
                Store parent = null;
                for (Node n : nearby) {
                    if (storeOne.ownsNode(n)) { storeOne.addDisplayUnit(unit); unit.setParentStore(storeOne); parent = storeOne; break; }
                    if (storeTwo.ownsNode(n)) { storeTwo.addDisplayUnit(unit); unit.setParentStore(storeTwo); parent = storeTwo; break; }
                }
                if (parent != null) {
                    // Derive provided product classes from any already-stocked Product instances
                    java.util.Set<Class<? extends Product>> provided = new java.util.HashSet<Class<? extends Product>>();
                    for (Product p : unit.getStockedItems()) {
                        if (p != null) provided.add((Class<? extends Product>) p.getClass());
                    }
                    if (!provided.isEmpty()) {
                        for (Class<? extends Product> pc : provided) parent.addAvailableProductTypes(pc);
                    }
                    System.out.println("Added " + unit.getClass().getSimpleName() + " to " + (parent == storeOne ? "Store 1" : "Store 2") + " -> products: " + (provided.isEmpty() ? "<none yet>" : provided));
                } else {
                    System.out.println("Added " + unit.getClass().getSimpleName() + " at (" + ux + "," + uy + ") but could not assign to a store");
                }
            } catch (Exception e) {
                System.err.println("Error registering default display unit: " + e.getMessage());
            }
        }
    }
    
    public void act () 
    {
        //use zSort
        zSort ((ArrayList<Actor>)(getObjects(Actor.class)), this);
        actCount++;


        //spawnRestockingTruck();
        if(TimeOfDayManager.getHour() == 22 &&TimeOfDayManager.getMinute() == 0)
        {
          addObject(new RestockingTruck(),600,200);       
        }
        
        //update sale item daily
        updateSaleItem();

    }
    /**
     * Z-sort so actors with higher Y (lower on screen) render in front.
     * Uses precise Y for SuperSmoothMover when available. Stable for ties.
     */
    public static void zSort(java.util.ArrayList<greenfoot.Actor> actorsToSort, greenfoot.World world) {
        // Local container class (scoped to this method only).
        class Entry implements java.lang.Comparable<Entry> {
            final greenfoot.Actor actor;
            final boolean superSmooth;
            final int order;     // preserve original order for stable ties
            final int xi, yi;    // integer coords snapshot
            final double xd, yd; // precise coords snapshot
    
            // int-based actor
            Entry(greenfoot.Actor a, int x, int y, int order) {
                this.actor = a; this.superSmooth = false; this.order = order;
                this.xi = x; this.yi = y;
                this.xd = x; this.yd = y;
            }
            // precise-based actor
            Entry(greenfoot.Actor a, double x, double y, int order) {
                this.actor = a; this.superSmooth = true; this.order = order;
                this.xi = (int) x; this.yi = (int) y;
                this.xd = x; this.yd = y;
            }
    
            @Override
            public int compareTo(Entry other) {
                double thisY  = superSmooth ? yd : yi;
                double otherY = other.superSmooth ? other.yd : other.yi;
    
                // Handle rare NaN robustly: treat NaN as far back
                if (java.lang.Double.isNaN(thisY) && java.lang.Double.isNaN(otherY)) return java.lang.Integer.compare(order, other.order);
                if (java.lang.Double.isNaN(thisY)) return -1;
                if (java.lang.Double.isNaN(otherY)) return 1;
    
                int cmp = java.lang.Double.compare(thisY, otherY);
                if (cmp != 0) return cmp;
                return java.lang.Integer.compare(this.order, other.order); // stable tie-break
            }
        }
    
        // Snapshot actors and positions first.
        ArrayList<Entry> list = new ArrayList<Entry>(actorsToSort.size());
        int order = 0;
        for (greenfoot.Actor a : actorsToSort) {
            if (a instanceof SuperSmoothMover) {
                SuperSmoothMover s = (SuperSmoothMover) a;
                list.add(new Entry(a, s.getPreciseX(), s.getPreciseY(), order++));
            } else {
                list.add(new Entry(a, a.getX(), a.getY(), order++));
            }
        }
    
        // Sort farthest-back (smallest Y) first.
        java.util.Collections.sort(list);
    
        // Re-add in paint order with consistent rounding, then restore precise coords.
        for (Entry e : list) {
            // Remove if currently in any world to ensure paint-order reset
            if (e.actor.getWorld() != null) {
                world.removeObject(e.actor);
            }
            if (e.superSmooth) {
                int rx = roundAwayFromZero(e.xd);
                int ry = roundAwayFromZero(e.yd);
                world.addObject(e.actor, rx, ry);
                // Restore exact double-precision location to avoid drift
                ((SuperSmoothMover) e.actor).setLocation(e.xd, e.yd);
            } else {
                world.addObject(e.actor, e.xi, e.yi);
            }
        }
    }
    /** Helper: symmetric rounding that rounds halves away from zero. */
    private static int roundAwayFromZero(double v) 
    {
        return (int)(v + Math.signum(v) * 0.5);
    }
    
    /**
     * Draw a world-wide grid overlay covering the entire screen below y=100
     */
    private void drawWorldGrid() {
        GreenfootImage bg = getBackground();
        
        // Save current color
        Color gridColor = new Color(0, 0, 0);
        bg.setColor(gridColor);
        bg.setTransparency(40); // Semi-transparent so background shows through
        
        int worldWidth = getWidth();
        int worldHeight = getHeight();
        
        // Draw vertical grid lines
        for (int x = 0; x <= worldWidth; x += GRID_CELL_SIZE) {
            bg.drawLine(x, GRID_START_Y, x, worldHeight);
        }
        
        // Draw horizontal grid lines
        for (int y = GRID_START_Y; y <= worldHeight; y += GRID_CELL_SIZE) {
            bg.drawLine(0, y, worldWidth, y);
        }
        
        // Draw a thicker line at y=100 to mark the grid boundary
        bg.setColor(new Color(0, 0, 0));
        bg.setTransparency(150);
        for (int i = 0; i < 3; i++) {
            bg.drawLine(0, GRID_START_Y + i, worldWidth, GRID_START_Y + i);
        }
    }
    //update sale items daily
    private void updateSaleItem() {
        int currentHour = TimeOfDayManager.getHour();
        int currentMinute = TimeOfDayManager.getMinute();
    
        //Before stcoking truck appear at 22
        if (currentHour == 21 && lastHour != 0) {
            saleChosenToday = false;
        }
        
        lastHour = currentHour;
        
        //Before custoer comes in update the sale item
        if (!saleChosenToday && currentHour == 6 && currentMinute == 59) {
            storeOne.startNewDay(this);
            storeTwo.startNewDay(this);
            saleChosenToday = true;
        }
    }
     /*
     * Try to stop all the long sound effect
     * Sometimes it still does not work when pause is pressed
     */
    public void stopped()
    {
        //stop ambience sound
        SoundManager.stopAmbienceSound();
        SoundManager.stopNightSound();
    
        //stop Restocking Truck sound
        SoundManager.stopTruckSound();
        
        //stop butcher sound
        SoundManager.stopButcherSound();
    }
    /*
     * 
     * Tf program is paused, try to resume some of the sound effects
     */
    public void started()
    {
        //replay ambience sound
        if(NightEffect.isDayTime)
        {
             SoundManager.startAmbienceSound();
        }
        else
        {
             SoundManager.startNightSound();
        }
       
        
        //replay butcher sound
        SoundManager.playButcherSound();
    }

}



