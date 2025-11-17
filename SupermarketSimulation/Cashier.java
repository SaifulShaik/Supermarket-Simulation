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
    private int serviceSpeed; // acts required per product service
    
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
        serviceSpeed = 180;
    }
    
    /*
     * Use specifiedfile for cashier images
     */
    public Cashier(String frame0, String frame1)
    {
        GreenfootImage frame0Image = new GreenfootImage(frame0);
        GreenfootImage frame1Image = new GreenfootImage(frame1);
        frame0Image.scale(frame0Image.getWidth()/6, frame0Image.getHeight()/6);
        frame1Image.scale(frame1Image.getWidth()/6, frame1Image.getHeight()/6);

        frames=new GreenfootImage[]{frame0Image, frame1Image};
        setImage(frames[0]);
        
        queue = new LinkedList<Customer>();
        serviceSpeed = 10;
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
        System.out.println("Started handling new customer");
        currentCustomer = queue.poll();
        timer = serviceSpeed;
    }
    
    private void processCurrentCustomer() {
        if (currentCustomer == null) return;
        System.out.println("Dealing with customer");
        
        timer--;
        
        if (timer <= 0) {
            totalEarnings = currentCustomer.calculatePriceOfCart();
            showEarnings();
            currentCustomer.checkOut();
            currentCustomer = null;
            System.out.println("[Cashier] customer has checked out");
        }
    }
    
    public void addCustomerToQueue(Customer c) {
        if (c == null || c == currentCustomer || queue.contains(c)) return;
        queue.offer(c);
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
