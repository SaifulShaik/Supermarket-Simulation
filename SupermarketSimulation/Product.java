import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Product here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public abstract class Product extends Actor
{
    protected double price;
    protected int stock;
    
    protected double getPrice() { return price; }
    protected void applyDiscount(double percent) {}
    protected int getStock() { return stock; }
    protected void setStock(int amount) {}
}
