import greenfoot.*;

/**
 * A small flashing SALE sign that sits above a DisplayUnit and
 * shows which product is on sale.
 */
public class SaleSign extends Actor
{
    private DisplayUnit owner;
    protected Class<? extends Product> productType;

    private int flashCounter = 0;

    public SaleSign(DisplayUnit owner, Class<? extends Product> productType) {
        this.owner = owner;
        this.productType = productType;
        buildImage();
    }

    /** Build a compact text-only image: narrower + smaller font. */
    private void buildImage() {
        String unitName = owner.getClass().getSimpleName();
        String productName = productType.getSimpleName();

        int w = 70;   // narrower
        int h = 28;   // shorter
        GreenfootImage img = new GreenfootImage(w, h);

        // dark background rectangle
        img.setColor(new Color(0, 0, 0, 200));
        img.fillRect(0, 0, w, h);

        // small yellow "SALE!"
        img.setColor(Color.YELLOW);
        img.setFont(new Font(true, false, 11));   // smaller
        img.drawString("SALE", 4, 12);

        // tiny text: product name only (to save space)
        img.setFont(new Font(true, false, 9));
        img.drawString(productName.toUpperCase(), 4, 24);

        setImage(img);
    }

    public void act()
    {
        // if owner is gone, or sale changed/ended, remove the sign
        if (owner == null || owner.getWorld() == null || !SaleManager.isOnSale(productType)) {
            if (getWorld() != null) {
                getWorld().removeObject(this);
            }
            return;
        }

        // keep sign sitting just above the shelf
        int shelfTopY = owner.getY() - owner.getImage().getHeight() / 2;
        setLocation(owner.getX(), shelfTopY - getImage().getHeight() / 2 - 2);

        // flashing animation: toggle transparency every few acts
        flashCounter++;
        int phase = (flashCounter / 60) % 2;   // slightly slower flash
        if (phase == 0) {
            getImage().setTransparency(255);   // bright
        } else {
            getImage().setTransparency(80);    // dim
        }
    }
}