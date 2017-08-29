package sigma.trading;

import sigma.optimiser.MaximiseTheta;
import org.jquantlib.instruments.Option;

import java.util.ArrayList;
import java.util.List;

/**
 * Portfolio optimiser that tries to minimise option portfolio gamma
 * and maximise theta. Downloads current positions and options chain 
 * via TWS API and then using Quantlib calculates option Greeks,
 * sets up optimisation as a BIP and solves it. 
 * 
 * @author Peeter Meos
 * @version 0.1
 *
 */
public class VolTrader {
	VolConnector tws;
	MaximiseTheta opt;
	List<Option> options;
	
	/**
	 * 
	 */
	public VolTrader() {
		tws = new VolConnector();
		options = new ArrayList<Option>();
	}
	
	/**
	 * Portfolio optimisation method for the class.
	 * Formulates the portfolio as a BIP model with
	 * options selected to the portfolio as binary
	 * variables.
	 * <p>
	 * Either minimises portfolio gamma or maximises
	 * theta while keeping the other within set
	 * bounds.
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
		
		// TODO Calculate option Greeks
		
		// TODO Run optimiser
		trader.optimisePortoflio();
		
		// Return the results
		
		// Save orders
		
		
	}

}
