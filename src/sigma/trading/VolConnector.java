package sigma.trading;

//import java.util.List;

import com.ib.client.Contract;
import com.ib.client.Types.SecType;
//import com.joptimizer.optimizers.BIPLokbaTableMethod;
//import com.joptimizer.optimizers.BIPOptimizationRequest;

//import cern.colt.matrix.tdouble.DoubleFactory1D;
//import cern.colt.matrix.tdouble.DoubleFactory2D;
//import cern.colt.matrix.tdouble.DoubleMatrix1D;
//import cern.colt.matrix.tdouble.DoubleMatrix2D;

/**
 * TWS Connector for volatility surface mapping
 * @author Peeter Meos
 *
 */
public class VolConnector extends TwsConnector {
	
	private Contract inst;
	
	//private List<Contract> hedgeInst;

	/**
	 * Constructs VolConnector instance that does the TWS connection
	 * and information retrieval.
	 */
	public VolConnector() {
		super("TWS Volatility Optimizer Connector");
	}
	
	/**
	 * Creates contract for the full instrument option chain
	 */
	public void createContract() {
		logger.log("Creating contract");
		
		inst = new Contract();
		
		inst.symbol("CL");
		inst.exchange("NYMEX");
		inst.currency("USD");
		inst.secType(SecType.FOP);
	}
	
	/**
	 * Retrieves portfolio info
	 */
	public void retrievePortfolio() {
		logger.log("Retrieving active portfolio");
		if (tws.isConnected()) {
			tws.reqPositions();
		}
	}
	
	/**
	 * Requests option chain contract details
	 */
	public void getOptionChain() {
		logger.log("Retrieving option chain");
		if (inst != null && tws.isConnected()) {
			tws.reqContractDetails(nextOrderID, inst);
		}
	}
	
	/**
	 * 
	 */
	public void calculateGreeks() {
		logger.log("Calculating greeks");
	}
	
	
	/**
	 * 
	 */
	public void createOrders() {
		logger.log("Creating orders");
	}
	
	@Override
    public void position(String account, Contract contract, double pos,
            double avgCost) {
        logger.log("Position. " + account+
        		" - Symbol: " + contract.symbol() +
        		", SecType: " + contract.secType() +
        		", Currency: " + contract.currency() +
        		", Position: " + pos + 
        		", Avg cost: " + avgCost);
    }
}
