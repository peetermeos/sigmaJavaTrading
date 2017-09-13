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
	
	private DoubleMatrix1D objCoef;
	private DoubleMatrix1D RHSCoef;
	private DoubleMatrix2D AMatrix;
	
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
		
		BIPOptimizationRequest or = new BIPOptimizationRequest();
		
		or.setC(objCoef);
		or.setG(AMatrix);
		or.setH(RHSCoef);
		
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
	public DoubleMatrix1D getObjCoef() {
		return objCoef;
	}


	/**
	 * @param objCoef the objCoef to set
	 */
	public void setObjCoef(double[] objCoef) {
		DoubleFactory1D F1 = DoubleFactory1D.sparse;
		
		this.objCoef = F1.make(objCoef);
	}


	/**
	 * @return the rHSCoef
	 */
	public DoubleMatrix1D getRHSCoef() {
		return RHSCoef;
	}


	/**
	 * @param rHSCoef the rHSCoef to set
	 */
	public void setRHSCoef(double[] rHSCoef) {
		DoubleFactory1D F1 = DoubleFactory1D.sparse;
		
		RHSCoef = F1.make(rHSCoef);
	}


	/**
	 * @return the aMatrix
	 */
	public DoubleMatrix2D getAMatrix() {
		return AMatrix;
	}


	/**
	 * @param aMatrix the aMatrix to set
	 */
	public void setAMatrix(double[][] aMatrix) {
		DoubleFactory2D F2 = DoubleFactory2D.sparse;
		
		AMatrix = F2.make(aMatrix);
	}

}
