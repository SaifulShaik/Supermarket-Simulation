import java.io.*;
import java.util.*;

/**
 * Handles saving and loading display unit layout data.
 * Stores type, x, y position for each display unit.
 * 
 * @author AI Assistant
 * @version Nov 8, 2025
 */
public class DisplayUnitData implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String SAVE_FILE = "display_layout.dat";
    
    private final String displayUnitType;  // Class name: "Fridge", "SnackShelf", etc.
    private final int x;
    private final int y;
    
    public DisplayUnitData(String type, int x, int y) {
        this.displayUnitType = type;
        this.x = x;
        this.y = y;
    }
    
    public String getType() {
        return displayUnitType;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    /**
     * Save a list of display unit data to file
     */
    public static void saveLayout(List<DisplayUnitData> units) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
            oos.writeObject(units);
        }
    }
    
    /**
     * Load display unit data from file
     */
    @SuppressWarnings("unchecked")
    public static List<DisplayUnitData> loadLayout() {
        File file = new File(SAVE_FILE);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SAVE_FILE))) {
            return (List<DisplayUnitData>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading layout: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Check if a saved layout exists
     */
    public static boolean hasSavedLayout() {
        return new File(SAVE_FILE).exists();
    }
    
    /**
     * Create a DisplayUnit instance from this data
     */
    public DisplayUnit createDisplayUnit() {
        try {
            Class<?> clazz = Class.forName(displayUnitType);
            return (DisplayUnit) clazz.getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | 
                 NoSuchMethodException | java.lang.reflect.InvocationTargetException e) {
            System.err.println("Error creating display unit: " + displayUnitType + " - " + e.getMessage());
            return null;
        }
    }
}
