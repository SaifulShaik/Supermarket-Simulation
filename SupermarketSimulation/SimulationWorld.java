import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)  
import java.util.ArrayList;
import java.util.List;

/**
 * <h1>Supermarket Simulation - Dual Store Competition Game</h1>
 * 
 * <p>This simulation models two competing supermarkets in a single world environment. Customers spawn,
 * navigate via pathfinding nodes, browse display units, pick products based on their shopping preferences,
 * queue at cashiers, and leave. The simulation includes dynamic time-based events (day/night cycle, storms,
 * restocking trucks), customer behaviour variants, and special actors (zombies and soldiers).</p>
 * 
 * <h2>What am I about to watch?</h2>
 * <p>You'll see two competing stores: Store 1 (blue/gray) on the left and Store 2 (wooden) on the right. 
 * Different customer types spawn during store hours (8 AM - 11 PM) and select products based on their behaviour—
 * some hunt for bargains, others buy in bulk, and some make impulse purchases. Daily sales are announced at 
 * 6:59 AM, restocking trucks arrive at 11:30 PM, and random storms may occur between 8 AM and 6 PM. The world 
 * transitions between day and night with matching visual and audio changes. Occasionally, zombies spawn and 
 * disrupt normal shopping, triggering the deployment of soldiers to eliminate them. Store ratings are displayed 
 * at the top of the screen and reflect customer satisfaction and store management.</p>
 * 
 * <h2>World and Local Effects</h2>
 * <p><strong>Global (World-wide) Effects</strong> affect the entire simulation. Storms spawn randomly once per 
 * day (8 AM - 6 PM) and overlay the entire world with visual and sound effects. The NightEffect darkens the 
 * entire world during night hours. The Day/Night Cycle, managed by TimeOfDayManager, switches ambient sounds 
 * and lighting for the whole simulation. RestockingTrucks arrive at 11:30 PM and restock all display units 
 * across both stores. Daily Sales are selected at 6:59 AM and affect all matching products in both stores.</p>
 * 
 * <p><strong>Local (Targeted) Effects</strong> affect specific actors or areas. SaleSign visual indicators appear 
 * above display units carrying sale items. Fire and Explosion effects are single-location visuals that affect 
 * only nearby actors. FloatingText shows individual indicators (price changes, low stock warnings) attached to 
 * specific display units. Emoji mood indicators (happy, mad, neutral) appear above individual customers. Bullets 
 * are fired by soldiers and target specific zombies only.</p>
 * 
 * <h2>Customer Types and Movement Behavior</h2>
 * <p>Customers navigate using a node-based pathfinding system. They do NOT change lanes in the traditional sense
 * (this is not a vehicle simulation), but they do navigate between different store sections. RegularShoppers 
 * exhibit standard behaviour, picking random products and navigating to cashiers. BargainShoppers prioritize 
 * sale items and navigate to display units with active sales first. BulkShoppers purchase larger quantities 
 * and prefer multi-row units and bins. ImpulseShoppers make spontaneous decisions and may grab nearby items 
 * even if not on their shopping list. Zombies do NOT follow normal customer rules—they wander randomly and may 
 * attack other customers or damage products. Soldiers spawn when zombies are detected, navigate to their assigned 
 * store entrance, hunt zombies within that store, then exit.</p>
 * 
 * <h2>Sound Management</h2>
 * <p>Audio is centrally controlled via the SoundManager class. Day mode features cheerful background music 
 * while night mode has quieter, atmospheric audio. Effect sounds include truck arrivals, butcher chopping, 
 * soldier gunshots, explosions, and storm rumbles. The world's stopped() and started() methods attempt to 
 * pause and resume long-running sounds to match the simulation state.</p>
 * 
 * <h2>Credits and Borrowed Assets</h2>
 * <p>Background images were custom-created by the team. Product images were sourced from free icon sites 
 * including Flaticon.com and Freepik.com. Customer and zombie sprites were modified from OpenGameArt.org 
 * using CC0 and CC-BY licenses. Sound effects came from Freesound.org with contributions from various CC0 
 * and CC-BY artists. Pathfinding logic was adapted from Greenfoot community examples and A* algorithm 
 * resources. The Z-sort algorithm was inspired by Mr. Cohen's Vehicle Simulation assignment template, 
 * implementing depth-sorting based on Y-coordinate positioning.</p>
 * 
 * <h2>Known Bugs</h2>
 * <p>Occasionally, long-running sounds (truck, ambience) do not fully stop when the simulation is paused. 
 * The workaround is to restart the world. Rarely, a customer may get stuck if a display unit is removed 
 * while they are navigating toward it—they will eventually time out and leave. Zombies may overlap with 
 * other actors when spawn density is high. Visual Z-sorting handles this, but collision detection may not 
 * be pixel-perfect. On very tall display units, the sale sign may render slightly off-center. This is 
 * cosmetic and does not affect gameplay.</p>
 * 
 * <h2>Version Notes</h2>
 * <p>V2025_11 - Initial release with dual-store competition, customer variants, zombies, soldiers, and 
 * time-based events. Includes dynamic display unit placement and saving/loading of store layouts. 
 * Implements Z-sort for proper visual layering, inspired by Mr. Cohen's Vehicle Simulation depth-sorting.</p>
 * 
 * @author Saiful Shaik, Owen Kung, Joe Zhuo, Angelina Zhou, Owen Lee
 * @version November 24, 2025
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





