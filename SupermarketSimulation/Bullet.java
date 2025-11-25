import greenfoot.*;

/**
 * Bullet that are shot by soldiers that travels toward and kills zombies
 * Each bullet tracks a specific zombie target and automatically removes itself upon impact or if the target is no longer in the world
 * @author Owen Lee
 * @version Nov 2025
 */
public class Bullet extends Actor {
    private Zombie target;
    private double speed = 8.0;
    
    /**
     * Constructs a new bullet that will home in on the specified zombie target.
     * The bullet appears as a yellow circular projectile.
     * 
     * @param target the zombie that this bullet will track and attempt to hit
     */
    public Bullet(Zombie target) {
        this.target = target;
        GreenfootImage img = new GreenfootImage(8, 8);
        img.setColor(Color.YELLOW);
        img.fillOval(0, 0, 8, 8);
        setImage(img);
    }
    
    /**
     * Main act loop for the bullet.
     * Each cycle, the bullet moves toward its target and checks for collision.
     * If the target no longer exists in the world, the bullet removes itself.
     */
    public void act() {
        if (target == null || target.getWorld() == null) {
            getWorld().removeObject(this);
            return;
        }
        
        moveTowardsTarget();
        checkHit();
    }
    
    /**
     * Moves the bullet toward its target zombie by predicting where it will move
     * Calculates the angle and distance to the target,then moves the bullet at its specified speed. 
     */
    private void moveTowardsTarget() {
        int dx = target.getX() - getX();
        int dy = target.getY() - getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        
        if (distance < speed) {
            setLocation(target.getX(), target.getY());
        } else {
            double angle = Math.atan2(dy, dx);
            int newX = (int)(getX() + Math.cos(angle) * speed);
            int newY = (int)(getY() + Math.sin(angle) * speed);
            setLocation(newX, newY);
        }
    }
    
    /**
     * Checks if the bullet has collided with a zombie.
     * Upon hitting a zombie, removes the zombie's carried items and basket,
     * removes the zombie from the world, and removes this bullet.
     */
    private void checkHit() {
        if (isTouching(Zombie.class)) {
            Zombie zombie = (Zombie) getOneIntersectingObject(Zombie.class);
            if (zombie != null) {
                zombie.removeAllCarriedItems();
                getWorld().removeObject(zombie);
            }
            getWorld().removeObject(this);
        }
    }
}