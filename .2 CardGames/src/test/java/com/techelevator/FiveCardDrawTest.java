package com.techelevator;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.cardgames.Card;
import com.cardgames.FiveCardDraw;
import com.cardgames.Player;

public class FiveCardDrawTest {
	
	FiveCardDraw testFiveCard = new FiveCardDraw();
	
	@Test
	public void test_orderHand() {
		
		Player testPlayer = new Player("Zach");
		testPlayer.addToHand(new Card(3, "Hearts"));
		testPlayer.addToHand(new Card(5, "Hearts"));
		testPlayer.addToHand(new Card(4, "Hearts"));
		testPlayer.addToHand(new Card(9, "Hearts"));
		testPlayer.addToHand(new Card(3, "Spades"));
		
		List<Card> orderedHand = testFiveCard.orderHand(testPlayer.getHand());
		
		boolean ordered = false;
		boolean brokeOrder = false;
		for (int i = 0; i < orderedHand.size() - 1; i++) {
			if (orderedHand.get(i).getValue() <= orderedHand.get(i+1).getValue()) {
				if(brokeOrder == false) {
					ordered = true;
				}
			} else {
				brokeOrder = true;
				ordered = false;
			}
		}
		Assert.assertTrue(ordered);
		Assert.assertFalse(brokeOrder);
	}
	
	@Test
	public void test_orderHand2() {
		
		Player testPlayer = new Player("Evan");
		testPlayer.addToHand(new Card(6, "Clubs"));
		testPlayer.addToHand(new Card(5, "Hearts"));
		testPlayer.addToHand(new Card(4, "Hearts"));
		testPlayer.addToHand(new Card(9, "Hearts"));
		testPlayer.addToHand(new Card(2, "Spades"));
		
		List<Card> orderedHand = testFiveCard.orderHand(testPlayer.getHand());
		
		Assert.assertEquals(2, orderedHand.get(0).getValue());
		Assert.assertEquals(6, orderedHand.get(3).getValue());
		Assert.assertEquals("Clubs", orderedHand.get(3).getSuit());
	}

	@Test
	public void test_rankHand1_royalFlush() {
		
		Player testPlayer = new Player("Zach");
		testPlayer.addToHand(new Card(14, "Hearts"));
		testPlayer.addToHand(new Card(12, "Hearts"));
		testPlayer.addToHand(new Card(13, "Hearts"));
		testPlayer.addToHand(new Card(10, "Hearts"));
		testPlayer.addToHand(new Card(11, "Hearts"));
		
		int rank = testFiveCard.rankHand(testPlayer);
		
		Assert.assertEquals(1, rank);
	}
	
	@Test
	public void test_rankHand2_straightFlush() {
		
		Player testPlayer = new Player("Zach");
		testPlayer.addToHand(new Card(9, "Hearts"));
		testPlayer.addToHand(new Card(12, "Hearts"));
		testPlayer.addToHand(new Card(13, "Hearts"));
		testPlayer.addToHand(new Card(10, "Hearts"));
		testPlayer.addToHand(new Card(11, "Hearts"));
		
		int rank = testFiveCard.rankHand(testPlayer);
		
		Assert.assertEquals(2, rank);
	}
	
	@Test
	public void test_rankHand3_FourOfAKind() {
		
		Player testPlayer = new Player("Zach");
		testPlayer.addToHand(new Card(5, "Hearts"));
		testPlayer.addToHand(new Card(5, "Diamonds"));
		testPlayer.addToHand(new Card(6, "Hearts"));
		testPlayer.addToHand(new Card(5, "Clubs"));
		testPlayer.addToHand(new Card(5, "Spades"));
		
		int rank = testFiveCard.rankHand(testPlayer);
		
		Assert.assertEquals(3, rank);
	}
	
	@Test
	public void test_rankHand4_fullHouse() {
		
		Player testPlayer = new Player("Zach");
		testPlayer.addToHand(new Card(5, "Hearts"));
		testPlayer.addToHand(new Card(5, "Diamonds"));
		testPlayer.addToHand(new Card(6, "Hearts"));
		testPlayer.addToHand(new Card(6, "Clubs"));
		testPlayer.addToHand(new Card(5, "Spades"));
		
		int rank = testFiveCard.rankHand(testPlayer);
		
		Assert.assertEquals(4, rank);
	}
	
	@Test
	public void test_rankHand5_flush() {
		
		Player testPlayer = new Player("Zach");
		testPlayer.addToHand(new Card(3, "Hearts"));
		testPlayer.addToHand(new Card(5, "Hearts"));
		testPlayer.addToHand(new Card(4, "Hearts"));
		testPlayer.addToHand(new Card(9, "Hearts"));
		testPlayer.addToHand(new Card(11, "Hearts"));
		
		int rank = testFiveCard.rankHand(testPlayer);
		
		Assert.assertEquals(5, rank);
	}

