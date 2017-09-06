/**
 * 
 */
package sigma.trading.news;

import com.ib.client.Contract;
import com.ib.client.Execution;

import sigma.trading.TwsConnector;
// import sigma.utils.TraderState;

/**
 * @author Peeter Meos
 * @version 0.1
 *
 */
public class Connector extends TwsConnector{

	/**
	 * 
	 */
	public void twsConnect() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 
	 */
	public void twsDisconnect() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Overriden tickPrice method that updates current spot price of the instrument and
	 * if needed, adjusts the orders.
	 */
	@Override
	public void tickPrice(int tickerId, int field, double price, int canAutoExecute) {
		String tckType = null;
		
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
			//this.spotPrice = price;
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
