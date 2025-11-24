import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
/**
 * Zombie customer that wanders stores and converts regular customers into zombies.
 * Zombies are special customer entities that do not shop or check out. Instead, they
 * continuously wander through stores, converting any regular customers they touch.
 * Each zombie conversion and presence negatively impacts store ratings.
 * Zombies cannot convert soldiers or customers who have already checked out.
 * 
 * @author Owen Lee
 * @version Nov 2025
 */
public class Zombie extends Customer
{
    GreenfootImage shopper = new GreenfootImage("Zombie.png");
    
    /**
     * Constructs a zombie at the specified node location.
     * The zombie inherits movement capabilities from Customer but has all
     * shopping behaviors disabled. The sprite is scaled to 25% size and
     * padded vertically for proper visual alignment.
     * 
     * @param n the starting node where the zombie spawns
     */
    public Zombie(Node n)
    {
        super(1, 0, n, 0, 0, 700);
        shopper.scale(shopper.getWidth()/4, shopper.getHeight()/4);
        int w = shopper.getWidth();
        int h = shopper.getHeight();
        GreenfootImage padded = new GreenfootImage(w, h * 2);
        padded.drawImage(shopper, 0, 0);
        setImage(padded);
        
        // Clear inherited customer state to prevent checkout behavior
        this.hasCheckedOut = false;
        this.targetCashier = null;
        this.path = null;
        this.shoppingList.clear();
        this.cart.clear();
    }
    
    /**
     * Main action method called on each game cycle.
     * Zombies continuously wander through stores using the node navigation system.
     * Unlike regular customers, zombies never attempt to check out or exit.
     * Each act cycle includes collision checking to detect and convert nearby customers.
     */
    @Override
    public void act()
    {
        // Zombie behavior: just wander and check for collisions
        if (targetNode != null) {
            moveToNode(targetNode, 0, 0);
        } else if (currentNode != null) {
            move(false); // Keep wandering, never exit
        }
        
        checkCollision(); // Check for customers to convert
    }
    
    /**
     * Checks for collisions with regular customers and converts them into zombies.
     * Only customers who are currently in a store and have not checked out can be converted.
     * Soldiers are immune to zombie infection. When a customer is converted:
     * <ul>
     *   <li>A death animation appears at the collision location</li>
     *   <li>A zombie sound effect plays</li>
     *   <li>The victim's carried items and basket are removed</li>
     *   <li>A new zombie spawns at the victim's location</li>
     *   <li>The store's rating decreases</li>
     * </ul>
     */
    public void checkCollision()
    {
        Customer victim = (Customer) getOneIntersectingObject(Customer.class);
        
        if (victim != null && !(victim instanceof Zombie))
        {
            // Only convert customers who are in a store
            if (victim.getStore() == null) {
                return;
            }
            
            // Don't convert customers who have already checked out and are leaving
            if (victim.hasCheckedOut) {
                return; // Let them leave peacefully
            }
            
            // Soldiers are immune to zombie infection!
            if (victim instanceof Soldier) {
                return; // Don't infect soldiers
            }
            
            // Save position and node info
            Node currentNodePos = victim.currentNode;
            int x = victim.getX();
            int y = victim.getY();
            
            
            //emoji shows up to visually let viewer know a victim disappear
            getWorld().addObject(new Death(), getX(), getY() - 40);
            
            //playsound effect
            SoundManager.playZombieSound();
            
            // Remove the customer and it's carriedItems
            victim.removeAllCarriedItems();
            // Remove the victim's basket and carried items first
            victim.removeAllCarriedItems();
            
            // Remove the customer
            getWorld().removeObject(victim);
            
            // Add a new zombie at the same location
            Zombie newZombie = new Zombie(currentNodePos);
            getWorld().addObject(newZombie, x, y);
            
            //whenever it appears, reduce rating
            if (SimulationWorld.storeOne.isInStore(x, y)) {
               SimulationWorld.storeUI.addStar( 0, 1);
            }
            
            if (SimulationWorld.storeTwo.isInStore(x, y)) {
               SimulationWorld.storeUI.addStar( 0, 2);
            }
        }
    }
    
    /**
     * Calculates and applies rating penalties when a zombie exits a store.
     * Overrides the parent Customer's rating calculation to apply negative
     * rating changes instead of positive ones. Each zombie that leaves
     * reduces the store's rating, reflecting customer dissatisfaction
     * with the zombie presence.
     */
    @Override
    protected void calculateRating()
    {
        if (SimulationWorld.storeOne.isInStore(getX(), getY())) {
            SimulationWorld.storeUI.addStar( 0, 1);
        }
        if (SimulationWorld.storeTwo.isInStore(getX(), getY())) {
            SimulationWorld.storeUI.addStar( 0, 2);
        }
    }
}