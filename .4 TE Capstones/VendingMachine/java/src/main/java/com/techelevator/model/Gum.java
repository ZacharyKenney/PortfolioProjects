package com.techelevator.model;

import java.math.BigDecimal;

public class Gum implements Item {

	private String name;
	private BigDecimal price;
	private String machineSlot;
	
// Constructor
	public Gum(String name, BigDecimal price, String machineSlot) {
		this.name = name;
		this.price = price;
		this.machineSlot = machineSlot;
	}
	
// Getters
	@Override
	public String getName() {
		return name;
	}
	@Override
	public BigDecimal getPrice() {
		return price;
	}
	@Override
	public String getInteraction() {
		return "Chew Chew, Yum!";
	}
	@Override
	public String getMachineSlot() {
		return machineSlot;
	}
	
}
