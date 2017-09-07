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

/**
 * Trader class for news trading
 * 
 * @author Peeter Meos
 * @version 0.1
 *
 */
public class Trader {
	
	public List<NewsInstrument> instList;
	
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
		
		double diff = 0;
	
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
				for(NewsInstrument item: instList) {
					diff = con.getPrice(item.getID()) - item.getLast();
					logger.verbose("Diff  for " + item.getSymbol() + " is " + diff);
					if (Math.abs(diff) > item.getAdjLimit() ) {
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
		trader.con.setSimulated(true);
		
		// Instrument add CL
		trader.log("Adding CL");
		trader.instList.add(new NewsInstrument("CL", "FUT", "NYMEX",  "201710", 1, 0.1, 0.15, 0.05));
		
		// Instrument add E7
		trader.log("Adding EURO");
		trader.instList.add(new NewsInstrument("E7", "FUT", "GLOBEX", "201712", 1, 0.0005, 0.0003, 0.0002));

		// Create and submit orders
		for(NewsInstrument item: trader.instList) {
			trader.log("Creating order for " + item.getSymbol());
			item.createOrders(trader.con);
		}
		
		// Trade
		trader.trade();
		
		// Disconnect
		trader.disconnect();
	}

}
