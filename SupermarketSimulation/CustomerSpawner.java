import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Spawns customers into the road 
 * 
 * @author Joe 
 * @version November 2025
 */
public class CustomerSpawner extends Actor
{
    private final static int spawnRate = 200;
    
    public void act() {
        spawnCustomers();
    }
    
    private void spawnCustomers() {
        if (Greenfoot.getRandomNumber(spawnRate) == 0) {
            int customerType = Greenfoot.getRandomNumber(1);
            
            // Grid cell size is 20 pixels
            int gridSize = 20;
            
            // Snap spawn position to grid (center of world, aligned to grid)
            int worldCenterX = getWorld().getWidth() / 2;
            int spawnY = 200;
            
            // Snap to nearest grid cell center
            int gridX = worldCenterX / gridSize;
            int gridY = spawnY / gridSize;
            int snappedX = gridX * gridSize + gridSize / 2;
            int snappedY = gridY * gridSize + gridSize / 2;
            
            switch (customerType) {
                case 0:
                    getWorld().addObject(new RegularShopper(), snappedX, snappedY);
                    break;
            }
        }
    }
}
