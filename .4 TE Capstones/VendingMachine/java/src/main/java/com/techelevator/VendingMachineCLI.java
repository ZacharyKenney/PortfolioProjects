package com.techelevator;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import com.techelevator.model.VendingMachine;
import com.techelevator.view.Menu;

public class VendingMachineCLI {

// Main Menu
	private static final String MAIN_MENU_OPTION_DISPLAY_ITEMS = "Display Vending Machine Items";
	private static final String MAIN_MENU_OPTION_PURCHASE = "Purchase";
	private static final String MAIN_MENU_OPTION_SALES_REPORT = "";
	private static final String MAIN_MENU_OPTION_EXIT = "Exit Program";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_DISPLAY_ITEMS, MAIN_MENU_OPTION_PURCHASE,
			MAIN_MENU_OPTION_EXIT, MAIN_MENU_OPTION_SALES_REPORT };
//Purchase Menu
	private static final String PURCHASE_MENU_OPTION_FEED_MONEY = "Feed Money";
	private static final String PURCHASE_MENU_OPTION_SELECT_PRODUCT = "Select Product";
	private static final String PURCHASE_MENU_OPTION_FINISH_TRANSACTION = "Finish Transaction";
	private static final String[] PURCHASE_MENU_OPTIONS = { PURCHASE_MENU_OPTION_FEED_MONEY,
			PURCHASE_MENU_OPTION_SELECT_PRODUCT, PURCHASE_MENU_OPTION_FINISH_TRANSACTION };
// Inventory File for Vending Machine Creation
	private static final File INVENTORY_FILE = new File("VendingMachine.txt");
//Vending Machine Creation
	private static VendingMachine ourMachine;
	
	static {
		try {
			ourMachine = new VendingMachine(INVENTORY_FILE);
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("********File Corrupt*******");
		}
	}

	private Menu menu;
	private static Scanner input = new Scanner(System.in);

	public VendingMachineCLI(Menu menu) {
		this.menu = menu;
	}

	public void run() {
		boolean done = false;
		while (done == false) {
			String choice = (String) menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			if (choice.equals(MAIN_MENU_OPTION_DISPLAY_ITEMS)) {
				displayOptions();
			} else if (choice.equals(MAIN_MENU_OPTION_PURCHASE)) {
				displayPurchaseMenu();
			} else if (choice.equals(MAIN_MENU_OPTION_EXIT)) {
				System.out.println("Thank you! Please come again soon!");
				done = true;
			} else if (choice.equals(MAIN_MENU_OPTION_SALES_REPORT)) {
				String result = ourMachine.generateSalesReportforCLI();
				System.out.print(result);
			}
		}
	}

	public static void main(String[] args) {
		Menu menu = new Menu(System.in, System.out);
		VendingMachineCLI cli = new VendingMachineCLI(menu);
		cli.run();
	}

	public void displayOptions() {
		String options = ourMachine.displayItemsToCLI();
		System.out.println(options);
	}

	public void displayPurchaseMenu() {
		boolean done = false;
		while (done == false) {
			String purchaseChoice = (String) menu.getChoiceFromOptions(PURCHASE_MENU_OPTIONS);
			if (purchaseChoice.equals(PURCHASE_MENU_OPTION_FEED_MONEY)) {
				feedMoney();
			} else if (purchaseChoice.equals(PURCHASE_MENU_OPTION_SELECT_PRODUCT)) {
				selectProduct();
			} else if (purchaseChoice.equals(PURCHASE_MENU_OPTION_FINISH_TRANSACTION)) {
				finishTransaction();
				done = true;
			}
		}
	}

	public void feedMoney() {
		
		System.out.print("Please deposit money in whole dollars >>> ");
		String moneyInString = input.nextLine();
		String result = ourMachine.displayFeedMoneyToCLI(moneyInString);
		System.out.print(result);

	}

	public void selectProduct() {
		displayOptions();
		System.out.println();
		System.out.print("Please select a Slot >>> ");
		String purchaseSlot = input.nextLine().toUpperCase();
		String result = ourMachine.displaySelectProductToCLI(purchaseSlot);
		System.out.print(result);
	}

	public void finishTransaction() {
		try {
			String change = ourMachine.giveChange();
			System.out.println();
			System.out.println("Your Change is:");
			System.out.println(change);
			System.out.format("Balance>>> $%.02f\n", ourMachine.getBalance());
		} catch (IOException e) {
			System.out.println("Could not write to log.");
		}

	}
}
