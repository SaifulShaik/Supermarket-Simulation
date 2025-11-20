import greenfoot.*; 
import java.util.*;

/**
 * An abstract customer that uses A* pathfinding to navigate the store.
 * Each customer has a shopping list, cart, and can find the shortest
 * path to each product's location before collecting it.
 *
 * @author Joe and saiful
 * @version November 2025
 */

public abstract class Customer extends SuperSmoothMover 
{
    private double movementSpeed;
    private double budget;
    
    protected ArrayList<String> shoppingList = new ArrayList<>();
    protected ArrayList<String> shoppingListStore = new ArrayList<>();
    public final ArrayList<String> supermarketProductsList = new ArrayList<>(Arrays.asList(
        SimulationWorld.PRODUCT_COKE, 
        SimulationWorld.PRODUCT_SPRITE, 
        SimulationWorld.PRODUCT_FANTA, 
        SimulationWorld.PRODUCT_WATER,
        SimulationWorld.PRODUCT_CANDY, 
        SimulationWorld.PRODUCT_LAYS, 
        SimulationWorld.PRODUCT_RUFFLES, 
        SimulationWorld.PRODUCT_LAYS, 
        SimulationWorld.PRODUCT_DORITOS, 
        SimulationWorld.PRODUCT_XING_RAMEN,
        SimulationWorld.PRODUCT_NISSIN, 
        SimulationWorld.PRODUCT_JIN_RAMEN, 
        SimulationWorld.PRODUCT_SPRITE
        ));
    public final ArrayList<String> butcherProductsList = new ArrayList<>(Arrays.asList(
        SimulationWorld.PRODUCT_APPLE, 
        SimulationWorld.PRODUCT_ORANGE, 
        SimulationWorld.PRODUCT_LETTUCE, 
        SimulationWorld.PRODUCT_CARROT, 
        SimulationWorld.PRODUCT_RAW_BEEF, 
        SimulationWorld.PRODUCT_STEAK, 
        SimulationWorld.PRODUCT_DRUM_STICK       
        ));
    
    private ArrayList<Product> cart;
    
    private Node previousNode;
    private Node currentNode;
    private Node targetNode;
    
    private int pauseTimer = 0;
    
    private Store store;
    
    public Customer() {}
    
    public Customer(double movementSpeed, double budget, Node currentNode) {
        this.movementSpeed = movementSpeed;
        this.budget = budget;
        
        this.currentNode = currentNode;
        targetNode = null;
        previousNode = null;
        
        store = null;
        
        createShoppingList(10);
        //owenTesting();
        //initiazlied cart      
        cart=new ArrayList();
        
        
    }
    
    public void act() {
        if (store == null) {
            chooseStore();
        }
        move();
        retrieveProdcuts(); 
    }
    
    protected void chooseStore() {
        List<Store> stores = getWorld().getObjects(Store.class);
        
        if (stores.isEmpty()) { return; }
        
        int chosenStore;
        if (shoppingListStore.get(0) == "butcher"){ chosenStore=0; }
        else{ chosenStore=1; }
        
        System.out.println("Hi");
        
        Store s = stores.get(chosenStore);

        targetNode = s.getEntranceNode();
        store = s;
        System.out.println("Heading to store entrance at: " 
        + targetNode.getX() + "," + targetNode.getY());
    }
    
