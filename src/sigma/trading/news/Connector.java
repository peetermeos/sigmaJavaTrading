/**
 * 
 */
package sigma.trading.news;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import com.ib.client.Contract;
import com.ib.client.Execution;
import com.ib.client.Order;
import com.ib.client.TagValue;
import sigma.trading.TwsConnector;

/**
 * @author Peeter Meos
 * @version 0.1
 *
 */
public class Connector extends TwsConnector {
	
	List<Ticker> prices;
	
	/**
	 * Constructor just adds recordkeeping of ticker prices to twsConnector
	 */
	public Connector() {
		super();
		prices = new ArrayList<>();
	}
	
	/**
	 * Places bracket order set to the market.
	 *
	 * @param i Instrument order ID
	 * @param c Contract
	 * @param o Order
	 */
	public void placeOrder(int i, Contract c, Order o) {
		if (tws.isConnected()) {
			tws.placeOrder(i, c, o);
		} else {
			logger.error("Cannot place order, not connected to TWS.");
		}
	}
	
	/**
	 * Request market data for given contract.
	 * @param c
	 */
	public void reqMktData(Contract c) {
		Vector<TagValue> mktDataOptions = new Vector<>();
		
		if (tws.isConnected()) {
			String genericTickList = null;
			
			tws.reqMktData(nextOrderID, c, genericTickList, false, mktDataOptions);
		} else {
			logger.error("Cannot request data, not connected to TWS.");
		}	
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
				if(prices.get(i).getId() == tickerId) {
					found = true;
					prices.get(i).setPrice(price);
				}
			}
			
			// If not found add new ticker
			if (!found) {
				prices.add(new Ticker(tickerId, price));
			}
			break;
		default:
			tckType = null;
		}
		if (tckType != null) {
			logger.log("Price ticker " + tickerId + " field " + tckType + " price " + price);	
		}
			
		// Adjust orders if price has moved too much
		//if (this.currentSpot > 0 && this.state == TraderState.LIVE) {
		//	adjustOrders(price);
		//}
	}
	
	/**
	 * Overriden to change trader status, when order executes
	 */
	@Override
    public void execDetails(int reqId, Contract contract, Execution execution) {
		
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
