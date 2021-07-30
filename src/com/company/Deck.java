package com.company;

import java.util.ArrayList;
import java.util.Collections;

/**
 * This class represents the main Deck of the game.
 * @author Leili
 */
public class Deck {
    private String direction; //the direction of the game:clockwise or anticlockwise
    private ArrayList<Card> deckCards; //an ArrayList of deck cards
    private Card faceUpCard; //the face up card
    private String deckColor; //the deck color
    private int deckNumber; //the deck number
    private ArrayList<Player>players; //an ArrayList of players

    /**
     * Creates a deck for the game with the given ArrayList of players.
     * It sets the deck cards, shuffle them,deal 7 cards to each player,
     * sets the face up card to the first deck card, if the card is an instance of
     * Wild Card, it will shuffle again the deck cards and face up another one.
     * It initializes the direction of the game to clockwise
     * It removes the face up card from the deckCards
     * It will shuffle the ArrayList of players,in order to select the first player by chance.
     * @param players an ArrayList of players
     */
    public Deck(ArrayList<Player>players){
        this.players=new ArrayList<>();
        this.players.addAll(players);
        this.deckCards=new ArrayList<>();
        setDeckCards(); //set all the cards
        Collections.shuffle(deckCards); //shuffle the cards
        for(Player p1:this.players){  //deal 7 card to each player
            dealCards(p1);
        }
        this.faceUpCard=deckCards.get(0); //face up the first card
        //if the face up card is a wild card, it shuffles again and again until the first card will be a colored card and then will make it face up.
        while (faceUpCard instanceof WildCard){
            Collections.shuffle(deckCards);
        }
        this.direction="clockwise"; //set the direction of the deck as clockwise
        setFaceUpCard(deckCards.get(0)); //set the face up card
        deckCards.remove(0); //remove the face up card from the deck cards
        Collections.shuffle(this.players); //shuffle the players ArrayList
    }

    /**
     * Gets the players
     * @return players field
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * Gets the direction of the game.
     * @return direction field
     */
    public String getDirection() {
        return direction;
    }

    /**
     * Gets the deck cards
     * @return deckCards field
     */
    public ArrayList<Card> getDeckCards() {
        return deckCards;
    }

    /**
     * Gets the face up card
     * @return faceUpCard field
     */
    public Card getFaceUpCard() {
        return faceUpCard;
    }

    /**
     * Gets the deck color
     * @return deckColor field
     */
    public String getDeckColor() {
        return deckColor;
    }

    /**
     * Gets the deck number
     * @return deckNumber field
     */
    public int getDeckNumber() {
        return deckNumber;
    }

    /**
     * Sets the deck cards.
     * It passes 3 empty ArrayLists to the setWildCards,setActionCards and setNumericCards methods
     * and then after filling them by the methods,it will add all the ArrayLists to the deckCards.
     */
    public void setDeckCards() {
        ArrayList<WildCard> wildCards=new ArrayList<>();
        ArrayList<ActionCard> actionCards=new ArrayList<>();
        ArrayList<NumericCard> numericCards=new ArrayList<>();
        setWildCards(wildCards);
        setActionCards(actionCards);
        setNumericCards(numericCards);
        this.deckCards.addAll(wildCards);
        this.deckCards.addAll(actionCards);
        this.deckCards.addAll(numericCards);
    }

    /**
     * Sets the wild cards.(4 wild color card and 4 wild draw card)
     * @param wildCards an empty ArrayList for adding the cards to it
     */
    private void setWildCards(ArrayList<WildCard> wildCards){
        for(int i=0;i<4;i++){
            WildCard w=new WildCard("wildColor");
            wildCards.add(w);
        }
        for(int i=0;i<4;i++){
            WildCard w=new WildCard("wildDraw");
            wildCards.add(w);
        }
    }

    /**
     * Sets the numeric cards.
     * @param numericCards an empty ArrayList for adding the cards to it
     */
    private void setNumericCards(ArrayList<NumericCard>numericCards){
        ArrayList<String> colors=new ArrayList<>(4);
        colors.add("blue");
        colors.add("green");
        colors.add("yellow");
        colors.add("red");
        for(String color:colors) {
            int k = 0;
            for (int j = 0; j < 2; j++) {
                for (int i = k; i <= 9; i++) {
                    NumericCard c = new NumericCard(color, i);
                    numericCards.add(c);
                }
                k = 1;
            }
        }

    }

