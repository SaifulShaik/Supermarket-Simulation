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
    private final static int spawnRate = 90;
    
    public CustomerSpawner() {
        setImage((GreenfootImage) null);
    }
    
    public void act() {
        //stop spawning earlier than night cycle to make sure all customers are gone by the time the night comes
        spawnCustomers();
    }
    
    private void spawnCustomers() {
        ArrayList<RegularShopper> customers = (ArrayList<RegularShopper>) getWorld().getObjects(RegularShopper.class);
        
        if (Greenfoot.getRandomNumber(spawnRate) == 0) {
            Node startNode = SimulationWorld.getStartNode();
            
            int zombie = Greenfoot.getRandomNumber(150);
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


