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
    
    public EndScreen(int endType)
    {    
        // Create a new world with 1200x600 cells with a cell size of 1x1 pixels.
        super(1200, 600, 1);
        setBackground(new GreenfootImage("End Screen.PNG"));
        addObject(showStatisticsButton, 125, 575);
        
        if (endType==0){
            endText="End 1: Supermarket wins!\n Saiful chose to shop there";
        }
        else if (endType==1){
            endText="End 2: Butcher wins!\n Saiful chose to shop there";
        }
        
        GreenfootImage text = new GreenfootImage(endText, 30, Color.BLACK, new Color(0,0,0,0));
        getBackground().drawImage(text,725,200);
        saiful.scale(1200,600);
        getBackground().drawImage(saiful,550,70);
    }
    
    public void act(){
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (showStatisticsButton != null && Greenfoot.mouseClicked(null) && mouse != null)
        {
            if (showStatisticsButton.containsPoint(mouse.getX(), mouse.getY()))
            {
                showReceipt();
            }
        }
    }
    
    private void showReceipt(){
        getBackground().drawImage(new GreenfootImage("Statistics Receipt.PNG"),0,0);
        
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
            
        System.out.println(supermarketReceipt);
            
        String butcherReceipt = 
            Customer.butcherTotalBargainShoppers + " Bargain Shoppers\n" 
            + Customer.butcherTotalBulkShoppers + " Bulk Shoppers\n" 
            + Customer.butcherTotalImpulseShoppers + " Impulse Shoppers\n" 
            + Customer.butcherTotalRegularShoppers + " Regular Shoppers\n"
            + "Total Customers: " + butcherTotalCustomers
            + "\n\n" + Customer.butcherTotalProductsSold + " total items sold" + "\n\n" 
            + Zombie.butcherTotalZombies + " Zombies\n";
            
        System.out.println(butcherReceipt);
        
        GreenfootImage receipt = new GreenfootImage(supermarketReceipt, 20, Color.BLACK, new Color(0,0,0,0));
        getBackground().drawImage(receipt,405,200);
        
        GreenfootImage receipt2 = new GreenfootImage(butcherReceipt, 20, Color.BLACK, new Color(0,0,0,0));
        getBackground().drawImage(receipt2,610,200);
        
        String supermarketTotalProfit= String.valueOf(SimulationWorld.storeOne.getProfit());
        String butcherTotalProfit= String.valueOf(SimulationWorld.storeTwo.getProfit());
        
        GreenfootImage total1 = new GreenfootImage(supermarketTotalProfit, 20, Color.BLACK, new Color(0,0,0,0));
        getBackground().drawImage(total1,720,540);
        
        GreenfootImage total2 = new GreenfootImage(butcherTotalProfit, 20, Color.BLACK, new Color(0,0,0,0));
        getBackground().drawImage(total2,515,540);
    }
}