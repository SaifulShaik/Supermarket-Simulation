import greenfoot.*;
import java.util.ArrayList;
/**
 * Soldier that enters a store, kills all zombies, and leaves
 * 
 * @author Joe
 * @version November 2025
 */
public class Soldier extends Customer {
    private boolean flipped = false;
    private int shootCooldown = 0;
    private boolean atEntrance = false;
    private boolean missionComplete = false;
    
    /**
     * Constructor that assigns soldier to a specific store
     * Soldier spawns at the road node and navigates to assigned store
     */
    public Soldier(Node startNode, Store assignedStore) {
        super(3, 0, startNode, 0, 0, 10000);
        
        GreenfootImage img = new GreenfootImage("Soldier.png");
        img.scale(80, 75);
        setImage(img);
        
        // Clear shopping behavior
        this.shoppingList.clear();
        this.cart.clear();
        
        // Pre-assign the store instead of choosing randomly
        this.store = assignedStore;
        this.targetNode = assignedStore.getEntranceNode();
        
        System.out.println("[Soldier] Created, assigned to " + assignedStore.name + ", spawning from road");
    }
    
    @Override
    public void act() {
        if (getWorld() == null) return;
    
        // Navigate from road to store entrance
        if (targetNode != null && !atEntrance) {
            boolean arrived = moveToNode(targetNode, 0, 0);
            if (arrived && currentNode == store.getEntranceNode()) {
                atEntrance = true;
                targetNode = null;
                System.out.println("[Soldier] Arrived at " + store.name + " entrance, starting mission!");
            }
            return;
        }
        
        // If mission complete, leave the store
        if (missionComplete) {
            // Check if we've reached the final exit node
            if (currentNode != null && currentNode.checkIsEnd()) {
                getWorld().removeObject(this);
                return;
            }
            // Use Customer's move(true) to navigate toward exit nodes
            move(true);
            return;
        }
        
        // Only shoot if at entrance
        if (!atEntrance) {
            return;
        }
        
        // Get zombies in MY store only
        ArrayList<Zombie> allZombies = (ArrayList<Zombie>) getWorld().getObjects(Zombie.class);
        ArrayList<Zombie> zombiesInMyStore = new ArrayList<>();
        
        for (Zombie z : allZombies) {
            if (store.isInStore(z.getX(), z.getY())) {
                zombiesInMyStore.add(z);
            }
        }
        
        // Mission complete when no zombies left
        if (zombiesInMyStore.isEmpty()) {
            System.out.println("[Soldier] Mission complete at " + store.name + "!");
            missionComplete = true;
            hasCheckedOut = true; // Tell parent we're done
            return;
        }
        
        // Shoot nearest zombie
        Zombie nearestZombie = getNearestZombie(zombiesInMyStore);
        if (nearestZombie != null) {
            faceZombie(nearestZombie);
            
            if (shootCooldown <= 0) {
                //PlaySoundEffect
                SoundManager.playBulletSound();

                shootBullet(nearestZombie);
                shootCooldown = 40;
            } else {
                shootCooldown--;
            }
        }
    }
    
    private Zombie getNearestZombie(ArrayList<Zombie> zombies) {
        if (zombies.isEmpty()) return null;
        
        Zombie nearest = zombies.get(0);
        double nearestDistance = getDistanceTo(nearest);
        
        for (Zombie zombie : zombies) {
            double distance = getDistanceTo(zombie);
            if (distance < nearestDistance) {
                nearest = zombie;
                nearestDistance = distance;
            }
        }
        return nearest;
    }
    
    private double getDistanceTo(Actor other) {
        int dx = other.getX() - getX();
        int dy = other.getY() - getY();
        return Math.sqrt(dx * dx + dy * dy);
    }
    
    private void flipImage() {
        GreenfootImage img = getImage();
        img.mirrorHorizontally();
        setImage(img);
    }
    
    private void faceZombie(Zombie zombie) {
        if (zombie.getX() < getX() && !flipped) {
            flipImage();
            flipped = true;
        } else if (zombie.getX() > getX() && flipped) {
            flipImage();
            flipped = false;
        }
    }
    
    private void shootBullet(Zombie zombie) {
        getWorld().addObject(new Bullet(zombie), getX(), getY() - 20);
    }
}