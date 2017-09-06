package sigma.trading.news;

import java.util.ArrayList;
import java.util.List;

import sigma.trading.TwsConnector;

public class Trader {
	
	public List<Instrument> instList;
	
	private Connector con;
	
	/**
	 * 
	 */
	public Trader() {
		instList = new ArrayList<>();
		con = new Connector();
	}
	
	/**
	 * Connect trading system, request market data for the instruments
	 */
	public void connect() {
		con.twsConnect();
		for(Instrument item: instList) {
			System.out.println(item.getID());
		}
	}
	
	/**
	 * The main trading loop
	 */
	public void trade() {
		
	}
	
	/**
	 * Disconnect and shut down trading system
	 */
	public void disconnect() {
		con.twsDisconnect();
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
        Trader trader;
		
		// New object
		trader = new Trader();
		
		// Connect
		trader.connect();
		
		// Instrument add CL
		trader.instList.add(new Instrument());
		
		// Instrument add E7
		trader.instList.add(new Instrument());
		
		// Trade
		trader.trade();
		
		// Disconnect
		trader.disconnect();
	}

}
