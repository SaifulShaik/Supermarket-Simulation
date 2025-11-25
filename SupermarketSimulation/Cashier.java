import greenfoot.*;
import java.util.*;

/**
 * Cashier class that handles customer checkout and store profits
 * 
 * @author Saiful Shaik, Joe Zhuo, and Owen Kung
 * @version Nov 4, 2025
 */
public class Cashier extends Actor
{
    private GreenfootImage[] frames;
    //animation parameters
    private int frame = 0;      //frame index 
    private int tick = 0;       //framee counter
    private int delay = 50;     // lower = faster animation
    
    private Queue<Customer> queue;
    private Customer currentCustomer;
    
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
        
        // start with an empty queue
        queue = new LinkedList<>();
        
        // default service speed
        this.serviceSpeed = 25.0;
    }
    
    /**
     * Plays the animation and handles customers inside the queue
     */
    public void act() 
    {
        // advance frame every `delay` ticks
        tick++;
        if (tick >= delay) {
            tick = 0;
            frame = (frame + 1) % frames.length;
            setImage(frames[frame]);
        }
        
        // start dealing with next customer if there are any in the queue and if not already processing one
        if (currentCustomer == null && !queue.isEmpty()) {
            startNextCustomer();
        }
        // continue processign current customer
        else {
            processCurrentCustomer();
        } 
        
        // faster processing when day is nearing the end
        if (TimeOfDayManager.getHour() > 17){
            this.serviceSpeed = 3.0;
        } 
        // use default processing speed
        else {
            this.serviceSpeed = 25.0;
        }
    }
    
    /**
     * Method to start dealing with the next customer
     */
    private void startNextCustomer() {
        // takes first customer in queue
        currentCustomer = queue.poll();
        
        // counts number of items in their cart and starts the timer
        timer = (int) serviceSpeed * currentCustomer.getCartSize();
    }
    
    /**
     * Method to continue dealing with the current customer
     */
    private void processCurrentCustomer() {
        // cannot do this if no current customer
        if (currentCustomer == null) return;
        
        // reduces timer
        timer--;
        
        // finished processing
        if (timer <= 0) {
            // calculates total price
            double totalEarnings = currentCustomer.calculatePriceOfCart();
            
            // displays profits
            showEarnings(totalEarnings);
            
            // adds profit to store
            currentCustomer.getStore().addProfit(totalEarnings);
            
            // checks out customer
            currentCustomer.checkOut();
            
            // resets current customer
            currentCustomer = null;
        }
    }
    
    /**
     * Method to add a customer to the cashier's queue
     * 
     * @param customer to add to the queue
     */
    public void addCustomerToQueue(Customer c) {
        // cannot add if it is already in the queue, if it isn't available, or if it's the current customer
        if (c == null || c == currentCustomer || queue.contains(c)) return;
        
        // adds to queue
        queue.offer(c);
    }
    
    /**
     * Method to get the customer's position in queue
     * 
     * @param customer to get position for
     * @return position in queue
     */
    public int getPositionInQueue(Customer c) {
        int index = 0;
        
        // loops through queue and checks if it is the requested customer
        for (Customer cur : queue) {
            if (cur == c) { 
                return index; 
            }
            index++;
        }
        
        // not found
        return -1; 
    }
    
    /**
     * Method to set the customer node to access the cashier from
     * 
     * @param node to access from
     */
    public void setCustomerNode(Node n) {
        this.customerNode = n;
    }
    
    /**
     * Method to get the customer access node of the cashier
     * 
     * @return customer access node
     */
    public Node getCustomerNode() {
        return customerNode;
    }
    
    /**
     * Method to get the queue size of the cashier
     * 
     * @return queue size
     */
    public int getQueueSize() {
        return queue.size();
    }
    
    /**
     * Displays a floating text representing total earnings
     * 
     * @param earnings number
     */
    public void showEarnings(double totalEarnings) {
        if (getWorld() == null) return;
        
        int offsetX = Greenfoot.getRandomNumber(40) - 20;
        int offsetY = Greenfoot.getRandomNumber(40) - 20;
  
        getWorld().addObject(new FloatingText("$: " + totalEarnings, Color.GREEN), getX() + offsetX, getY() + offsetY);
    }
}
