package sigma.optimiser;

import com.joptimizer.exception.JOptimizerException;
import com.joptimizer.optimizers.BIPLokbaTableMethod;
import com.joptimizer.optimizers.BIPOptimizationRequest;

import cern.colt.matrix.tdouble.DoubleFactory1D;
import cern.colt.matrix.tdouble.DoubleFactory2D;
import cern.colt.matrix.tdouble.DoubleMatrix1D;
import cern.colt.matrix.tdouble.DoubleMatrix2D;

/**
 * 
 * @author peeterm
 *
 */
public class MaximiseTheta {
	
	/**
	 * 
	 * @return
	 * @throws JOptimizerException
	 */
	public String optimise() throws JOptimizerException {
		final DoubleFactory1D F1;
		final DoubleFactory2D F2;
		
		// Objective function
		DoubleMatrix1D c = F1.make(new double[] { 1, 4, 0, 7, 0, 0, 8, 6, 0, 4 });
		
		// Constraint coefficients
		DoubleMatrix2D G = F2.make(new double[][] { 
				{ -3, -1, -4, -4, -1, -5, -4, -4, -1, -1 },
				{  0,  0, -3, -1, -5, -5, -5, -1,  0, 0 }, 
				{ -4, -1, -5, -2, -4, -3, -2, -4, -4, 0 },
				{ -3, -4, -3, -5, -3, -1, -4, -5, -1, -4 } });
		
		// Constrain RHS vector
		DoubleMatrix1D h = F1.make(new double[] { 0, -2, -2, -8 });
		
		BIPOptimizationRequest or = new BIPOptimizationRequest();
		
		or.setC(c);
		or.setG(G);
		or.setH(h);
		
		or.setDumpProblem(true);
		
		//optimisation
		BIPLokbaTableMethod opt = new BIPLokbaTableMethod();
		opt.setBIPOptimizationRequest(or);
		opt.optimize();
		
		return(opt.getBIPOptimizationResponse().toString());
	}

}
