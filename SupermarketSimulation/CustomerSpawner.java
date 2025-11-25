import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;
/**
 * Spawns customers into the road at regular intervals.
 * Handles customer spawning during store hours (8 AM - 5 PM) and deploys soldiers
 * at 5 PM to stores with zombie 
 * Different customer types spawn randomly.
 * 
 * @author Saiful Shaik, Joe, & Owen Lee
 * @version November 2025
 */
public class CustomerSpawner extends Actor
{
    private final static int spawnRate = 150;
    private int actCount = 0;
    private boolean spawn = true;
    private int lastSoldierSpawnDay = -1;  // Track which day soldiers were spawned
    private boolean zombieAdded = false;
    
    /**
     * Constructs a CustomerSpawner with no visible image.
     */
    public CustomerSpawner() {
        setImage((GreenfootImage) null);
    }
    
    /**
     * Main act method that controls spawning timing.
     * Spawns soldiers at 5 PM if zombies are present, spawns regular customers
     * between 8 AM and 5 PM. Prevents duplicate soldier spawns on the same day.
     */
    public void act() {
        int currentDay = TimeOfDayManager.getDaysPassed();  // Get current day from TimeOfDayManager
        
        // Spawn soldiers at 17:00 each day (only once per day)
        if (TimeOfDayManager.getHour() == 17 && TimeOfDayManager.getMinute() == 0 && lastSoldierSpawnDay != currentDay){
            spawnSoldiersIfNeeded();
            lastSoldierSpawnDay = currentDay;
        } 
        
        // Only spawn customers before 17:00
        if (TimeOfDayManager.getHour() > 16  || TimeOfDayManager.getHour()<8){
            return;
        } else{
            spawnCustomers();
        }
        
        
    }
    
    /**
     * Spawns soldiers to stores that currently have zombies.
     * Checks both stores for zombie presence and spawns one soldier per infected store.
     */
    private void spawnSoldiersIfNeeded() {
        Node startNode = SimulationWorld.getStartNode();
        ArrayList<Zombie> allZombies = (ArrayList<Zombie>) getWorld().getObjects(Zombie.class);
        
        boolean store1HasZombies = false;
        boolean store2HasZombies = false;
        
        // Check which stores have zombies
        for (Zombie z : allZombies) {
            if (SimulationWorld.storeOne.isInStore(z.getX(), z.getY())) {
                store1HasZombies = true;
            }
            if (SimulationWorld.storeTwo.isInStore(z.getX(), z.getY())) {
                store2HasZombies = true;
            }
        }
        
        // Spawn soldier for Store 1 if it has zombies
        if (store1HasZombies) {
            Soldier soldier1 = new Soldier(startNode, SimulationWorld.storeOne);
            getWorld().addObject(soldier1, startNode.getX(), startNode.getY());
        }
        
        // Spawn soldier for Store 2 if it has zombies
        if (store2HasZombies) {
            Soldier soldier2 = new Soldier(startNode, SimulationWorld.storeTwo);
            getWorld().addObject(soldier2, startNode.getX(), startNode.getY());
        }
    }
    
    /**
     * Spawns customers randomly during store hours.
     * Has a small chance each frame to spawn a zombie  or
     * a regular customer type (RegularShopper, BulkShopper, BargainShopper, ImpulseShopper).
     * Limits total customers to 10 at a time.
     */
    private void spawnCustomers() {
        Node startNode = SimulationWorld.getStartNode();
        
        ArrayList<RegularShopper> customers = (ArrayList<RegularShopper>) getWorld().getObjects(RegularShopper.class);
       
        
        if (Greenfoot.getRandomNumber(spawnRate) == 0 && customers.size() <= 10 && spawn) {
            //chance for zombie
            int zombie = Greenfoot.getRandomNumber(20);
            if (zombie == 0){
                int storeChoice = Greenfoot.getRandomNumber(2);
                Node spawnNode;
                if (storeChoice == 0) {
                    // Spawn in Store 1
                    spawnNode = SimulationWorld.storeOne.getEntranceNode(); 
                    //rating                    
                    SimulationWorld.storeUI.addStar( 1, 1);
                    SimulationWorld.storeUI.addStar( 1, 1);
                } else {
                    // Spawn in Store 2
                    spawnNode = SimulationWorld.storeTwo.getEntranceNode();
                    //rating                    
                    SimulationWorld.storeUI.addStar( 1, 2);
                    SimulationWorld.storeUI.addStar( 1, 2);
                }
                getWorld().addObject(new Zombie(spawnNode), spawnNode.getX(), spawnNode.getY());
                return;
            }
            
            int customerType = Greenfoot.getRandomNumber(5);
            switch (customerType) {
                case 0:
                    getWorld().addObject(new RegularShopper(startNode), startNode.getX(), startNode.getY());
                    break;
                case 1:
                    getWorld().addObject(new BulkShopper(startNode), startNode.getX(), startNode.getY());
                    break;
                case 2:
                    getWorld().addObject(new BargainShopper(startNode), startNode.getX(), startNode.getY());
                    break; 
                case 3:
                    getWorld().addObject(new ImpulseShopper(startNode), startNode.getX(), startNode.getY());
                    break;
            }
        }
    }
}