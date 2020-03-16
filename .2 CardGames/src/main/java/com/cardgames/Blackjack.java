package com.cardgames;


import java.util.Scanner;

public class Blackjack {
	
	static Scanner input = new Scanner(System.in);
	
	public static void main(String[] args) {
		
// Introduction: As a user, I want to understand what this program does
		Player dealer = new Player("House");
		System.out.println("Welcome to the table!");
		System.out.println("I see you've already got your drink.");
		System.out.println("Do let Alexa know if you'd like a refill.");
		System.out.println();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Now, shall we play some BlackJack?");
		System.out.print("Type in the Name you'd like to be called & Press enter to play: ");
		String playerName = input.nextLine();
		
		Player p = new Player(playerName);
		
		Deck myDeck = new Deck();
		myDeck.shuffle();
		
		System.out.println();
		System.out.println("Lovely... " + p.getPlayerName() + "... Nice to meet you! (Begins to shuffle cards)");
		System.out.print("Do you need a refresher on the rules? (y/n): ");
		String needsRules = input.nextLine();
		
		if (needsRules.equals("y")) {
			System.out.println();
			System.out.println("Firstly, the goal of Blackjack is to have a hand of cards that totals to 21");
			System.out.println("Or... as close as you can get without going over.");
			System.out.println();
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("Each card has a numeric value:");
			
			int i = 0;
			for (int v : Card.BLACKJACK_VALUES) {
				
				System.out.println(Card.ALL_RANKS[i] + " is worth: " + v);
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				i++;
			}
			
			System.out.println("Now the \"Ace\" is special in the fact that it can also be worth 1.");
			System.out.println("So you will use 1 or 11 as it's value, whichever is more adventageous.");
			System.out.println("Don't worry though! I'll do the calculation for you.");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			System.out.println();
			System.out.println("Each hand, you'll be able to put down a bet and be dealt two cards");
			System.out.println("You'll then choose \"Hit\" to recieve another card or \"Stay\" to stick with what you got.");
			System.out.println("If you hand values more than 21, thats a \"Bust\" and you lose...");
			System.out.println("But if you win you'll get your bet back twofold!");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println();
			System.out.println("Lastly, you are playing against me, the \"House,\"");
			System.out.println("I start with two cards as well, but I get to keep one facedown... Sneaky, right?");
			System.out.println("This makes it difficult for you to know what number to beat.");
			System.out.println("However \"House\" rules state that I must stay at 17");
			System.out.println();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("Oh one more thing! House wins ties...");
			System.out.println("So if we both hit 20, your bet is going to paying the electricty bill around here!");
			System.out.println("Unless you win it back of course.");
			System.out.println();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("But remember... It's not my money!");
			System.out.println("So I'm rooting for you, " + p.getPlayerName() + "!");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println();
			System.out.print("So are you ready to play? (y/n): ");
			String begin = input.nextLine();
		}
// End Introduction
			
// Game: As a user I want to place bets, be dealt cards, know what they are, 
//       know my next options are, and know the outcome of the hand.
			
		int tableMinimum = 5;
		System.out.println();
		System.out.println("Looks like you already have some chips.");
		System.out.println("$" + p.getPlayerMoney() + " by my count...");
		System.out.println("That'll work just fine. The table minimum is just $" + tableMinimum);
		
		boolean donePlaying = false;
		
		while (!donePlaying) {
			
			System.out.println();
			System.out.println("NEW HAND:");
			System.out.println("How much would you like to bet?");
			System.out.println("($" + tableMinimum + " is the minimum)");
			System.out.println("Bets must be whole dollars (No cents).");
			System.out.print("Your Bet: ");
			int bet = Integer.parseInt(input.nextLine());
			int winnings = bet * 2;
			
			if (p.placeBet(bet)) {
				
				System.out.println();
				System.out.println("$" + bet + " down, lets deal 'em!");
				System.out.println();
				
				boolean won = false;
				boolean liveHand = true;
				
				Card draw1 = myDeck.draw(); 
				Card draw2 = myDeck.draw();
				draw1.flipCard();
				draw2.flipCard();
				p.addToHand(draw1);
				p.addToHand(draw2);
				System.out.println(draw1);
				System.out.println(draw2);
				
				System.out.println("Your count is: " + p.countHand());
				
				if (p.countHand() == 21) {
					System.out.println("Winner Winner Chicken Dinner!");
					won = true;
					liveHand = false;
				}
				while (liveHand) {
				System.out.println();
				Card draw3 = myDeck.draw();
				Card draw4 = myDeck.draw();
				draw3.flipCard();
				dealer.addToHand(draw3);
				dealer.addToHand(draw4);
								
				System.out.println("House is showing a " + draw3 + " & one card " + draw4);
				System.out.println();
				
				
				boolean hitMe = true;
								
				while (hitMe) {
					System.out.print("So " + p.getPlayerName() + "... Hit? Would you like another card added to your count? (y/n): ");
					String hitPlease = input.nextLine();
					if (hitPlease.equals("n")) {
						hitMe = false;
						System.out.println("Staying at " + p.countHand()  + "!");
					} else {
						Card drawX = myDeck.draw();
						drawX.flipCard();
						p.addToHand(drawX);
						System.out.println();
						System.out.println(drawX);
						System.out.println("Your count is: " + p.countHand());
						System.out.println();
						
						if (drawX.isAce() && p.countHand() > 21) {
							Card.lowAce(drawX);
							System.out.println("Lets make that Ace low...");
							System.out.println("Your count is: " + p.countHand());
						}
						
						if (draw1.isAce() && p.countHand() > 21) {
							Card.lowAce(draw1);
							System.out.println("Lets make that Ace low...");
							System.out.println("Your count is: " + p.countHand());
						}
						
						if (draw2.isAce() && p.countHand() > 21) {
							Card.lowAce(draw2);
							System.out.println("Lets make that Ace low...");
							System.out.println("Your count is: " + p.countHand());
						}
						
						if (p.countHand() > 21) {
							System.out.println("Bust! Sorry to see it " + p.getPlayerName() + "...");
							hitMe = false;
							liveHand = false;
						}
					}
				}
				
				if (!won && liveHand) {	
					
					System.out.println("My turn...");
					draw4.flipCard();
					System.out.println("House was dealt a " + draw3 + " & a " + draw4);
					System.out.println("House count is: " + dealer.countHand());
					
					if (dealer.countHand() < 17) {
						hitMe = true;
					} else {
						System.out.println("House Stays!");
						liveHand = false;
					}
					
					while (hitMe) {	
						Card drawX = myDeck.draw();
						drawX.flipCard();
						dealer.addToHand(drawX);
						System.out.println();
						System.out.println(drawX);
						System.out.println("House count is: " + dealer.countHand());
						System.out.println();
					
						if (drawX.isAce() && dealer.countHand() > 21) {
							Card.lowAce(drawX);
							System.out.println("Lets make that Ace low...");
							System.out.println("House count is: " + dealer.countHand());
						}
						
						if (draw3.isAce() && dealer.countHand() > 21) {
							Card.lowAce(draw3);
							System.out.println("Lets make that Ace low...");
							System.out.println("House count is: " + dealer.countHand());
						}
						
						if (draw4.isAce() && dealer.countHand() > 21) {
							Card.lowAce(draw4);
							System.out.println("Lets make that Ace low...");
							System.out.println("House count is: " + dealer.countHand());
						}
						
						if (dealer.countHand() > 21) {
							System.out.println("House Bust!");
							hitMe = false;
							won = true;
							liveHand = false;
						} else if(dealer.countHand() >= 17) {
							System.out.println("House Stays!");
							hitMe = false;
							liveHand = false;
						} 
						
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						
					}
				} 
				
				if (p.countHand() > dealer.countHand() && p.countHand() <= 21) {
					won = true;
				}
				}
				if (won) {
					System.out.println("!You Win!");
					System.out.println("$" + bet + " bet wins you $" + winnings);
					p.wonHand(winnings);
					System.out.println(p);
				} else {
					System.out.println("You Lose");
					System.out.println("$" + bet + " bet lost...");
					System.out.println(p);
				}
				
				p.discardHand();
				dealer.discardHand();
					
			} else {
				System.out.println("Sorry you don't have enought money to bet $" + bet +".");
				System.out.println(p);
			}
			
		}
		
	}
}
