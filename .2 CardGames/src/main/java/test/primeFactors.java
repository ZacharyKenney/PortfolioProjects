package test;

import java.util.ArrayList;
import java.util.List;

public class primeFactors {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int num = 64;
		int[] result = primeFactorsTest(num);
		for (int i : result) {
			System.out.println(i);
		}
	}
	
	public static int[] primeFactorsTest(int num) {
		
		List<Integer> results = new ArrayList<>();
		
		int n = 2;
		while(num > 1) {
			if (num % n == 0) {
				results.add(n);
				num = num/n;
				n = 2;
			} else {
				n++;
			}
		}
		
		int[] result = new int[results.size()];
		for (int i = 0; i < results.size(); i++) {
			result[i] = results.get(i);
		}
		return result;
	}

}
