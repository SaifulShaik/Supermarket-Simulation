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
        //<7, so that customers does not walk over the truck
        if (TimeOfDayManager.getHour() > 14 || TimeOfDayManager.getHour() <7){
            return;
        } else{
            spawnCustomers();
        }
    }
    
    private void spawnCustomers() {
        Node startNode = SimulationWorld.getStartNode();
        
        ArrayList<RegularShopper> customers = (ArrayList<RegularShopper>) getWorld().getObjects(RegularShopper.class);
       
        
        if (Greenfoot.getRandomNumber(spawnRate) == 0 && customers.size() <= 10 && spawn) {
            int customerType = Greenfoot.getRandomNumber(5);

            //chance for zombit
            int zombie = Greenfoot.getRandomNumber(100);
            if (zombie == 0){
                getWorld().addObject(new Zombie(startNode), startNode.getX(), startNode.getY());
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


