/**
 * 
 */
package sigma.gui;

import java.util.ArrayList;
import java.util.List;

import sigma.trading.TwsConnector;
import sigma.utils.Ticker;

/**
 * @author Peeter Meos
 * @version 0.1
 *
 */
public class Connector extends TwsConnector {
	
	protected List<Ticker> tickers;
	
	/**
	 * Constructs modified TwsConnector for news trader GUI
	 * 
	 * @param str Title string (name) of the connector
	 */
	public Connector(String str) {
		super(str);
		
		tickers = new ArrayList<>();
	}

	/**
	 * Returns tickers to the calling method
	 * @return List of tickers
	 */
	public List<Ticker> getTickers() {
		return(tickers);
	}
	
	/**
	 * Overriden tick price method
	 */
	@Override
	public void tickPrice(int tickerId, int field, double price, int canAutoExecute) {
		String tckType = null;
		Boolean found = false;
		int tckIndex = 0;
		
		// Check if the ticker exists, if it doesn't add one
		for(int i=0; i < tickers.size(); i++) {
			if (tickers.get(i).getId() == tickerId) {
				found = true;
				tckIndex = i;
			}
		}
		if (!found) {
			tickers.add(new Ticker(tickerId, 0));
			tckIndex = tickers.size() - 1;
		}
		
		switch(field) {
		case 1: 
			tckType = "bid";
			tickers.get(tckIndex).setBid(price);
			break;
		case 2:
			tckType = "ask";
			tickers.get(tckIndex).setAsk(price);
			break;
		case 4:
			tckType = "last";
			tickers.get(tckIndex).setPrice(price);
			break;
		default:
			tckType = null;
		}
		if (tckType != null) {
			logger.log("Price ticker " + tickerId + " field " + tckType + " price " + price);	
		}
			
	}
}
