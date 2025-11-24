import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.ArrayList;

/**
 * StoreUI is responsible for displaying the main UI elements
 * that compare the performance of two stores in the simulation.
 *
 * It shows:
 * - Profit labels for each store
 * - Money bars that fill based on store profit
 * - Star ratings (average of all ratings) for each store
 * 
 * The StoreUI actor itself has no image; it just manages other UI objects.
 * 
 * @author Joe and Owen L
 * @version November 11, 2025
 */
public class StoreUI extends Actor
{   
    //Current profits
    private Label storeOneProfitLabel;
    private Label storeTwoProfitLabel;
    
    //Money Tracker Bar
    private SuperStatBar storeOneMoneyBar;
    private SuperStatBar storeTwoMoneyBar;
    
    //star rating
    private ArrayList<Integer> storeOneRatings;
    private ArrayList<Integer> storeTwoRatings;
    
    //Visual Star Icon
    private Actor storeOneStarIcon;
    private Actor storeTwoStarIcon;
    
    //Average Rating Labels
    private Label storeOneRatingLabel;
    private Label storeTwoRatingLabel;
    
    //Store discount labels
    private Label storeOneDiscountLabel;
    private Label storeTwoDiscountLabel;
    
    //Maxium Money Value
    private final int MAX_MONEY = 500;
    
    /**
     * Constructor for StoreUI.
     * Initializes the rating lists for each store and removes the main actor image,
     * since this class only manages other UI objects.
     */
    public StoreUI() {
        // No main image – UI is made from separate Label and SuperStatBar objects.
        setImage((GreenfootImage) null);

        // Initialize rating arrays
        storeOneRatings = new ArrayList<Integer>();
        storeTwoRatings = new ArrayList<Integer>();
    }
    
    /**
     * Main act method for StoreUI.
     * On every frame, updates the money bars, profit labels, and rating displays
     * to match the current store profits and ratings.
     */
    public void act(){
        updateDisplay();
        updateRatingDisplay(1);
        updateRatingDisplay(2);
    }
    
    /**
     * Adds a star rating (1–5) to a specific store.
     *
     * @param rating the rating value (must be between 1 and 5, inclusive)
     * @param store  the store number to rate: 1 for Store 1, 2 for Store 2
     */
    public void addStar(int rating, int store) {
        // Validate rating is between 1–5
        if (rating < 1 || rating > 5) return; 
        
        switch (store) {
            case 1:
                storeOneRatings.add(rating);
                updateRatingDisplay(1);
                break;
            case 2:
                storeTwoRatings.add(rating);
                updateRatingDisplay(2);
                break;
            default:
                // Invalid store index; do nothing
                break;
        }
    }
    
    public void clearRatings(int store) {
        if (store == 1) {
            storeOneRatings.clear();
            updateRatingDisplay(1);
        } else if (store == 2) {
            storeTwoRatings.clear();
            updateRatingDisplay(2);
        }
    }
    
    /**
     * Calculates the average rating for a given store.
     * Base rating counts as 10 ratings to give it significant weight.
     * Customer ratings are then averaged with the base rating.
     *
     * @param store the store number (1 or 2)
     * @return the average rating for that store, rounded to one decimal place
     */
    private double getAverageRating(int store) {
        ArrayList<Integer> ratings = (store == 1) ? storeOneRatings : storeTwoRatings;
        
        // Get base rating from the store
        double baseRating = (store == 1) ? SimulationWorld.storeOne.getBaseRating() : SimulationWorld.storeTwo.getBaseRating();
        
        // Base rating counts as 10 ratings
        double totalSum = baseRating * 10;
        int totalCount = 10;
        
        // Add customer ratings
        for (int rating : ratings) {
            totalSum += rating;
            totalCount++;
        }
        
        // Calculate weighted average
        double average = totalSum / totalCount;
        return Math.round(average * 10.0) / 10.0;
    }
    
