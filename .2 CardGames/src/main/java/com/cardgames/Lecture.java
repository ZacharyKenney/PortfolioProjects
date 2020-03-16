package com.cardgames;

public class Lecture {

	public static void main(String[] args) {
		
		Card firstCard = new Card(1, "Hearts");
		
		System.out.println(firstCard);
		firstCard.flipCard();
		System.out.println(firstCard);
		
		
		Card secondCard = new Card(5, "Clubs");
		System.out.println(firstCard.equals(secondCard));
		
		Card thirdCard = new Card(5, "Clubs");
		System.out.println(thirdCard.equals(secondCard));
		
		System.out.println(firstCard.isGreaterThan(secondCard));
		System.out.println(secondCard.isGreaterThan(firstCard));
		System.out.println(secondCard.isGreaterThan(4));
		System.out.println(secondCard.isGreaterThan(6));
		
		
		System.out.println(Math.floor((Math.random() * 10) + 1));
		
		System.out.println(thirdCard.getSerialNumber());
		
		Deck myDeck = new Deck();
		
		Card nextCard = myDeck.draw();
		while (nextCard != null) {
		
			nextCard.flipCard();
			System.out.println(nextCard);
			nextCard = myDeck.draw();
		}
		
		myDeck.shuffle();
		
		
	}

}
