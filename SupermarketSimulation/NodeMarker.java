import greenfoot.*;

/**
 * A small visual marker that follows a DisplayUnit to show the node in front
 * of the unit. It's lightweight and simply positions itself on top of the
 * target DisplayUnit each act cycle.
 */
public class NodeMarker extends Actor {
    private DisplayUnit targetUnit;
    private Node targetNode;
    private int yOffset = 50; // draw slightly above the unit center

    public NodeMarker(DisplayUnit unit) {
        this.targetUnit = unit;
        this.targetNode = null;
        createMarkerImage();
    }

    /** Create a marker that points at a specific Node (world coordinates). */
    public NodeMarker(Node node) {
        this.targetNode = node;
        this.targetUnit = null;
        createMarkerImage();
    }

    private void createMarkerImage() {
        GreenfootImage img = new GreenfootImage(12, 12);
        img.setColor(new Color(255, 0, 0));
        img.setTransparency(200);
        img.fillOval(0, 0, 12, 12);
        setImage(img);
    }

    public void act() {
        // If this marker was built for a DisplayUnit, follow the unit as before
        if (targetUnit != null) {
            if (targetUnit.getWorld() == null) {
                if (getWorld() != null) getWorld().removeObject(this);
                return;
            }
            setLocation(targetUnit.getX(), targetUnit.getY() + yOffset);
            return;
        }

        // If this marker targets a Node, position it at the node's world coordinates
        if (targetNode != null) {
            // Nodes hold absolute world coordinates
            if (getWorld() == null) return; // world is required to be present
            setLocation(targetNode.getX(), targetNode.getY());
            return;
        }

        // If neither target exists, remove ourselves
        if (getWorld() != null) getWorld().removeObject(this);
    }
}
