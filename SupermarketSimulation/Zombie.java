import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
/**
 * Zombie customer that wanders stores and converts regular customers.
 * 
 * By Owen Lee
 * @version (a version number or a date)
 */
public class Zombie extends Customer
{
    GreenfootImage shopper = new GreenfootImage("Zombie.png");
    
    public static int supermarketTotalZombies = 0;
    public static int butcherTotalZombies = 0;
    
    public Zombie(Node n)
    {
        super(1, 0, n, 0, 0, 700);
        shopper.scale(shopper.getWidth()/4, shopper.getHeight()/4);
        int w = shopper.getWidth();
        int h = shopper.getHeight();
        GreenfootImage padded = new GreenfootImage(w, h * 2);
        padded.drawImage(shopper, 0, 0);
        setImage(padded);
        
        if (this.getStore()==SimulationWorld.storeOne){ supermarketTotalZombies++; }
        else{ butcherTotalZombies++; }
        
        // Clear inherited customer state to prevent checkout behavior
        this.hasCheckedOut = false;
        this.targetCashier = null;
        this.path = null;
        this.shoppingList.clear();
        this.cart.clear();
    }
    
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
    
    /*
     * When zombie leave the store, reduce more rating
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
    
    protected int getType(){
        return 4;
    }
}