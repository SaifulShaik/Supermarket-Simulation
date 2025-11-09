import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;
import java.io.IOException;

/**
 * SettingsWorld - An editor for placing and arranging display units.
 * Users can scroll through available display units, place them in free or grid mode,
 * save layouts, and navigate back with unsaved changes confirmation.
 * 
 * @author Saiful Shaik
 * @version Nov 8, 2025
 */
public class SettingWorld extends World
{
    private static final GreenfootImage bg = new GreenfootImage("background.png");
    
    // Cashier positions (hardcoded from SimulationWorld) - areas where display units cannot be placed
    private static class CashierZone {
        int x, y, width, height;
        CashierZone(int x, int y, int w, int h) {
            this.x = x; this.y = y; this.width = w; this.height = h;
        }
    }
    
    private List<CashierZone> cashierZones;
    
    // Available display unit types
    private static final String[] DISPLAY_UNIT_TYPES = {
        "Fridge", "SnackShelf", "LettuceBin", "CarrotBin", 
        "AppleBin", "OrangeBin", "SteakWarmer"
    };
    
    // UI Components
    private Button saveButton;
    private Button backButton;
    private Button gridModeButton;
    private Button prevUnitButton;
    private Button nextUnitButton;
    private Label unitTypeLabel;
    private Label instructionLabel;
    
    // Editor state
    private int currentUnitIndex = 0;
    private boolean gridMode = true;
    private static final int GRID_SIZE = 20;
    private DisplayUnit draggedUnit = null;
    private boolean hasUnsavedChanges = false;
    private List<DisplayUnit> placedUnits = new ArrayList<>();
    private ConfirmationDialog activeDialog = null;
    private List<DisplayUnitData> originalLayout = new ArrayList<>();
    
    /**
     * Constructor for objects of class SettingWorld.
     */
    public SettingWorld()
    {    
        super(bg.getWidth(), bg.getHeight(), 1); 
        setBackground(bg);
        
        // Disable stocking in editor mode
        DisplayUnit.setEnableStocking(false);
        
        // Define cashier zones (approximate positions based on SimulationWorld)
        setupCashierZones();
        
        // Store original layout for reverting
        originalLayout = DisplayUnitData.loadLayout();
        
        setupUI();
        loadExistingLayout();
    }
    
    /**
     * Define restricted areas where cashiers are located
     */
    private void setupCashierZones() {
        cashierZones = new ArrayList<>();
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        
        // Store 1 cashiers (2 cashiers at x+200 and x+300, y is center)
        // Covering area from x+175 to x+325, centered at y
        cashierZones.add(new CashierZone(centerX + 250, centerY, 150, 60));
        
        // Store 2 cashiers (2 cashiers at x-230 and x-330, y+130)
        // Covering area from x-355 to x-205, at y+130
        cashierZones.add(new CashierZone(centerX - 280, centerY + 130, 150, 60));
        
        // Butcher area (at x+380, y is center)
        cashierZones.add(new CashierZone(centerX + 380, centerY, 80, 80));
    }
    
    /**
     * Set up all UI components
     */
    private void setupUI() {
        // Save and Back buttons
        saveButton = new Button("SAVE", 120, 45);
        backButton = new Button("BACK", 120, 45);
        addObject(saveButton, getWidth() - 70, 30);
        addObject(backButton, 70, 30);
        
        // Grid mode toggle
        gridModeButton = new Button("Grid: ON", 140, 45);
        addObject(gridModeButton, getWidth() / 2, 30);
        
        // Unit selection buttons
        prevUnitButton = new Button("<", 50, 45);
        nextUnitButton = new Button(">", 50, 45);
        addObject(prevUnitButton, 100, getHeight() - 50);
        addObject(nextUnitButton, 300, getHeight() - 50);
        
        // Labels
        unitTypeLabel = new Label(DISPLAY_UNIT_TYPES[currentUnitIndex], 24);
        addObject(unitTypeLabel, 200, getHeight() - 50);
        
        instructionLabel = new Label("Click to place. Drag to move. Right-click to delete.", 18);
        addObject(instructionLabel, getWidth() / 2, getHeight() - 30);
        
        // Add visual indicators for cashier zones
        showCashierZones();
    }
    
    /**
     * Display semi-transparent overlays to show cashier zones
     */
    private void showCashierZones() {
        // Add actual cashier objects so you can see exactly where they are
        // add the Cashiers to store 1
        addObject(new Cashier(), getWidth()/2 + 200, getHeight()/2);
        addObject(new Cashier(), getWidth()/2 + 300, getHeight()/2);
        
        // add cashier to store 2
        addObject(new Store2Cashier(), getWidth()/2-230, getHeight()/2+130);
        addObject(new Store2Cashier(), getWidth()/2-330, getHeight()/2+130);
        
        // add the butcher
        Butcher butcher = new Butcher();
        addObject(butcher, getWidth()/2+ 380, getHeight()/2);
    }
    
