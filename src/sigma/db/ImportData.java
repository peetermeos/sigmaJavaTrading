/**
 * 
 */
package sigma.db;

import sigma.trading.TwsConnector;

/**
 * Loads historic data from TWS into database
 * 
 * @author Peeter Meos
 * @version 0.1
 */
public class ImportData extends TwsConnector {

	/** 
	 * Main entry point 
	 * 
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		ImportData t;
		
		t = new ImportData();
		t.twsConnect();
		
		// Create instrument
		// Open database
		// Request data 
		// Save results to database
		// Loop until finished
		// Close database
		
		t.twsDisconnect();
	}
}
