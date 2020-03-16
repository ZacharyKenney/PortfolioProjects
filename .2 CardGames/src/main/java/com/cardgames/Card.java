package com.cardgames;

public class Card implements Comparable<Card>{

	private int value;
	private final String suit;
	private boolean isAce;
	
	public static final String[] ALL_RANKS = {"Ace(Low)", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace(High)"};
	
	public static final int[] BLACKJACK_VALUES = { 1, 2 , 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10, 11};
	
	
	public static final int MIN_VALUE = 2;
	public static final int MAX_VALUE = 14;
	
	public static final String SUIT_HEARTS = "Hearts";
	public static final String SUIT_DIAMONDS = "Diamonds";
	public static final String SUIT_SPADES = "Spades";
	public static final String SUIT_CLUBS = "Clubs";
	public static final String[] ALL_SUITS = {SUIT_HEARTS, SUIT_DIAMONDS, SUIT_SPADES, SUIT_CLUBS};
	
	private boolean faceUp;
	
	private static int createdCount = 0;
	private final int serialNumber;
	
	public Card(int value, String suit) {
		this.value = value;
		this.suit = suit;
		
		faceUp = false;
		createdCount ++;
		serialNumber = createdCount;
		
		setAce(value);
	}
	
	public int getSerialNumber() {
		return serialNumber;
	}
	
	public int getValue() {
		return value;
	}
	
	
	public String getRank() {
		return ALL_RANKS[getValue() - 1];
	}
	
		
	public String getSuit() {
		return suit;
	}
	
	public void setAce(int value) {
		if (value == 14 || value == 1)
		isAce = true;
	}
	
	public boolean isAce() {
		return isAce;
	}
	
	
	public boolean isFaceUp() {
		return faceUp;
	}
	
	public void flipCard() {
		faceUp = !faceUp;
	}
	
	public boolean isGreaterThan(Card otherCard) {
		
		boolean result = false;
		
		if (getValue() > otherCard.getValue()) {
			result = true;
		}
		
		return result;
	}
	
	public boolean isGreaterThan(int otherValue) {
		boolean result = false;
		
		if (getValue() > otherValue) {
			result = true;
		}
		
		return result;
	}
	
	public static int getBlackjackValue(Card c) {
		return Card.BLACKJACK_VALUES[c.getValue() - 1];
	}
	
	public static void lowAce(Card c) {
		if (c.isAce()) {
			c.value = 1;
		}
	}
	
	@Override
	public String toString() {
		
		String result = "Face Down...No Peeking!";
		
		if (isFaceUp()) {
			result = getRank() + " of " + getSuit();
		} 
		
		return result;
	}
	
	@Override
	public boolean equals(Object o) {
		
		boolean result = false;
		
		if (o instanceof Card) {
			Card otherCard = (Card) o;
			result = getRank().equals(otherCard.getRank()) && getSuit().equals(otherCard.getSuit());
		}
		
		return result;
	}

	@Override
	public int compareTo(Card o) {
		int result = 0;
		
		if(this.getValue() > o.getValue()) {
			result = 1;
		} else if (this.getValue() < o.getValue()) {
			result = -1;
		}
		return result;
	}
}