	@Test
	public void test_rankHand6_Straight() {
		
		Player testPlayer = new Player("Zach");
		testPlayer.addToHand(new Card(5, "Hearts"));
		testPlayer.addToHand(new Card(9, "Spades"));
		testPlayer.addToHand(new Card(7, "Hearts"));
		testPlayer.addToHand(new Card(8, "Diamons"));
		testPlayer.addToHand(new Card(6, "Clubs"));
		
		int rank = testFiveCard.rankHand(testPlayer);
		
		Assert.assertEquals(6, rank);
	}
	@Test
	public void test_rankHand6_Straight_highAce() {
		
		Player testPlayer = new Player("Zach");
		testPlayer.addToHand(new Card(10, "Hearts"));
		testPlayer.addToHand(new Card(14, "Diamond"));
		testPlayer.addToHand(new Card(12, "Hearts"));
		testPlayer.addToHand(new Card(13, "Clubs"));
		testPlayer.addToHand(new Card(11, "Spades"));
		
		int rank = testFiveCard.rankHand(testPlayer);
		
		Assert.assertEquals(6, rank);
	}
	
	@Test
	public void test_rankHand6_Straight_lowAce() {
		
		Player testPlayer = new Player("Zach");
		testPlayer.addToHand(new Card(3, "Hearts"));
		testPlayer.addToHand(new Card(14, "Diamond"));
		testPlayer.addToHand(new Card(4, "Hearts"));
		testPlayer.addToHand(new Card(2, "Clubs"));
		testPlayer.addToHand(new Card(5, "Spades"));
		
		int rank = testFiveCard.rankHand(testPlayer);
		
		Assert.assertEquals(6, rank);
	}

	@Test
	public void test_rankHand7_ThreeOfAKind() {
		
		Player testPlayer = new Player("Zach");
		testPlayer.addToHand(new Card(5, "Hearts"));
		testPlayer.addToHand(new Card(5, "Diamond"));
		testPlayer.addToHand(new Card(6, "Hearts"));
		testPlayer.addToHand(new Card(8, "Clubs"));
		testPlayer.addToHand(new Card(5, "Spades"));
		
		int rank = testFiveCard.rankHand(testPlayer);
		
		Assert.assertEquals(7, rank);
	}
	
	@Test
	public void test_rankHand8_TwoPair() {
		
		Player testPlayer = new Player("Zach");
		testPlayer.addToHand(new Card(5, "Hearts"));
		testPlayer.addToHand(new Card(8, "Diamond"));
		testPlayer.addToHand(new Card(6, "Hearts"));
		testPlayer.addToHand(new Card(6, "Clubs"));
		testPlayer.addToHand(new Card(5, "Spades"));
		
		int rank = testFiveCard.rankHand(testPlayer);
		
		Assert.assertEquals(8, rank);
	}
	
	@Test
	public void test_rankHand9_Pair() {
		
		Player testPlayer = new Player("Zach");
		testPlayer.addToHand(new Card(3, "Hearts"));
		testPlayer.addToHand(new Card(5, "Hearts"));
		testPlayer.addToHand(new Card(4, "Hearts"));
		testPlayer.addToHand(new Card(3, "Clubs"));
		testPlayer.addToHand(new Card(12, "Hearts"));
		
		int rank = testFiveCard.rankHand(testPlayer);
		
		Assert.assertEquals(9, rank);
	}
	
	@Test
	public void test_rankHand10_highCard() {
		
		Player testPlayer = new Player("Zach");
		testPlayer.addToHand(new Card(3, "Hearts"));
		testPlayer.addToHand(new Card(5, "Hearts"));
		testPlayer.addToHand(new Card(4, "Hearts"));
		testPlayer.addToHand(new Card(6, "Clubs"));
		testPlayer.addToHand(new Card(12, "Hearts"));
		
		int rank = testFiveCard.rankHand(testPlayer);
		
		Assert.assertEquals(10, rank);
	}
	
	@Test
	public void test_highCardHand_Standard() {
		
		Player testPlayer = new Player("Zach");
		testPlayer.addToHand(new Card(3, "Hearts"));
		testPlayer.addToHand(new Card(5, "Hearts"));
		testPlayer.addToHand(new Card(4, "Hearts"));
		testPlayer.addToHand(new Card(6, "Clubs"));
		testPlayer.addToHand(new Card(12, "Hearts"));
		
		int rank = testFiveCard.rankHand(testPlayer);
		List<Card> result = testFiveCard.highCardOrder(testPlayer, rank);
		
		Assert.assertEquals(12, result.get(0).getValue());
		Assert.assertEquals(6, result.get(1).getValue());
		Assert.assertEquals(5, result.get(2).getValue());
		Assert.assertEquals(4, result.get(3).getValue());
		Assert.assertEquals(3, result.get(4).getValue());
	}
	
