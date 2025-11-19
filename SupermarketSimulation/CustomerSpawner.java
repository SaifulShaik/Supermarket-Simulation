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
    private int actCount = 0;
    private boolean spawn = true;
    
    public CustomerSpawner() {
        setImage((GreenfootImage) null);
    }
    
    public void act() {
        //stop spawning earlier than night cycle to make sure all customers are gone by the time the night comes
        actCount++;
        if (actCount > 3500 && actCount < 5000){
            return;
        } else{
            spawnCustomers();
        }
        
        if (actCount > 4000){
            actCount = 0;
        }
    }
    
    private void spawnCustomers() {
        ArrayList<RegularShopper> customers = (ArrayList<RegularShopper>) getWorld().getObjects(RegularShopper.class);
        
        if (Greenfoot.getRandomNumber(spawnRate) == 0 && customers.size() <= 10 && spawn) {
            int customerType = Greenfoot.getRandomNumber(2);
            
            Node startNode = SimulationWorld.getStartNode();
            
            switch (customerType) {
                case 0:
                    getWorld().addObject(new RegularShopper(startNode), startNode.getX(), startNode.getY());
                    break;
                case 1:
                    getWorld().addObject(new Zombie(startNode), startNode.getX(), startNode.getY());
                    break;
            }
        }
    }
}


