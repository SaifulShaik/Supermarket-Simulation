import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A typical customer who shops in the supermarket.
 *
 * RegularShopper represents an average shopper with a moderate budget,
 * normal walking speed, and a medium-sized shopping list.
 * This class loads the padded walking sprites so the feet line up with
 * the node grid in the world.
 *
 * Author: Owen Kung
 * Version: Nov 2025
 */
public class RegularShopper extends Customer
{
    /**
     * Creates a new RegularShopper starting on the given node.
     *
     * super(2, 100, n, 5, 7, 700) sets:
     *  - movementSpeed = 2
     *  - budget = 100
     *  - startNode = n
     *  - minItems = 5
     *  - maxItems = 7
     *  - maxActCycles = 700
     *
     * Sprite images are padded so the shopper's feet align correctly
     * with the nodes on the path.
     *
     * @param n  the starting Node for the shopper
     */
    public RegularShopper(Node n) {
        // movementSpeed = 2, budget = 100, start node = n,
        // 5–7 items, 700 act cycles before timing out
        super(2, 100, n, 5, 7, 700);

        rightImages = new GreenfootImage[] {
            padImage(new GreenfootImage("regularShopper/right1.png")),
            padImage(new GreenfootImage("regularShopper/right2.png"))
        };

        leftImages = new GreenfootImage[] {
            padImage(new GreenfootImage("regularShopper/left1.png")),
            padImage(new GreenfootImage("regularShopper/left2.png"))
        };

        upImages = new GreenfootImage[] {
            padImage(new GreenfootImage("regularShopper/up1.png")),
            padImage(new GreenfootImage("regularShopper/up2.png"))
        };

        downImages = new GreenfootImage[] {
            padImage(new GreenfootImage("regularShopper/down1.png")),
            padImage(new GreenfootImage("regularShopper/down2.png"))
        };
    }
}

