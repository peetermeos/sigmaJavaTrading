package sigma.utils;

/**
 * Simple helper class to implement various functionalities
 * 
 * @author Peeter Meos
 * @version 0.1
 *
 */
public class Helper {
	
	/** 
	 * Sleep for given number of milliseconds
	 * 
	 * @param msec Milliseconds to wait
	 */
	public static void sleep(int msec) {
		try {
			Thread.sleep(msec);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
