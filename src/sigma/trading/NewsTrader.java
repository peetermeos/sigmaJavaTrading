
package sigma.trading;

import sigma.utils.Helper;

/**
 * Main news trader code, primarily just a wrapper for 
 * news trader object.
 * 
 * @author Peeter Meos
 * @version 1.0
 *
 */
public class NewsTrader {

	/**
	 * Main method and entry point for news trader
	 * @param args command line arguments for trader
	 */
	public static void main(String[] args) {
		TwsTrader trader;
		
		trader = new TwsTrader(false);
		
		trader.twsConnect();
		trader.createContracts();
		Helper.sleep(2000);
		trader.createOrders();
		
		trader.doTrading();
		trader.tws.cancelMktData(1);

		trader.twsDisconnect(); 
	}

}

