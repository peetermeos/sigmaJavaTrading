/**
 * 
 */
package sigma.optimiser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.ib.client.Contract;
import com.ib.client.TagValue;
import com.ib.client.Types.Right;
import com.ib.client.Types.SecType;

import sigma.trading.TwsConnector;
import sigma.utils.Helper;
import sigma.utils.OptSide;
import sigma.utils.Option;

/**
 * @author Peeter Meos
 * @version 0.1
 *
 */
public class VolSurface extends TwsConnector{

	protected Contract inst;
	// Currently the strikes and expiries are hardcoded
	protected double[] k = {40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55};
	protected String[] expiry = {"20171115", "20171214", "20180117", "20180214"};
	protected List<Option> surface;
	
	/**
	 * Simple constructor
	 */
	public VolSurface() {
		super("Volatility Surface");
		this.twsConnect();
		
		inst = new Contract();
		surface = new ArrayList<>();
	}
	
	/**
	 * Volatility request. Tickers 10-13 contain volatility data
	 */
	public void reqSurface() {
		Vector<TagValue> mktDataOptions = new Vector<>();
		int seq = 1;
		
		inst.symbol("CL");
		inst.secType(SecType.FOP);
		inst.exchange("NYMEX");
		inst.currency("USD");
		inst.multiplier("1000");

		for(double i: k) {
			for(String j: expiry) {
				inst.lastTradeDateOrContractMonth(j);
				inst.strike(i);
				
				// Request call
				inst.right(Right.Call);
				this.getTws().reqMktData(seq++, inst, "10,11,12,13", true, mktDataOptions);
				surface.add(new Option(seq, i, 0, j, OptSide.CALL));
				Helper.sleep(100);
				
				// Request put
				inst.right(Right.Put);
				this.getTws().reqMktData(seq++, inst, "10,11,12,13", true, mktDataOptions);
				surface.add(new Option(seq, i, 0, j, OptSide.PUT));
				Helper.sleep(100);
			}
		}
	}
	
	/**
	 * Option value, volatility and greeks response
	 */
	@Override
	public void tickOptionComputation(int tickerId, int field, double impliedVol, double delta, double optPrice,
			double pvDividend, double gamma, double vega, double theta, double undPrice) {
		logger.log("Tick option computation ticker " + tickerId + " field " + field + " IV "+ impliedVol + " delta " + delta +
				" gamma " + gamma + " theta " + theta + " vega " + vega);
		
		// Add data
		for(Option item: surface) {
			if (item.getId() == tickerId) {
				item.setSigma(impliedVol);
				item.setPrice(optPrice);
				item.setS(undPrice);
				item.setGamma(gamma);
				item.setTheta(theta);
				item.setVega(vega);
			}
		}
	}
	
	/**
	 * Tick price is not needed, we get volatility and greeks otherwise
	 */
	@Override
	public void tickPrice(int tickerId, int field, double price, int canAutoExecute) {
		// Nothing to see here, tick price is not needed
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		VolSurface v;
		
		v = new VolSurface();
		v.reqSurface();
		
		try {
			while (System.in.available() == 0) {
				Helper.sleep(1000);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
