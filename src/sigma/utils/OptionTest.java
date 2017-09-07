/**
 * 
 */
package sigma.utils;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Unit testing for option Greeks calculation
 * 
 * @author Peeter Meos
 * @version 1.0
 *
 */
public class OptionTest {

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
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		Option tester = new Option(50, 10, 0, OptSide.CALL);
		
		assertEquals("Option value must be 0.", 0.0, tester.value(), 0.0);
		assertEquals("Option delta must be 0.", 0.0, tester.delta(), 0.0);
	}

}
