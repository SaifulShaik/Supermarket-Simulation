import greenfoot.*; 
import java.util.*;

public class OwenTesting extends SuperSmoothMover
{
    // movement variables
    private int speed = 2;
    private int itemIndex = 0; //for looping through shopping list
    private int itemX=0;  //x value for finding the item
    private int itemY=0;  //y value for finding the item
    private int currentNode = 0; //for getting to the shop
    private boolean shoppingDone = false;
    private boolean leaving=false;

    //shopping list
    private ArrayList<String> shoppingList = new ArrayList<>();
     //whatever products obtained from the store
    private ArrayList<Product> basket=new ArrayList<>();

    // --- references to world path points ---
    private int[][] pathPoints =
    // simple example path around the middle wall
        new int[][] {
        {500, 500},  // into store
        {150, 500}   //to displays
        };
    public OwenTesting()
    {
        //shopping list for testing 
        shoppingList.add(SimulationWorld.PRODUCT_COKE);
        shoppingList.add(SimulationWorld.PRODUCT_LAYS);
        shoppingList.add(SimulationWorld.PRODUCT_SPRITE);
        shoppingList.add(SimulationWorld.PRODUCT_APPLE);
        shoppingList.add(SimulationWorld.PRODUCT_APPLE);
        shoppingList.add(SimulationWorld.PRODUCT_STEAK);
    }

    public void act()
    {
        if (leaving) {
            leave();
            return;
        }
        if (shoppingDone) {
            goToExit();
            return;
        }

        //avoid bumping into furniture
        avoidObstacles();

        //move to next target area
        moveToNextItem();
    }

