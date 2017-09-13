/**
 * 
 */
package sigma.optimiser;

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
		
		o = new Optimiser();
		
		// Get data
		o.surface.twsConnect();
		o.surface.reqSurface();
		Helper.sleep(60000);
		o.surface.twsDisconnect();

		// Optimise - set objective function
		o.problem.setObjCoef(o.surface.getTheta());
		// Set A matrix and RHS vector
		// Run optimisation
	}
}
