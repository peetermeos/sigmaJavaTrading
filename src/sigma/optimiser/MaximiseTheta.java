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
 * @author Peeter Meos
 * @version 0.1
 *
 */
public class MaximiseTheta {
	
	private DoubleFactory1D objCoef;
	private DoubleFactory1D RHSCoef;
	private DoubleFactory2D AMatrix;
	
	/**
	 * Constructor for MaximiseTheta
	 */
	public MaximiseTheta() {
		
	}
	
	/**
	 * 
	 * @return
	 * @throws JOptimizerException
	 */
	public String optimise() throws JOptimizerException {
		final DoubleFactory1D F1 = DoubleFactory1D.sparse;
		final DoubleFactory2D F2 = DoubleFactory2D.sparse;
		
		// Objective function coefficients (gamma)
		DoubleMatrix1D c = F1.make(new double[] { 1, 4, 0, 7, 0, 0, 8, 6, 0, 4 });
		
		// Constraint coefficients
		DoubleMatrix2D G = F2.make(new double[][] { 
				{ -3, -1, -4, -4, -1, -5, -4, -4, -1, -1 },
				{  0,  0, -3, -1, -5, -5, -5, -1,  0, 0 }, 
				{ -4, -1, -5, -2, -4, -3, -2, -4, -4, 0 },
				{ -3, -4, -3, -5, -3, -1, -4, -5, -1, -4 } });
		
		// Constraint RHS vector
		DoubleMatrix1D h = F1.make(new double[] { 0, -2, -2, -8 });
		
		BIPOptimizationRequest or = new BIPOptimizationRequest();
		
		or.setC(c);
		or.setG(G);
		or.setH(h);
		
		or.setDumpProblem(true);
		
		// Run optimization
		BIPLokbaTableMethod opt = new BIPLokbaTableMethod();
		opt.setBIPOptimizationRequest(or);
		opt.optimize();
		
		return(opt.getBIPOptimizationResponse().toString());
	}



	/**
	 * @return the objCoef
	 */
	public DoubleFactory1D getObjCoef() {
		return objCoef;
	}



	/**
	 * @param objCoef the objCoef to set
	 */
	public void setObjCoef(DoubleFactory1D objCoef) {
		this.objCoef = objCoef;
	}



	/**
	 * @return the rHSCoef
	 */
	public DoubleFactory1D getRHSCoef() {
		return RHSCoef;
	}



	/**
	 * @param rHSCoef the rHSCoef to set
	 */
	public void setRHSCoef(DoubleFactory1D rHSCoef) {
		RHSCoef = rHSCoef;
	}



	/**
	 * @return the aMatrix
	 */
	public DoubleFactory2D getAMatrix() {
		return AMatrix;
	}



	/**
	 * @param aMatrix the aMatrix to set
	 */
	public void setAMatrix(DoubleFactory2D aMatrix) {
		AMatrix = aMatrix;
	}

}
