package com.company;

import java.util.ArrayList;

/**
 * This class represents a Computer Player.
 * @author Leili
 */
public class Player {
    protected ArrayList<Card> cards; //an ArrayList of player's cards
    protected String name; //the name of player
    protected int nextPlayer; //the index of next player

    /**
     * Creates a player with the given name.
     * @param name the player's name
     */
    public Player(String name){
        this.cards=new ArrayList<>();
        this.name=name;
    }

    /**
     * Gets the next player's index
     * @return nextPlayer field
     */
    public int getNextPlayer() {
        return nextPlayer;
    }

    /**
     * Sets the next player's index.
     * If the direction of the game is clockwise,it will return the current index +1 .
     * If the direction of the game is anticlockwise, it will return the current index -1 .
     * @param mainDeck the main deck of the game
     */
    public void setNextPlayer(Deck mainDeck){
            String direction=mainDeck.getDirection();
            if(direction.equals("clockwise")){
                nextPlayer++;
                if(nextPlayer>=mainDeck.getPlayers().size()){
                    //if reaches to the end,so it is the first player's turn
                    nextPlayer=0;
                }
            }
            else{
                nextPlayer--;
                if(nextPlayer<0){
                    //if reaches to the end,so it is the last player's turn
                    nextPlayer=mainDeck.getPlayers().size()-1;
                }
            }
    }

