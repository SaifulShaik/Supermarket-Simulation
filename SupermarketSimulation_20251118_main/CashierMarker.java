import greenfoot.*;

/**
 * Visual marker to show where cashiers are located in the SettingsWorld editor.
 * Displays a semi-transparent red box with "CASHIER" text.
 * 
 * @author Saiful Shaik
 * @version Nov 8, 2025
 */
public class CashierMarker extends Actor {
    
    public CashierMarker(int width, int height) {
        createImage(width, height);
    }
    
    private void createImage(int width, int height) {
        GreenfootImage img = new GreenfootImage(width, height);
        
        // Fill with semi-transparent red
        img.setColor(new Color(255, 0, 0, 80));
        img.fillRect(0, 0, width, height);
        
        // Draw border
        img.setColor(new Color(255, 0, 0, 200));
        for (int i = 0; i < 3; i++) {
            img.drawRect(i, i, width - 1 - i * 2, height - 1 - i * 2);
        }
        
        // Add text
        Font font = new Font("Arial", true, false, 18);
        img.setFont(font);
        img.setColor(Color.WHITE);
        String text = "CASHIER";
        int textWidth = 70; // approximate
        int x = (width - textWidth) / 2;
        int y = (height - 20) / 2;
        img.drawString(text, x, y + 15);
        
        setImage(img);
    }
}
