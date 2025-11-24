import greenfoot.*;
import java.util.ArrayList;
/**
 * Special customer unit that enters a store to eliminate all zombies and then leaves.
 * Soldiers spawn at the road, navigate to their assigned store's entrance, systematically
 * shoot all zombies within that store, and exit once the mission is complete.
 * Unlike regular customers, soldiers do not shop and are immune to zombie infection.
 * 
 * @author Owen
 * @version Nov 2025
 */
public class Soldier extends Customer {
    private boolean flipped = false;
    private int shootCooldown = 0;
    private boolean atEntrance = false;
    private boolean missionComplete = false;
    
    /**
     * Constructs a soldier assigned to eliminate zombies in a specific store.
     * The soldier spawns at the specified road node and automatically navigates
     * to the entrance of the assigned store.
     * 
     * @param startNode the node where the soldier spawns (typically on the road)
     * @param assignedStore the store that this soldier is tasked to protect
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
    
    /**
     * Main action method called on each game cycle. Handles the soldier's behavior
     * through three phases:
     * <ol>
     *   <li>Navigation from road to store entrance</li>
     *   <li>Combat phase - eliminates all zombies in the assigned store</li>
     *   <li>Exit phase - leaves the store after mission completion</li>
     * </ol>
     */
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
    
     /**
     * Finds the zombie closest to the soldier from a given list.
     * Uses Euclidean distance to determine proximity.
     * 
     * @param zombies the list of zombies to search through
     * @return the nearest zombie, or null if the list is empty
     */
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
    
    /**
     * Calculates the Euclidean distance between the soldier and another actor.
     * 
     * @param other the actor to measure distance to
     * @return the distance in pixels
     */
    private double getDistanceTo(Actor other) {
        int dx = other.getX() - getX();
        int dy = other.getY() - getY();
        return Math.sqrt(dx * dx + dy * dy);
    }
    
    /**
     * Flips the soldier's image horizontally to face the opposite direction.
     * Used in combination with the flipped flag to ensure the soldier faces
     * toward the zombie they're targeting.
     */
    private void flipImage() {
        GreenfootImage img = getImage();
        img.mirrorHorizontally();
        setImage(img);
    }
    
    /**
     * Orients the soldier to face the specified zombie by flipping the image
     * horizontally as needed. Ensures the soldier is visually aiming at their target.
     * 
     * @param zombie the zombie to face toward
     */
    private void faceZombie(Zombie zombie) {
        if (zombie.getX() < getX() && !flipped) {
            flipImage();
            flipped = true;
        } else if (zombie.getX() > getX() && flipped) {
            flipImage();
            flipped = false;
        }
    }
    
    /**
     * Creates and fires a bullet at the specified zombie target.
     * The bullet is spawned slightly above the soldier's position and
     * automatically tracks the target zombie.
     * 
     * @param zombie the zombie to shoot at
     */
    private void shootBullet(Zombie zombie) {
        getWorld().addObject(new Bullet(zombie), getX(), getY() - 20);
    }
}