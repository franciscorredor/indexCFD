/**
 * 
 */
package test.com.wireless.soft.indices.cfd.statistics;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author HP
 *
 */
public class NotaPonderada {
	
	com.wireless.soft.indices.cfd.statistics.NotaPonderada testNotaPonderada = null;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		testNotaPonderada = new com.wireless.soft.indices.cfd.statistics.NotaPonderada();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link com.wireless.soft.indices.cfd.statistics.NotaPonderada#getWinStatistics(java.lang.Double)}.
	 */
	@Test
	public final void testGetWinStatistics() {
		Double ganoCentenas = 0.214285714d;
		assertEquals(ganoCentenas, testNotaPonderada.getWinStatistics(101d));
	}

	/**
	 * Test method for {@link com.wireless.soft.indices.cfd.statistics.NotaPonderada#getLostStatistics(java.lang.Double)}.
	 */
	@Test
	public final void testGetLostStatistics() {
		Double perdioMiles = 0.010989011d;
		assertEquals(perdioMiles, testNotaPonderada.getLostStatistics(1023d));
	}

}
