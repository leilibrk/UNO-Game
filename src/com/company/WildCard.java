package com.company;

/**
 * This class represents a Wild card.
 * @author Leili
 */
public class WildCard extends Card {
    private String type; //the card's type: wildColor or wildDraw
    private boolean isActive; //the activity situation of the card(for wildDraw)
    private String nextCardColor; //the color of the next card
    private int wildDrawValue; //the wildDraw card's value(for wildDraw)

    /**
     * Creates a wild card with the given type and score.
     * It sets the isActive to true.
     * If the type is wildDraw, it initializes the wildDrawValue to 4.
     * The card's score is set to 50 for all wild cards.
     * @param type the card's type
     */
    public WildCard(String type){
        super(50);
        this.type=type;
        this.isActive=true;
        if(type.equals("wildDraw")){
            this.wildDrawValue=4;
        }
    }

    /**
     * Sets the next card's color
     * @param nextCardColor the next card's color
     */
    public void setNextCardColor(String nextCardColor) {
        this.nextCardColor = nextCardColor;
    }

    /**
     * Gets the next card's color
     * @return nextCardColor field
     */
    public String getNextCardColor() {
        return nextCardColor;
    }

    /**
     * Gets the type of card
     * @return type field
     */
    public String getType() {
        return type;
    }

    /**
     * Gets the activity situation of the card.(for wild draw card)
     * @return isActive field
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Sets the activity situation of the card(for wild draw card)
     * @param active a boolean represents the activity situation
     */
    public void setActive(boolean active) {
        isActive = active;
    }

    /**
     * In case of playing a wildDraw card over another one, it will get the previous wildDraw card's value and
     * increases by 4 and set it to this wildDrawValue.
     * @param value the previous wildDraw card's value
     */
    public void doublingWildDrawValue(int value){
        this.wildDrawValue=value+4;
    }

    /**
     * Gets the wildDraw card's value
     * @return wildDrawValue field
     */
    public int getWildDrawValue() {
        return wildDrawValue;
    }
}
