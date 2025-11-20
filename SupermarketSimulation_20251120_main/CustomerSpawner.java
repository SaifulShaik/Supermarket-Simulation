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
    private final static int spawnRate = 120;
    
    public CustomerSpawner() {
        setImage((GreenfootImage) null);
    }
    
    public void act() {
        spawnCustomers();
    }
    
    private void spawnCustomers() {
        
        //Don't spawn customers during this hous
        //store close
        if (TimeOfDayManager.getHour()>=20 || TimeOfDayManager.getHour()<=1)
        {
            return;
        }

        ArrayList<RegularShopper> customers = (ArrayList<RegularShopper>) getWorld().getObjects(RegularShopper.class);
        
        if (Greenfoot.getRandomNumber(spawnRate) == 0) {
            int customerType = Greenfoot.getRandomNumber(2);
            
            Node startNode = SimulationWorld.getStartNode();
            
            switch (customerType) {
                case 0:
                    getWorld().addObject(new RegularShopper(startNode), startNode.getX(), startNode.getY());
                    break;
                case 1:
                    getWorld().addObject(new ImpulseShopper(startNode), startNode.getX(), startNode.getY());
                    break;
    
            }
        }
    }
}


