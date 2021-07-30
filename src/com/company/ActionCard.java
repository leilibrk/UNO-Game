package com.company;

/**
 * This class represents an Action card.
 * @author Leili
 */
public class ActionCard extends ColoredCard {
    private String action; //the card's action: Draw2  or  Reverse  or Skip
    private boolean isActive; //the card is active or not(the activity situation of card)
    private int draw2CardValue; //the draw2 card's value(for Draw2 card)

    /**
     * Creates an action card with the given color,action and score.
     * It sets the isActive field to true.
     * If the card is a Draw2 card, it initializes the draw2CardValue to 2.
     * The card's score is set t0 20 for all action cards
     * @param color the card's color
     * @param action the card's action
     */

    public ActionCard(String color,String action) {
        super(color,20);
        this.action = action;
        this.isActive = true;
        if (action.equals("Draw2")) {
            this.draw2CardValue = 2;
        }
    }

    /**
     * Gets the card's action.
     * @return action field
     */
    public String getAction() {
        return action;
    }

    /**
     * Gets the activity situation of the card.
     * @return isActive field
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Sets the activity situation of the card.
     * @param active a boolean represents the activity situation
     */
    public void setActive(boolean active) {
        isActive = active;
    }

    /**
     * In case of playing a draw2 card over another one, it will get the previous draw2 card's value and
     * increases by 2 and set it to this draw2CardValue.
     * @param value the previous Draw2 card's value
     */
    public void doublingDraw2CardValue(int value){
        this.draw2CardValue=value+2;
    }

    /**
     * Gets the draw 2 card's value.
     * @return draw2CardValue field
     */
    public int getDraw2CardValue() {
        return draw2CardValue;
    }
}