    /**
     * Load existing layout from file if available
     */
    private void loadExistingLayout() {
        List<DisplayUnitData> savedUnits = DisplayUnitData.loadLayout();
        for (DisplayUnitData data : savedUnits) {
            DisplayUnit unit = data.createDisplayUnit();
            if (unit != null) {
                addObject(unit, data.getX(), data.getY());
                placedUnits.add(unit);
            }
        }
    }
    
    public void act() {
        if (activeDialog != null) {
            handleDialogResponse();
            return;
        }
        
        handleUnitSelection();
        handleGridModeToggle();
        handleUnitPlacement();
        handleUnitDragging();
        handleUnitDeletion();
        handleSaveButton();
        handleBackButton();
    }
    
    /**
     * Handle dialog responses
     */
    private void handleDialogResponse() {
        if (!activeDialog.isActive()) {
            ConfirmationDialog.DialogResult result = activeDialog.getResult();
            
            if (result == ConfirmationDialog.DialogResult.YES) {
                // User wants to save before exiting
                saveLayout();
                cleanupAndExit();
            } else if (result == ConfirmationDialog.DialogResult.NO) {
                // User wants to exit without saving - revert changes
                revertToOriginal();
                cleanupAndExit();
            } else if (result == ConfirmationDialog.DialogResult.OK) {
                // Just close the notification - reset activeDialog so user can continue
                activeDialog = null;
            }
            
            // Reset dialog for save/back flow
            if (result == ConfirmationDialog.DialogResult.YES || result == ConfirmationDialog.DialogResult.NO) {
                activeDialog = null;
            }
        }
    }
    
    /**
     * Handle unit type selection (prev/next buttons)
     */
    private void handleUnitSelection() {
        if (prevUnitButton.wasClicked()) {
            currentUnitIndex = (currentUnitIndex - 1 + DISPLAY_UNIT_TYPES.length) % DISPLAY_UNIT_TYPES.length;
            updateUnitLabel();
        }
        
        if (nextUnitButton.wasClicked()) {
            currentUnitIndex = (currentUnitIndex + 1) % DISPLAY_UNIT_TYPES.length;
            updateUnitLabel();
        }
    }
    
    /**
     * Update the unit type label
     */
    private void updateUnitLabel() {
        unitTypeLabel.setValue(DISPLAY_UNIT_TYPES[currentUnitIndex]);
    }
    
    /**
     * Handle grid mode toggle
     */
    private void handleGridModeToggle() {
        if (gridModeButton.wasClicked()) {
            gridMode = !gridMode;
            gridModeButton.setText(gridMode ? "Grid: ON" : "Grid: OFF");
        }
    }
    
