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


