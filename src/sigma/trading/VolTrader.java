package sigma.trading;

public class VolTrader {

	public static void main(String[] args) {
		VolConnector tws;
		
		tws = new VolConnector();
		tws.twsConnect();
		tws.twsDisconnect();
	}

}
