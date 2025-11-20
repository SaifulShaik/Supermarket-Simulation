import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Zombie here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Zombie extends Customer
{
    //saiful
    GreenfootImage shopper = new GreenfootImage("Zombie.png");
    
    public Zombie(Node n)
    {
        super(3, 0, n, 0, 0, 700);
        shopper.scale(shopper.getWidth()/4, shopper.getHeight()/4);
        int w = shopper.getWidth();
        int h = shopper.getHeight();
        GreenfootImage padded = new GreenfootImage(w, h * 2);
        padded.drawImage(shopper, 0, 0);
        setImage(padded);
    }
    
    @Override
    public void act()
    {
        // Zombie behavior: just wander and check for collisions
        if (targetNode != null) {
            moveToNode(targetNode, 0, 0);
        } else if (currentNode != null) {
            move(false); // Keep wandering
        }
        
        checkCollision(); // Check for customers to convert
    }
    
    public void checkCollision()
    {
        Customer victim = (Customer) getOneIntersectingObject(Customer.class);
        
        if (victim != null && !(victim instanceof Zombie))
        {
            // Save position and node info
            Node currentNodePos = victim.currentNode;
            int x = victim.getX();
            int y = victim.getY();
            
            // Remove the customer
            getWorld().removeObject(victim);
            
            // Add a new zombie at the same location
            Zombie newZombie = new Zombie(currentNodePos);
            getWorld().addObject(newZombie, x, y);
        }
    }
}
