import greenfoot.*;

/**
 * A small visual marker that follows a DisplayUnit to show the node in front
 * of the unit. It's lightweight and simply positions itself on top of the
 * target DisplayUnit each act cycle.
 */
public class NodeMarker extends Actor {
    private DisplayUnit targetUnit;
    private int yOffset = -8; // draw slightly above the unit center

    public NodeMarker(DisplayUnit unit) {
        this.targetUnit = unit;
        createMarkerImage();
    }

    private void createMarkerImage() {
        GreenfootImage img = new GreenfootImage(12, 12);
        img.setColor(new Color(255, 0, 0));
        img.setTransparency(200);
        img.fillOval(0, 0, 12, 12);
        setImage(img);
    }

    @Override
    public void act() {
        if (targetUnit == null || targetUnit.getWorld() == null) {
            // remove self if the target is gone
            if (getWorld() != null) getWorld().removeObject(this);
            return;
        }

        // follow the target unit's position
        setLocation(targetUnit.getX(), targetUnit.getY() + yOffset);
    }
}
