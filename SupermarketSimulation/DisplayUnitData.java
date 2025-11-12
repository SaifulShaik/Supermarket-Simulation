import java.io.*;
import java.util.*;

/**
 * Handles saving and loading display unit layout data.
 * Stores type, x, y position for each display unit.
 * Now uses JSON format so layouts can be tracked in Git and shared across team members.
 * 
 * @author Saiful Shaik
 * @version Nov 10, 2025
 */
public class DisplayUnitData {
    private static final String SAVE_FILE = "display_layout.json";
    
    private String displayUnitType;  // Class name: "Fridge", "SnackShelf", etc.
    private int x;
    private int y;
    
    // Default constructor for JSON deserialization
    public DisplayUnitData() {}
    
    public DisplayUnitData(String type, int x, int y) {
        this.displayUnitType = type;
        this.x = x;
        this.y = y;
    }
    
    // Getters and setters for JSON serialization
    public String getDisplayUnitType() {
        return displayUnitType;
    }
    
    public void setDisplayUnitType(String type) {
        this.displayUnitType = type;
    }
    
    public int getX() {
        return x;
    }
    
    public void setX(int x) {
        this.x = x;
    }
    
    public int getY() {
        return y;
    }
    
    public void setY(int y) {
        this.y = y;
    }
    
    /**
     * Save a list of display unit data to JSON file (Git-trackable)
     */
    public static void saveLayout(List<DisplayUnitData> units) throws IOException {
        StringBuilder json = new StringBuilder();
        json.append("[\n");
        
        for (int i = 0; i < units.size(); i++) {
            DisplayUnitData unit = units.get(i);
            json.append("  {\n");
            json.append("    \"displayUnitType\": \"").append(unit.displayUnitType).append("\",\n");
            json.append("    \"x\": ").append(unit.x).append(",\n");
            json.append("    \"y\": ").append(unit.y).append("\n");
            json.append("  }");
            if (i < units.size() - 1) {
                json.append(",");
            }
            json.append("\n");
        }
        
        json.append("]\n");
        
        try (FileWriter writer = new FileWriter(SAVE_FILE)) {
            writer.write(json.toString());
        }
    }
    
    /**
     * Load display unit data from JSON file
     */
    public static List<DisplayUnitData> loadLayout() {
        File file = new File(SAVE_FILE);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(SAVE_FILE))) {
            StringBuilder jsonContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonContent.append(line).append("\n");
            }
            
            return parseJSON(jsonContent.toString());
        } catch (IOException e) {
            System.err.println("Error loading layout: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Simple JSON parser for our specific format (avoids external dependencies)
     */
    private static List<DisplayUnitData> parseJSON(String json) {
        List<DisplayUnitData> units = new ArrayList<>();
        
        // Remove whitespace and array brackets
        json = json.trim();
        if (json.startsWith("[")) json = json.substring(1);
        if (json.endsWith("]")) json = json.substring(0, json.length() - 1);
        
        // Split by object boundaries
        String[] objects = json.split("\\},\\s*\\{");
        
        for (String obj : objects) {
            // Clean up the object string
            obj = obj.replace("{", "").replace("}", "").trim();
            if (obj.isEmpty()) continue;
            
            String type = null;
            int x = 0, y = 0;
            
            // Parse key-value pairs
            String[] pairs = obj.split(",");
            for (String pair : pairs) {
                pair = pair.trim();
                if (pair.contains("displayUnitType")) {
                    type = extractStringValue(pair);
                } else if (pair.contains("\"x\"")) {
                    x = extractIntValue(pair);
                } else if (pair.contains("\"y\"")) {
                    y = extractIntValue(pair);
                }
            }
            
            if (type != null) {
                units.add(new DisplayUnitData(type, x, y));
            }
        }
        
        return units;
    }
    
    private static String extractStringValue(String pair) {
        int start = pair.indexOf("\"", pair.indexOf(":")) + 1;
        int end = pair.lastIndexOf("\"");
        if (start > 0 && end > start) {
            return pair.substring(start, end);
        }
        return "";
    }
    
    private static int extractIntValue(String pair) {
        String[] parts = pair.split(":");
        if (parts.length == 2) {
            try {
                return Integer.parseInt(parts[1].trim());
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
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
