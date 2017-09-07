/**
 * 
 */
package sigma.utils;

import java.util.Date;

/**
 * Class to contain trade data
 * 
 * @author Peeter Meos
 * @version 0.1
 */
public class Trade {
	String symbol;
	double q;
	
	double entryPrice;
	Date entryDtg;
	
	double exitPrice;
	Date exitDtg;
	
	/**
	 * Simple constructor for trade that assigns entry
	 * @param sym
	 * @param q 
	 * @param entry
	 * @param dtg
	 */
	public Trade(String sym, double q, double entry, Date dtg) {
		this.symbol = sym;
		this.q = q;
		entryPrice = entry;
		entryDtg = dtg;
		exitPrice = -1;
		exitDtg = null;
	}
}
