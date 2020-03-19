package com.techelevator.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class VendingMachine {

//Properties of a Vending Machine
	private Map<String, List<Item>> inventoryMap = new LinkedHashMap<String, List<Item>>();
	private Map<String, Integer> salesMap = new LinkedHashMap<String, Integer>();
	private BigDecimal balance = new BigDecimal("0.0");
	private BigDecimal totalSales = new BigDecimal("0.0");
// Constants 
	private static final BigDecimal DOLLAR = new BigDecimal("1");
	private static final BigDecimal QUARTER = new BigDecimal("0.25");
	private static final BigDecimal DIME = new BigDecimal("0.10");
	private static final BigDecimal NICKEL = new BigDecimal("0.05");	
	private static final BigDecimal[] CHANGE_OPTIONS = { DOLLAR, QUARTER, DIME, NICKEL };
// Options
	private File log = new File("log.txt");
	public static final BigDecimal MONEY_IN_MAX = new BigDecimal("1000");
	public static final BigDecimal BALANCE_MAX = new BigDecimal("2000");
	public static final int ITEM_QUANTITY_PER_SLOT = 5;
//Date Time Format for Log
	public static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss a");
//Date Time Format for Sales Report
	public static final DateTimeFormatter SALES_REPORT_DTF = DateTimeFormatter.ofPattern("MM_dd_yyyy - HH_mm_ss_a");

//Constructor
	public VendingMachine(File inventory) throws ArrayIndexOutOfBoundsException {
		try (Scanner inventoryScanner = new Scanner(inventory)) {
			while (inventoryScanner.hasNextLine()) {
				String line = inventoryScanner.nextLine().trim();
				if (!line.equals("")) {
					String[] contents = line.split("\\|");
					List<Item> slotList = new ArrayList<>();
					Item currentItem;
					BigDecimal price = new BigDecimal(contents[2].trim());
					boolean validPrice = price.compareTo(new BigDecimal("0")) >= 0;
					String name = contents[1].trim();
					String slot = contents[0].trim();
					String type = contents[3].trim();
					if (validPrice) {
						for (int i = 0; i < ITEM_QUANTITY_PER_SLOT; i++) {
							// 5 is a magic number, can easily be changed to a variable.
							if (type.equals("Chip")) {
								currentItem = new Chip(name, price, slot);
								slotList.add(currentItem);
							} else if (type.equals("Candy")) {
								currentItem = new Candy(name, price, slot);
								slotList.add(currentItem);
							} else if (type.equals("Drink")) {
								currentItem = new Drink(name, price, slot);
								slotList.add(currentItem);
							} else if (type.equals("Gum")) {
								currentItem = new Gum(name, price, slot);
								slotList.add(currentItem);
							}
						}
						if (slotList.size() > 0) {
							salesMap.put(name, 0);
							inventoryMap.put(slot, slotList);	
						}
					}
				}
			}
		} catch (IOException e) {
			// Eat
		}
	}

// Getters
	public BigDecimal getBalance() {
		return balance;
	}

	public Map<String, Integer> getSalesMap() {
		return salesMap;
	}

	public Map<String, List<Item>> getInventoryMap() {
		return inventoryMap;
	}

	public BigDecimal getTotalSales() {
		return totalSales;
	}
	
// Class Methods

	public void balanceAdd(BigDecimal moneyIn) throws IOException {
		balance = balance.add(moneyIn);
		writeToLog("FEED MONEY", moneyIn, balance);
	}

	public void spendMoney(BigDecimal moneyOut, Item purchaseItem) throws IOException {
		balance = balance.subtract(moneyOut);
		String message = purchaseItem.getName() + " " + purchaseItem.getMachineSlot();
		writeToLog(message, moneyOut, balance);
	}

	public String giveChange() throws IOException {
		int[] changeArr = new int[4];
		BigDecimal balanceBefore = balance;
		for (int i = 0; i < CHANGE_OPTIONS.length; i++) {
			while (balance.compareTo(CHANGE_OPTIONS[i]) >= 0) {
				changeArr[i]++;
				balance = balance.subtract(CHANGE_OPTIONS[i]);
			}
		}
		writeToLog("GIVE CHANGE", balanceBefore, balance);
		return "$" + changeArr[0] + ", " + changeArr[1] + " Quarters, " + changeArr[2] + " Dimes, " + changeArr[3]
				+ " Nickels";
	}

	public void writeToLog(String message, BigDecimal logAmount, BigDecimal balance) throws IOException {
		log.createNewFile();
		try (PrintWriter logWriter = new PrintWriter(new FileOutputStream(log, true))) {
			logWriter.println(DTF.format(LocalDateTime.now()) + " " + message + " $" + logAmount + " $" + balance);
		}
	}

	public void updateSalesMap(Item purchaseItem) {
		String name = purchaseItem.getName();
		salesMap.put(name, salesMap.get(name) + 1);
		totalSales = totalSales.add(purchaseItem.getPrice());
	}
	
	public void writeToSalesReportFile(String name) throws IOException {
		File salesReport = new File(name);
		salesReport.createNewFile();
		try (PrintWriter salesReportWriter = new PrintWriter(salesReport)) {
			salesReportWriter.println(name);
			salesReportWriter.println();
			salesReportWriter.format("%-20s   %s\n", "Item Name", "Quantity Sold");
			salesReportWriter.println("*************************************");
			for (String salesItem : getSalesMap().keySet()) {
				salesReportWriter.format("%-20s | %7s\n", salesItem, getSalesMap().get(salesItem));
			}
			salesReportWriter.println();
			salesReportWriter.println("**TOTAL SALES** $" + getTotalSales());
			salesReportWriter.println();

		}
	}
	
// CLI Methods
	
	public String displayItemsToCLI() {
		Map<String, List<Item>> ourInventory = getInventoryMap();
		String result = String.format("%-10s %-20s $%s\n", "Slot", "Item Name", "Price");
		result += "********************************************\n";
		for (String s : ourInventory.keySet()) {
			String slot = s;
			if (ourInventory.get(s).size() > 0) {
				Item item = ourInventory.get(s).get(0);
				String name = item.getName();
				String price = String.valueOf(item.getPrice());
				result += String.format("%-10s %-20s $%s\n", slot + " :", name, price);
			} else {
				result += String.format("%-10s %s\n", slot + " :", "SOLD OUT");
			}
		}
		return result;
	}
	
	public String displayFeedMoneyToCLI(String moneyInString) {
		BigDecimal moneyIn = new BigDecimal("0.00");
		String result = "";
		boolean numberFormat = false;
		
		while (numberFormat == false) {
			
			try {
				moneyIn = new BigDecimal(moneyInString);
				if (moneyIn.compareTo(new BigDecimal("0")) >= 0 && moneyIn.remainder(new BigDecimal("1")).compareTo(new BigDecimal("0")) == 0) {
					numberFormat = true;
				} else {
					result += "No Change! OR Negative Money!\n";
					return result;
				}
			} catch (NumberFormatException e) {
				result += "Please enter a whole dollar amount!\n";
				return result;
			}
		}
		
		if (moneyIn.compareTo(new BigDecimal("100")) >= 0) {
			result += "BIG spender over here!\n";
		}

		if (moneyIn.compareTo(VendingMachine.MONEY_IN_MAX) <= 0 && getBalance().add(moneyIn).compareTo(VendingMachine.BALANCE_MAX) <= 0) {
			try {
				balanceAdd(moneyIn);
			} catch (IOException e) {
				result += "Could not write to log.\n";
			}
			result += String.format("Balance>>> $%.02f\n", getBalance());
		} else if (moneyIn.compareTo(VendingMachine.MONEY_IN_MAX) > 0) {
			result += "Sorry Bill Gates, machine does not accept bill higher than $1,000.00\n";
			result += String.format("Balance>>> $%.02f\n", getBalance());
		} else {
			System.out.println("Machine total balance may not exceed $2000.00\n");
			result += String.format("Balance>>> $%.02f\n", getBalance());
		}
		return result;
	}

	public String displaySelectProductToCLI(String purchaseSlot) {
		Map<String, List<Item>> inventory = getInventoryMap();
		String result = "";
		
		if (inventory.containsKey(purchaseSlot)) {
			if (inventory.get(purchaseSlot).size() > 0) {
				Item purchaseItem = inventory.get(purchaseSlot).get(0);
				BigDecimal price = purchaseItem.getPrice();
				if (getBalance().compareTo(price) >= 0) {
					inventory.get(purchaseSlot).remove(0);
					updateSalesMap(purchaseItem);
					try {
						spendMoney(price, purchaseItem);
					} catch (IOException e) {
						result += "Could not write to log.\n";
					}
					result += purchaseItem.getInteraction()+"\n";
					result += String.format("Balance>>> $%.02f\n", getBalance());
				} else {
					result += "Insufficient funds for purchase! PLEASE FEED ME MORE MONEY!\n";
				}
			} else {
				result += "SOLD OUT! Please select another item.\n";
			}
		} else {
			result += "Please Enter A Valid Slot!\n";
		}
		return result; 
	}
	
	public String generateSalesReportforCLI() {
		String result = "";
		result += String.format("%-20s   %s\n", "Item Name", "Quantity Sold");
		result += "*************************************\n";
		for (String salesItem : getSalesMap().keySet()) {
			result += String.format("%-20s | %7s\n", salesItem, getSalesMap().get(salesItem));
		}
		result += "\n";
		result += "**TOTAL SALES** $" + getTotalSales() + "\n";
		result += "\n";
		String salesReportName = "Sales_Report_" + SALES_REPORT_DTF.format(LocalDateTime.now()) + ".txt";
		result += "Sales Report saved as " + salesReportName + "\n";

		try {
			writeToSalesReportFile(salesReportName);
		} catch (IOException e) {
			result += "File could not be saved.\n";
		}
		return result;
	}
}
