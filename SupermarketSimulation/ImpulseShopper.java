import greenfoot.*;  
import java.util.ArrayList;

/**
 * The impulsive is impatient
 * Can't wait for a long time
 * 
 * @author Owen Kung 
 * @version Nov 2025
 */
public class ImpulseShopper extends Customer
{
    
    private boolean shoppingDone = false;
    private boolean leaving=false;
    
    //shopping list
    private ArrayList<String> shoppingList = new ArrayList<>();
    //whatever products obtained from the store
    private ArrayList<Product> basket=new ArrayList<>();
    public ImpulseShopper()
    {
        //shopping list for testing 
        shoppingList.add(SimulationWorld.PRODUCT_COKE); 
        shoppingList.add(SimulationWorld.PRODUCT_LAYS);
        shoppingList.add(SimulationWorld.PRODUCT_DORITOS);
        shoppingList.add(SimulationWorld.PRODUCT_RUFFLES);
        shoppingList.add(SimulationWorld.PRODUCT_XING_RAMEN);
        shoppingList.add(SimulationWorld.PRODUCT_NISSIN);
        shoppingList.add(SimulationWorld.PRODUCT_JIN_RAMEN);
        shoppingList.add(SimulationWorld.PRODUCT_CANDY);
        shoppingList.add(SimulationWorld.PRODUCT_CANDY);
        shoppingList.add(SimulationWorld.PRODUCT_CANDY);
        shoppingList.add(SimulationWorld.PRODUCT_CANDY);
        shoppingList.add(SimulationWorld.PRODUCT_CANDY);
        shoppingList.add(SimulationWorld.PRODUCT_CANDY);
        shoppingList.add(SimulationWorld.PRODUCT_CANDY);
        shoppingList.add(SimulationWorld.PRODUCT_CANDY);
        shoppingList.add(SimulationWorld.PRODUCT_SPRITE);
        shoppingList.add(SimulationWorld.PRODUCT_FANTA);
        shoppingList.add(SimulationWorld.PRODUCT_WATER);
        shoppingList.add(SimulationWorld.PRODUCT_APPLE);
        shoppingList.add(SimulationWorld.PRODUCT_ORANGE);
        shoppingList.add(SimulationWorld.PRODUCT_ORANGE);
        shoppingList.add(SimulationWorld.PRODUCT_ORANGE);
        shoppingList.add(SimulationWorld.PRODUCT_ORANGE);
        shoppingList.add(SimulationWorld.PRODUCT_LETTUCE);
        shoppingList.add(SimulationWorld.PRODUCT_CARROT);
        shoppingList.add(SimulationWorld.PRODUCT_STEAK);
        shoppingList.add(SimulationWorld.PRODUCT_RAWBEEF);
        shoppingList.add(SimulationWorld.PRODUCT_DRUM_STICK);

    }
    /**
     * Act - do whatever the ImpulseShopper wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        // Add your action code here.
    }
}
