package sigma.trading;

import sigma.optimiser.MaximiseTheta;

/**
 * 
 * @author Peeter Meos
 *
 */
public class VolTrader {
	VolConnector tws;
	MaximiseTheta opt;
	
	/**
	 * 
	 */
	public VolTrader() {
		tws = new VolConnector();
	}
	
	/**
	 * 
	 */
	public void optimisePortoflio() {
		tws.logger.log("Optimising portfolio");	
		opt = new MaximiseTheta();
	}
	
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		VolTrader trader;
		
		// TODO Get volatility surface and portfolio here
		trader = new VolTrader();
		trader.tws.twsConnect();
		trader.tws.retrievePortfolio();
		trader.tws.createContract();
		trader.tws.getOptionChain();
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		trader.tws.twsDisconnect();
		
		// TODO Calculate option greeks
		
		// TODO Run optimiser
		trader.optimisePortoflio();
		
		// Return the results
		
		// Save orders
		
		
	}

}
