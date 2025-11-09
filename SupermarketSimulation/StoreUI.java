import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Store UI
 * 
 * @author Joe
 * @version Novemeber 11, 2025
 */
public class StoreUI extends Actor
{   
    private double storeOneEarnings;
    private double storeTwoEarnings;
    
    private Label storeOneProfitLabel;
    private Label storeTwoProfitLabel;
    
    public StoreUI() {
        storeOneEarnings = 0;
        storeTwoEarnings = 0;
    }
    
    public void act() {
        updateDisplay();
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
    
    public void createDisplay() {
        storeOneProfitLabel = new Label("Profit: $" + String.format("%.2f", storeOneEarnings), 30);
        storeOneProfitLabel.setLineColor(Color.WHITE);
        storeOneProfitLabel.setFillColor(new Color(0, 0, 0, 128));
        
        storeTwoProfitLabel = new Label("Profit: $" + String.format("%.2f", storeTwoEarnings), 30);
        storeTwoProfitLabel.setLineColor(Color.WHITE);
        storeTwoProfitLabel.setFillColor(new Color(0, 0, 0, 128));
        
        getWorld().addObject(storeOneProfitLabel, getWorld().getWidth() - 200, getY());
        getWorld().addObject(storeTwoProfitLabel, getWorld().getWidth() / 2 - 200, getY());
    }
    
    private void updateDisplay() {
        if (storeOneProfitLabel == null || storeTwoProfitLabel == null) return;

        storeOneProfitLabel.setValue("Profit: $" + String.format("%.2f", storeOneEarnings));
        storeTwoProfitLabel.setValue("Profit: $" + String.format("%.2f", storeTwoEarnings));
    }
}
