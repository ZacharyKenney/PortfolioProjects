package Awakennings.InventoryApp;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ReportTest {

	private Report testReport;
	
	@Before 
	public void setup() {
		testReport = new Report("Test" , "Report");
	}
	
	@Test
	public void test_logReport() throws IOException {
		testReport.logReportFile();
		File testFile = new File("Test-Report.txt");
		Assert.assertTrue(testFile.exists());
	}
	
}
