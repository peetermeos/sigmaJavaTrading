/**
 * 
 */
package sigma.optimiser;

import java.io.IOException;
import java.util.Vector;

import com.ib.client.Contract;
import com.ib.client.TagValue;
import com.ib.client.Types.Right;
import com.ib.client.Types.SecType;

import sigma.trading.TwsConnector;
import sigma.utils.Helper;

/**
 * @author Peeter Meos
 * @version 0.1
 *
 */
public class VolSurface extends TwsConnector{

	protected Contract inst;
	protected double[] k = {40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55};
	protected String[] expiry = {"20171115", "20171214", "20180117", "20180214"};
	
	/**
	 * 
	 */
	public VolSurface() {
		super("Volatility Surface");
		this.twsConnect();
		
		inst = new Contract();
	}
	
	/**
	 * 
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
				
				inst.right(Right.Call);
				this.getTws().reqMktData(seq++, inst, "10,11,12,13", true, mktDataOptions);
				Helper.sleep(100);
				
				inst.right(Right.Put);
				this.getTws().reqMktData(seq++, inst, "10,11,12,13", true, mktDataOptions);
				Helper.sleep(100);

			}
		}
	}
	
	/**
	 * 
	 */
	@Override
	public void tickOptionComputation(int tickerId, int field, double impliedVol, double delta, double optPrice,
			double pvDividend, double gamma, double vega, double theta, double undPrice) {
		logger.log("Tick option computation delta " + delta +
				" gamma " + gamma + " theta " + theta + " vega " + vega);		
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
