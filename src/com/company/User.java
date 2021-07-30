package com.company;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class represents a User(a human Player).
 * @author Leili
 */
public class User extends Player {
    /**
     * Creates a user with the given name.
     * @param name the name
     */
    public User(String name) {
        super(name);
    }

    /**
     * The override version of wildDrawCardFaceUp method from the super class.
     * It is implemented when the face up card is a wild card.
     * First it checks that the wild draw card is active or not.If it isn't it calls the
     * wildColorCardFaceUp method.(The card works just like a wild color card)
     * Then it checks that if the player has a wild draw card or not.If the user has it,
     * it asks the user to choose a card(the wildDraw card is the only card he/she can play)then if the user
     * selected the wildDraw card,it plays it,call the doublingWildDrawValue method for the card,prints the card and
     * return true and the player's turn will be over.
     * In case of wrong inputs,it prints a message and the user looses the turn and must take the cards!
     * If the player doesn't have any wildDraw card,he/she must draws some cards in the amount of wildDrawCardValue and it makes the card
     * not active and the player's turn will be over.
     * @param w the wild card which is face up
     * @param mainDeck the main deck
     * @param playableCards an ArrayList of playable cards
     * @return true or false
     */
    @Override
    public boolean wildDrawCardFaceUp(WildCard w, Deck mainDeck, ArrayList<Card> playableCards) {
        if(w.isActive()){
            for(Card c:cards){
                if(c instanceof WildCard){
                    if(((WildCard) c).getType().equals("wildDraw")){
                        System.out.println("Choose the card you want to play. exp: Skip red or WildDraw or 1 yellow");
                        Scanner a=new Scanner(System.in);
                        String answer=a.nextLine();
                        if(answer.equals("wildDraw")){
                            ((WildCard) c).doublingWildDrawValue(w.getWildDrawValue());
                            playWildCard(mainDeck,((WildCard) c));
                            return true;
                        }
                        else{
                            System.out.println("Wrong input!");
                        }
                    }
                }
            }
            System.out.println("You have to draw "+w.getWildDrawValue()+" cards!");
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
     * The override version of actionCardFaceUp method from the super class.
     * It is implemented when the face up card is an action card.
     * If the action equals to Skip,the action of the card sets to false , the player looses the turn and the method returns true.
     * If the action equals to Reverse, the card works like a not active action card.
     * If the action equals to Draw2, first, it checks that their is any draw2 card or not; if their
     * is,it adds them to an ArrayList of draw2Cards.
     * Then asks the player to choose a card(in this case he/she can only plays a draw2 card).
     * If the player selected a Draw2 card, it plays the card, call the doublingDraw2CardValue for the card,
     * prints the card,returns true and the player's turn will be over.
     * In case of wrong inputs,it prints a message and the user looses the turn and must draw cards!
     * If their isn't any Draw2 card, the player must draw some cards in amount of draw2CardValue and
     * it makes the card not active and returns true and the player's turn is over.
     * If the card is an not active card,it calls the actionNotActive method.
     * @param actionCard the action card which is faced up
     * @param mainDeck the main deck
     * @param playableCards an ArrayList of playable cards
     * @return true or false
     */
    @Override
    public boolean actionCardFaceUp(ActionCard actionCard, Deck mainDeck, ArrayList<Card> playableCards) {
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
            ArrayList<ActionCard>draw2cards=new ArrayList<>();
            if(actionCard.isActive()){
                for(Card c:cards){
                    if(c instanceof ColoredCard){
                        if(c instanceof ActionCard){
                            if(((ActionCard) c).getAction().equals("Draw2")){
                                draw2cards.add((ActionCard) c);
                            }
                        }
                    }
                }
                if(draw2cards.size()!=0) {
                    System.out.println("Choose the card you want to play. exp: Skip red or WildDraw or 1 yellow");
                    Scanner a = new Scanner(System.in);
                    String answer = a.nextLine();
                    String s[] = answer.split(" ");
                    if(s.length!=2){
                        System.out.println("Wrong input");
                        return true;
                    }
                    for (ActionCard draw2card : draw2cards) {
                        if (s[0].equals("Draw2") && s[1].equals(draw2card.getColor())) {
                            (draw2card).doublingDraw2CardValue(actionCard.getDraw2CardValue());
                            System.out.println("Player plays this card:");
                            mainDeck.setFaceUpCard(draw2card);
                            cards.remove(draw2card);
                            System.out.println();
                            printOneCard(draw2card);
                            return true;
                        }
                    }
                }
                System.out.println("You have to draw " +actionCard.getDraw2CardValue() +" cards!");
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
     * The override version of playOneCard method from the super class.
     * First it adds the wild cards to the list of player's cards.
     * If the playable cards list is empty, it calls the noPlayableCards method.
     * Else, it asks the user to choose a card,plays it and remove it from the player's cards,if the inputs are correct!
     * If it is a wild card, it asks the user to choose a color.
     * If it is a wildDraw card, it checks that their isn't any other available card to play,
     * and then plays it! If their are still some cards to play, prints a message and the user looses the turn!
     * In case of wrong inputs,it prints a message and the user looses the turn!
     * @param playableCards an ArrayList of playable cards
     * @param mainDeck the main deck
     */
    @Override
    public void playOneCard(ArrayList<Card> playableCards, Deck mainDeck) {
        for(Card c:cards){
            if(c instanceof WildCard){
                if(!(playableCards.contains(c))){
                    playableCards.add(c);
                }
            }
        }
        if(playableCards.size()==0){
            System.out.println("You don't have any playable cards!Draw 1 card please");
            noPlayableCards(mainDeck);
            return;
        }
        System.out.println("Choose the card you want to play. exp: Skip red or WildDraw or 1 yellow");
        Scanner a=new Scanner(System.in);
        String answer=a.nextLine();
        if(answer.equals("wildDraw")){
            for(Card c:playableCards){
                if(c instanceof ColoredCard){
                    //the player still have some cards to play!
                    System.out.println("You cannot play wildCard until you still have other cards to play!You have loosed your turn.");
                    return;
                }
                else if(c instanceof WildCard){
                    if(((WildCard) c).getType().equals("wildColor")){
                        //the player has a wildColor card to play!
                        System.out.println("You cannot play wildCard until you still have other cards to play!You have loosed your turn.");
                        return;
                    }
                }
            }
            for(Card c:playableCards){
                if(c instanceof WildCard){
                    if(((WildCard) c).getType().equals("wildDraw")){
                        playWildCard(mainDeck, ((WildCard) c)); //call the play wild card method
                        return;
                    }
                }
            }

        }
        else if(answer.equals("wildColor")){
            for (Card c:playableCards){
                if(c instanceof WildCard){
                    if(((WildCard) c).getType().equals("wildColor")){
                        playWildCard(mainDeck, ((WildCard) c)); //call the play wild card method
                        return;
                    }
                }
            }
        }
        else{
            String s[]=answer.split(" "); //split the answer
            if(s[0].length()==1){ //a number
                int num=Integer.parseInt(s[0]);
                for(Card c:playableCards){
                    if(c instanceof ColoredCard){
                        if(c instanceof NumericCard){
                            if(((NumericCard) c).getColor().equals(s[1])&& ((NumericCard) c).getNumber()==num){
                                mainDeck.setFaceUpCard(c);
                                System.out.println("Player plays this card:");
                                printOneCard(c);
                                cards.remove(c);
                                return;
                            }
                        }
                    }
                }
            }
            else{  //an action
                for(Card c:playableCards){
                    if(c instanceof ColoredCard){
                        if(c instanceof ActionCard){
                            if(((ActionCard) c).getAction().equals(s[0]) && ((ActionCard) c).getColor().equals(s[1])){
                                mainDeck.setFaceUpCard(c);
                                System.out.println("Player plays this card:");
                                printOneCard(c);
                                cards.remove(c);
                                return;
                            }
                        }
                    }
                }
            }
        }
        System.out.println("Incorrect input!You have loosed your turn!");
    }

    /**
     * The override version of noPlayableCards from the super class.
     * It draws 1 card form the deck by calling the drawCard method.
     * If the drawn card is a playable card (is a wild card or matches the deck color or the deck number or the deck action)
     * ,it asks the player if he/she wants to play the card or not.
     * If the player says no, the player turn is over.
     * Else,it plays the card.(if the card is a wild card it calls the playWildCard method)
     * @param mainDeck the main deck of the game
     */
    @Override
    public void noPlayableCards(Deck mainDeck) {
        drawCard(1,mainDeck);
        if(cards.get(cards.size()-1) instanceof WildCard){
            System.out.println("You can play your drawn card.If you don't want enter no");
            Scanner a=new Scanner(System.in);
            String answer=a.nextLine();
            if(answer.equals("No") || answer.equals("no")){
                return;
            }
            playWildCard(mainDeck,((WildCard) cards.get(cards.size()-1)));
        }
        else if(cards.get(cards.size()-1) instanceof ColoredCard){ //it is a colored card
            if(((ColoredCard) cards.get(cards.size()-1)).getColor().equals(mainDeck.getDeckColor())){
                System.out.println("You can play your drawn card.If you don't want enter no");
                Scanner a=new Scanner(System.in);
                String answer=a.nextLine();
                if(answer.equals("No") || answer.equals("no")){
                    return;
                }
                mainDeck.setFaceUpCard(cards.get(cards.size()-1));
                cards.remove(cards.get(cards.size()-1));
                System.out.println("Player plays this card:");
                printOneCard(mainDeck.getFaceUpCard());
            }
            else if(cards.get(cards.size()-1) instanceof NumericCard){
                if(mainDeck.getFaceUpCard() instanceof ColoredCard) {
                    if(mainDeck.getFaceUpCard() instanceof NumericCard) {
                        if (((NumericCard) cards.get(cards.size() - 1)).getNumber() ==((NumericCard) mainDeck.getFaceUpCard()).getNumber()) {
                            System.out.println("You can play your drawn card.If you don't want enter no");
                            Scanner a = new Scanner(System.in);
                            String answer = a.nextLine();
                            if (answer.equals("No") || answer.equals("no")) {
                                return;
                            }
                            mainDeck.setFaceUpCard(cards.get(cards.size() - 1));
                            cards.remove(cards.get(cards.size() - 1));
                            System.out.println("Player plays this card:");
                            printOneCard(mainDeck.getFaceUpCard());
                        }
                    }
                }
            }

            else if(cards.get(cards.size()-1) instanceof ActionCard){
                if(mainDeck.getFaceUpCard() instanceof ColoredCard){
                    if(mainDeck.getFaceUpCard() instanceof ActionCard){
                        if(((ActionCard) cards.get(cards.size()-1)).getAction().equals(((ActionCard) mainDeck.getFaceUpCard()).getAction())){
                            System.out.println("You can play your drawn card.If you don't want enter no");
                            Scanner a=new Scanner(System.in);
                            String answer=a.nextLine();
                            if(answer.equals("No") || answer.equals("no")){
                                return;
                            }
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
     * It is implemented when the user wants to play a wild card.
     * It asks for a color and sets the wild card's nextCardColor to the selected color and plays the card.
     * In case of wrong color, it prints a message and calls the chooseColor() method in the super class.
     * @param mainDeck the deck of the game
     * @param c the wild card
     */
    private void playWildCard(Deck mainDeck,WildCard c) {
        System.out.println("Choose the deck color: exp. yellow,red,blue,green");
        Scanner b=new Scanner(System.in);
        String color=b.nextLine();
        System.out.println("Player plays this card:");
        printOneCard(c);
        if(!color.equals("yellow") && !color.equals("red") && !color.equals("blue") && !color.equals("green")){
            System.out.println("wrong input! The color is chosen by the system");
            c.setNextCardColor(chooseColor());
        }
        else{
            c.setNextCardColor(color);
        }
        mainDeck.setFaceUpCard(c);
        cards.remove(c);
    }
}
