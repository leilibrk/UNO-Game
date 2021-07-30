package com.company;


import java.util.ArrayList;
import java.util.Scanner;

/**
 * This program represents the UNO game.
 * All of the players can be computer players,human players or a combination of both!
 * In this program we can also have n human players!
 * @author Leili
 */
public class Main {

    public static void main(String[] args) {
        ArrayList<Player>players=new ArrayList<>(); //an ArrayList of all players
        System.out.println("Welcome to UNO game!");
        System.out.println("Enter the number of computer players:");
        Scanner a=new Scanner(System.in);
        int num1=a.nextInt(); //Scan the number of computer players
        for(int i=0;i<num1;i++){ //Scan the name of each computer player
            System.out.println("Enter the name of computer player");
            Scanner c=new Scanner(System.in);
            String name=c.nextLine();
            Player p=new Player(name);
            players.add(p);
        }
        System.out.println("Enter the number of human players:");
        Scanner b=new Scanner(System.in);
        int num2=b.nextInt(); //Scan the number of human players
        for(int i=0;i<num2;i++){ //Scan the name of each human player
            System.out.println("Enter the name of human player");
            Scanner e=new Scanner(System.in);
            String name=e.nextLine();
            User user=new User(name);
            players.add(user);
        }
        Deck mainDeck=new Deck(players); //the main deck of the game
        int i=0;
        while (!mainDeck.isEnd()) {
            i = mainDeck.getPlayers().get(i).getNextPlayer(); //the next player's index
            System.out.println(mainDeck.getPlayers().get(i).getName() + " :");
            mainDeck.getPlayers().get(i).playerTurn(mainDeck,i);
        }
       mainDeck.printScores(); //prints the scores
    }
}
