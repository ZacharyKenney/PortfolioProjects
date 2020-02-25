package Awakennings.InventoryApp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import com.InventoryApp.jdbc.ItemDAO;
import com.InventoryApp.jdbc.JDBCItemDAO;

public class Demo {
	
	private static ItemDAO itemDao;
	
	public Demo() {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/inventoryApp_db");
		dataSource.setUsername("postgres");
		dataSource.setPassword("DB_PASSWORD");
		
//		itemDao = new JDBCItemDAO(dataSource);
		
	}
	static Scanner input = new Scanner(System.in);
	
	public static void main(String[] args) {
		
		Demo demo = new Demo();
		
// Introduction: As a user, I want to understand what program I'm looking at
		System.out.println("Welcome to the Awakenings Coffee and Wine Inventory Application!");
		System.out.println("From this command line you may automate the inventory tracking and ordering process...");
		System.out.print("Take a sip of coffee & then press ENTER to begin:");
		String begin = input.nextLine();

		
// Done Condition		
		boolean done = false;
		
			
//Collections/Initializers in use
		List<Item> itemList = new ArrayList<Item>();
		itemList.add(new Item("Coffee", 50, 10.75));
		itemList.add(new Item("Milk", 100, 8.50));
		itemList.add(new Item("Wine", 25, 9.99));
		
		Report mostRecentInv = new Report("", "Inventory");
		
		while (!done) {
//Menu: As a user, I want to understand what my options are
// As a user, I want to be able to select an option
			System.out.println();
			System.out.println("Home Menu:");
			System.out.println();
			System.out.println(" 1 = Add New Items");
			System.out.println(" 2 = Edit Existing Items");
			System.out.println(" 3 = Take Inventory");
			System.out.println(" 4 = Create New Order");
			System.out.println(" 10 = Exit Application");
			System.out.println();
			System.out.print("Select an option by entering the corresponding number: ");
			String selection = input.nextLine();
		
//Adding items: As a user, I want to be able to add items (w/ target quantity after receiving an order)
			
			if (selection.equals("1")) {
				
				boolean done1 = false;
				while (!done1) {
				
					System.out.println();
					System.out.println("Please enter the name of the item you wish to add.");
					System.out.println("--Remember that all item names must be unique--");
					System.out.print("Name: ");
					String itemName = input.nextLine();
					
					System.out.println();
					System.out.println("Please enter the Target Quantity of {" + itemName + "}.");
					System.out.println("--Target Quantity (Quantity) is the amount of that item you wish to have in total, after receiving an order--");
					System.out.println("--Quantity must be a whole number, so create an appropriate unit counting system per item--");
					System.out.print("Quantity: ");
					int itemTargetQty = Integer.parseInt(input.nextLine());
					
					System.out.println();
					System.out.println("Please enter the Price per unit of {" + itemName + "}.");
					System.out.println("--Price per unit (Price) must be a dollar amount with cent as a decimal--");
					System.out.println("-- 0.5 = $0.50 , 1 = $1.00 , 1.75 = $1.75 --");
					System.out.print("Price: ");
					double itemPricePerItem = Double.parseDouble(input.nextLine());
					
					Item currentItem = new Item(itemName, itemTargetQty, itemPricePerItem);
					
					itemDao.logItemToSystem(currentItem);
					
					itemList.add(currentItem);
					
					System.out.println();
					System.out.println("The Item has been added to your system,");
					System.out.println("Here are your current Items:");
					for (Item i : itemList) {
						System.out.format("%15s : %15s\n", i, i.getDetails());
					}
					System.out.println();
					System.out.print("Would you like to add another item? (y/n): ");
					String continueAdding = input.nextLine();
					
					if (continueAdding.equals("n")) {
						done1 = true;
					}
					
				}
				System.out.println();
				
				for (Item i : itemList) {
					System.out.format("%15s : %15s\n", i, i.getDetails());
				}
				System.out.print("Would you like to edit any items? (y/n): ");
				String moveToEdit = input.nextLine();
				
				if (moveToEdit.equals("y")) {
					selection = "2";
				} else {
					System.out.println();
					System.out.print("Would you like to Take Inventory for your current items? (y/n): ");
					String moveToTakeInv = input.nextLine();
					
					if (moveToTakeInv.equals("y")) {
						selection = "3";
					} else {
						
						System.out.println();
						System.out.print("Would you like to exit the application? (y/n): ");
						String exitProgram = input.nextLine();
						if (exitProgram.equals("y")) {
							selection = "10";
						}
					}
				}
			}
//Add Items = Done
				
//Editing Items: As a user, I want to be able to delete items
// As a user, I want to be able to change the name of an item
			if (selection.equals("2")) {
				
				boolean done2 = false;
				
				System.out.println();
				System.out.println("You can edit the Name, Quantity, or Price of an Item.");
				System.out.println("Here are your current Items");
				
				while (!done2 ) {	
					
					for (Item i : itemList) {
						System.out.format("%15s : %15s\n", i, i.getDetails());
					}
					System.out.println();
					System.out.println("Please enter the Name of the item you wish to edit.");
					System.out.println("If you do not wish to edit any items, Please enter \"NA\".");
					System.out.print("Name: ");
					String nameToEdit = input.nextLine();
					
					Item itemToEdit = itemDao.getItemByName(nameToEdit);	
					
					if (nameToEdit.equals("NA")) {
						done2 = true;
					}
					
					if (itemToEdit != null) {
						System.out.println();
						System.out.print("Would you like to change the name of {" + nameToEdit + "}? (y/n): ");
						String editName = input.nextLine();
						if (editName.equals("y")) {
							System.out.print("What would you like to change the name to: ");
							String newName = input.nextLine();
							
							itemToEdit.setName(newName);;
							
							System.out.println("The Item Name has been editted to {" + newName + "}.");
							
						} else {
							System.out.print("Would you like to edit the Quantity of {" + nameToEdit + "}? (y/n): ");
							String editTargetQty = input.nextLine();
							if (editTargetQty.equals("y")) {
								System.out.println();
								System.out.println("--Target Quantity (Quantity) is the amount of that item you wish to have in total, after receiving an order--");
								System.out.println("--Quantity must be a whole number, so create an appropriate counting system per item--");
								System.out.print("What would you like to change the Quantity to: ");
								int newTargetQty = Integer.parseInt(input.nextLine());
								
								itemToEdit.setTargetQty(newTargetQty);
								System.out.println("The Item Quantity has been editted to [" + newTargetQty + "].");
							} else {
								
								System.out.print("Would you like to edit the Price of {" + nameToEdit + "}? (y/n): ");
								String editPricePerUnit = input.nextLine();
								if (editPricePerUnit.equals("y")) {
									System.out.println();
									System.out.println("--Price must be a dollar amount with cent as a decimal--");
									System.out.println("-- .5 = $0.50 , 1 = $1.00 , 1.75 = $1.75 --");
									System.out.print("What would you like to change the Price to: ");
									double newPricePerItem = Double.parseDouble(input.nextLine());
									
									itemToEdit.setPricePerItem(newPricePerItem);
									System.out.println("The Item Price has been editted to [" + newPricePerItem + "].");
								
								}	
							
							}
						}
						
						System.out.println();
						System.out.print("Would you like to edit another item? (y/n): ");	
						String doneEditing = input.nextLine();
						if (doneEditing.equals("n")) {
							done2 = true;
						}
						
						
					} else {
							System.out.println("Sorry, that Name does not match any current Items.");
							System.out.println("Please try again.");
					}
				}
				
				
				
					System.out.print("Would you like to exit the application? (y/n): ");
					String exitProgram = input.nextLine();
					if (exitProgram.equals("y")) {
						selection = "10";
					}
			}
				
		
//Taking Inventory: As a user, I want to be able to complete a new inventory report
			if (selection.equals("3")) {
				
				if (itemList.size() == 0) {
					System.out.println("It doesn't appear you have an Items added to your system.");
					System.out.println("Please add items, before taking Inventory.");
				} else {
				
				System.out.println();
				System.out.print("Would you like to create a new inventory report? (y/n): ");
				String runInventory = input.nextLine();
				if (runInventory.equals("y")) {
					System.out.print("What is today's date?: ");
					String date = input.nextLine().replace("/","-");
					Report newInventory = new Report(date, "Inventory");
					
					for (Item i : itemList) {
						System.out.println();
						System.out.print("What is the current inventory quanity of " + i + "?: " );
						int iQuantity = Integer.parseInt(input.nextLine());
						try { 
							newInventory.addItem(i, iQuantity);
						} catch (IOException e) {
							//Something here
						}
					}
					System.out.println();
					System.out.println("New Inventory Report... " + newInventory.getName());
					System.out.format("%15s   %15s\n", "Item Name", "Inventory Qty.");
					System.out.println("------------------------------------------");
					for (Item i : newInventory.getItemsMap().keySet()) {
						System.out.format("%15s : %15d\n", i, newInventory.getItemsMap().get(i));
					}
					
					mostRecentInv = newInventory;
					try {
						newInventory.logReportFile();
					} catch (IOException e) {
						System.out.println(e.getMessage());
					}
				}
				System.out.println();
				System.out.print("Would you like to create an Order for this new Inventory Report? (y/n): ");
				String goToOrder = input.nextLine();
				if (goToOrder.equals("y")) {
					selection = "4";
				} else {
					System.out.print("Would you like to exit the application? (y/n): ");
					String exitProgram = input.nextLine();
					if (exitProgram.equals("y")) {
						selection = "10";
					}
				}
				
			}
			
//Creating an Order: As a user, I want to be able to complete an order for my needed items
			if (selection.equals("4")) {
				
				if (mostRecentInv.getName().equals("")) {
					System.out.println("It doesn't appear you have an Inventory Report to create an Order for.");
					System.out.println("Please create an Inventory Report first.");
					
					selection = "3";
				} else {
				
					Report newOrderReport = new Report(mostRecentInv.getName(), "Order");
					
					for (Item i : mostRecentInv.getItemsMap().keySet()) {
						
						int quantity = i.getTargetQty() - mostRecentInv.getItemsMap().get(i);
						if (quantity > 0) {
							try {
								newOrderReport.addItem(i, quantity);
							} catch (IOException e) {
								//Something here
							}
						}
					}
					
					System.out.println();
					System.out.print("Here is the Order for you most recent Inventory Report");
					System.out.println();
					System.out.println(newOrderReport.getName());
					System.out.format("%15s   %15s   %15s\n", "Item Name", "Order Qty.", "Cost");
					System.out.println("-------------------------------------------------------");
					
					double totalCost = 0.0;
					
					for (Item i : newOrderReport.getItemsMap().keySet()) {
						
						double cost = i.getPricePerItem() * newOrderReport.getItemsMap().get(i);
						System.out.format("%15s : %15d : %15s\n", i, newOrderReport.getItemsMap().get(i), "$" + cost);
						totalCost += cost;
					}
					System.out.println();
					System.out.format("Total Cost of Order: $%.02f\n", totalCost);
					
					try {
						newOrderReport.logReportFile();
					} catch (IOException e) {
						System.out.println("Didn't Work!");
					}
					
					System.out.println();
					System.out.print("Would you like to exit the application? (y/n): ");
					String exitProgram = input.nextLine();
					if (exitProgram.equals("y")) {
						selection = "10";
					}
				}
			}
			
// Selection Error	
			
			if (((!selection.equals("1") && !selection.equals("2")) && (!selection.equals("3") && !selection.equals("4"))) && !selection.equals("10")) {
				System.out.println();
				System.out.println("Thats doesn't seem to match any of the menu options.");
				System.out.println("Please try entering your selection again.");
			}
// Exit	
			if (selection.equals("10")) {
				System.out.println("Thank you and goodbye!");
				done = true;
			}
			
			}
		}
	}
}

