/**
 * Sigma news trader.
 * 
 * @author Peeter Meos
 */
package sigma.trading.news;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import sigma.utils.Logger;

public class Trader {
	
	public List<Instrument> instList;
	
	protected Logger logger; 
	protected Connector con;
	
	/**
	 * Simple constructor for news trader.
	 */
	public Trader() {
		logger = new Logger();
		logger.log("Sigma News Trader init");
		
		instList = new ArrayList<>();
		con = new Connector();
	}
	
	/**
	 * Connect trading system
	 */
	public void connect() {
		logger.log("Connecting to TWS");
		con.twsConnect();
	}
	
	/**
	 * The main trading loop
	 */
	public void trade() {
	
		if (!con.isConnected()) {
			logger.error("doTrading(): Not connected to TWS.");
			return;
		}
		
		logger.log("Entering main trading loop");
		try {
			// Infinite loop until keypress
			while (System.in.available() == 0) {
				Thread.sleep(100);
				
				// Get last prices, adjust prices if needed
				for(Instrument item: instList) {
					if (Math.abs(con.getPrice(item.getID()) - item.getLast()) > item.getAdjLimit() ) {
						// Adjust orders
						item.adjustOrders(con);
					}
				}

				// Here check key presses to arm/disarm/quit trader
			}
			
		} catch (InterruptedException | IOException e) {
			logger.error(e);
		}
		logger.log("Main trading loop finished");
	}
	
	/**
	 * Disconnect and shut down trading system
	 */
	public void disconnect() {
		logger.log("Closing TWS connection");
		con.twsDisconnect();
		logger.log("Shutting down news trader");
	}
	
	/**
	 * Logging wrapper
	 * @param str
	 */
	public void log(String str) {
		logger.log(str);
	}

	/**
	 * Main entry point for news trader algorithm
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
        Trader trader;
		
		// New object
		trader = new Trader();
		
		// Connect
		trader.connect();
		
		// Instrument add CL
		trader.log("Adding CL");
		trader.instList.add(new Instrument("CL", "NYMEX", "FUT",  "201710", 1, 0.1, 0.05, 0.02));
		
		// Instrument add E7
		trader.log("Adding EURO");
		trader.instList.add(new Instrument("E7", "GLOBEX", "FUT", "201712", 1, 0.0005, 0.0003, 0.0002));

		// Create and submit orders
		for(Instrument item: trader.instList) {
			item.createOrders(trader.con);
		}
		
		// Trade
		trader.trade();
		
		// Disconnect
		trader.disconnect();
	}

}
