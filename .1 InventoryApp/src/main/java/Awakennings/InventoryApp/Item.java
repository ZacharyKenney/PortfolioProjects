package Awakennings.InventoryApp;

import java.math.BigDecimal;

public class Item {

	private String name;
	private int targetQty;
	private int inventoryQty = 0;
	private double pricePerItem;
	
// Constructors
	
	public Item(String name, int targetQty, double pricePerItem) {
		this.setName(name);
		this.setTargetQty(targetQty);
		this.setPricePerItem(pricePerItem);
	}

// Getters & Setters
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getTargetQty() {
		return targetQty;
	}
	
	public void setTargetQty(int targetQty) {
		this.targetQty = targetQty;
	}
	
	public int getInventoryQty() {
		return inventoryQty;
	}
	
	public void setInventoryQty(int inventoryQty) {
		this.inventoryQty = inventoryQty;
	}
	
	public double getPricePerItem() {
		return pricePerItem;
	}
	
	public void setPricePerItem(double price) {
		this.pricePerItem = price;
	}
	
// Class Methods
	
	public String getDetails() {
		return "Quantity. [" +getTargetQty()+ "], Price $" +getPricePerItem();
	}
	
	@Override
	public String toString() {
		return "{" +getName()+ "}";
	}
	
}
