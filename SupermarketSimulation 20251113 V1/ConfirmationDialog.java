import greenfoot.*;

/**
 * A reusable dialog box for displaying messages and optional YES/NO buttons.
 * Can be used for confirmations and notifications.
 * 
 * @author Saiful Shaik
 * @version Nov 8, 2025
 */
public class ConfirmationDialog extends Actor {
    private String message;
    private boolean hasButtons;
    private Button yesButton;
    private Button noButton;
    private int width;
    private int height;
    private DialogResult result;
    private boolean isActive;
    
    public enum DialogResult {
        NONE, YES, NO, OK
    }
    
    /**
     * Create a simple notification dialog with just a message
     */
    public ConfirmationDialog(String message) {
        this(message, false);
    }
    
    /**
     * Create a dialog with YES/NO buttons
     */
    public ConfirmationDialog(String message, boolean hasButtons) {
        this.message = message;
        this.hasButtons = hasButtons;
        this.result = DialogResult.NONE;
        this.isActive = true;
        this.width = 400;
        this.height = hasButtons ? 200 : 120;
        updateImage();
    }
    
    @Override
    protected void addedToWorld(World world) {
        if (hasButtons) {
            // Add YES and NO buttons
            int centerX = getX();
            int centerY = getY();
            
            yesButton = new Button("YES", 100, 40);
            noButton = new Button("NO", 100, 40);
            
            world.addObject(yesButton, centerX - 70, centerY + 50);
            world.addObject(noButton, centerX + 70, centerY + 50);
        }
    }
    
    public void act() {
        if (!isActive) return;
        
        if (hasButtons) {
            // Check button clicks
            if (yesButton != null && yesButton.wasClicked()) {
                result = DialogResult.YES;
                close();
            } else if (noButton != null && noButton.wasClicked()) {
                result = DialogResult.NO;
                close();
            }
        } else {
            // Simple notification - close on click anywhere on dialog
            if (Greenfoot.mouseClicked(this)) {
                result = DialogResult.OK;
                close();
            }
        }
    }
    
    /**
     * Get the user's response
     */
    public DialogResult getResult() {
        return result;
    }
    
    /**
     * Check if the dialog is still active
     */
    public boolean isActive() {
        return isActive;
    }
    
    /**
     * Close the dialog and remove from world
     */
    private void close() {
        isActive = false;
        World world = getWorld();
        if (world != null) {
            if (yesButton != null) world.removeObject(yesButton);
            if (noButton != null) world.removeObject(noButton);
            world.removeObject(this);
        }
    }
    
    /**
     * Create the dialog's visual appearance
     */
    private void updateImage() {
        GreenfootImage img = new GreenfootImage(width, height);
        
        // Draw semi-transparent background
        img.setColor(new Color(0, 0, 0, 200));
        img.fillRect(0, 0, width, height);
        
        // Draw border
        img.setColor(new Color(100, 149, 237));
        for (int i = 0; i < 3; i++) {
            img.drawRect(i, i, width - 1 - i * 2, height - 1 - i * 2);
        }
        
        // Draw message text
        img.setColor(Color.WHITE);
        Font font = new Font("Arial", true, false, 20);
        img.setFont(font);
        
        // Split message into lines if too long
        String[] lines = splitMessage(message, 35);
        int startY = hasButtons ? 40 : (height - lines.length * 25) / 2;
        
        for (int i = 0; i < lines.length; i++) {
            GreenfootImage textImg = new GreenfootImage(lines[i], 20, Color.WHITE, new Color(0, 0, 0, 0));
            int x = (width - textImg.getWidth()) / 2;
            int y = startY + i * 25;
            img.drawImage(textImg, x, y);
        }
        
        setImage(img);
    }
    
    /**
     * Split a message into multiple lines
     */
    private String[] splitMessage(String msg, int maxCharsPerLine) {
        if (msg.length() <= maxCharsPerLine) {
            return new String[]{msg};
        }
        
        String[] words = msg.split(" ");
        java.util.List<String> lines = new java.util.ArrayList<>();
        StringBuilder currentLine = new StringBuilder();
        
        for (String word : words) {
            if (currentLine.length() + word.length() + 1 <= maxCharsPerLine) {
                if (currentLine.length() > 0) currentLine.append(" ");
                currentLine.append(word);
            } else {
                lines.add(currentLine.toString());
                currentLine = new StringBuilder(word);
            }
        }
        if (currentLine.length() > 0) {
            lines.add(currentLine.toString());
        }
        
        return lines.toArray(new String[lines.size()]);
    }
}
