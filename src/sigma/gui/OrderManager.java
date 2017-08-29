package sigma.gui;

import sigma.trading.TwsConnector;

public class OrderManager extends TwsConnector{

	public OrderManager() {
		super("Sigma Order Manager");
	}
	
	public static void main(String[] args) {
		OrderManager app;
		
		app = new OrderManager();
		app.twsConnect();
		app.twsDisconnect();

	}

}
