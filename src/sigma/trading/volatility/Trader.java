/**
 * 
 */
package sigma.trading.volatility;

import sigma.optimiser.MaximiseTheta;
import sigma.trading.Instrument;
import sigma.utils.Helper;
import sigma.utils.OptSide;

import org.jquantlib.instruments.Option;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
public class Trader {
	Connector tws;
	MaximiseTheta opt;
	List<Option> options;
	private Set<String> expirySet;
	private Set<Double> strikeSet;
	private List<Instrument> hedgeInst;
	private List<Instrument> futCurve;
	
	/**
	 * Initialises the volatility optimiser.
	 * Creates all the required objects and the solution 
	 * space for optimal portfolio (ie. expiries x strikes)
	 */
	public Trader() {
		tws = new Connector();
		options = new ArrayList<Option>();
		expirySet = new HashSet<String>();
		strikeSet = new HashSet<Double>();
		hedgeInst = new ArrayList<>();
		futCurve = new ArrayList<>();
		
		
		// Describe list of valid expiries and strikes
		expirySet.add("201712");
		expirySet.add("201803");
		
		strikeSet.add(45.0);	
		strikeSet.add(55.0);
		
		// Futures
		futCurve.add(new Instrument("CL", "FUT", "NYMEX", "201712"));
		futCurve.add(new Instrument("CL", "FUT", "NYMEX", "201803"));
		
		// Create instruments
		for (String i: expirySet) {
			for (Double j: strikeSet) {
				hedgeInst.add(new Instrument("CL", "FOP", "NYMEX", i, j, OptSide.CALL));
				hedgeInst.add(new Instrument("CL", "FOP", "NYMEX", i, j, OptSide.PUT));
			}
		}
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
	 * 
	 * @param maxGamma maximal Gamma allowed for the portfolio
	 *        units - currency
	 *        
	 * @param minTheta minimal Theta allowed for the portfolio
	 *        units - currency  
	 */
	public void optimisePortoflio(double maxGamma, double minTheta) {
		tws.log("Optimising portfolio");	
		opt = new MaximiseTheta();
	}
	
	
	/**
	 * 
	 * @param args Command line parameters for the trader code.
	 */
	public static void main(String[] args) {
		Trader trader;
		
		// TODO Get volatility surface and portfolio here
		trader = new Trader();
		trader.tws.twsConnect();
		trader.tws.retrievePortfolio();
		
		// Request contract details for underlying contracts
		trader.hedgeInst.forEach(item->trader.tws.createContract(item));
		trader.futCurve.forEach(item->trader.tws.getFutureCurve(item));
		
		trader.tws.getOptionChain(trader.hedgeInst);
		
		Helper.sleep(5000);
		
		trader.tws.twsDisconnect();
		
		// TODO Calculate option Greeks
		
		// TODO Run optimiser
		trader.optimisePortoflio(500, 0);
		
		// Return the results
		
		// Save orders
		
		
	}

}