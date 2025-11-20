import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class MyWorld here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class CreditsWorld extends World
{
    private Button backButton;
    private Label titleLabel;
    private Label versionLabel;
    private Label teamLabel;
    private Label developersLabel;
    private Label nameLabel1;
    private Label nameLabel2;
    private Label nameLabel3;
    private Label nameLabel4;
    private Label nameLabel5;
    
    public CreditsWorld()
    {    
        super(1200, 600, 1);
        prepare();
    }
    
    public void act()
    {
        if (backButton != null && backButton.wasClicked())
        {
            Greenfoot.setWorld(new LandingPage());
        }
    }

    private void prepare()
    {
        GreenfootImage bg = getBackground();
        bg.setColor(new Color(200, 200, 200)); // Light grey
        bg.fill();
        
        // Title - white with no outline
        titleLabel = new Label("CREDITS", 48);
        titleLabel.setLineColor(new Color(0, 0, 0, 0)); 
        titleLabel.setFillColor(Color.WHITE);
        addObject(titleLabel, 600, 80);
        
        // Version info
        versionLabel = new Label("Version 1.0.0", 20);
        versionLabel.setLineColor(new Color(0, 0, 0, 0));
        versionLabel.setFillColor(new Color(100, 100, 100));
        addObject(versionLabel, 600, 140);
        
        // Team header
        teamLabel = new Label("DEVELOPMENT TEAM", 28);
        teamLabel.setLineColor(new Color(0, 0, 0, 0));
        teamLabel.setFillColor(new Color(80, 80, 80));
        addObject(teamLabel, 600, 220);
        
        // Developers header
        developersLabel = new Label("Developed by:", 22);
        developersLabel.setLineColor(new Color(0, 0, 0, 0));
        developersLabel.setFillColor(new Color(100, 100, 100));
        addObject(developersLabel, 600, 280);
        
        // Developer names - Centered
        nameLabel1 = new Label("Saiful Shaik", 20);
        nameLabel1.setLineColor(new Color(0, 0, 0, 0));
        nameLabel1.setFillColor(new Color(60, 60, 60));
        addObject(nameLabel1, 600, 330);
        
        nameLabel2 = new Label("Owen Lee", 20);
        nameLabel2.setLineColor(new Color(0, 0, 0, 0));
        nameLabel2.setFillColor(new Color(60, 60, 60));
        addObject(nameLabel2, 600, 365);
        
        nameLabel3 = new Label("Joe Zhuo", 20);
        nameLabel3.setLineColor(new Color(0, 0, 0, 0));
        nameLabel3.setFillColor(new Color(60, 60, 60));
        addObject(nameLabel3, 600, 400);
        
        nameLabel4 = new Label("Owen Kung", 20);
        nameLabel4.setLineColor(new Color(0, 0, 0, 0));
        nameLabel4.setFillColor(new Color(60, 60, 60));
        addObject(nameLabel4, 600, 435);
        
        nameLabel5 = new Label("Angelina Zhou", 20);
        nameLabel5.setLineColor(new Color(0, 0, 0, 0));
        nameLabel5.setFillColor(new Color(60, 60, 60));
        addObject(nameLabel5, 600, 470);
        
        backButton = new Button("BACK", 150, 50,
                               new Color(70, 70, 70),
                               new Color(100, 100, 100),
                               Color.WHITE, 24);
        addObject(backButton, 600, 540);
    }
}
