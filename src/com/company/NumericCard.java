package com.company;

/**
 * This class represents a Numeric card.
 * @author Leili
 */
public class NumericCard extends ColoredCard {
    private int number; //the card's number

    /**
     * Creates a NumericCard with the given color and number
     * @param color the card's color
     * @param number the card's number
     */
    public NumericCard(String color,int number) {
        super(color,number);
        this.number=number;
    }

    /**
     * Gets the card's number.
     * @return number field
     */
    public int getNumber() {
        return number;
    }
}