    /**
     * Sets the action cards.
     * @param actionCards an empty ArrayList for adding the cards to it
     */
    private void setActionCards(ArrayList<ActionCard> actionCards) {
        ArrayList<String> colors=new ArrayList<>(4);
        colors.add("blue");
        colors.add("green");
        colors.add("yellow");
        colors.add("red");
        ArrayList<String> actions=new ArrayList<>(3);
        actions.add("Skip");
        actions.add("Reverse");
        actions.add("Draw2");
        for(String color:colors){
            for(String action:actions) {
                for (int i = 0; i < 2; i++) {
                    ActionCard c = new ActionCard(color, action);
                    actionCards.add(c);
                }
            }
        }
    }

    /**
     * Deals the cards.
     * It chooses 7 cards from the first of the deckCards and remove them from the deckCards and set them as the player's cards
     * @param player a selected player
     */
    public void dealCards(Player player){
        ArrayList<Card> sevenCards=new ArrayList<>(7);
        for(int i=0;i<7;i++){
            sevenCards.add(deckCards.get(i));
        }
        deckCards.removeAll(sevenCards);
        player.setCards(sevenCards);
    }

    /**
     * Set face up card.
     * It adds the previous face up card to the end of the deckCards list.
     * If the face up card is an instance of colored card, it will initializes the deck color.
     * If the face up card is an instance of Numeric card, it will initializes the deck number.
     * If the face up card is a Reverse card, it will change the direction of the game and make the card not active.
     * If the face up card is an instance of wild card, it will set the deck color to the wild card's nextCardColor.
     * @param faceUpCard the face up card
     */
    public void setFaceUpCard(Card faceUpCard) {
        if(this.faceUpCard!=null){ //add the previous face up card to the end of deck cards ArrayList
            deckCards.add(faceUpCard);
        }
        this.faceUpCard = faceUpCard;
        if(faceUpCard instanceof ColoredCard){
            this.deckColor=((ColoredCard) faceUpCard).getColor();
            if(faceUpCard instanceof NumericCard){
                this.deckNumber=((NumericCard) faceUpCard).getNumber();
            }
            else if(faceUpCard instanceof ActionCard){
                if(((ActionCard) faceUpCard).getAction().equals("Reverse")){
                    changeDirection();
                    ((ActionCard) faceUpCard).setActive(false); //the action is implemented.
                }
            }
        }
        else if(faceUpCard instanceof WildCard){
            this.deckColor=((WildCard) faceUpCard).getNextCardColor();
            System.out.println("The deck color is set to "+deckColor);
        }
    }

    /**
     * Changes the direction of the game.
     */
    public void changeDirection(){
        if(this.direction.equals("clockwise")){
            this.direction="anticlockwise";
        }
        else{
            this.direction="clockwise";
        }
    }

    /**
     * Checks that the game is over or not.
     * It will check each player's cards size and if it equals to 0, the game is over and it returns true.
     * @return true or false
     */
    public boolean isEnd(){
        for(Player p:players){
            if(p.getCards().size()==0){
                System.out.println(p.getName()+" is the winner!");
                return true;
            }
        }
        return false;
    }

    /**
     * Prints the players scores in a sorted order.
     */
    public void printScores(){
        System.out.println("The scores: ");
        ArrayList<Integer>scores=new ArrayList<>();
        for(Player p:players){
            scores.add(p.getScore());
        }
        Collections.sort(scores);
            for(Integer i:scores){
                for(Player p:players) {
                    if (p.getScore() == i) {
                        System.out.println(p.getName() + " : " + p.getScore());
                    }
                }
            }
    }

    /**
     * Prints the number of each player's cards
     */
    public void printNumberOfCards(){
        for(Player p:players){
            System.out.println(p.getName()+" = "+p.getCards().size());
        }
    }
}