    protected void createShoppingList(int listLength){
        for(int i = 0 ; i < listLength ; i++){
            int productStore = Greenfoot.getRandomNumber(2);
            int productListLength;
            String product;
            if (productStore==0){ 
                productListLength = supermarketProductsList.size();
                product = supermarketProductsList.get(Greenfoot.getRandomNumber(productListLength));
                shoppingListStore.add("supermarket");
            }
            else{
                productListLength = butcherProductsList.size();
                product = butcherProductsList.get(Greenfoot.getRandomNumber(productListLength));
                shoppingListStore.add("butcher");
            }
            shoppingList.add(product);
        }
        //System.out.println("Going to " + shoppingListStore.get(0) + " for " + shoppingList.get(0));
    }
    private void owenTestng()
    {
        shoppingList.add(SimulationWorld.PRODUCT_COKE); 
        shoppingList.add(SimulationWorld.PRODUCT_LAYS);
        shoppingList.add(SimulationWorld.PRODUCT_DORITOS);
        shoppingList.add(SimulationWorld.PRODUCT_RUFFLES);
        shoppingList.add(SimulationWorld.PRODUCT_XING_RAMEN);
        shoppingList.add(SimulationWorld.PRODUCT_NISSIN);
        shoppingList.add(SimulationWorld.PRODUCT_JIN_RAMEN);
        shoppingList.add(SimulationWorld.PRODUCT_CANDY);
        shoppingList.add(SimulationWorld.PRODUCT_SPRITE);
        shoppingList.add(SimulationWorld.PRODUCT_FANTA);
        shoppingList.add(SimulationWorld.PRODUCT_WATER);
        shoppingList.add(SimulationWorld.PRODUCT_APPLE);
        shoppingList.add(SimulationWorld.PRODUCT_ORANGE);
        shoppingList.add(SimulationWorld.PRODUCT_LETTUCE);
        shoppingList.add(SimulationWorld.PRODUCT_CARROT);
        shoppingList.add(SimulationWorld.PRODUCT_STEAK);
        shoppingList.add(SimulationWorld.PRODUCT_RAW_BEEF);
        shoppingList.add(SimulationWorld.PRODUCT_DRUM_STICK);
    }
    /*
     * Retrieve products in the shopping list
     * Add the product to the basket  - can later be used for checking out.
     * 
     * @author: Owen Kung
     * @version: Nov 2025
     */
    protected void retrieveProdcuts()
    {
        //ArrayList<DisplayUnit> units= (ArrayList<DisplayUnit>)getIntersectingObjects(DisplayUnit.class);
        DisplayUnit unit=(DisplayUnit) getOneIntersectingObject(DisplayUnit.class);
        if(isTouching(Butcher.class))
        {
            //go to butcher for RawBeef
            unit=getDisplayUnit(RawBeefHangers.class);
        }
        if(unit==null)
        {
            return;
        }

        //Retrieve what's in fridge
        if(unit.getClass().getSimpleName().equals("Fridge"))
        {
            if(shoppingList.contains(SimulationWorld.PRODUCT_COKE))
            {
                cart.add(unit.retrieve(Coke.class));
                shoppingList.remove(SimulationWorld.PRODUCT_COKE);
            }
            if(shoppingList.contains(SimulationWorld.PRODUCT_SPRITE))
            {
                cart.add(unit.retrieve(Sprite.class));
                shoppingList.remove(SimulationWorld.PRODUCT_SPRITE);
            }
            if(shoppingList.contains(SimulationWorld.PRODUCT_FANTA))
            {
                cart.add(unit.retrieve(Fanta.class));
                shoppingList.remove(SimulationWorld.PRODUCT_FANTA);
            }
            if(shoppingList.contains(SimulationWorld.PRODUCT_WATER))
            {
                cart.add(unit.retrieve(Water.class));
                shoppingList.remove(SimulationWorld.PRODUCT_WATER);
            }
           
        }
        //Retrieve what's in snack shelf
        if(unit.getClass().getSimpleName().equals("SnackShelf"))
        {
            if(shoppingList.contains(SimulationWorld.PRODUCT_LAYS))
            {
                cart.add(unit.retrieve(Lays.class));
                shoppingList.remove(SimulationWorld.PRODUCT_LAYS);
            }
            if(shoppingList.contains(SimulationWorld.PRODUCT_RUFFLES))
            {
                cart.add(unit.retrieve(Ruffles.class));
                shoppingList.remove(SimulationWorld.PRODUCT_RUFFLES);
            }
            if(shoppingList.contains(SimulationWorld.PRODUCT_DORITOS))
            {
                cart.add(unit.retrieve(Doritos.class));
                shoppingList.remove(SimulationWorld.PRODUCT_DORITOS);
            }
        }
        //Retrieve what's in cup noodle shelf
        if(unit.getClass().getSimpleName().equals("CupNoodleShelf"))
        {
            if(shoppingList.contains(SimulationWorld.PRODUCT_XING_RAMEN))
            {
                cart.add(unit.retrieve(XingRamen.class));
                shoppingList.remove(SimulationWorld.PRODUCT_XING_RAMEN);
            }
            if(shoppingList.contains(SimulationWorld.PRODUCT_NISSIN))
            {
                cart.add(unit.retrieve(Nissin.class));
                shoppingList.remove(SimulationWorld.PRODUCT_NISSIN);
            }
            if(shoppingList.contains(SimulationWorld.PRODUCT_JIN_RAMEN))
            {
                cart.add(unit.retrieve(JinRamen.class));
                shoppingList.remove(SimulationWorld.PRODUCT_JIN_RAMEN);
            }
        }
        //Retrieve what's in applebin
        if(unit.getClass().getSimpleName().equals("CandyBin"))
        {
            if(shoppingList.contains(SimulationWorld.PRODUCT_CANDY))
            {
                cart.add(unit.retrieve(Candy.class));
                shoppingList.remove(SimulationWorld.PRODUCT_CANDY);
            }
        }
        if(unit.getClass().getSimpleName().equals("AppleBin"))
        {
            if(shoppingList.contains(SimulationWorld.PRODUCT_APPLE))
            {
                cart.add(unit.retrieve(Apple.class));
                shoppingList.remove(SimulationWorld.PRODUCT_APPLE);
            }
        }
        if(unit.getClass().getSimpleName().equals("OrangeBin"))
        {
            if(shoppingList.contains(SimulationWorld.PRODUCT_ORANGE))
            {
                cart.add(unit.retrieve(Orange.class));
                shoppingList.remove(SimulationWorld.PRODUCT_ORANGE);
            }
        }
        if(unit.getClass().getSimpleName().equals("LettuceBin"))
        {
            if(shoppingList.contains(SimulationWorld.PRODUCT_LETTUCE))
            {
                cart.add(unit.retrieve(Lettuce.class));
                shoppingList.remove(SimulationWorld.PRODUCT_LETTUCE);
            }
        }
        if(unit.getClass().getSimpleName().equals("CarrotBin"))
        {
            if(shoppingList.contains(SimulationWorld.PRODUCT_CARROT))
            {
                cart.add(unit.retrieve(Carrot.class));
                shoppingList.remove(SimulationWorld.PRODUCT_CARROT);
            }
        }
        if(unit.getClass().getSimpleName().equals("SteakWarmer"))
        {
            if(shoppingList.contains(SimulationWorld.PRODUCT_STEAK))
            {
                cart.add(unit.retrieve(Steak.class));
                shoppingList.remove(SimulationWorld.PRODUCT_STEAK);
            }
            if(shoppingList.contains(SimulationWorld.PRODUCT_DRUM_STICK))
            {
                cart.add(unit.retrieve(DrumStick.class));
                shoppingList.remove(SimulationWorld.PRODUCT_DRUM_STICK);
            }
        }
        if(unit.getClass().getSimpleName().equals("RawBeefHangers"))
        {
            if(shoppingList.contains(SimulationWorld.PRODUCT_RAW_BEEF))
            {
                cart.add(unit.retrieve(RawBeef.class));
                shoppingList.remove(SimulationWorld.PRODUCT_RAW_BEEF);
            }
        }

    }
    /*
     * Get the requested DisplayUnit with class type
     * 
     * @author:Owen Kung
     * @version: Nov 2025
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
    protected void move() {
        if (pauseTimer > 0) {
            pauseTimer--;
            return;
        }

        if (targetNode != null) {
            moveToNode(targetNode); 
            return;
        }

        List<Node> neighbouringNodes = currentNode.getNeighbouringNodes();
        
        if (neighbouringNodes.isEmpty() || neighbouringNodes == null) {
            return;
        }
        
        List<Node> availableNodes = new ArrayList<>();
        
        for (Node n : neighbouringNodes) {
            if (n == previousNode) continue;
            else {
                availableNodes.add(n);
            }
        }
        
        if (availableNodes.isEmpty() || availableNodes == null) {
            return;
        }
        
        Node nextNode = availableNodes.get(Greenfoot.getRandomNumber(availableNodes.size()));
        previousNode = currentNode;
        targetNode = nextNode;
        
        moveToNode(nextNode);
    }
    
    protected void moveToNode(Node n) {
        int dx = n.getX() - getX();
        int dy = n.getY() - getY();
        
        double distance = Math.sqrt(dx * dx + dy * dy);
        
        if (distance < movementSpeed) {
            setLocation(n.getX(), n.getY());
            
            currentNode = n;
            targetNode = null;
            
            pauseTimer = 5 + Greenfoot.getRandomNumber(5);
            return;
        }
        
        double angle = Math.atan2(dy, dx);
        
        double newX = getX() + Math.cos(angle) * movementSpeed;
        double newY = getY() + Math.sin(angle) * movementSpeed;
        
        setLocation(newX, newY);
    }
    
    public void leaveStore() {
        getWorld().removeObject(this);
    }
    
    public double calculatePriceOfCart() {
        double total = 0;
        
        for (Product p : cart) {
            total += p.getPrice();
        }
        
        return total;
    }
}

