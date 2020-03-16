package Awakennings.InventoryApp;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class Report {

	private String name;
	private String type;
	private Map<Item, Integer> itemsMap = new HashMap<Item, Integer>();
	private File reportFile;

// Constructor

	public Report(String name, String type) {
		this.name = name;
		this.type = type;
		reportFile = new File(name + "-" + type + ".txt");
	}

// Getter 
	public String getName() {
		return name;
	}

	public Map<Item, Integer> getItemsMap() {
		return itemsMap;
	}

// Class Methods

	public void addItem(Item i, Integer quantity) throws IOException {
		itemsMap.put(i, quantity);
	}

	public void logReportFile() throws IOException {
		if (reportFile.createNewFile()) {
			try (PrintWriter orderWriter = new PrintWriter(reportFile)) {

				orderWriter.println("**********" + type + " : " + name + "**********");
				orderWriter.println();
				
				if (type.equals("Inventory")) {
					orderWriter.format("%-20s   %s\n", "Item Name", "Inventory Qty.");
					orderWriter.println("-------------------------------------");
					for (Item i : itemsMap.keySet()) {
					orderWriter.format("%-20s : %d\n", i.getName(), itemsMap.get(i));
					}
				} else {
					orderWriter.format("%-20s   %-20s   %s\n", "Item Name", "Order Qty.", "Cost");
					orderWriter.println("------------------------------------------------------");
					double totalCost = 0.0;
					for (Item i : itemsMap.keySet()) {
						double cost = i.getPricePerItem() * itemsMap.get(i);
						orderWriter.format("%-20s : %-20d : %s\n", i, itemsMap.get(i), "$" + cost);
						totalCost += cost;					
					}
					orderWriter.println();
					orderWriter.format("Total Cost of Order: $%.02f\n", totalCost);
				}

				
			}
		} else {
			throw new IOException();
		}
	}
}
