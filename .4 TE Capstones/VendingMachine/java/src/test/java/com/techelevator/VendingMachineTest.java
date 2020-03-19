package com.techelevator;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.techelevator.model.Gum;
import com.techelevator.model.Item;
import com.techelevator.model.VendingMachine;

public class VendingMachineTest {

	private VendingMachine testVendingMachine;
	private File filePath = new File("VendingMachine.txt");
	private File filePath2 = new File("TestMachine.txt");
	private File filePath3 = new File("corrupt.txt");

	@Before
	public void setup() throws IOException {
		testVendingMachine = new VendingMachine(filePath);
	}

	@Test
	public void test_giveChange_and_balanceAdd() {
		try {
			testVendingMachine.balanceAdd(new BigDecimal("10.95"));
			String change = testVendingMachine.giveChange();
			Assert.assertEquals("$10, 3 Quarters, 2 Dimes, 0 Nickels", change);
		} catch (IOException e) {
			System.out.println("Could not write to log");
		}

	}
	
	@Test
	public void test_getBalance() {
		try {
			BigDecimal testBalance = new BigDecimal("1");
			testVendingMachine.balanceAdd(testBalance);
			Assert.assertEquals(0, testVendingMachine.getBalance().compareTo(testBalance));
		} catch (IOException e) {
			System.out.println("Could not write to log");
		}
	}
	
	@Test 
	public void test_getSalesMap_and_getInventoryMap_and_getTotalSales() {
		Map<String, Integer> testMap = testVendingMachine.getSalesMap();
		try (Scanner firstLine = new Scanner(filePath)) {
			String line = firstLine.nextLine();
			String[] testLine = line.split("\\|");
			String name = testLine[1];
			String slot = testLine[0];
			BigDecimal price = new BigDecimal(testLine[2]);
			Assert.assertEquals(0, (int) testMap.get(name));
		Map<String, List<Item>> testMap2 = testVendingMachine.getInventoryMap();
			Item testItem = testMap2.get(testLine[0]).get(0);
			testVendingMachine.balanceAdd(price);
			testVendingMachine.spendMoney(price, testItem);
			Assert.assertEquals(0, testVendingMachine.getBalance().compareTo(new BigDecimal("0")));
			testVendingMachine.updateSalesMap(testItem);
			Assert.assertEquals(1, (int) testMap.get(name));
			Assert.assertEquals(price, testVendingMachine.getTotalSales());
		} catch (IOException e) {
			//
		}
		
	}
	
	@Test 
	public void test_file_creation() {
		testVendingMachine = new VendingMachine(filePath2);
		Assert.assertEquals(4, testVendingMachine.getInventoryMap().size());
		
	}
	@Test (expected = ArrayIndexOutOfBoundsException.class)
	public void corrupt_file_creation() {
		testVendingMachine = new VendingMachine(filePath3);		
	}
	
	@Test
	public void test_gum_getPrice() {
		Item gumStuffs = new Gum("bubble", new BigDecimal("2.00"), "A1");
		Assert.assertEquals(0, gumStuffs.getPrice().compareTo(new BigDecimal("2.00")));
	}

}
