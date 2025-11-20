import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;
import java.io.IOException;

/**
 * SettingsWorld - An editor for placing and arranging display units.
 * Users can scroll through available display units, place them in free or grid mode,
 * save layouts, and navigate back with unsaved changes confirmation.
 * 
 * @author AI Assistant
 * @version Nov 8, 2025
 */
public class SettingWorld extends World
{
    private static final GreenfootImage bg = new GreenfootImage("background.png");
    
    // Available display unit types
    private static final String[] DISPLAY_UNIT_TYPES = {
        "Fridge", "SnackShelf", "LettuceBin", "CarrotBin", 
        "AppleBin", "OrangeBin", "SteakWarmer", "RawBeefHangers"
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
        
        // Store original layout for reverting
        originalLayout = DisplayUnitData.loadLayout();
        
        setupUI();
        loadExistingLayout();
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
                Greenfoot.setWorld(new SimulationWorld());
            } else if (result == ConfirmationDialog.DialogResult.NO) {
                // User wants to exit without saving - revert changes
                revertToOriginal();
                Greenfoot.setWorld(new SimulationWorld());
            } else if (result == ConfirmationDialog.DialogResult.OK) {
                // Just close the notification
            }
            
            activeDialog = null;
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
        try {
            String typeName = DISPLAY_UNIT_TYPES[currentUnitIndex];
            Class<?> clazz = Class.forName(typeName);
            DisplayUnit unit = (DisplayUnit) clazz.getDeclaredConstructor().newInstance();
            
            if (gridMode) {
                x = (x / GRID_SIZE) * GRID_SIZE;
                y = (y / GRID_SIZE) * GRID_SIZE;
            }
            
            addObject(unit, x, y);
            placedUnits.add(unit);
            hasUnsavedChanges = true;
            
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | 
                 NoSuchMethodException | java.lang.reflect.InvocationTargetException e) {
            System.err.println("Error placing unit: " + e.getMessage());
        }
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
                
                draggedUnit.setLocation(newX, newY);
                hasUnsavedChanges = true;
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
                Greenfoot.setWorld(new SimulationWorld());
            }
        }
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

