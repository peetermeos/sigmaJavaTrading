package sigma.trading;

/**
 * 
 * @author Peeter Meos
 *
 */
public class VolTrader {

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		VolConnector tws;
		
		tws = new VolConnector();
		tws.twsConnect();
		tws.twsDisconnect();
	}

}