    /**
     * Creates and positions all UI elements (money bars, profit labels,
     * star icons, rating labels) in the given world.
     *
     * This method should only be called once. If the UI has already been
     * created, the method returns immediately.
     *
     * @param w the world in which the UI components are added
     */
    public void createDisplay(World w) {
        // Only create if not already created
        if (storeOneProfitLabel != null) return;
        
        // Create money tracker bars (green filled, red missing, with border)
        storeOneMoneyBar = new SuperStatBar(
            MAX_MONEY,      // maxVal
            0,              // start at 0
            null,           // owner (null = do not follow any actor)
            180,            // width
            30,             // height
            0,              // offset
            Color.GREEN,    // filled color
            Color.RED,      // missing color
            false,          // hideAtMax
            Color.WHITE,    // border color
            2               // border thickness
        );
        
        storeTwoMoneyBar = new SuperStatBar(
            MAX_MONEY, 0, null, 180, 30, 0,
            Color.GREEN, Color.RED, false, Color.WHITE, 2
        );
        
        // Add money bars first (so they are behind labels)
        w.addObject(storeOneMoneyBar, w.getWidth() / 2 - 200, getY());
        w.addObject(storeTwoMoneyBar, w.getWidth() / 2 + 250, getY());
        
        // Create profit labels
        storeOneProfitLabel = new Label(
            "Profit: $" + String.format("%.2f", SimulationWorld.storeOne.getProfit()),
            27
        );
        storeOneProfitLabel.setLineColor(Color.BLACK);
        storeOneProfitLabel.setFillColor(new Color(255, 255, 255, 255));
        
        storeTwoProfitLabel = new Label(
            "Profit: $" + String.format("%.2f", SimulationWorld.storeTwo.getProfit()),
            27
        );
        storeTwoProfitLabel.setLineColor(Color.BLACK);
        storeTwoProfitLabel.setFillColor(new Color(255, 255, 255, 255));
        
        // Add profit labels second (on top of bars)
        w.addObject(storeOneProfitLabel, w.getWidth() / 2 - 200, getY());
        w.addObject(storeTwoProfitLabel, w.getWidth() / 2 + 250, getY());
        
        // Create star icons
        storeOneStarIcon = new Actor() {
            {
                setImage("star.png");
                getImage().scale(30, 30);
            }
        };
        
        storeTwoStarIcon = new Actor() {
            {
                setImage("star.png");
                getImage().scale(30, 30);
            }
        };
        
        // Create rating labels (for displaying average rating)
        storeOneRatingLabel = new Label(
            String.format("%.1f", SimulationWorld.storeOne.getBaseRating()), 
            24
        );
        storeOneRatingLabel.setLineColor(Color.YELLOW);
        storeOneRatingLabel.setFillColor(new Color(255, 255, 0, 255));
        
        storeTwoRatingLabel = new Label(
            String.format("%.1f", SimulationWorld.storeTwo.getBaseRating()), 
            24
        );
        storeTwoRatingLabel.setLineColor(Color.YELLOW);
        storeTwoRatingLabel.setFillColor(new Color(255, 255, 0, 255));
        
        // Add star icons and ratings near the money bars
        w.addObject(storeOneStarIcon, w.getWidth() / 2 - 365, getY());
        w.addObject(storeOneRatingLabel, w.getWidth() / 2 - 330, getY());
        
        w.addObject(storeTwoStarIcon, w.getWidth() / 2 + 365, getY());
        w.addObject(storeTwoRatingLabel, w.getWidth() / 2 + 400, getY());
        
        storeOneDiscountLabel = new Label((int)SimulationWorld.storeOne.getStoreDiscount() + "% OFF", 27);
        storeOneDiscountLabel.setLineColor(Color.BLACK);
        storeOneDiscountLabel.setFillColor(new Color(255, 200, 200, 255));
        w.addObject(storeOneDiscountLabel, w.getWidth() / 2 - 450, getY());  
        
        storeTwoDiscountLabel = new Label((int)SimulationWorld.storeTwo.getStoreDiscount() + "% OFF", 27 );
        storeTwoDiscountLabel.setLineColor(Color.BLACK);
        storeTwoDiscountLabel.setFillColor(new Color(255, 200, 200, 255));
        w.addObject(storeTwoDiscountLabel, w.getWidth() / 2 + 480, getY()); 


    }
    
    /**
     * Called automatically when this StoreUI object is added to a world.
     * Ensures that the display is created once the actor appears in the world.
     *
     * @param world the world that this actor was added to
     */
    @Override
    protected void addedToWorld(World world) {
        createDisplay(world);
    }
    
    /**
     * Updates the money bars and profit labels to match the current store profits.
     * The bars are capped at MAX_MONEY and re-added to keep them visually on top
     * in the correct order.
     */
    private void updateDisplay() {
        if (storeOneProfitLabel == null || storeTwoProfitLabel == null) return;
        
        // Update label text
        storeOneProfitLabel.setValue(
            "Profit: $" + String.format("%.2f", SimulationWorld.storeOne.getProfit())
        );
        storeTwoProfitLabel.setValue(
            "Profit: $" + String.format("%.2f", SimulationWorld.storeTwo.getProfit())
        );
        
        // Remove old bars and labels from world
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
        
        // Update money bar values (capped at MAX_MONEY)
        double storeOneBarValue = Math.min(SimulationWorld.storeOne.getProfit(), MAX_MONEY);
        double storeTwoBarValue = Math.min(SimulationWorld.storeTwo.getProfit(), MAX_MONEY);
        
        storeOneMoneyBar.update((int)(storeOneBarValue));
        storeTwoMoneyBar.update((int)(storeTwoBarValue));
        
        // Add bars back first (behind labels)
        if (w != null) {
            w.addObject(storeOneMoneyBar, w.getWidth() / 2 - 200, getY());
            w.addObject(storeTwoMoneyBar, w.getWidth() / 2 + 250, getY());
            
            // Add labels back second (on top)
            w.addObject(storeOneProfitLabel, w.getWidth() / 2 - 200, getY());
            w.addObject(storeTwoProfitLabel, w.getWidth() / 2 + 250, getY());
        }
        
        // Update discount labels 
        if (storeOneDiscountLabel != null) {
            storeOneDiscountLabel.setValue((int)SimulationWorld.storeOne.getStoreDiscount() + "% OFF");
        }
        if (storeTwoDiscountLabel != null) {
            storeTwoDiscountLabel.setValue((int)SimulationWorld.storeTwo.getStoreDiscount() + "% OFF");
        }
    }
    
    /**
     * Updates the rating display label for a specific store,
     * based on the current average rating value.
     *
     * @param store the store number (1 or 2)
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