	@Test
	public void test_highCardHand_pair() {
		
		Player testPlayer = new Player("Zach");
		testPlayer.addToHand(new Card(3, "Hearts"));
		testPlayer.addToHand(new Card(5, "Hearts"));
		testPlayer.addToHand(new Card(4, "Hearts"));
		testPlayer.addToHand(new Card(3, "Clubs"));
		testPlayer.addToHand(new Card(12, "Hearts"));
		
		int rank = testFiveCard.rankHand(testPlayer);
		List<Card> result = testFiveCard.highCardOrder(testPlayer, rank);
		
		Assert.assertEquals(3, result.get(0).getValue());
		Assert.assertEquals(3, result.get(1).getValue());
		Assert.assertEquals(12, result.get(2).getValue());
		Assert.assertEquals(5, result.get(3).getValue());
		Assert.assertEquals(4, result.get(4).getValue());
	}
	
	@Test
	public void test_highCardHand_Twopair() {
		
		Player testPlayer = new Player("Zach");
		testPlayer.addToHand(new Card(3, "Hearts"));
		testPlayer.addToHand(new Card(5, "Hearts"));
		testPlayer.addToHand(new Card(5, "Spades"));
		testPlayer.addToHand(new Card(3, "Clubs"));
		testPlayer.addToHand(new Card(12, "Hearts"));
		
		int rank = testFiveCard.rankHand(testPlayer);
		List<Card> result = testFiveCard.highCardOrder(testPlayer, rank);
		
		Assert.assertEquals(5, result.get(0).getValue());
		Assert.assertEquals(5, result.get(1).getValue());
		Assert.assertEquals(3, result.get(2).getValue());
		Assert.assertEquals(3, result.get(3).getValue());
		Assert.assertEquals(12, result.get(4).getValue());
	}
	
	@Test
	public void test_highCardHand_ThreeOfAKind() {
		
		Player testPlayer = new Player("Zach");
		testPlayer.addToHand(new Card(3, "Hearts"));
		testPlayer.addToHand(new Card(3, "Diamonds"));
		testPlayer.addToHand(new Card(5, "Spades"));
		testPlayer.addToHand(new Card(3, "Clubs"));
		testPlayer.addToHand(new Card(12, "Hearts"));
		
		int rank = testFiveCard.rankHand(testPlayer);
		List<Card> result = testFiveCard.highCardOrder(testPlayer, rank);
		
		Assert.assertEquals(3, result.get(0).getValue());
		Assert.assertEquals(3, result.get(1).getValue());
		Assert.assertEquals(3, result.get(2).getValue());
		Assert.assertEquals(12, result.get(3).getValue());
		Assert.assertEquals(5, result.get(4).getValue());
	}
	@Test
	public void test_highCardHand_FullHouse() {
		
		Player testPlayer = new Player("Zach");
		testPlayer.addToHand(new Card(3, "Hearts"));
		testPlayer.addToHand(new Card(3, "Diamonds"));
		testPlayer.addToHand(new Card(5, "Spades"));
		testPlayer.addToHand(new Card(3, "Clubs"));
		testPlayer.addToHand(new Card(5, "Hearts"));
		
		int rank = testFiveCard.rankHand(testPlayer);
		List<Card> result = testFiveCard.highCardOrder(testPlayer, rank);
		
		Assert.assertEquals(3, result.get(0).getValue());
		Assert.assertEquals(3, result.get(1).getValue());
		Assert.assertEquals(3, result.get(2).getValue());
		Assert.assertEquals(5, result.get(3).getValue());
		Assert.assertEquals(5, result.get(4).getValue());
	}
	@Test
	public void test_highCardHand_FourOfAKind() {
		
		Player testPlayer = new Player("Zach");
		testPlayer.addToHand(new Card(3, "Hearts"));
		testPlayer.addToHand(new Card(3, "Diamonds"));
		testPlayer.addToHand(new Card(5, "Spades"));
		testPlayer.addToHand(new Card(3, "Clubs"));
		testPlayer.addToHand(new Card(3, "Spades"));
		
		int rank = testFiveCard.rankHand(testPlayer);
		List<Card> result = testFiveCard.highCardOrder(testPlayer, rank);
		
		Assert.assertEquals(3, result.get(0).getValue());
		Assert.assertEquals(3, result.get(1).getValue());
		Assert.assertEquals(3, result.get(2).getValue());
		Assert.assertEquals(3, result.get(3).getValue());
		Assert.assertEquals(5, result.get(4).getValue());
	}
	@Test
	public void test_highCardHand_lowAceStraight() {
		
		Player testPlayer = new Player("Zach");
		testPlayer.addToHand(new Card(14, "Hearts"));
		testPlayer.addToHand(new Card(3, "Diamonds"));
		testPlayer.addToHand(new Card(5, "Spades"));
		testPlayer.addToHand(new Card(2, "Clubs"));
		testPlayer.addToHand(new Card(4, "Spades"));
		
		int rank = testFiveCard.rankHand(testPlayer);
		List<Card> result = testFiveCard.highCardOrder(testPlayer, rank);
		
		Assert.assertEquals(5, result.get(0).getValue());
		Assert.assertEquals(4, result.get(1).getValue());
		Assert.assertEquals(3, result.get(2).getValue());
		Assert.assertEquals(2, result.get(3).getValue());
		Assert.assertEquals(14, result.get(4).getValue());
	}
}

