import greenfoot.*;

/**
 * Bullet that travels toward and damages zombies
 */
public class Bullet extends Actor {
    private Zombie target;
    private double speed = 8.0;
    
    public Bullet(Zombie target) {
        this.target = target;
        GreenfootImage img = new GreenfootImage(8, 8);
        img.setColor(Color.YELLOW);
        img.fillOval(0, 0, 8, 8);
        setImage(img);
    }
    
    public void act() {
        if (target == null || target.getWorld() == null) {
            getWorld().removeObject(this);
            return;
        }
        
        moveTowardsTarget();
        checkHit();
    }
    
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