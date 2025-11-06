import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)  
import java.util.ArrayList;

/**
 * Write a description of class MyWorld here.
 * 
 * @author Owen Kung
 * @version Nov 4 2025
 */
public class SimulationWorld extends World
{
    //public static final int WORLD_WIDTH = 1048;
    //public static final int WORLD_HEIGHT = 514;
    private static final GreenfootImage bg = new GreenfootImage("background.png");
    public SimulationWorld()
    { 
             
        super(bg.getWidth(), bg.getHeight(), 1);
        setBackground(bg); 
        
        // add the butcher
        Butcher butcher = new Butcher();
        addObject(butcher, getWidth()/2+ 480, getHeight()/2);
        
        // add the Cashiers to store 1
        addObject(new Cashier(), getWidth()/2 + 200, getHeight()/2);
        addObject(new Cashier(), getWidth()/2 + 300, getHeight()/2);
        
        // add cashier to store 2
        addObject(new Store2Cashier(), getWidth()/2-230, getHeight()/2+130);
        addObject(new Store2Cashier(), getWidth()/2-330, getHeight()/2+130);
        
        // add fridge to store 2
        //addObject(new Fridge(),60,170);
        
    }
    public void act () 
    {
        //use zSort
        zSort ((ArrayList<Actor>)(getObjects(Actor.class)), this);
    } 
    /**
     * Z-sort so actors with higher Y (lower on screen) render in front.
     * Uses precise Y for SuperSmoothMover when available. Stable for ties.
     */
    public static void zSort(java.util.ArrayList<greenfoot.Actor> actorsToSort, greenfoot.World world) {
        // Local container class (scoped to this method only).
        class Entry implements java.lang.Comparable<Entry> {
            final greenfoot.Actor actor;
            final boolean superSmooth;
            final int order;     // preserve original order for stable ties
            final int xi, yi;    // integer coords snapshot
            final double xd, yd; // precise coords snapshot
    
            // int-based actor
            Entry(greenfoot.Actor a, int x, int y, int order) {
                this.actor = a; this.superSmooth = false; this.order = order;
                this.xi = x; this.yi = y;
                this.xd = x; this.yd = y;
            }
            // precise-based actor
            Entry(greenfoot.Actor a, double x, double y, int order) {
                this.actor = a; this.superSmooth = true; this.order = order;
                this.xi = (int) x; this.yi = (int) y;
                this.xd = x; this.yd = y;
            }
    
            @Override
            public int compareTo(Entry other) {
                double thisY  = superSmooth ? yd : yi;
                double otherY = other.superSmooth ? other.yd : other.yi;
    
                // Handle rare NaN robustly: treat NaN as far back
                if (java.lang.Double.isNaN(thisY) && java.lang.Double.isNaN(otherY)) return java.lang.Integer.compare(order, other.order);
                if (java.lang.Double.isNaN(thisY)) return -1;
                if (java.lang.Double.isNaN(otherY)) return 1;
    
                int cmp = java.lang.Double.compare(thisY, otherY);
                if (cmp != 0) return cmp;
                return java.lang.Integer.compare(this.order, other.order); // stable tie-break
            }
        }
    
        // Snapshot actors and positions first.
        java.util.ArrayList<Entry> list = new java.util.ArrayList<Entry>(actorsToSort.size());
        int order = 0;
        for (greenfoot.Actor a : actorsToSort) {
            if (a instanceof SuperSmoothMover) {
                SuperSmoothMover s = (SuperSmoothMover) a;
                list.add(new Entry(a, s.getPreciseX(), s.getPreciseY(), order++));
            } else {
                list.add(new Entry(a, a.getX(), a.getY(), order++));
            }
        }
    
        // Sort farthest-back (smallest Y) first.
        java.util.Collections.sort(list);
    
        // Re-add in paint order with consistent rounding, then restore precise coords.
        for (Entry e : list) {
            // Remove if currently in any world to ensure paint-order reset
            if (e.actor.getWorld() != null) {
                world.removeObject(e.actor);
            }
            if (e.superSmooth) {
                int rx = roundAwayFromZero(e.xd);
                int ry = roundAwayFromZero(e.yd);
                world.addObject(e.actor, rx, ry);
                // Restore exact double-precision location to avoid drift
                ((SuperSmoothMover) e.actor).setLocation(e.xd, e.yd);
            } else {
                world.addObject(e.actor, e.xi, e.yi);
            }
        }
    }
    /** Helper: symmetric rounding that rounds halves away from zero. */
    private static int roundAwayFromZero(double v) 
    {
        return (int)(v + Math.signum(v) * 0.5);
    }
}




