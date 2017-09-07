/**
 * 
 */
package sigma.trading.news;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.ib.client.Contract;
import com.ib.client.Execution;
import sigma.trading.TwsConnector;
import sigma.utils.Ticker;
import sigma.utils.Trade;

/**
 * TwsConnector extension for multi-instrument news trading
 * 
 * @author Peeter Meos
 * @version 0.2
 *
 */
public class Connector extends TwsConnector {
	
	List<Ticker> prices;
	List<Trade> trades;
	
	/**
	 * Constructor just adds recordkeeping of ticker prices to twsConnector
	 */
	public Connector() {
		super();
		prices = new ArrayList<>();
		trades = new ArrayList<>();
	}
	

	/**
	 * Returns connection status
	 * @return connection status
	 */
	public boolean isConnected() {
		return tws.isConnected();
	}
	
	/**
	 * Returns prices list
	 * @return
	 */
	public List<Ticker> getPrices() {
		return(prices);
	}
	
	/**
	 * Returns list of trades
	 * @return
	 */
	public List<Trade> getTrades() {
		return(trades);
	}
	
	/**
	 * Returns last known price of the instrument
	 * 
	 * @param id Instrument ticker ID
	 * @return Last price of the instrument
	 */
	public double getPrice(int id) {
		
		for (int i = 0; i < prices.size(); i++) { 
			if (prices.get(i).getId() == id) {
				return(prices.get(i).getPrice());
			}
		}
		return(-1);
	}
	
	/**
	 * Overriden tickPrice method that updates current spot price of the instrument and
	 * if needed, adjusts the orders.
	 */
	@Override
	public void tickPrice(int tickerId, int field, double price, int canAutoExecute) {
		String tckType = null;
		boolean found = false;
		
		// Currently assumes only one instrument and ticker id.
		// If we trade many, then many ticker IDs are needed.
		
		switch(field) {
		case 1: 
			tckType = "bid";
			break;
		case 2:
			tckType = "ask";
			break;
		case 4:
			tckType = "last";
			
			// Try to update existing ticker data
			for (int i = 0; i < prices.size(); i++) {
				logger.verbose("Checking ticker " + prices.get(i).getId());
				if(prices.get(i).getId() == tickerId) {
					found = true;
					logger.verbose("Updating ticker " + prices.get(i).getId());
					prices.get(i).setPrice(price);
				}
			}
			
			// If not found add new ticker
			if (!found) {
				logger.verbose("Adding new ticker");
				prices.add(new Ticker(tickerId, price));
			}
			break;
		default:
			tckType = null;
		}
		
		// Adjustment needs to be done here
		
		if (tckType == "last") {
			logger.verbose("Price ticker " + tickerId + " field " + tckType + " price " + price);	
		}
			
	}
	
	/**
	 * Overriden to change trader status, when order executes
	 */
	@Override
    public void execDetails(int reqId, Contract contract, Execution execution) {
		
		logger.log("New execution for " + contract.symbol());	

		trades.add(new Trade(contract.symbol(), execution.cumQty(), execution.price(), execution.side(), new Date()));
		
		// If the order was regular stop limit (entry)
		//if ((execution.orderId() == oID) || (execution.orderId() == oID + 1)) {
			// Stop has fired
		//	logger.log("Entry order " + reqId + " for " + contract.symbol() + " has executed");
			//this.state = TraderState.EXEC;
			// Here we should run a check that order on the other side got cancelled and only the trail 
			// is active

		//}
		
		// If the order was trail stop (exit)
		//if ((execution.orderId() == oID + 2) || (execution.orderId() == oID + 3)) {
			// Stop has fired
		//	logger.log("Exit order " + reqId + " for " + contract.symbol() + " has executed");
			//this.state = TraderState.WAIT;
			// Here we should check that we have no active orders
		//}
	}

}
