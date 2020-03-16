package com.InventoryApp.jdbc;

import Awakennings.InventoryApp.Item;

public interface ItemDAO {

	public boolean logItemToSystem(Item newItem);
	public String printItemsInSystem();
	public Item getItemByName(String itemName);
}
