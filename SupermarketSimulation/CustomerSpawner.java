import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;
/**
 * Spawns customers into the road 
 * 
 * @author Joe & Owen Lee
 * @version November 2025
 */
public class CustomerSpawner extends Actor
{
    private final static int spawnRate = 150;
    private int actCount = 0;
    private boolean spawn = true;
    private boolean soldiersAdded = false;
    private boolean zombieAdded = false;
    
    public CustomerSpawner() {
        setImage((GreenfootImage) null);
    }
    
    public void act() {
        
        if (TimeOfDayManager.getHour() == 17 && TimeOfDayManager.getMinute() == 0 && !soldiersAdded){
            spawnSoldiersIfNeeded();
            soldiersAdded = true;
        } 
        
        if (TimeOfDayManager.getHour() > 16){
            return;
        } else{
            spawnCustomers();
        }
        
        
    }
    
    /**
     * Spawns soldiers only to stores that have zombies
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
            System.out.println("[CustomerSpawner] Spawned soldier for Store 1 (has zombies)");
        }
        
        // Spawn soldier for Store 2 if it has zombies
        if (store2HasZombies) {
            Soldier soldier2 = new Soldier(startNode, SimulationWorld.storeTwo);
            getWorld().addObject(soldier2, startNode.getX(), startNode.getY());
            System.out.println("[CustomerSpawner] Spawned soldier for Store 2 (has zombies)");
        }
        
        if (!store1HasZombies && !store2HasZombies) {
            System.out.println("[CustomerSpawner] No zombies detected, no soldiers spawned");
        }
    }
    
    private void spawnCustomers() {
        Node startNode = SimulationWorld.getStartNode();
        
        ArrayList<RegularShopper> customers = (ArrayList<RegularShopper>) getWorld().getObjects(RegularShopper.class);
       
        
        if (Greenfoot.getRandomNumber(spawnRate) == 0 && customers.size() <= 10 && spawn) {
            int customerType = Greenfoot.getRandomNumber(5);
            //chance for zombie
            int zombie = Greenfoot.getRandomNumber(10);
            if (zombie == 0){
                int storeChoice = Greenfoot.getRandomNumber(2);
                Node spawnNode;
                if (storeChoice == 0) {
                    // Spawn in Store 1
                    spawnNode = SimulationWorld.storeOne.getEntranceNode();
                } else {
                    // Spawn in Store 2
                    spawnNode = SimulationWorld.storeTwo.getEntranceNode();
                }
                getWorld().addObject(new Zombie(spawnNode), spawnNode.getX(), spawnNode.getY());
                return;
            }
            
            
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