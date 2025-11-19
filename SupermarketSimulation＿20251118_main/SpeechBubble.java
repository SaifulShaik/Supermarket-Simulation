import greenfoot.*;

/**
 * Speech bubble that appears above characters
 */
class SpeechBubble extends Actor
{
    public SpeechBubble(String text) {
        createBubble(text);
    }

    private void createBubble(String text) {
        int bubbleWidth = 200;
        int bubbleHeight = 100;

        // Create the bubble image
        GreenfootImage bubble = new GreenfootImage(bubbleWidth, bubbleHeight);

        // Draw white oval for bubble
        bubble.setColor(Color.WHITE);
        bubble.fillOval(0, 0, bubbleWidth, bubbleHeight);

        // Draw black border
        bubble.setColor(Color.BLACK);
        bubble.drawOval(0, 0, bubbleWidth - 1, bubbleHeight - 1);

        // Draw text
        bubble.setFont(new Font("Arial", false, false, 14));
        bubble.setColor(Color.BLACK);

        // Split text by newlines and draw each line
        String[] lines = text.split("\n");
        int y = 30; // Starting y position
        for (String line : lines) {
            bubble.drawString(line, 20, y);
            y += 20; // Space between lines
        }

        setImage(bubble);
    }
}