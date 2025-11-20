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

    //for image animation
    private GreenfootImage[] frames;
    private int currentFrame = 0;
    private int animationDelay = 10; // smaller = faster animation
    private int delayCounter = 0;
    
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
        frames = new GreenfootImage[2];
        for (int i = 0; i < 2; i++) {
            frames[i] = new GreenfootImage("maleShopper/down" + (i + 1) + ".png");
            frames[i].scale(45,75);
        }
        setImage(frames[0]);
        
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
        shoppingList.add(SimulationWorld.PRODUCT_RAW_BEEF);
        shoppingList.add(SimulationWorld.PRODUCT_DRUM_STICK);
    }

    public void act()
    {
        animate();
        if (leaving) {
            leave();
            return;
        }
        if (shoppingDone) {
            goToExit();
            return;
        }

        //**avoid bumping into display unit, cashier and butcher
        avoidObstacles();

        //move to next target area
        moveToNextItem();
    }

    private void animate() {
        delayCounter++;
        if (delayCounter >= animationDelay) {
            currentFrame = (currentFrame + 1) % frames.length;
            //frames[currentFrame].scale(frames[currentFrame].getWidth()/4,frames[currentFrame].getHeight()/4);
            setImage(frames[currentFrame]);
            delayCounter = 0;
        }
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
         if (isTouching(Butcher.class)) {
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
        //based on chatGPT code and modified on top
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
            //lastnote go directly to find items
            String itemName = shoppingList.get(itemIndex);
            setItemXY(itemName); //find th x, y value where item can be found

            turnTowards(itemX, itemY);
            move(speed);
            //moveToward(itemX,itemY); //not working yet
            

            if (near(itemX,itemY)) 
            {   
                retrieveItem(itemName);
                itemIndex++;  //move to the next one
            }
        }
    }
    //still under construction
    //chatGPT
    private void moveToward(int targetX, int targetY)
    {
        int x = getX();
        int y = getY();

        // 1) Move in X first
        if (x != targetX) {
            int dx = targetX - x;
            int stepX = Math.min(speed, Math.abs(dx)) * Integer.signum(dx);
            face(stepX, 0); 
            setLocation(x + stepX, y);
            return; // stop here this frame so Y waits until X is done
        }

        // 2) Then move in Y
        if (y != targetY) {
            int dy = targetY - y;
            int stepY = Math.min(speed, Math.abs(dy)) * Integer.signum(dy);
             face(0, stepY);
            setLocation(x, y + stepY);
            return;
        }
        
    }
    //Point the sprite toward the step direction. */
    //chatGPT still under construction
    private void face(int dx, int dy) {
        if (dx > 0)      setRotation(0);   // right
        else if (dx < 0) setRotation(180); // left
        else if (dy > 0) setRotation(90);  // down (y grows downward in Greenfoot)
        else if (dy < 0) setRotation(270); // up
        // if dx=dy=0, keep current rotation
    }
    private boolean near(int x, int y)
    {
        return Math.abs(getX() - x) < 10 && Math.abs(getY() - y) < 10;
    }
    /*
     * set the x  y value where the item can be found
     */
    private void setItemXY(String productName)
    {
        itemX = 0;
        itemY = 0;
        if (productName.equals(SimulationWorld.PRODUCT_SPRITE) ||
            productName.equals(SimulationWorld.PRODUCT_FANTA) )
        { 
            itemX = 65; itemY = 325; //fridge one area
        } 
        else if (productName.equals(SimulationWorld.PRODUCT_COKE)||
            productName.equals(SimulationWorld.PRODUCT_WATER) ) 
        { 
            itemX = 135; itemY = 325; //fridge two area
        } 
        else if (productName.equals(SimulationWorld.PRODUCT_LAYS) ||
            productName.equals(SimulationWorld.PRODUCT_DORITOS) ||
            productName.equals(SimulationWorld.PRODUCT_RUFFLES) )
        { 
            itemX = 230; itemY = 325; //snack shelve area
        }   
        else if (productName.equals(SimulationWorld.PRODUCT_XING_RAMEN) ||
            productName.equals(SimulationWorld.PRODUCT_NISSIN) ||
            productName.equals(SimulationWorld.PRODUCT_JIN_RAMEN) )
        { 
            itemX = 335; itemY = 325; //cup noodle shelve area
        } 
        else if (productName.equals(SimulationWorld.PRODUCT_CANDY)) 
        { 
            itemX = 100; itemY = 400; //lettuce bin area
        } 
        else if (productName.equals(SimulationWorld.PRODUCT_LETTUCE)) 
        { 
            itemX = 1010; itemY = 350; //lettuce bin area
        } 
        else if (productName.equals(SimulationWorld.PRODUCT_CARROT)) 
        { 
            itemX = 940; itemY = 350; //carrot bin area
        } 
        else if (productName.equals(SimulationWorld.PRODUCT_APPLE)) 
        { 
            itemX = 870; itemY = 350; //apple bin area
        } 
        else if (productName.equals(SimulationWorld.PRODUCT_ORANGE)) 
        { 
            itemX = 800; itemY = 350; //orange bin area
        } 
        else if (productName.equals(SimulationWorld.PRODUCT_RAW_BEEF) )
        {
            itemX = 975; itemY = 290; //next to steak warmer
        }
        else if (productName.equals(SimulationWorld.PRODUCT_STEAK) ||
                 productName.equals(SimulationWorld.PRODUCT_DRUM_STICK)) 
        {
            itemX = 1060; itemY = 280; //Display Warmer
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
        else if(productName.equals(SimulationWorld.PRODUCT_DORITOS))
        {
            unit = getDisplayUnit(SnackShelf.class);
            item=unit.retrieve(Doritos.class);

        }
        else if(productName.equals(SimulationWorld.PRODUCT_RUFFLES))
        {
            unit = getDisplayUnit(SnackShelf.class);
            item=unit.retrieve(Ruffles.class);

        }
        else if(productName.equals(SimulationWorld.PRODUCT_CANDY))
        {
            unit = getDisplayUnit(CandyBin.class);
            item=unit.retrieve(Candy.class);

        }
        else if(productName.equals(SimulationWorld.PRODUCT_XING_RAMEN))
        {
            unit = getDisplayUnit(CupNoodleShelf.class);
            item=unit.retrieve(XingRamen.class);

        }
        else if(productName.equals(SimulationWorld.PRODUCT_NISSIN))
        {
            unit = getDisplayUnit(CupNoodleShelf.class);
            item=unit.retrieve(Nissin.class);

        }
        else if(productName.equals(SimulationWorld.PRODUCT_JIN_RAMEN))
        {
            unit = getDisplayUnit(CupNoodleShelf.class);
            item=unit.retrieve(JinRamen.class);

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
        else if(productName.equals(SimulationWorld.PRODUCT_DRUM_STICK))
        {
            unit = getDisplayUnit(SteakWarmer.class);
            item=unit.retrieve(DrumStick.class);;
        }
        else if(productName.equals(SimulationWorld.PRODUCT_RAW_BEEF))
        {
            unit = getDisplayUnit(RawBeefHangers.class);
            item=unit.retrieve(RawBeef.class);;
        }
        else
        {
            item.setStock(item.getStock()-1);  //change the count back to one to fix item may bot be initialzed error fro the compiler
        }
        if(item !=null)
        {
            item.setLocation(getX(),getY());    
            basket.add(item);
getWorld().showText(item.getName()+" has "+item.getStock(),350,50);//testing
getWorld().showText("basket size="+basket.size(),150, 50);//testing
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
       
        checkOut();
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
    private void checkOut()
    {
        String text="";
        double total=0;
        int count=0;
        for(Product i:basket)
        {   text=text+"\n "+i.getName()+" bought at "+i.getPrice();
            total=total+i.getPrice();  
            //i.setLocation(550,400);
            //i.getImage().setTransparency(255);
            getWorld().removeObject(i);
            count++;
        }

        text=text+"\n"+"Total Item:"+count;
        text=text+"\n"+"Total:"+Math.round(total*100.0)/100.0;

 showText(text,Color.RED,600,150);//for testing

    }
}