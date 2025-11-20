import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;

/**
 * Spawns customers into the road 
 * 
 * @author Joe 
 * @version November 2025
 */

public class CustomerSpawner extends Actor
{
    private final static int spawnRate = 150;
    private final static int maxCustomers = 12;
    
    private boolean zombieSpawned = false;
    private int actCount = 0;
    private boolean spawn = true;
    
    public CustomerSpawner() {
        setImage((GreenfootImage) null);
    }
    
    public void act() {
        //stop spawning earlier than night cycle to make sure all customers are gone by the time the night comes
        if (TimeOfDayManager.getHour() > 16 || TimeOfDayManager.getHour() < 3){
            return;
        } else{
            spawnCustomers();
        }
        
        spawnCustomers();
    }
    
    private void spawnCustomers() {
        ArrayList<RegularShopper> customers = (ArrayList<RegularShopper>) getWorld().getObjects(RegularShopper.class);
        
        if (Greenfoot.getRandomNumber(spawnRate) == 0) {
            Node startNode = SimulationWorld.getStartNode();
            
            int zombie = Greenfoot.getRandomNumber(100);
            if (zombie == 0){
                getWorld().addObject(new Zombie(startNode), startNode.getX(), startNode.getY());
                return;
            }

            int customerType = Greenfoot.getRandomNumber(2);
            switch (customerType) {
                case 0:
                    getWorld().addObject(new RegularShopper(startNode), startNode.getX(), startNode.getY());
                    break;
                case 1:
                    getWorld().addObject(new ImpulseShopper(startNode), startNode.getX(), startNode.getY());
                    break;
                case 3:
                    getWorld().addObject(new BulkShopper(startNode), startNode.getX(), startNode.getY());
                    break;
                case 4:
                    getWorld().addObject(new BargainShopper(startNode), startNode.getX(), startNode.getY());
                    break;
    
            }
        }
    }
}


