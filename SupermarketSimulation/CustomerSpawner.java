import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Spawns customers into the road 
 * 
 * @author Joe 
 * @version November 2025
 */
public class CustomerSpawner extends Actor
{
    private final static int spawnRate = 400;
    
    public void act() {
        spawnCustomers();
    }
    
    private void spawnCustomers() {
        if (Greenfoot.getRandomNumber(spawnRate) == 0) {
            int customerType = Greenfoot.getRandomNumber(1);
            
            switch (customerType) {
                case 0:
                    getWorld().addObject(new RegularShopper(), getWorld().getWidth()/2, 200);
                    break;
            }
        }
    }
}
