package suncertify.db;

import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;

public class SampleTestNG {

	@Test
	public void testAddRealEntry() {
		String str = "TestNG is working fine";
		assertEquals("TestNG is working fine", str);
	}
	
	@Test
	public void testAddMaxSizeEntry() {
		String str = "TestNG is working fine";
		assertEquals("TestNG is working fine", str);
	}
	
}
