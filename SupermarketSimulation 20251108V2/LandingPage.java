import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class MyWorld here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class LandingPage extends World
{
    // Labels and Buttons
    private Button playButton;
    private Button settingsButton;
    private Button creditsButton;
    private Label titleLabel;
    private Label subtitleLabel;
    
    // Animation variables
    private int titleOpacity;
    private int titleY;
    private int targetTitleY;
    private int frameCount;
    private boolean titleAnimationComplete;
    
    // Background animation
    private double cloudOffset;
    private static final double CLOUD_SPEED = 0.3;
    
    public LandingPage()
    {    
        super(1200, 600, 1);
        titleOpacity = 0;
        titleY = 200;
        targetTitleY = 150;
        frameCount = 0;
        titleAnimationComplete = false;
        cloudOffset = 0;
        
        prepare();
    }
    
    /**
     * Act method - called every frame
     * Handles animations and button interactions
     */
    public void act()
    {
        frameCount++;
        
        // Fades in and slides the title up
        animateTitle();
        
        animateBackground();
        
        if (playButton != null && playButton.wasClicked())
        {
            
            transitionToSimulation();
        }
        
        if (settingsButton != null && settingsButton.wasClicked())
        {
            transitionToSettings();
        }
        
        if (creditsButton != null && creditsButton.wasClicked())
        {
            transitionToCredits();
        }
    }
    
    // Adding all UI elements
    private void prepare()
    {
        createBackground();
        
        titleLabel = new Label("SUPERMARKET SIMULATOR", 56);
        titleLabel.setLineColor(new Color(0, 0, 0, 0));
        titleLabel.setFillColor(new Color(255, 255, 255, titleOpacity));
        subtitleLabel = new Label("BUILD. SAIFUL. WITH.", 22);
        subtitleLabel.setLineColor(new Color(0, 0, 0, 0));
        subtitleLabel.setFillColor(new Color(225, 225, 225, titleOpacity));
        addObject(titleLabel, 600, titleY);
        addObject(subtitleLabel, 600, 220);
        playButton = new Button("START GAME", 220, 65, 
                                new Color(34, 139, 34), 
                                new Color(50, 205, 50), 
                                Color.WHITE, 30);
        addObject(playButton, 600, 340);
        
        settingsButton = new Button("OPTIONS", 180, 55,
                                    new Color(70, 70, 70),
                                    new Color(100, 100, 100),
                                    Color.WHITE, 24);
        addObject(settingsButton, 600, 430);
        
        creditsButton = new Button("CREDITS", 180, 55,
                                   new Color(50, 50, 50),
                                   new Color(80, 80, 80),
                                   Color.WHITE, 24);
        addObject(creditsButton, 600, 520);
    }

    private void createBackground()
    {
        GreenfootImage bg = getBackground();
        bg.setColor(Color.WHITE);
        bg.fill();
    }
    
    private void addCloudEffect(GreenfootImage bg)
    {
    }
    
    private void addLightingEffect(GreenfootImage bg)
    {
    }
    
    // Fade in and Slide the Title
    private void animateTitle()
    {
        if (!titleAnimationComplete)
        {
            if (titleOpacity < 255)
            {
                titleOpacity = Math.min(titleOpacity + 5, 255);
                titleLabel.setFillColor(new Color(0, 0, 0, titleOpacity));
            }
    
            if (titleY > targetTitleY)
            {
                titleY -= 2;
                titleLabel.setLocation(600, titleY);
            }
    
            int subtitleOpacity = Math.max(0, titleOpacity - 50);
            subtitleLabel.setFillColor(new Color(0, 0, 0, Math.min(subtitleOpacity, 255)));
    
            if (titleOpacity >= 255 && titleY <= targetTitleY)
            {
                titleAnimationComplete = true;
            }
        }
    }
    
    private void animateBackground()
    {
        cloudOffset += CLOUD_SPEED;
        
        // Subtle parallax effect - could be expanded with actual moving sprites
        if (cloudOffset > 1200)
        {
            cloudOffset = 0;
        }
    }
    
    private void transitionToSimulation()
    {
        // Play sound effect if available
        
        // Smooth transition
        fadeOutAndTransition(new Cutscene());
        
    }
    
    private void transitionToSettings()
    {
        // Play sound effect if available
        
        // Transition to settings
        fadeOutAndTransition(new SettingWorld());
    }
    
    /**
     * Transition to credits world
     */
    private void transitionToCredits()
    {
        // Play sound effect if available
        
        // Transition to credits
        fadeOutAndTransition(new CreditsWorld());
    }
    
    private void fadeOutAndTransition(World nextWorld)
    {
        GreenfootImage overlay = new GreenfootImage(getWidth(), getHeight());
        overlay.setColor(new Color(0, 0, 0, 50));
        overlay.fill();
        
        GreenfootImage bg = getBackground();
        bg.drawImage(overlay, 0, 0);
        Greenfoot.delay(5);
        
        Greenfoot.setWorld(nextWorld);
    }
    
    private void applyDynamicScaling(double scaleFactor)
    {
        // Future enhancement: scale all UI elements based on resolution
        // For now, the 1200x600 base resolution works well
    }
    
    public int getFrameCount()
    {
        return frameCount;
    }
    
    // Reset all the animations after the game
    public void resetAnimations()
    {
        titleOpacity = 0;
        titleY = 200;
        frameCount = 0;
        titleAnimationComplete = false;
        cloudOffset = 0;
    }
}
