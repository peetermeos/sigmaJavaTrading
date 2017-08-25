package sigma.trading;

public class NewsTrader {

	public static void main(String[] args) {
		TwsTrader trader;
		
		trader = new TwsTrader();
		
		trader.twsConnect();
		trader.createContracts();
		trader.createOrders();
		
		trader.doTrading();
		trader.twsDisconnect(); 
	}

}

