package com.cardgames;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {

	private List<Card> listOfCards = new ArrayList<Card>();
	
	
	public Deck() {
		for (String suit : Card.ALL_SUITS) {
			for (int v = Card.MIN_VALUE; v <= Card.MAX_VALUE; v++) {
				
				listOfCards.add(new Card(v, suit));
			
			}
		}
	}
	
	public Card draw() {
		
		Card drawnCard = null;
		
		if (listOfCards.isEmpty() == false) {
			drawnCard = listOfCards.remove(0);
		} 
		
		return drawnCard;
	}
	
	public void shuffle() {
		Collections.shuffle(listOfCards);
	}
	
	
}
