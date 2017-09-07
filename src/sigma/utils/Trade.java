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
	int q;
	
	double price;
	Date dtg;
	
	String side;
	
	/**
	 * Simple constructor for trade that assigns entry
	 * @param sym
	 * @param q 
	 * @param entry
	 * @param side
	 * @param dtg
	 */
	public Trade(String sym, int q, double entry, String side, Date dtg) {
		this.symbol = sym;
		this.q = q;
		this.price = entry;
		this.dtg = dtg;
		this.side = side;
	}
}
