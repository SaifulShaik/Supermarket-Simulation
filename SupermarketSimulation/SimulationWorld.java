import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)  
import java.util.ArrayList;
import java.util.List;

/*
 * Supermarket Simulation - SimulationWorld
 *
 * Top-level overview
 * ------------------
 * This file contains the main world for the Supermarket Simulation game. The
 * simulation models two competing stores in a single world. Customers spawn,
 * navigate to display units, pick products, and queue at cashiers. The world
 * also drives environmental events (storms), day/night changes, restocking
 * trucks and sale selection.
 *
 * High-level components
 * - Store: Manages nodes, cashiers and the set of available product types.
 * - DisplayUnit / DisplayUnitData: Physical shelving/fridges/bins which hold
 *   Product instances. These are loaded from a saved layout or created with
 *   a default layout and then stocked.
 * - Customer / CustomerSpawner: Spawns customers and controls their behaviour
 *   via a Node graph and store-provided product information.
 * - Cashier: Checkout points customers queue at; stores register cashiers so
 *   customers can find them.
 * - TimeOfDayManager / ClockDisplay: Advances in-game time and triggers time
 *   based events (sale updates, truck arrivals, day/night swaps).
 * - SoundManager: Central control for ambience and effect sounds.
 *
 * Sub-customer types (behavioural variants)
 * -----------------------------------------
 * The simulation includes multiple customer subclasses that alter shopping
 * behaviour (found as separate classes in the project):
 * - `BargainShopper`: Prioritises sale items and looks for cheapest options.
 * - `BulkShopper`: Buys larger quantities and prefers bulk-sized display units.
 * - `ImpulseShopper`: Makes spontaneous purchases of nearby, attention-grabbing
 *   items (e.g. endcaps, snack shelves).
 * - Other variants: Additional specialised shoppers may exist; each subclass
 *   overrides selection and movement logic to produce distinct gameplay.
 *
 * Zombies (special actors / adversarial customers)
 * -------------------------------------------------
 * The project contains a 'zombie' style actor concept (if present) used to
 * create unusual or adversarial behaviour patterns. Zombies differ from
 * normal customers by ignoring normal shopping rules (they may wander,
 * pursue specific targets, or interact differently with display units). Use
 * them to test resilience of store layout or as a gameplay hazard. If a
 * `Zombie` class isn't present in the codebase, any similarly-named actor
 * implements the same pattern: alternate movement and different interaction
 * handlers compared to normal customers.
 *
 * Global vs Local effects
 * ------------------------
 * - Global effects: These impact the whole world and are managed by the
 *   `SimulationWorld` or central subsystems. Examples include:
 *   - Storm events: Spawned by `handleStorms()` and visually represented by
 *     a `Storm` actor that affects the entire world (visibility, spawning).
 *   - Day/night cycle: Managed by `TimeOfDayManager` and `NightEffect`, and
 *     switch ambient sound and visual tone for the whole world.
 *   - Restocking trucks: Arrive at fixed times and affect store-wide stock.
 *
 * - Local effects: These are targeted to specific actors/areas and do not
 *   affect the whole world. Examples include:
 *   - `SaleSign`: A local visual indicator above a display unit or aisle.
 *   - Single-display animations/effects (e.g., `Explosion`, `Fire`) that
 *     affect nearby customers or a single display unit.
 *   - NodeMarkers or UI floating text attached to single display units.
 *
 * Sound management
 * ----------------
 * Sounds are centrally managed via `SoundManager`. Responsibilities include:
 * - Starting/stopping ambience sounds for day/night modes (`startAmbienceSound`,
 *   `startNightSound`, `stopAmbienceSound`, `stopNightSound`).
 * - Playing one-off effect sounds (butcher, truck, etc.).
 * - Handling paused/resumed state: `stopped()` and `started()` attempt to
 *   suspend or resume long-running sounds so the audio state matches the
 *   simulation pause state. Consider moving to a small audio subsystem if
 *   finer-grained control (volume per-channel, muting, or layering) is
 *   required.
 *
 * Maintainer notes
 * ----------------
 * - Display units are registered with nearby stores so each store can derive
 *   what product types it offers. This is done during `loadDisplayUnits()` or
 *   `createDefaultLayout()`.
 * - Debug prints were removed from this file; use a logging facility or a
 *   configurable debug flag if you need diagnostic output later.
 * - Paint order is explicitly set using `setPaintOrder(...)` so visual layers
 *   render consistently.
 *
 * @authors Saiful Shaik, Owen Kung, Joe Zhuo, Angelina Zhou, Owen Lee
 * @version Nov, 8, 2025
 */
public class SimulationWorld extends World
{
    // Grid settings
    public static final int GRID_CELL_SIZE = 20; // pixels per cell
    public static final int GRID_START_Y = 100;  // Grid starts at y=100
    
   private static final GreenfootImage bg = new GreenfootImage("background.png");
    
    public static Store storeOne = new Store("Store 1"); 
    public static Store storeTwo = new Store("Store 2");
    public static StoreUI storeUI = new StoreUI();
    private static List<Node> roadNodes;
    