    /**
     * Sets the player's cards.
     * @param cards the player's cards
     */
    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }

    /**
     * Gets the player's cards
     * @return cards field
     */
    public ArrayList<Card> getCards() {
        return cards;
    }

    /**
     * Gets the player's name
     * @return name field
     */
    public String getName() {
        return name;
    }

    /**
     * Draws some cards from the main deck, print them,add them to the player's cards
     * and remove them from the main deck.
     * @param number the number of drawn cards
     * @param mainDeck the main deck of the game
     */
    public void drawCard(int number, Deck mainDeck){
        ArrayList<Card> drawnCards=new ArrayList<>();
        System.out.println("Player draws this cards:");
        for(int i=0;i<number;i++){
            cards.add(mainDeck.getDeckCards().get(i));
            drawnCards.add(mainDeck.getDeckCards().get(i));
            printOneCard(mainDeck.getDeckCards().get(i));
        }
        mainDeck.getDeckCards().removeAll(drawnCards);
    }

    /**
     * Implements the methods in the player's turn.
     * It creates an ArrayList of playable cards in this turn.
     * It prints the direction of the game,the current number of cards of all players,the player's cards
     * and the deck's face up card.
     * It checks the main deck's face up card and call the related methods.
     * Then if the card wasn't a draw2 card or wildDraw card,it will call the playOneCard method
     * and then if the cards number equals to 1, it prints UNO.
     * In the end, it calls the setNextPlayer method.
     * @param mainDeck the main deck of the game
     * @param playersTurn the index of this player
     */
    public void playerTurn(Deck mainDeck,int playersTurn){
        this.nextPlayer=playersTurn;
        System.out.println("The direction of the game is: "+mainDeck.getDirection());
        System.out.println("The players current number of cards:");
        mainDeck.printNumberOfCards();
        printAllCards();
        System.out.println();
        System.out.println("The deck card is: ");
        printOneCard(mainDeck.getFaceUpCard());
        ArrayList<Card> playableCards=new ArrayList<>();
        Card deckCard=mainDeck.getFaceUpCard();
        if(deckCard instanceof WildCard) {
            WildCard w=(WildCard)deckCard;
            if(w.getType().equals("wildColor")){
                wildColorCardFaceUp(mainDeck,playableCards);
            }
            else{  //it was a wildDraw card
               boolean b= wildDrawCardFaceUp(w,mainDeck,playableCards);
               if(b){
                   setNextPlayer(mainDeck);
                   //there isn't anything else to do!
                   return;
               }
            }
        }
        else{ //it was a ColoredCard
            ColoredCard c=(ColoredCard)deckCard;
            if(c instanceof ActionCard){
                ActionCard a=(ActionCard)c;
                boolean b=actionCardFaceUp(a,mainDeck,playableCards);
                if(b){
                    setNextPlayer(mainDeck);
                    //there isn't anything else to do!
                    return;
                }
            }
            else{ //is an instance of Numeric card
                NumericCard n=(NumericCard) c;
                numericCardFaceUp(n,playableCards);
            }
        }
        playOneCard(playableCards,mainDeck);
        if(cards.size()==1){
            System.out.println("UNO");
        }
        setNextPlayer(mainDeck);
    }

    /**
     * It is implemented when the face up card is a wild card.
     * First it checks that the wild draw card is active or not.If it isn't it calls the
     * wildColorCardFaceUp method.(The card works just like a wild color card)
     * Then it checks that if the player has a wild draw card,
     * it plays it,call the doublingWildDrawValue method for the card,prints the card and
     * return true and the player's turn will be over.
     * Else, the player must draws some cards in the amount of wildDrawCardValue and make the card
     * not active and the player's turn will be over.
     * @param w the wild card which is face up
     * @param mainDeck the main deck
     * @param playableCards an ArrayList of playable cards
     * @return true or false
     */
    public boolean wildDrawCardFaceUp(WildCard w,Deck mainDeck,ArrayList<Card>playableCards){
        if(w.isActive()){
            for(Card c:cards){
                if(c instanceof WildCard){
                    if(((WildCard) c).getType().equals("wildDraw")){
                        ((WildCard) c).doublingWildDrawValue(w.getWildDrawValue());
                        System.out.println("Player plays this card:");
                        mainDeck.setFaceUpCard(c);
                        cards.remove(c);
                        System.out.println();
                        printOneCard(c);
                        ((WildCard) c).setNextCardColor(chooseColor());
                        return true;
                    }
                }
            }
            System.out.println("Player draws "+w.getWildDrawValue()+" cards!");
            drawCard(w.getWildDrawValue(),mainDeck);
            w.setActive(false);
            return true;
        }
        else{
            wildColorCardFaceUp(mainDeck,playableCards);
        }
        return false;
    }

    /**
     * It is implemented when the face up card is a wild color card.
     * It adds the cards with the same color as the main deck color to the playable cards list.
     * @param mainDeck the main deck
     * @param playableCards an ArrayList of playable cards
     */
    public void wildColorCardFaceUp(Deck mainDeck,ArrayList<Card> playableCards){
            for(Card card:cards){
                if(card instanceof ColoredCard){
                    ColoredCard c1=(ColoredCard)card;
                    if(c1.getColor().equals(mainDeck.getDeckColor())){
                        playableCards.add(c1);
                    }
                }

            }
    }

    /**
     * It is implemented when the face up card is an action card.
     * If the action equals to Skip,the action of the card sets to false ,the player looses the turn and the method returns true.
     * If the action equals to Reverse, the card works like a not active action card.
     * If the action equals to Draw2, first, it checks that their is any draw2 card or not; if their
     * is, it plays the card, call the doublingDraw2CardValue for the card,prints the card, returns true
     * and the player's turn will be over.if their isn't any Draw2 card,draw some cards
     * in amount of draw2CardValue for the player, makes the card not active , returns true and the player's turn will be over.
     * If the card is a not active card,it calls the actionNotActive method.
     * @param actionCard the action card which is faced up
     * @param mainDeck the main deck
     * @param playableCards an ArrayList of playable cards
     * @return true or false
     */
    public boolean actionCardFaceUp(ActionCard actionCard,Deck mainDeck,ArrayList<Card> playableCards){
            if(actionCard.getAction().equals("Skip")){
                if(actionCard.isActive()){
                    actionCard.setActive(false);
                    System.out.println("Skip!!");
                    return true;
                }
                else{
                    actionNotActiveCardFaceUp(actionCard,playableCards);
                    return false;
                }

            }
            else if(actionCard.getAction().equals("Reverse")){
                    actionNotActiveCardFaceUp(actionCard,playableCards);
                    return false;
            }
            else if(actionCard.getAction().equals("Draw2")){
                if(actionCard.isActive()){
                    for(Card c:cards){
                        if(c instanceof ColoredCard){
                            if(c instanceof ActionCard){
                                if(((ActionCard) c).getAction().equals("Draw2")){
                                    ((ActionCard) c).doublingDraw2CardValue(actionCard.getDraw2CardValue());
                                    System.out.println("Player plays this card:");
                                    mainDeck.setFaceUpCard(c);
                                    cards.remove(c);
                                    System.out.println();
                                    printOneCard(c);
                                    return true;
                                }
                            }
                        }
                    }
                    System.out.println("Player draws " +actionCard.getDraw2CardValue() +" card!");
                    drawCard(actionCard.getDraw2CardValue(),mainDeck); //loose turn
                    actionCard.setActive(false);
                    return true;
                }
                else{
                    actionNotActiveCardFaceUp(actionCard,playableCards);
                    return false;
                }
            }
            return false;
    }

    /**
     * It is implemented when the action card which is faced up isn't active.
     * It adds the cards with the same color or the same action to the playable cards ArrayList.
     * @param notActiveCard an action card which is not active
     * @param playableCards the list of playable cards
     */
    public void actionNotActiveCardFaceUp(ActionCard notActiveCard,ArrayList<Card> playableCards){
        for(Card card:cards){
            if(card instanceof ColoredCard){
                ColoredCard c=(ColoredCard)card;
                if(c.getColor().equals(notActiveCard.getColor())){
                    playableCards.add(c);
                }
                else if(c instanceof ActionCard){
                    if(((ActionCard) c).getAction().equals(notActiveCard.getAction())){
                        playableCards.add(c);
                    }
                }

            }
        }
    }

    /**
     * It is implemented when the face up card is a Numeric card.
     * It adds the cards with the same color or same number to the playableCards ArrayList.
     * @param numericCard the numeric card which is faced up
     * @param playableCards an ArrayList of playable cards
     */
    public void numericCardFaceUp(NumericCard numericCard,ArrayList<Card> playableCards){
        for(Card card:cards){
            if(card instanceof ColoredCard){
                if(((ColoredCard) card).getColor().equals(numericCard.getColor())){
                    playableCards.add(card);
                }
                else if(card instanceof NumericCard){
                    if(((NumericCard) card).getNumber()==numericCard.getNumber()){
                        playableCards.add(card);
                    }
                }
            }
        }
    }

    /**
     * Plays one card.
     * First it adds the wildColor cards to the list of player's cards.(Wild color cars can be used anytime)
     * If the playable cards list is empty, it calls the noPlayableCards method.
     * Else, it will choose a card randomly from the playable cards,plays it and remove it from the player's cards.
     * If it is a wild card, it will choose a color by calling the chooseColor method.
     * @param playableCards an ArrayList of playable cards
     * @param mainDeck the main deck
     */
    public void playOneCard(ArrayList<Card>playableCards,Deck mainDeck){
        for(Card c:cards){
            if(c instanceof WildCard){
                if(((WildCard) c).getType().equals("wildColor")) {
                    if (!(playableCards.contains(c))) {
                        playableCards.add(c);
                    }
                }
            }
        }
        if(playableCards.size()==0){
            //there isn't any card to play
            noPlayableCards(mainDeck);
            return;
        }
        int i= (int) (Math.random()+playableCards.size()-1); //random choose a card from the playable cards list
        System.out.println("Player plays this card:");
        printOneCard(playableCards.get(i));
        if(playableCards.get(i) instanceof WildCard){
            ((WildCard) playableCards.get(i)).setNextCardColor(chooseColor());
        }
        mainDeck.setFaceUpCard(playableCards.get(i));
        cards.remove(playableCards.get(i));
    }

    /**
     * It is implemented when their isn't any playable cards.
     * First it checks that if the player has a wildDraw card, it plays it.
     * Then if the player hasn't any available cards,It draws 1 card form the deck by calling the drawCard method.
     * Then,if the drawn card is a wild card,it plays it.Else if the card matches the deck's color or
     * the deck number or deck action ,it plays it.
     * Else, the player's turn is over.
     * @param mainDeck the main deck of the game
     */
    public void noPlayableCards(Deck mainDeck){
        for(Card card:cards){
            if(card instanceof WildCard){
                    WildCard w=(WildCard)card;
                    System.out.println("Player plays this card:");
                    printOneCard(w);
                    w.setNextCardColor(chooseColor());
                    mainDeck.setFaceUpCard(w);
                    cards.remove(w);
                    return;
            }
        }
        System.out.println("Player draws one card!");
        drawCard(1,mainDeck);
        if(cards.get(cards.size()-1) instanceof WildCard){
            ((WildCard) cards.get(cards.size() - 1)).setNextCardColor(chooseColor());
            mainDeck.setFaceUpCard(cards.get(cards.size()-1));
            cards.remove(cards.get(cards.size()-1));
            System.out.println("Player plays this card:");
            printOneCard(mainDeck.getFaceUpCard());
        }
        else if(cards.get(cards.size()-1) instanceof ColoredCard){ //it is a colored card
            if(((ColoredCard) cards.get(cards.size()-1)).getColor().equals(mainDeck.getDeckColor())){
                mainDeck.setFaceUpCard(cards.get(cards.size()-1));
                cards.remove(cards.get(cards.size()-1));
                System.out.println("Player plays this card:");
                printOneCard(mainDeck.getFaceUpCard());
            }
            else if(cards.get(cards.size()-1) instanceof NumericCard){
                if(((NumericCard) cards.get(cards.size()-1)).getNumber()==mainDeck.getDeckNumber()){
                    mainDeck.setFaceUpCard(cards.get(cards.size()-1));
                    cards.remove(cards.get(cards.size()-1));
                    System.out.println("Player plays this card:");
                    printOneCard(mainDeck.getFaceUpCard());
                }
            }
            else if(cards.get(cards.size()-1) instanceof ActionCard){
                if(mainDeck.getFaceUpCard() instanceof ColoredCard){
                    if(mainDeck.getFaceUpCard() instanceof ActionCard){
                         if(((ActionCard) cards.get(cards.size()-1)).getAction().equals(((ActionCard) mainDeck.getFaceUpCard()).getAction())){
                            mainDeck.setFaceUpCard(cards.get(cards.size()-1));
                            cards.remove(cards.get(cards.size()-1));
                            System.out.println("Player plays this card:");
                            printOneCard(mainDeck.getFaceUpCard());
                         }
                    }
                }
            }
        }

    }

    /**
     * Prints one card.
     * @param card a given card
     */
    public void printOneCard(Card card){
        String name=null;
        String color=null;
        String printColor;
        if(card instanceof WildCard){
            name=((WildCard) card).getType();
        }
        else if(card instanceof ColoredCard){
            if(card instanceof ActionCard){
                name=((ActionCard) card).getAction();
                color=((ActionCard) card).getColor();
            }
            else if(card instanceof NumericCard){
                name="    "+((NumericCard) card).getNumber()+"    ";
                color=((NumericCard) card).getColor();
            }
        }
        if(color==null){
            printColor="\033[0m";
        }
        else{
            printColor = getColor(color);
        }
        if(name.equals("wildDraw")){
            name+=" ";
        }
        else if(name.equals("Skip")){
            name="  Skip   ";
        }
        else if(name.equals("Reverse")){
            name=" Reverse ";
        }
        else if(name.equals("Draw2")){
            name="  Draw2  ";
        }
        System.out.println(printColor+"|$$$$$$$$$$$$$$$|"); //15ta
        System.out.println(printColor+"|               |");
        System.out.println(printColor+"|   "+name+"   |");
        System.out.println(printColor+"|               |");
        System.out.println(printColor+"|$$$$$$$$$$$$$$$|");
        System.out.print("\033[0m");
    }

    /**
     * prints all cards of the player
     */
    public void printAllCards(){
        printFirstLine();
        print2And4Line();
        printThirdLine();
        print2And4Line();
        printFirstLine();
    }

    /**
     * Prints the first line of all the player's cards.
     */
    private void printFirstLine(){
        System.out.println();
        for(Card card:cards){
            String color=null;
            String printColor;
            if(card instanceof ColoredCard){
                if(card instanceof ActionCard){
                    color=((ActionCard) card).getColor();
                }
                else if(card instanceof NumericCard){
                    color=((NumericCard) card).getColor();
                }
            }
            if(color==null){
                printColor="\033[0m";
            }
            else{
                printColor = getColor(color);
            }

            System.out.print(printColor+"|$$$$$$$$$$$$$$$|"); //15ta
            System.out.print("\033[0m");
        }
    }

    /**
     * Prints the 2nd and the 4th line of the player's cards.
     */
    private void print2And4Line(){
        System.out.println();
        for(Card card:cards){
            String color=null;
            String printColor;
            if(card instanceof ColoredCard){
                if(card instanceof ActionCard){
                    color=((ActionCard) card).getColor();
                }
                else if(card instanceof NumericCard){
                    color=((NumericCard) card).getColor();
                }
            }
            if(color==null){
                printColor="\033[0m";
            }
            else{
                printColor = getColor(color);
            }
            System.out.print(printColor+"|               |");
            System.out.print("\033[0m");
        }
    }

    /**
     * Prints the 3d line of all player's cards
     */
    private void printThirdLine(){
        System.out.println();
        for(Card card:cards){
            String name=null;
            String color=null;
            String printColor;
            if(card instanceof WildCard){
                name=((WildCard) card).getType();
            }
            else if(card instanceof ColoredCard){
                if(card instanceof ActionCard){
                    name=((ActionCard) card).getAction();
                    color=((ActionCard) card).getColor();
                }
                else if(card instanceof NumericCard){
                    name="    "+((NumericCard) card).getNumber()+"    ";
                    color=((NumericCard) card).getColor();
                }
            }
            if(color==null){
                printColor="\033[0m";
            }
            else{
                printColor = getColor(color);
            }
            if(name.equals("wildDraw")){
                name+=" ";
            }
            else if(name.equals("Skip")){
                name="  Skip   ";
            }
            else if(name.equals("Reverse")){
                name=" Reverse ";
            }
            else if(name.equals("Draw2")){
                name="  Draw2  ";
            }
            System.out.print(printColor+"|   "+name+"   |");
            System.out.print("\033[0m");
        }
    }

    /**
     * Returns the print code of the given color
     * @param color a String represent a color
     * @return a String represents the code of the color
     */
    private String getColor(String color) {
        String printColor="\033[0m";
        switch (color){
            case "yellow":
                printColor="\033[0;33m";
                break;
            case "red":
                printColor="\033[0;31m";
                break;
            case "green":
                printColor="\033[0;36m";
                break;
            case "blue" :
                printColor="\033[0;34m";
                break;
        }
        return printColor;
    }

    /**
     * Gets the score of the player's cards.
     * @return the summation of the score of cards.
     */
    public int getScore(){
        int score=0;
        for(Card card:cards){
            score+=card.getScoreNumber();
        }
        return score;
    }

    /**
     * It is implemented when a wild card is playing.
     * It checks all of the player's cards color and return the most repetitive color.
     * @return a string represents a color
     */
    public String chooseColor(){
        int b,r,g,y;
        b=r=g=y=0;
        for(Card c:cards){
            if(c instanceof ColoredCard){
                switch (((ColoredCard) c).getColor()){
                    case "yellow":
                        y++;
                        break;
                    case "blue":
                        b++;
                        break;
                    case "green":
                        g++;
                        break;
                    case "red":
                        r++;
                        break;
                }
            }
        }
        if(y>=g && y>=r && y>=b){
            return "yellow";
        }
        else if(g>y && g>=r && g>=b){
            return "green";
        }
        else if(b>=r && b>y && b>g){
            return "blue";
        }
        else {
            return "red";
        }
    }
}
