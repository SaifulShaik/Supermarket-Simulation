import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;
/**
 * Store UI
 * 
 * @author Joe
 * @version November 11, 2025
 */
public class StoreUI extends Actor
{   
    private double storeOneEarnings;
    private double storeTwoEarnings;
    
    private Label storeOneProfitLabel;
    private Label storeTwoProfitLabel;
    
    // Money tracker bars
    private SuperStatBar storeOneMoneyBar;
    private SuperStatBar storeTwoMoneyBar;
    
    // Star rating variables
    private ArrayList<Integer> storeOneRatings;
    private ArrayList<Integer> storeTwoRatings;
    private Actor storeOneStarIcon;
    private Actor storeTwoStarIcon;
    private Label storeOneRatingLabel;
    private Label storeTwoRatingLabel;
    
    private final int MAX_MONEY = 100;
    private boolean tested = false;
    
    public StoreUI() {
        storeOneEarnings = 0;
        storeTwoEarnings = 0;
        
        // Initialize rating arrays
        storeOneRatings = new ArrayList<Integer>();
        storeTwoRatings = new ArrayList<Integer>();
    }
    
    public void act(){
        if (!tested){
            addEarnings(10, 1);
            addEarnings(50, 2);
            addStar(4, 1);  // Test rating for store 1
            addStar(5, 2);  // Test rating for store 2
            tested = true;
        }
    }
    
    public void addEarnings(double amount, int store) {
        switch (store) {
            case 1:
                storeOneEarnings += amount;
                updateDisplay();
                break;
            case 2:
                storeTwoEarnings += amount;
                updateDisplay();
                break;
        }
    }
    
    public double getEarnings(int store) {
        switch (store) {
            case 1:
                return storeOneEarnings;
            case 2:
                return storeTwoEarnings;
        }
        return -1;
    }
    
    /**
     * Add a star rating (1-5) to a specific store
     * @param rating The rating value (1-5)
     * @param store The store number (1 or 2)
     */
    public void addStar(int rating, int store) {
        if (rating < 1 || rating > 5) return; // Validate rating is between 1-5
        
        switch (store) {
            case 1:
                storeOneRatings.add(rating);
                updateRatingDisplay(1);
                break;
            case 2:
                storeTwoRatings.add(rating);
                updateRatingDisplay(2);
                break;
        }
    }
    
    /**
     * Calculate average rating for a store, rounded to 1 decimal place
     */
    private double getAverageRating(int store) {
        ArrayList<Integer> ratings = (store == 1) ? storeOneRatings : storeTwoRatings;
        
        if (ratings.isEmpty()) return 0.0;
        
        int sum = 0;
        for (int rating : ratings) {
            sum += rating;
        }
        
        double average = (double) sum / ratings.size();
        return Math.round(average * 10.0) / 10.0; // Round to 1 decimal
    }
    
