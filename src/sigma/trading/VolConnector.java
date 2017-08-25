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
	 * 
	 */
	public VolConnector() {
		super("TWS Volatility Optimizer Connector");
	}
	
	/**
	 * 
	 */
	public void CreateContract() {
		logger.log("Creating contract");
		
		inst = new Contract();
		
		inst.symbol("CL");
		inst.exchange("NYMEX");
		inst.currency("USD");
		inst.secType(SecType.FOP);
	}
	
	/**
	 * 
	 */
	public void RetrievePortfolio() {
		logger.log("Retrieving active portfolio");
		
	}
	
	/**
	 * 
	 */
	public void GetOptionChain() {
		logger.log("Retrieving option chain");
	}
	
	/**
	 * 
	 */
	public void CalculateGreeks() {
		logger.log("Calculating greeks");
	}
	
	/**
	 * 
	 */
	public void OptimisePortoflio() {
		logger.log("Optimising portfolio");
	}
	
	/**
	 * 
	 */
	public void CreateOrders() {
		logger.log("Creating orders");
	}
}
