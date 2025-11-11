import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)  
import java.util.ArrayList;
import java.util.List;

/**
 * Write a description of class MyWorld here.
 * 
 * @author Saiful Shaik, Owen Kung
 * @version Modified: Nov, 8, 2025
 */
public class SimulationWorld extends World
{
    //public static final int WORLD_WIDTH = 1048;
    //public static final int WORLD_HEIGHT = 514;
    
    // Product name constants
    public static final String PRODUCT_APPLE = "Apple";
    public static final String PRODUCT_ORANGE = "Orange";
    public static final String PRODUCT_CARROT = "Carrot";
    public static final String PRODUCT_LETTUCE = "Lettuce";
    public static final String PRODUCT_COKE = "Coke";
    public static final String PRODUCT_SPRITE = "Sprite";
    public static final String PRODUCT_FANTA = "Fanta";
    public static final String PRODUCT_WATER = "Water";
    public static final String PRODUCT_DORITOS = "Doritos";
    public static final String PRODUCT_LAYS = "Lays";
    public static final String PRODUCT_RUFFLES = "Ruffles";
    public static final String PRODUCT_STEAK = "Steak";
    public static final String PRODUCT_RAWBEEF = "Raw Beef";
    public static final String PRODUCT_CANDY = "Candy";
    public static final String PRODUCT_DRUM_STICK = "Drumstick";
    public static final String PRODUCT_XING_RAMEN = "Xing Ramen";
    public static final String PRODUCT_JIN_RAMEN = "Jin Ramen";
    public static final String PRODUCT_NISSIN = "Raw Beef";
    
    
    // Grid settings
    public static final int GRID_CELL_SIZE = 20; // pixels per cell
    public static final int GRID_START_Y = 100;  // Grid starts at y=100
    
    private static final GreenfootImage bg = new GreenfootImage("background.png");
    public SimulationWorld()
    { 
        super(bg.getWidth(), bg.getHeight(), 1);
        setBackground(bg); 
        
        // Draw world-wide grid overlay
        drawWorldGrid();
        
        addObject(new CustomerSpawner(), 0, 0);
        addObject(new StoreUI(), getWidth()/2, 50);
        
        // Enable stocking in simulation mode
        DisplayUnit.setEnableStocking(true);
        
        addObject(new Store(480, 480, 20, true), 480, 220); // x: 40 - 480 y: 220 - 480
        addObject(new Store(360, 120, 20, false), getWidth() - 240, 360);
        
        // add the Cashiers to store 1
        addObject(new Cashier(), getWidth()/2 + 200, getHeight()/2);
        addObject(new Cashier(), getWidth()/2 + 300, getHeight()/2);
        
        // add cashier to store 2
        addObject(new Store2Cashier(), getWidth()/2-230, getHeight()/2+130);
        addObject(new Store2Cashier(), getWidth()/2-330, getHeight()/2+130);
        
        // add the butcher
        Butcher butcher = new Butcher();
        addObject(butcher, 975, 260);
        
        // Load display units from saved layout, or use default if no saved layout exists
        loadDisplayUnits();

        // After display units are created and added to the world, update each DisplayUnit's
        // customer node based on its world position
        for (DisplayUnit unit : getObjects(DisplayUnit.class)) {
            unit.updateCustomerNode();
        }

        // After display units are created and added to the world, refresh
        // all stores so they map product nodes from the actual display unit
        // positions (instead of relying on hard-coded positions).
        for (Store s : getObjects(Store.class)) {
            s.updateProductLocations();
        }

        // Add visual markers for each display unit so nodes in front of units are visible
        addNodeMarkersForDisplayUnits();
        
        //set paint order for products and shelves to properly display
        setPaintOrder(
            Customer.class,                                      // customers (very front)
            NodeMarker.class,                                    // node markers (above display units)
            Doritos.class, Lays.class, Ruffles.class,           // snacks (front)
            Coke.class, Water.class, Sprite.class, Fanta.class, // drinks (middle)
            Lettuce.class,Carrot.class,Apple.class,Orange.class,Steak.class,RawBeef.class,
            SnackShelf.class, Fridge.class, LettuceBin.class, CarrotBin.class, AppleBin.class,OrangeBin.class, SteakWarmer.class,RawBeefHangers.class           // furniture (back)
            );
        
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
        addObject(new RawBeefHangers(),920,175);
    }
    
    public void act () 
    {
        //use zSort
        zSort ((ArrayList<Actor>)(getObjects(Actor.class)), this);
        
        //spawnRestockingTruck();
    } 
    /*
    private void spawnRestockingTruck()
    {
        truckDelay++;
        if(truckDelay>=420)
        {
            addObject(new RestockingTruck(),600,200);
            truckDelay=0;
        }
    }
    */
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
}
