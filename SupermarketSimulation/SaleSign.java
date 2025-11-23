import greenfoot.*;

/**
 * A small flashing SALE sign that appears above a DisplayUnit
 * and shows which product is currently on sale.
 *
 * The sign rebuilds its text image in the constructor and then
 * flashes by changing transparency at regular intervals.
 * It automatically removes itself when the sale ends or the
 * DisplayUnit is removed from the world.
 *
 * @author Owen Kung
 * @version Nov 2025
 */
public class SaleSign extends Actor
{
    private DisplayUnit owner;
    protected Class<? extends Product> productType;

    private int flashCounter = 0;

    /**
     * Creates a new SaleSign attached to a DisplayUnit.
     * The sign displays the product type currently on sale.
     *
     * @param owner the DisplayUnit this sign belongs to
     * @param productType the product class that is on sale
     */
    public SaleSign(DisplayUnit owner, Class<? extends Product> productType) {
        this.owner = owner;
        this.productType = productType;
        buildImage();
    }

    /**
     * Builds the sign image using a small yellow SALE label
     * and the product name in smaller text. The image is compact
     * so it does not block objects behind it.
     */
    private void buildImage() {
        String unitName = owner.getClass().getSimpleName();
        String productName = productType.getSimpleName();

        int w = 70;
        int h = 28;
        GreenfootImage img = new GreenfootImage(w, h);

        img.setColor(new Color(0, 0, 0, 200));
        img.fillRect(0, 0, w, h);

        img.setColor(Color.YELLOW);
        img.setFont(new Font(true, false, 11));
        img.drawString("SALE", 4, 12);

        img.setFont(new Font(true, false, 9));
        img.drawString(productName.toUpperCase(), 4, 24);

        setImage(img);
    }

    /**
     * Updates the sign each act:
     * - Removes the sign if the sale ends or the display unit is gone
     * - Keeps the sign positioned above the shelf
     * - Creates a flashing effect by alternating transparency
     */
    public void act()
    {
        if (owner == null || owner.getWorld() == null || !SaleManager.isOnSale(productType)) {
            if (getWorld() != null) {
                getWorld().removeObject(this);
            }
            return;
        }

        int shelfTopY = owner.getY() - owner.getImage().getHeight() / 2;
        setLocation(owner.getX(), shelfTopY - getImage().getHeight() / 2 - 2);

        flashCounter++;
        int phase = (flashCounter / 20) % 2;
        if (phase == 0) {
            getImage().setTransparency(255);
        } else {
            getImage().setTransparency(80);
        }
    }
}