    //move around obstacles
    private void avoidObstacles()
    {
        if (isTouching(DisplayUnit.class)) {
            move(5);
            turn(Greenfoot.getRandomNumber(90)); // random small turn
            move(speed);
        }
        if (isTouching(Cashier.class)) {
            move(-5);
            turn(Greenfoot.getRandomNumber(90)); // random small turn
            move(speed);
        }
    }
    private void moveToNextItem()
    {

        if (itemIndex >= shoppingList.size()) {
            shoppingDone = true;

            return;
        }
        // follow waypoint route
        if (pathPoints != null && currentNode < pathPoints.length) {
            int nx = pathPoints[currentNode][0];
            int ny = pathPoints[currentNode][1];

            turnTowards(nx, ny);
            move(speed);
            // reached this node
            if(near(nx,ny))
            {
                currentNode++;
            }
        } else {    
            //lastnote go directly to target
            String itemName = shoppingList.get(itemIndex);
            setItemXY(itemName); //find th x, y value where item can be found

            turnTowards(itemX, itemY);
            move(speed);
            if (near(itemX,itemY)) 
            {   
                retrieveItem(itemName);
                itemIndex++;  //move to the next one

            }
        }
    }
    private boolean near(int x, int y)
    {
        return Math.abs(getX() - x) < 10 && Math.abs(getY() - y) < 10;
    }
    /*
     * set the x  y value where the itme can be found
     */
    private void setItemXY(String productName)
    {
        itemX = 0;
        itemY = 0;
        if (productName.equals(SimulationWorld.PRODUCT_SPRITE) ||
            productName.equals(SimulationWorld.PRODUCT_FANTA) )
        { 
            itemX = 75; itemY = 350; //fridge one area
        } 
        else if (productName.equals(SimulationWorld.PRODUCT_COKE)||
            productName.equals(SimulationWorld.PRODUCT_WATER) ) 
        { 
            itemX = 175; itemY = 350; //fridge two area
        } 
        else if (productName.equals(SimulationWorld.PRODUCT_LAYS)) 
        { 
            itemX = 300; itemY = 350; //snack shelve area
        }       
        else if (productName.equals(SimulationWorld.PRODUCT_APPLE)) 
        { 
            itemX = 870; itemY = 400; //apple bin area
        } 
        else if (productName.equals(SimulationWorld.PRODUCT_LETTUCE)) 
        { 
            itemX = 950; itemY = 400; //lettuce bin area
        } 
        else if (productName.equals(SimulationWorld.PRODUCT_STEAK)) 
        {
            itemX = 1050; itemY = 300; //Display Warmer
        }
    }
    /*
     * Retrieve the product by giving product name
     * Add the product to the basket  - can later be used for checking out.
     */
    private void retrieveItem(String productName)
    {
        DisplayUnit unit;
        Product item=new Coke();


        if(productName.equals(SimulationWorld.PRODUCT_SPRITE))
                {
            unit = getDisplayUnit(FridgeOne.class);
            item=unit.retrieve(Sprite.class);

        }
        else if(productName.equals(SimulationWorld.PRODUCT_FANTA))
                {
            unit = getDisplayUnit(FridgeOne.class);
            item=unit.retrieve(Fanta.class);

        }
        else if(productName.equals(SimulationWorld.PRODUCT_WATER))
                {
            unit = getDisplayUnit(FridgeTwo.class);
            item=unit.retrieve(Water.class);

        }
        else if(productName.equals(SimulationWorld.PRODUCT_COKE))
        {
            unit= getDisplayUnit(FridgeTwo.class);
            item=unit.retrieve(Coke.class);
        }
        else if(productName.equals(SimulationWorld.PRODUCT_LAYS))
                {
            unit = getDisplayUnit(SnackShelf.class);
            item=unit.retrieve(Lays.class);

        }
        else if(productName.equals(SimulationWorld.PRODUCT_LETTUCE))
                {
            unit = getDisplayUnit(LettuceBin.class);
            item=unit.retrieve(Lettuce.class);

        }
        else if(productName.equals(SimulationWorld.PRODUCT_CARROT))
        {
            unit = getDisplayUnit(CarrotBin.class);
            item=unit.retrieve(Carrot.class);

        }
        else if(productName.equals(SimulationWorld.PRODUCT_APPLE))
        {
            unit = getDisplayUnit(AppleBin.class);
            item=unit.retrieve(Apple.class);

        }
        else if(productName.equals(SimulationWorld.PRODUCT_ORANGE))
        {
            unit = getDisplayUnit(OrangeBin.class);
            item=unit.retrieve(Orange.class);

        }
        else if(productName.equals(SimulationWorld.PRODUCT_STEAK))
        {
            unit = getDisplayUnit(SteakWarmer.class);
            item=unit.retrieve(Steak.class);;
        }
        else
        {
            item.setStock(item.getStock()-1);  //change the count back to one to fix item may bot be initialzed error fro the compiler
        }
        if(item !=null)
        {
            item.setLocation(getX(),getY());    
            basket.add(item);
            getWorld().addObject(new SpeechBubble("name:"+item.getName()),getX(),getY());
        }
    }
    //Got to the exit of the shop
    private void goToExit()
    {
        int exitX = 550;
        int exitY = 400;
        turnTowards(exitX, exitY);
        move(speed);
        
        if(near(exitX,exitY))
        {
            leaving=true;
        }
       
    }
    /*
     * After exit the store, leave the shopping mall
     */
    private void leave()
    {
        if(!leaving)
        {
            return;
        }
        setRotation(90);
        setLocation(getX(),getY()-5);
         if (isAtEdge()) {
            getWorld().removeObject(this);
        }
    }
    /*
     * Get the requested DisplayUnit with class type
     */
    private DisplayUnit getDisplayUnit(Class displayClass)
    {
        ArrayList<DisplayUnit> units= (ArrayList<DisplayUnit>) getWorld().getObjects(DisplayUnit.class);
        for(DisplayUnit u:units)
        {
            //return the requested disply unit
            if (u.getClass()==displayClass) 
            {
                return u;
            }
        }
        return null;
    }
}