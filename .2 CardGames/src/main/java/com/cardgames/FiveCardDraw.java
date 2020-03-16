package com.cardgames;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FiveCardDraw {
	
	public static final int NUM_OF_CARD_IN_HAND = 5;
	public static final int NUM_OF_EXCHANGES = 1;
	public static final int MAX_CARD_FOR_EXCHANGE = 5;
	
	public Card winningCard = null;
	
	public void dealHand(Player p , Deck d) {
		for (int i = 0; i < NUM_OF_CARD_IN_HAND; i++) {
			p.addToHand(d.draw());
		}
	}
	
	public void flipHand(Player p) {
		for (Card c : p.getHand()) {
			c.flipCard();
		}
	}
	
	public String showHand(Player p) {
		String result = "";
		int cardCounter = 1;
		for (Card c : p.getHand()) {
			result += "Card " +cardCounter+ ": " +c+ "\n";
			cardCounter++;
		}
		return result;
	}
	
	public void exchangeCards(Player p, Deck d, String exchange) {
		exchange = exchange.replace(" ", "");
		String[] exchangeArr = exchange.split(",");
		int[] exchangeIntArr = new int[exchangeArr.length];
		
		for (int i = 0; i < exchangeArr.length; i++) {
			exchangeIntArr[i] = Integer.parseInt(exchangeArr[i]);
		}
		
		List<Card> discardCards = new ArrayList<>();
		List<Card> hand = p.getHand();
		for (int i : exchangeIntArr) {
			discardCards.add(hand.get(i - 1));
		}
		p.discardFromHand(discardCards);
		
		for (int i = 0; i < exchangeArr.length; i++) {
			Card newCard = d.draw();
			newCard.flipCard();
			p.addToHand(newCard);
		}
	}
	
	public List<Card> orderHand(List<Card> hand) {
		Collections.sort(hand);

		return hand;
	}
	
	public int rankHand(Player p) {
		int rank = 0;

		List<Card> orderedHand = orderHand(p.getHand());

		boolean hasAce = false;
		boolean isStraight = false;
		boolean brokeStraight = false;
		boolean isFlush = false;
		boolean brokeFlush = false;
		boolean isFourOfaKind = false;
		boolean isThreeOfaKind = false;
		boolean isTwoOfaKind = false;
		int ofAKindCOunter = 0;
		int pairCounter = 0;

		int firstCard = orderedHand.get(0).getValue();
		int secondCard = orderedHand.get(1).getValue();
		int middleCard = orderedHand.get(2).getValue();
		int secondLastCard = orderedHand.get(3).getValue();
		int lastCard = orderedHand.get(4).getValue();
//Four of a Kind
		if (firstCard == secondLastCard) {
			isFourOfaKind = true;
		} else if (secondCard == lastCard) {
			isFourOfaKind = true;
		}
//Three of a Kind
		if (firstCard == middleCard) {
			isThreeOfaKind = true;
		} else if (secondCard == secondLastCard) {
			isThreeOfaKind = true;
		} else if (middleCard == lastCard) {
			isThreeOfaKind = true;
		}
// Has Ace
		if (orderedHand.get(orderedHand.size() - 1).getValue() == 14) {
			hasAce = true;
		}
//Straight
		for (int i = 0; i < orderedHand.size() - 1; i++) {
			if (orderedHand.get(i).getValue() == orderedHand.get(i + 1).getValue() - 1) {
				if (brokeStraight == false) {
					isStraight = true;
				}
			} else if ((i == 3 && isStraight) && (orderedHand.get(0).getValue() == 2 && orderedHand.get(4).getValue() == 14)) {
				if (brokeStraight == false) {
					isStraight = true;
				}
			} else {
				brokeStraight = true;
				isStraight = false;
			}
//Flush
			if (orderedHand.get(i).getSuit().equals(orderedHand.get(i + 1).getSuit())) {
				if (brokeFlush == false) {
					isFlush = true;
				}
			} else {
				brokeFlush = true;
				isFlush = false;
			}
// Pair
			if (orderedHand.get(i).getValue() == orderedHand.get(i + 1).getValue()) {
				if (pairCounter == 0) {
					pairCounter++;
				} else if (orderedHand.get(i).getValue() != orderedHand.get(i - 1).getValue()) {
					pairCounter++;
				}
			}

		}
		
		if ((isStraight && isFlush) && hasAce) {
			rank = 1;
		} else if (isStraight && isFlush) {
			rank = 2;
		} else if (isFourOfaKind) {
			rank = 3;
		} else if (isThreeOfaKind && pairCounter == 2) {
			rank = 4;
		} else if (isFlush) {
			rank = 5;
		} else if (isStraight) {
			rank = 6;
		} else if (isThreeOfaKind) {
			rank = 7;
		} else if (pairCounter == 2) {
			rank = 8;
		} else if (pairCounter == 1) {
			rank = 9;
		} else {
			rank = 10;
		}

		return rank;
	}
	
	public String rankName(int rank) {
		String result = "";
		
		if (rank == 1) {
			result = "!!! ROYAL FLU$H !!!";
		} else if (rank == 2) {
			result = "STRAIGHT FLUSH!";
		} else if (rank == 3) {
			result = "FOUR OF A KIND!";
		} else if (rank == 4) {
			result = "Full House!";
		} else if (rank == 5) {
			result = "Flush";
		} else if (rank == 6) {
			result = "Straight";
		} else if (rank == 7) {
			result = "Three of a Kind";
		} else if (rank == 8) {
			result = "Two Pair";
		} else if (rank == 9) {
			result = "Pair";
		} else if (rank == 10) {
			result = "High Card";
		}
		
		return result;
	}
	
	public List<Card> highCardOrder(Player p, int rank) {
		
		List<Card> hand = p.getHand();
		List<Card> orderedHand = orderHand(hand);
		
		List<Card> result = new ArrayList<>();
// Cards High	
		Card card1 = orderedHand.remove(4);
		Card card2 = orderedHand.remove(3);
		Card card3 = orderedHand.remove(2);
		Card card4 = orderedHand.remove(1);
		Card card5 = orderedHand.remove(0);
		
		Card[] originalHand = {card1, card2, card3, card4, card5};
		
		boolean hasAce = card1.getValue() == 14;
		
		if (rank == 3) {
// Four Of A Kind
			if(card1.getValue() == card2.getValue()) {
				for (Card c : originalHand) {
					result.add(c);
				}
			} else {
				originalHand[0] = card5;
				originalHand[4] = card1;
				for (Card c : originalHand) {
					result.add(c);
				}
			}
		} else if (rank == 4) {
// Full House
			if (card1.getValue() == card3.getValue()) {
				for (Card c : originalHand) {
					result.add(c);
				}
			} else {
				for (int i = originalHand.length; i > 0; i --) {
					result.add(originalHand[i -1]);
				}
			}
		} else if (rank == 6 && hasAce) {
//Low Ace Straight
			if (card5.getValue() == 2) {
				for (int i = 1; i < originalHand.length; i ++) {
					result.add(originalHand[i]);
				}
				result.add(originalHand[0]);
			} else {
				for (Card c : originalHand) {
					result.add(c);
				}
			}
		} else if (rank == 7) {
// Three Of A Kind
			if (card1.getValue() == card2.getValue()) {
				for (Card c : originalHand) {
					result.add(c);
				}
			} else if (card2.getValue() == card3.getValue()) {
				originalHand[0] = card4;
				originalHand[3] = card1;
				for (Card c : originalHand) {
					result.add(c);
				}
			} else {
				originalHand[0] = card2;
				originalHand[1] = card1;
				for (int i = originalHand.length; i > 0; i --) {
					result.add(originalHand[i -1]);
				}
			}
		} else if (rank == 8) {
// Two Pair
			if (card1.getValue() == card2.getValue() && card3.getValue() == card4.getValue()) {
				for (Card c : originalHand) {
					result.add(c);
				}
			} else if(card1.getValue() == card2.getValue()) {
				originalHand[2] = card5;
				originalHand[4] = card3;
				for (Card c : originalHand) {
					result.add(c);
				}
			} else {
				originalHand[0] = card3;
				originalHand[2] = card5;
				originalHand[4] = card1;
				for (Card c : originalHand) {
					result.add(c);
				}
			}
		} else if (rank == 9) {
			int highCardValue = 0;
			for (int i = 0;  i < originalHand.length - 1; i++) {
				if (originalHand[i].getValue() == originalHand[i + 1].getValue()) {
					highCardValue = originalHand[i].getValue();
				}
			}
			for (Card c : originalHand) {
				if (c.getValue() == highCardValue) {
					result.add(c);
				}
			}
			for (Card c : originalHand) {
				if (c.getValue() != highCardValue) {
					result.add(c);
				}
			}
		} else {
			for (Card c : originalHand) {
				result.add(c);
			}
		}
		
		return result;
	}

	public boolean winOrLose(int dealerRank, int playerRank, List<Card> dealerHighCards, List<Card> playerHighCards) {
		boolean playerWon = false;
		
		if (playerRank < dealerRank) {
			playerWon = true;
			winningCard = playerHighCards.get(0);
		} else if (playerRank == dealerRank) {
			boolean done = false;
			while (done == false) {
				if (playerHighCards.get(0).getValue() != dealerHighCards.get(0).getValue()) {
					if (playerHighCards.get(0).getValue() > dealerHighCards.get(0).getValue()) {
						playerWon = true;
						winningCard = playerHighCards.get(0);
						done = true;
					} else {
						winningCard = dealerHighCards.get(0);
						done = true;
					}
				} else {
					playerHighCards.remove(0);
					dealerHighCards.remove(0);
				}
			}
		} else {
			winningCard = dealerHighCards.get(0);
		}
		
		return playerWon;
	}
	
}
