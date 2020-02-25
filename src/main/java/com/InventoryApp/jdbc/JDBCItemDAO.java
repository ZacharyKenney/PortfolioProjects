package com.InventoryApp.jdbc;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import Awakennings.InventoryApp.Item;

public class JDBCItemDAO implements ItemDAO {

	@Override
	public boolean logItemToSystem(Item newItem) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String printItemsInSystem() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Item getItemByName(String itemName) {
		// TODO Auto-generated method stub
		return null;
	}

//	private JdbcTemplate jdbcTemplate;
//	
//	public JDBCItemDAO(DataSource dataSource) {
//		this.jdbcTemplate = new JdbcTemplate(dataSource);
//	}
//	
//	@Override
//	public boolean logItemToSystem(Item newItem){
//		
//		String insertItem = "INSERT INTO item (item_name, target_quantity, price_per_item) " +
//							"VALUES (?, ?, ?);";
//		
//		int i = jdbcTemplate.update(insertItem, newItem.getName(), newItem.getTargetQty(), newItem.getPricePerItem());
//		
//		return i > 0;
//	}
//
//	@Override
//	public String printItemsInSystem() {
//		String result = String.format("%15s %15s %s\n", "Item Name", "Target Qty", "Price/Item");
//		
//		SqlRowSet row = jdbcTemplate.queryForRowSet("SELECT item_name, target_quantity, price_per_item FROM item");
//		
//		while (row.next()) {
//			result += String.format("%15s, %15d, %15.02f\n", row.getString("item_name"), 
//									row.getInt("target_quantity"), row.getBigDecimal("price_per_item"));
//		}
//		return result;
//	}
//	
//	@Override
//	public Item getItemByName(String itemName) {
//		String sql = "Select item_name, target_quantity, price_per_item " +
//					 "FROM item WHERE item_name = ?;";
//		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, itemName);
//		Item foundItem = new Item(results.getString("item_name"), results.getInt("target_quantity"), 
//								  results.getBigDecimal("price_per_item"));
//		return foundItem;
//	}
//
//	
	
}
