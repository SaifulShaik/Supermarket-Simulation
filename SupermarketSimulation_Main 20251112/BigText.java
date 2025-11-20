import greenfoot.*;

public class BigText extends Actor
{
    public BigText(String text) {
        // Create large dramatic text using Greenfoot's built-in font scaling
        String[] lines = text.split("\n");
        
        // Create image large enough for text
        GreenfootImage image = new GreenfootImage(800, 200);
        image.setColor(new Color(0, 0, 0, 0)); // Transparent background
        image.fill();
        
        int fontSize = 60;
        int y = 70;
        
        for (String line : lines) {
            // Draw shadow
            GreenfootImage shadow = new GreenfootImage(line, fontSize, new Color(0, 0, 0, 100), new Color(0, 0, 0, 0));
            int shadowX = (800 - shadow.getWidth()) / 2 + 3;
            image.drawImage(shadow, shadowX, y + 3);
            
            // Draw main text in gold
            GreenfootImage mainText = new GreenfootImage(line, fontSize, new Color(255, 200, 0), new Color(0, 0, 0, 0));
            int mainX = (800 - mainText.getWidth()) / 2;
            image.drawImage(mainText, mainX, y);
            
            y += 80;
        }
        
        setImage(image);
    }
}