    /**
     * Handle placing new units
     */
    private void handleUnitPlacement() {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse != null && Greenfoot.mouseClicked(null)) {
            // Check if clicked on empty space (not on buttons or existing units)
            if (!isClickOnUI(mouse) && getObjectsAt(mouse.getX(), mouse.getY(), DisplayUnit.class).isEmpty()) {
                placeNewUnit(mouse.getX(), mouse.getY());
            }
        }
    }
    
    /**
     * Place a new display unit at the specified location
     */
    private void placeNewUnit(int x, int y) {
        if (gridMode) {
            x = (x / GRID_SIZE) * GRID_SIZE;
            y = (y / GRID_SIZE) * GRID_SIZE;
        }
        
        // Check if placement would overlap with cashier zones - silently block without popup
        if (isOnCashier(x, y, 50)) {
            return; // Don't place, just return silently
        }
        
        try {
            String typeName = DISPLAY_UNIT_TYPES[currentUnitIndex];
            Class<?> clazz = Class.forName(typeName);
            DisplayUnit unit = (DisplayUnit) clazz.getDeclaredConstructor().newInstance();
            
            addObject(unit, x, y);
            placedUnits.add(unit);
            hasUnsavedChanges = true;
            
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | 
                 NoSuchMethodException | java.lang.reflect.InvocationTargetException e) {
            System.err.println("Error placing unit: " + e.getMessage());
        }
    }
    
    /**
     * Check if a position overlaps with any cashier zone
     */
    private boolean isOnCashier(int x, int y, int radius) {
        for (CashierZone zone : cashierZones) {
            // Check if the circle (x,y,radius) intersects with the rectangle zone
            int closestX = Math.max(zone.x - zone.width/2, Math.min(x, zone.x + zone.width/2));
            int closestY = Math.max(zone.y - zone.height/2, Math.min(y, zone.y + zone.height/2));
            
            int distanceX = x - closestX;
            int distanceY = y - closestY;
            int distanceSquared = distanceX * distanceX + distanceY * distanceY;
            
            if (distanceSquared < (radius * radius)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Handle dragging existing units
     */
    private void handleUnitDragging() {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        
        if (mouse != null && Greenfoot.mousePressed(null)) {
            List<DisplayUnit> unitsAtMouse = getObjectsAt(mouse.getX(), mouse.getY(), DisplayUnit.class);
            if (!unitsAtMouse.isEmpty()) {
                draggedUnit = unitsAtMouse.get(0);
            }
        }
        
        if (draggedUnit != null && Greenfoot.mouseDragged(null)) {
            if (mouse != null) {
                int newX = mouse.getX();
                int newY = mouse.getY();
                
                if (gridMode) {
                    newX = (newX / GRID_SIZE) * GRID_SIZE;
                    newY = (newY / GRID_SIZE) * GRID_SIZE;
                }
                
                // Check if new position would overlap with cashier - silently block movement
                if (!isOnCashier(newX, newY, 50)) {
                    draggedUnit.setLocation(newX, newY);
                    hasUnsavedChanges = true;
                }
                // If on cashier, just don't move it - no popup
            }
        }
        
        if (Greenfoot.mouseDragEnded(null)) {
            draggedUnit = null;
        }
    }
    
    /**
     * Handle right-click deletion of units
     */
    private void handleUnitDeletion() {
        if (Greenfoot.mouseClicked(null)) {
            MouseInfo mouse = Greenfoot.getMouseInfo();
            if (mouse != null && mouse.getButton() == 3) { // Right click
                List<DisplayUnit> unitsAtMouse = getObjectsAt(mouse.getX(), mouse.getY(), DisplayUnit.class);
                if (!unitsAtMouse.isEmpty()) {
                    DisplayUnit unit = unitsAtMouse.get(0);
                    removeObject(unit);
                    placedUnits.remove(unit);
                    hasUnsavedChanges = true;
                }
            }
        }
    }
    
    /**
     * Handle save button click
     */
    private void handleSaveButton() {
        if (saveButton.wasClicked()) {
            saveLayout();
            activeDialog = new ConfirmationDialog("World Saved!");
            addObject(activeDialog, getWidth() / 2, getHeight() / 2);
        }
    }
    
    /**
     * Handle back button click
     */
    private void handleBackButton() {
        if (backButton.wasClicked()) {
            if (hasUnsavedChanges) {
                // Show confirmation dialog
                activeDialog = new ConfirmationDialog("World not Saved! Would you like to save it?", true);
                addObject(activeDialog, getWidth() / 2, getHeight() / 2);
            } else {
                // No unsaved changes, go back directly
                cleanupAndExit();
            }
        }
    }
    
    /**
     * Cleanup display units before exiting to prevent stocking issues
     */
    private void cleanupAndExit() {
        // Remove all placed display units to prevent them from stocking when enableStocking is set to true
        for (DisplayUnit unit : new ArrayList<>(placedUnits)) {
            removeObject(unit);
        }
        placedUnits.clear();
        
        Greenfoot.setWorld(new SimulationWorld());
    }
    
    /**
     * Save the current layout to file
     */
    private void saveLayout() {
        List<DisplayUnitData> layoutData = new ArrayList<>();
        
        for (DisplayUnit unit : placedUnits) {
            String typeName = unit.getClass().getSimpleName();
            layoutData.add(new DisplayUnitData(typeName, unit.getX(), unit.getY()));
        }
        
        try {
            DisplayUnitData.saveLayout(layoutData);
            hasUnsavedChanges = false;
            originalLayout = new ArrayList<>(layoutData); // Update original
        } catch (IOException e) {
            System.err.println("Error saving layout: " + e.getMessage());
        }
    }
    
    /**
     * Revert to the original layout (discard changes)
     */
    private void revertToOriginal() {
        try {
            DisplayUnitData.saveLayout(originalLayout);
        } catch (IOException e) {
            System.err.println("Error reverting layout: " + e.getMessage());
        }
    }
    
    /**
     * Check if a click was on UI elements
     */
    private boolean isClickOnUI(MouseInfo mouse) {
        int y = mouse.getY();
        
        // Check if click is on any button or label area
        return y < 80 || y > getHeight() - 80;
    }
}