    private static final boolean showNodes = true;
    
    private boolean stormSpawnedToday = false;
    
    //sale related variables
    private boolean saleChosenToday = false;
    private boolean saleUpdatedToday = false;
    private int lastHour = -1;
    
    public int numberOfStorms = 0;

    public SimulationWorld(){
        super(bg.getWidth(), bg.getHeight(), 1);
        setBackground(bg); 

        storeOne.resetStore();
        storeTwo.resetStore();
        storeUI.clearRatings(1);
        storeUI.clearRatings(2);
        roadNodes = new ArrayList<>();
        
        
        //drawStoreBoundaries();
        
        Node roadSpawn = new Node(600, 100);
        Node entranceAccess = new Node(600, 400);
        Node storeOneEntranceNode = storeOne.getEntranceNode();
        Node storeTwoEntranceNode = storeTwo.getEntranceNode();
        
        roadSpawn.addNeighbouringNode(entranceAccess);
        entranceAccess.addNeighbouringNode(storeOneEntranceNode);
        
        entranceAccess.addNeighbouringNode(storeTwoEntranceNode);
        
        roadNodes.add(roadSpawn);
        
        addObject(new CustomerSpawner(), 0, 0);
        //create UI
        //rating
        storeUI = new StoreUI();
        addObject(storeUI, getWidth() / 2, 50); 
        //rating, assign random rating to store 1 and store 2
        //start with maximum 4
        storeUI.addStar(2 + Greenfoot.getRandomNumber(3), 1);
        storeUI.addStar(2 + Greenfoot.getRandomNumber(3), 2);
         
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
        //setPaintOrder(Effect.class, Customer.class,FloatingText.class, Product.class,DisplayUnit.class);
        //setPaintOrder(Fire.class, FloatingText.class,Emoji.class,NightEffect.class,Customer.class, Product.class,DisplayUnit.class);  
        setPaintOrder(Fire.class,FloatingText.class,Emoji.class,NightEffect.class,Customer.class, SaleSign.class,Product.class,DisplayUnit.class);  

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

       SoundManager.startAmbienceSound();
       // music.setVolume(30);
        //music.playLoop();
    }
    
    public void act () 
    {
        //use zSort
        zSort ((ArrayList<Actor>)(getObjects(Actor.class)), this);
        
        handleStorms();
        
        //spawnRestockingTruck();
        if(TimeOfDayManager.getHour() == 23 &&TimeOfDayManager.getMinute() == 30)
        {
          addObject(new RestockingTruck(),600,200);       
        }
        updateSaleItem();

    }
    
    /**
     * Draw filled rectangles to visualize store boundaries for debugging
     * For testing
     */
    private void drawStoreBoundaries() {
        GreenfootImage bg = getBackground();
        
        // Store 1 boundaries (blue/gray store) - FILLED
        bg.setColor(new Color(0, 0, 255, 80)); // Semi-transparent blue
        bg.fillRect(25, 150, 450, 350); // x, y, width, height
        
        // Store 2 boundaries (wooden store) - FILLED
        bg.setColor(new Color(255, 0, 0, 80)); // Semi-transparent red
        bg.fillRect(725, 150, 370, 300); // x, y, width, height
        
        // Add labels
        bg.setColor(Color.WHITE);
        bg.setFont(new Font("Arial", true, false, 20));
        bg.drawString("Store 1", 280, 180);
        bg.drawString("Store 2", 1080, 180);
    }
    
    /**
     * Handles daily storm spawning with random timing.
     * Storms can spawn once per day between 8 AM and 6 PM with a small random chance.
     * The spawn flag resets at midnight. Increments the storm counter when spawned.
     */
    private void handleStorms() {
        int hour = TimeOfDayManager.getHour();
        
        // Reset flag at midnight
        if (hour == 0) {
            stormSpawnedToday = false;
        }
        
        // Spawn storm randomly between 10 AM and 6 PM (ensures it's done before 23:00)
        if (!stormSpawnedToday && hour >= 8 && hour <= 18) {
            // Small random chance each act
            if (Greenfoot.getRandomNumber(1000) == 0) {
                Storm storm = new Storm();
                addObject(storm, getWidth() / 2, getHeight() / 2);
                stormSpawnedToday = true;
                numberOfStorms++;
            }
        }
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
                            // Failed to stock this unit during load; continue without printing.
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
                            // Registered display unit with parent store (no debug print).
                        } else {
                            // Could not assign this display unit to any nearby store.
                        }
                    } catch (Exception e) {
                        // Error during display unit registration; swallowed to avoid noisy output.
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
                    // Registered default display unit with parent store (no debug print).
                } else {
                    // Could not assign this default display unit to any nearby store.
                }
            } catch (Exception e) {
                // Error registering default display unit; swallowed to avoid noisy output.
            }
        }
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
    ////update sale items daily
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

    public int getNumOfStorms(){ return numberOfStorms; }
}