    public void createDisplay(World w) {
        // Only create if not already created
        if (storeOneProfitLabel != null) return;
        
        // Create money tracker bars (green filled, red missing, with border)
        storeOneMoneyBar = new SuperStatBar(
            MAX_MONEY,                          // maxVal
            0,                                  // START AT 0
            null,                               // owner (null = don't follow)
            180,                                // width
            30,                                 // height
            0,                                  // offset
            Color.GREEN,                        // filled color
            Color.RED,                          // missing color
            false,                              // hideAtMax
            Color.WHITE,                        // border color
            2                                   // border thickness
        );
        
        storeTwoMoneyBar = new SuperStatBar(MAX_MONEY, 0, null, 180, 30, 0, Color.GREEN, Color.RED, false, Color.WHITE, 2);
        
        // Add money bars FIRST (so they're behind)
        w.addObject(storeOneMoneyBar, w.getWidth() / 2 - 200, getY());
        w.addObject(storeTwoMoneyBar, w.getWidth() / 2 + 250, getY());
        
        // Create labels with transparent backgrounds
        storeOneProfitLabel = new Label("Profit: $" + String.format("%.2f", storeOneEarnings), 30);
        storeOneProfitLabel.setLineColor(Color.WHITE);
        storeOneProfitLabel.setFillColor(new Color(255, 255, 255, 255)); // Transparent
        
        storeTwoProfitLabel = new Label("Profit: $" + String.format("%.2f", storeTwoEarnings), 30);
        storeTwoProfitLabel.setLineColor(Color.WHITE);
        storeTwoProfitLabel.setFillColor(new Color(255, 255, 255, 255)); // Transparent
        
        // Add labels SECOND (so they're on top) - SAME Y position as bars
        w.addObject(storeOneProfitLabel, w.getWidth() / 2 - 200, getY());
        w.addObject(storeTwoProfitLabel, w.getWidth() / 2 + 250, getY());
        
        // Create star icons
        storeOneStarIcon = new Actor() {
            {
                setImage("star.png");
                getImage().scale(30, 30); // Scale to appropriate size
            }
        };
        
        storeTwoStarIcon = new Actor() {
            {
                setImage("star.png");
                getImage().scale(30, 30);
            }
        };
        
        // Create rating labels
        storeOneRatingLabel = new Label("0.0", 24);
        storeOneRatingLabel.setLineColor(Color.YELLOW);
        storeOneRatingLabel.setFillColor(new Color(255, 255, 0, 255)); // Transparent
        
        storeTwoRatingLabel = new Label("0.0", 24);
        storeTwoRatingLabel.setLineColor(Color.YELLOW);
        storeTwoRatingLabel.setFillColor(new Color(255, 255, 0, 255)); // Transparent
        
        // Add star icons and ratings below the money bars
        w.addObject(storeOneStarIcon, w.getWidth() / 2 - 360, getY());
        w.addObject(storeOneRatingLabel, w.getWidth() / 2 - 320, getY());
        
        w.addObject(storeTwoStarIcon, w.getWidth() / 2 + 365, getY());
        w.addObject(storeTwoRatingLabel, w.getWidth() / 2 + 400, getY());
    }
    
    @Override
    protected void addedToWorld(World world) {
        createDisplay(world);
    }
    
    private void updateDisplay() {
        if (storeOneProfitLabel == null || storeTwoProfitLabel == null) return;
        
        // Update labels
        storeOneProfitLabel.setValue("Profit: $" + String.format("%.2f", storeOneEarnings));
        storeTwoProfitLabel.setValue("Profit: $" + String.format("%.2f", storeTwoEarnings));
        
        // Remove old bars AND labels from world
        World w = getWorld();
        if (w != null && storeOneMoneyBar.getWorld() != null) {
            w.removeObject(storeOneMoneyBar);
        }
        if (w != null && storeTwoMoneyBar.getWorld() != null) {
            w.removeObject(storeTwoMoneyBar);
        }
        if (w != null && storeOneProfitLabel.getWorld() != null) {
            w.removeObject(storeOneProfitLabel);
        }
        if (w != null && storeTwoProfitLabel.getWorld() != null) {
            w.removeObject(storeTwoProfitLabel);
        }
        
        // Update money bars (capped at MAX_MONEY)
        int storeOneBarValue = Math.min((int)Math.round(storeOneEarnings), MAX_MONEY);
        int storeTwoBarValue = Math.min((int)Math.round(storeTwoEarnings), MAX_MONEY);
        
        storeOneMoneyBar.update(storeOneBarValue);
        storeTwoMoneyBar.update(storeTwoBarValue);
        
        // Add bars back FIRST (behind)
        if (w != null) {
            w.addObject(storeOneMoneyBar, w.getWidth() / 2 - 200, getY());
            w.addObject(storeTwoMoneyBar, w.getWidth() / 2 + 250, getY());
            
            // Add labels back SECOND (on top)
            w.addObject(storeOneProfitLabel, w.getWidth() / 2 - 200, getY());
            w.addObject(storeTwoProfitLabel, w.getWidth() / 2 + 250, getY());
        }
    }
    
    /**
     * Update the rating display for a specific store
     */
    private void updateRatingDisplay(int store) {
        double avgRating = getAverageRating(store);
        
        if (store == 1 && storeOneRatingLabel != null) {
            storeOneRatingLabel.setValue(String.format("%.1f", avgRating));
        } else if (store == 2 && storeTwoRatingLabel != null) {
            storeTwoRatingLabel.setValue(String.format("%.1f", avgRating));
        }
    }
}