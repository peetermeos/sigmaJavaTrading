/**
 * 
 */
package sigma.optimiser;

import com.joptimizer.exception.JOptimizerException;

import sigma.utils.Helper;
import sigma.utils.VolSurface;

/**
 * @author Peeter Meos
 * @version 0.1
 */
public class Optimiser {
	public MaximiseTheta problem;
	public VolSurface surface;

	/**
	 * Simple constructor.
	 */
	public Optimiser() {
		problem = new MaximiseTheta();
		surface = new VolSurface();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Optimiser o;
		String res;
		
		o = new Optimiser();
		
		// Get data
		o.surface.reqSurface();
		Helper.sleep(60000);
		o.surface.twsDisconnect();

		// Optimise
		o.surface.log("Setting up the problem");
		
		// Set objective function
		o.problem.setObjCoef(o.surface.getTheta());
		
		// Set A matrix and RHS vector
		o.problem.setRHSCoef(new double[] {0});
		o.problem.setAMatrix(new double[][] {o.surface.getGamma()});
		// Run optimisation
		try {
			res = o.problem.optimise();
			
			// Show the result
			o.surface.log(res);
			
		} catch (JOptimizerException e) {
			e.printStackTrace();
		}
		
	}
}
