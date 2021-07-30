package com.company;

/**
 * This class represents a colored card.
 * @author Leili
 */
public class ColoredCard extends Card{
    protected String color; //the card's color
    /**
     * Creates a colored card with the given color and score.
     * @param color the card's color
     * @param score the card's score
     */
    public ColoredCard(String color,int score){
        super(score);
        this.color=color;
    }

    /**
     * Gets the card's color.
     * @return color field
     */
    public String getColor() {
        return color;
    }
}
