
package sigma.trading;

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
		
		trader = new TwsTrader();
		
		trader.twsConnect();
		trader.createContracts();
		//	trader.createOrders();
		
		trader.doTrading();
		trader.twsDisconnect(); 
	}

}

