import greenfoot.*;
import java.util.*;

/**
 * Manages the daily sale event for the supermarket.
 * 
 * Responsibilities:
 *  - Keep track of which product type is on sale.
 *  - Choose a new sale item from a list of possible product types.
 *  - Ensure every product type is used once before repeating (cycle).
 *  - Apply a percentage discount to all matching products already on shelves.
 *  - Let other classes check what is on sale and what the discount is.
 *
 * @author Owen Kung
 * @version Nov 2025
 */
public class SaleManager
{
    /** The product type that is currently on sale (same for all stores). */
    private static Class<? extends Product> saleProduct = null;

    /** Discount percentage for the sale product (for example, 30 means 30% off). */
    private static double discountPercent = 30.0;

    /**
     * Tracks which product types have already been used in the current cycle.
     * Once every type in the available list has been used, this list is cleared
     * and the cycle starts again.
     */
    private static List<Class<? extends Product>> usedProducts = new ArrayList<>();

    /**
     * Sets the current product type that is on sale.
     * Passing null means there is no sale today.
     *
     * @param productType the class of the product that should be on sale,
     *                    or null for no sale.
     */
    public static void setSaleProduct(Class<? extends Product> productType) {
        saleProduct = productType;

        if (productType == null) {
            System.out.println("[SALE] No sale today.");
        } else {
            System.out.println("[SALE] " + productType.getSimpleName() +
                               " is " + discountPercent + "% off!");
        }
    }

    /**
     * Returns the current sale product type.
     *
     * @return the product class that is currently on sale,
     *         or null if there is no active sale.
     */
    public static Class<? extends Product> getSaleProduct() {
        return saleProduct;
    }

    /**
     * Checks whether the given product type is currently on sale.
     *
     * @param type the product class to check.
     * @return true if the type is the active sale product,
     *         false otherwise.
     */
    public static boolean isOnSale(Class<? extends Product> type) {
        return saleProduct != null && saleProduct.equals(type);
    }

    /**
     * Returns the current discount percentage for the sale product.
     *
     * @return the discount percentage (for example, 30.0 means 30% off).
     */
    public static double getDiscountPercent() {
        return discountPercent;
    }

    /**
     * Chooses a new random sale product from the given list of possible types.
     * Behaviour:
     *  - Only picks from the list allTypes.
     *  - Does not repeat a product until every product in allTypes
     *    has been used once (cycle behaviour).
     *  - After choosing, it applies the discount to all matching
     *    Product objects that are already stocked in the given world.
     *
     * @param allTypes list of product classes that are allowed to go on sale.
     * @param world    the current world, used to find all DisplayUnit objects
     *                 and apply discounts to existing stock.
     */
    public static void chooseRandomSale(List<Class<? extends Product>> allTypes, World world) {

        if (allTypes == null || allTypes.isEmpty()) {
            setSaleProduct(null);
            return;
        }

        // If every type has been used once in this cycle, start a fresh cycle
        if (usedProducts.size() == allTypes.size()) {
            usedProducts.clear();
        }

        // Build a list of product types that have not been used in this cycle yet
        List<Class<? extends Product>> remaining = new ArrayList<>();
        for (Class<? extends Product> c : allTypes) {
            if (!usedProducts.contains(c)) {
                remaining.add(c);
            }
        }

        // Randomly pick one product type from the remaining list
        int index = Greenfoot.getRandomNumber(remaining.size());
        Class<? extends Product> newSale = remaining.get(index);

        // Remember that this type has been used in this cycle
        usedProducts.add(newSale);

        // Set the new global sale product
        setSaleProduct(newSale);

        // Apply discount to any existing on-shelf products of this type
        applyDiscountToExistingStock(world);
    }

    /**
     * Applies the current sale discount to all Product objects
     * in the given world that match the active sale product type.
     * This is called right after choosing a new sale item so that
     * already-stocked items get the lower price as well.
     *
     * @param world the current world containing DisplayUnit and Product objects.
     */
    private static void applyDiscountToExistingStock(World world) {
        if (saleProduct == null || world == null) return;

        for (DisplayUnit du : world.getObjects(DisplayUnit.class)) {
            for (Product p : du.getStockedItems()) {
                if (p != null &&
                    saleProduct.equals(p.getClass()) &&
                    !p.isDiscounted())
                {
                    p.applyDiscount(discountPercent);
                }
            }
        }
    }

    /**
     * Clears the current sale.
     * This can be called at midnight or at any time when you want to remove
     * the active sale before choosing a new one.
     */
    public static void resetSale() {
        saleProduct = null;
        System.out.println("[SALE] Sale reset at midnight.");
    }
}