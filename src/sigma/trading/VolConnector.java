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
		
		//final DoubleFactory1D F1;
		//final DoubleFactory2D F2;
		
		//DoubleMatrix1D c = F1.make(new double[] { 1, 4, 0, 7, 0, 0, 8, 6, 0, 4 });
		//DoubleMatrix2D G = F2.make(new double[][] { 
		//		{ -3, -1, -4, -4, -1, -5, -4, -4, -1, -1 },
		//		{  0,  0, -3, -1, -5, -5, -5, -1,  0, 0 }, 
		//		{ -4, -1, -5, -2, -4, -3, -2, -4, -4, 0 },
		//		{ -3, -4, -3, -5, -3, -1, -4, -5, -1, -4 } });
		//DoubleMatrix1D h = F1.make(new double[] { 0, -2, -2, -8 });
		
		//BIPOptimizationRequest or = new BIPOptimizationRequest();
		//or.setC(c);
		//or.setG(G);
		//or.setH(h);
		//or.setDumpProblem(true);
		
		//optimization
		//BIPLokbaTableMethod opt = new BIPLokbaTableMethod();
		//opt.setBIPOptimizationRequest(or);
		//opt.optimize();
	}
	
	/**
	 * 
	 */
	public void CreateOrders() {
		logger.log("Creating orders");
	}
}
