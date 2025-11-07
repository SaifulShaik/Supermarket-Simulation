import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
/**
 * Furniture Class
 * represents a piece of furniture (shelf, bin, etc.)
 * 
 * @author Joe 
 * @version November 2025
 */
public abstract class Furniture extends SuperSmoothMover
{
    private int x, y;
    private Store store;
    
    public Furniture(Store store, int x, int y) {
        this.store = store;
        this.x = x;
        this.y = y;
        
        store.setBlockedCells(x, y, true);
    }
    
    public Furniture() {}
    protected abstract void stock();
}


