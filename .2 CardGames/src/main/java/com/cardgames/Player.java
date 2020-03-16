package com.cardgames;

import java.util.ArrayList;
import java.util.List;

public class Player {

	private String playerName;
	private int playerMoney;
	private List<Card> hand = new ArrayList<Card>();
	
	
// Constructor
	
	public Player(String playerName) {
		this.playerName = playerName;
		playerMoney = 100;
	}

// Getter
	
	public String getPlayerName() {
		return playerName;
	}
	
	public int getPlayerMoney() {
		return playerMoney;
	}
	
	public List<Card> getHand() {
		return hand;
	}
	
	

// Class Methods
	
	public boolean placeBet(int bet) {
		boolean result = false;
		
		if(playerMoney >= bet) {
			playerMoney -= bet;
			result = true;
		}
		
		return result;
	}
	
	public void addToHand(Card drawnCard) {
		hand.add(drawnCard);
	}
	
	public void discardFromHand(List<Card> discardCards) {
		hand.removeAll(discardCards);
	}
	
	public int countHand() {
		
		int result = 0;
		for (Card c : hand) {
			
			result += Card.getBlackjackValue(c);
		}
		
		return result;
	}
	
	public void wonHand(int winnings) {
		playerMoney = getPlayerMoney() + winnings;
	}
	
	public void discardHand() {
		hand.clear();
	}
	
	@Override
	public String toString() {
		return "(" + getPlayerName() + " sits with $" + getPlayerMoney() + ")";
	}
	
	
}
