/**
 * Sigma news trader.
 * 
 * @author Peeter Meos
 */
package sigma.trading.news;

import java.io.IOException;

import sigma.utils.TraderState;

/**
 * Trader class for news trading
 * 
 * @author Peeter Meos
 * @version 0.2
 *
 */
public class Trader extends Connector {
	
	/**
	 * The main trading loop
	 */
	public void trade() {
		
		double diff = 0;
	
		if (!isConnected()) {
			logger.error("doTrading(): Not connected to TWS.");
			return;
		}
		
		logger.log("Entering main trading loop");
		try {
			// Infinite loop until keypress
			while (System.in.available() == 0) {
				Thread.sleep(1000);
				
				// Get last prices, adjust prices if needed
				for(NewsInstrument item: instList) {
					// Create new order if there are none
					if (item.getState() == TraderState.WAIT) {
						logger.log("Creating order for " + item.getSymbol());
						item.createOrders(this);
					}
					
					// Check whether order needs to be adjusted
					if (item.getState() == TraderState.LIVE) {
						diff = getPrice(item.getID()) - item.getLast();
						logger.verbose("Diff  for " + item.getSymbol() + " is " + diff);
						if (Math.abs(diff) > item.getAdjLimit() ) {
							// Adjust orders
							item.adjustOrders(this);
						}
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
	 * Main entry point for news trader algorithm
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
        Trader trader;
		
		// New object
		trader = new Trader();
		
		// Connect
		trader.twsConnect();
		trader.setSimulated(false);
		
		// Instrument add CL
		trader.log("Adding CL");
		trader.instList.add(new NewsInstrument("CL", "FUT", "NYMEX",  "201710", 1, 0.1, 0.05, 0.05));
		
		// Instrument add E7
		trader.log("Adding EURO");
		trader.instList.add(new NewsInstrument("E7", "FUT", "GLOBEX", "201712", 1, 0.0010, 0.0005, 0.0002));

		// Create and submit orders
		for(NewsInstrument item: trader.instList) {
			trader.log("Creating order for " + item.getSymbol());
			item.createOrders(trader);
		}
		
		// Trade
		trader.trade();
		
		// Disconnect
		trader.disconnect();
	}

}
