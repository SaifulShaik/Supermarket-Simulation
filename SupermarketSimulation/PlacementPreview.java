import greenfoot.*;

/**
 * PlacementPreview - shows a translucent preview image of the display unit
 * under the cursor while placing. It can tint the preview green (valid)
 * or red (invalid).
 */
public class PlacementPreview extends Actor {
    private GreenfootImage baseImage = null;

    public PlacementPreview() {
        // start hidden
        setImage(new GreenfootImage(1,1));
    }

    public void setBaseImage(GreenfootImage img) {
        if (img == null) return;
        // keep a copy of the original so we can re-tint it
        baseImage = new GreenfootImage(img);
        // default to semi-transparent copy
        applyTint(true);
    }

    public void applyTint(boolean valid) {
        if (baseImage == null) return;
        GreenfootImage img = new GreenfootImage(baseImage);
        int w = img.getWidth();
        int h = img.getHeight();

        // Create overlay with semi-transparency
        GreenfootImage overlay = new GreenfootImage(w, h);
        Color tint = valid ? new Color(0, 200, 0, 120) : new Color(200, 0, 0, 140);
        overlay.setColor(tint);
        overlay.fillRect(0, 0, w, h);

        // Draw overlay onto copy; use drawImage which respects alpha in the overlay
        img.drawImage(overlay, 0, 0);

        // Also make the whole preview slightly translucent so underlying world is visible
        img.setTransparency(200);
        setImage(img);
    }

    public void hide() {
        setImage(new GreenfootImage(1,1));
    }
}
