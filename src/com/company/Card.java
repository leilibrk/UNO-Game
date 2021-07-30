package com.company;

/**
 * This class represents a card.
 * @author Leili
 */
public class Card {
    protected int scoreNumber; //the score number

    /**
     * Creates a card with the given scoreNumber
     * @param scoreNumber the score number
     */
    public Card(int scoreNumber){
        setScoreNumber(scoreNumber);
    }

    /**
     * Sets the score number
     * @param scoreNumber the score number
     */
    public void setScoreNumber(int scoreNumber) {
        this.scoreNumber = scoreNumber;
    }

    /**
     * Gets the score number
     * @return scoreNumber field
     */
    public int getScoreNumber() {
        return scoreNumber;
    }
}
