import greenfoot.*;
import java.util.*;

/**
 * BasicCashier
 * Basically an animated objects in the store.
 * It used to images for animation effect
 * By default is uses cashier/cashier1.png and cashier/cashier2.png
 * The subclass can specify alternative by calling the public Cashier(String frame0, String frame1) constructor\
 * 
 * @author Joe Zhuo and Owen Kung
 * @version Nov 4, 2025
 */
public class Cashier extends SuperSmoothMover
{
    private GreenfootImage[] frames;
    //animation parameters
    private int frame = 0;      //frame index 
    private int tick = 0;       //framee counter
    private int delay = 50;     // lower = faster animation
    
    private Queue<Customer> queue;
    private Customer currentCustomer;
    
    private double totalEarnings;
    
    private int timer;
    private double serviceSpeed; // final service speed = number of items * service speed
    
    private Node customerNode;

    /*
     * Use default images for cashier if not file name specified
     */
    public Cashier() 
    {
        GreenfootImage frame0 = new GreenfootImage("cashier/cashier1.png");
        GreenfootImage frame1 = new GreenfootImage("cashier/cashier2.png");
        frame0.scale(frame0.getWidth()/6, frame0.getHeight()/6);
        frame1.scale(frame1.getWidth()/6, frame1.getHeight()/6);

        frames=new GreenfootImage[]{frame0, frame1};
        setImage(frames[0]);
        
        queue = new LinkedList<>();
        this.serviceSpeed = 50.0;
    }
    
    public void act() 
    {
        // advance frame every `delay` ticks
        tick++;
        if (tick >= delay) {
            tick = 0;
            frame = (frame + 1) % frames.length;
            setImage(frames[frame]);
        }
        
        
        if (currentCustomer == null && !queue.isEmpty()) {
            startNextCustomer();
        }
        else {
            processCurrentCustomer();
        } 
    }
    
    private void startNextCustomer() {
        currentCustomer = queue.poll();
        timer = (int) serviceSpeed * currentCustomer.getCartSize();
    }
    
    private void processCurrentCustomer() {
        if (currentCustomer == null) return;
        
        timer--;
        System.out.println("a");
        if (timer <= 0) {
            totalEarnings = currentCustomer.calculatePriceOfCart();
            
            showEarnings();
            
            currentCustomer.getStore().addProfit(totalEarnings);
            
            currentCustomer.checkOut();
            currentCustomer = null;
            
            System.out.println("b");
        }
    }
    
    public void addCustomerToQueue(Customer c) {
        if (c == null || c == currentCustomer || queue.contains(c)) return;
        queue.offer(c);
    }
    
    public int getPositionInQueue(Customer c) {
        int index = 0;
        for (Customer cur : queue) {
            if (cur == c) { 
                return index; 
            }
            index++;
        }
        return -1; 
    }
    
    public void setCustomerNode(Node n) {
        this.customerNode = n;
    }
    
    public Node getCustomerNode() {
        return customerNode;
    }
    
    public int getQueueSize() {
        return queue.size();
    }
    
    public void showEarnings() {
        if (getWorld() == null) return;
        
        int offsetX = Greenfoot.getRandomNumber(40) - 20;
        int offsetY = Greenfoot.getRandomNumber(40) - 20;
  
        getWorld().addObject(new FloatingText("$: " + totalEarnings, Color.GREEN), getX() + offsetX, getY() + offsetY);
    }
}
