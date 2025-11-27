import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class EndScreen here.
 * 
 * @author Angelina Zhou
 * @version November 23
 */
public class EndScreen extends World
{
    private Button showStatisticsButton = new Button("Show receipt", 150, 35);
    private String endText;
    
    private GreenfootImage saiful = new GreenfootImage("Cutscene/Saiful/Saiful Think.PNG");
    private GreenfootImage endImage;
    
    private boolean receiptOnScreen = false;
    
    private GreenfootImage background = new GreenfootImage("End Screen.PNG");
    private GreenfootImage receiptImage = new GreenfootImage("Statistics Receipt.PNG");
    private GreenfootImage currentBackground = new GreenfootImage("End Screen.PNG");
    
    public EndScreen(int endType)
    {    
        // Create a new world with 1200x600 cells with a cell size of 1x1 pixels.
        super(1200, 600, 1);
        setPaintOrder(Button.class, CutsceneImage.class);
        
        // Stop all sounds from the simulation
        SoundManager.stopAmbienceSound();
        SoundManager.stopNightSound();
        SoundManager.stopTruckSound();
        SoundManager.stopButcherSound();
        
        if (endType==0){
            endText="End 1: Supermarket wins!\n Saiful chose to shop there";
            endImage=new GreenfootImage("Supermarket End.PNG");
        }
        else if (endType==1){
            endText="End 2: Butcher wins!\n Saiful chose to shop there";
            endImage=new GreenfootImage("Butcher End.PNG");
        }
        
        GreenfootImage text = new GreenfootImage(endText, 30, Color.BLACK, new Color(0,0,0,0));
        background.drawImage(text,725,200);
        saiful.scale(1200,600);
        background.drawImage(saiful,550,70);
        background.drawImage(endImage,0,0);
        
        currentBackground.drawImage(background,0,0);
        setBackground(currentBackground);
        addObject(showStatisticsButton, 125, 575);
        
        createReceipt();
        
    }
    
    public void act(){
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (showStatisticsButton != null && Greenfoot.mouseClicked(null) && mouse != null)
        {
            if (showStatisticsButton.containsPoint(mouse.getX(), mouse.getY()))
            {
                if(!receiptOnScreen){
                    receiptOnScreen=true;
                    showReceipt();
                }
                else{
                    receiptOnScreen=false;
                    hideReceipt();
                }
            }
        }
        }
    
    private void showReceipt(){
        getBackground().drawImage(receiptImage,0,0);
    }
    
    private void hideReceipt(){
        getBackground().drawImage(background,0,0);
    }
    
    private void createReceipt(){
        int supermarketTotalCustomers = Customer.supermarketTotalBargainShoppers 
                                    + Customer.supermarketTotalBulkShoppers 
                                    + Customer.supermarketTotalImpulseShoppers 
                                    + Customer.supermarketTotalRegularShoppers;
        
        int butcherTotalCustomers = Customer.butcherTotalBargainShoppers 
                                    + Customer.butcherTotalBulkShoppers 
                                    + Customer.butcherTotalImpulseShoppers 
                                    + Customer.butcherTotalRegularShoppers;
        
        String supermarketReceipt = 
            Customer.supermarketTotalBargainShoppers + " Bargain Shoppers\n" 
            + Customer.supermarketTotalBulkShoppers + " Bulk Shoppers\n" 
            + Customer.supermarketTotalImpulseShoppers + " Impulse Shoppers\n" 
            + Customer.supermarketTotalRegularShoppers + " Regular Shoppers\n" 
            + "Total Customers: " + supermarketTotalCustomers
            + "\n\n" 
            + Customer.supermarketTotalProductsSold + " total items sold" 
            + "\n\n" 
            + Zombie.supermarketTotalZombies + " Zombies\n";
            
        String butcherReceipt = 
            Customer.butcherTotalBargainShoppers + " Bargain Shoppers\n" 
            + Customer.butcherTotalBulkShoppers + " Bulk Shoppers\n" 
            + Customer.butcherTotalImpulseShoppers + " Impulse Shoppers\n" 
            + Customer.butcherTotalRegularShoppers + " Regular Shoppers\n"
            + "Total Customers: " + butcherTotalCustomers
            + "\n\n" + Customer.butcherTotalProductsSold + " total items sold" + "\n\n" 
            + Zombie.butcherTotalZombies + " Zombies\n";
        
        GreenfootImage receipt = new GreenfootImage(supermarketReceipt, 20, Color.BLACK, new Color(0,0,0,0));
        receiptImage.drawImage(receipt,405,200);
        
        GreenfootImage receipt2 = new GreenfootImage(butcherReceipt, 20, Color.BLACK, new Color(0,0,0,0));
        receiptImage.drawImage(receipt2,610,200);
        
        double storeOneProfit = SimulationWorld.storeOne.getProfit();
        storeOneProfit = Math.round(storeOneProfit * 100.0) / 100.0;
        double storeTwoProfit = SimulationWorld.storeTwo.getProfit();
        storeTwoProfit = Math.round(storeTwoProfit * 100.0) / 100.0;
        
        String supermarketTotalProfit= String.valueOf(storeOneProfit);
        String butcherTotalProfit= String.valueOf(storeTwoProfit);
        
        GreenfootImage total1 = new GreenfootImage(supermarketTotalProfit, 20, Color.BLACK, new Color(0,0,0,0));
        receiptImage.drawImage(total1,720,540);
        
        GreenfootImage total2 = new GreenfootImage(butcherTotalProfit, 20, Color.BLACK, new Color(0,0,0,0));
        receiptImage.drawImage(total2,515,540);
    }
}