package sigma.trading;

public class NewsTrader {

	public static void main(String[] args) {
		TwsTrader trader;
		
		try {
			trader = new TwsTrader();
			
			trader.twsConnect();
			trader.createContracts();
			trader.createOrders();
			
			trader.doTrading();
			trader.twsDisconnect();
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}
