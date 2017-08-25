package sigma.trading;

import java.io.IOException;
import java.util.Vector;

import com.ib.client.Contract;
import com.ib.client.Order;
import com.ib.client.OrderType;
import com.ib.client.TagValue;

/**
 * 
 * @author Peeter Meos
 *
 */
public class TwsTrader extends TwsConnector {

    private int oID = 0;
    private String genericTickList = null;
    
    // Contract structure
    private Contract inst;
   
    private Order longStop;
    private Order shortStop;
    private Order longTrail;
    private Order shortTrail;
    
    private double spotPrice = -1;
    private double currentSpot = -1;
    private double delta = 0;
    private double trailAmt = 0;
    private double q = 0;
      
    /**
     * Constructor for TwsTrader object. 
     * Simply creates the instance and starts
     * the logger.
     */
	public TwsTrader() {
		super("Sigma News Trader");
	}
	
	/**
	 * 
	 */
	public void doTrading() {
		logger.log("Entering main trading loop");
		try {
			tws.reqCurrentTime();

			// Infinite loop until keypress
			while (System.in.available() == 0) {
				Thread.sleep(100);
				// Here check open orders and adjust them if needed
			}
			
		} catch (InterruptedException | IOException e) {
			logger.error(e);
		}
		logger.log("Main trading loop finished");
	}
	 	
	/**
	 * 
	 */
	public void createContracts() {
		logger.log("Creating contract structure");
		
		inst = new Contract();
		inst.exchange("NYMEX");
		inst.symbol("CL");
		inst.secType("FUT");
		inst.multiplier("1000");
		inst.lastTradeDateOrContractMonth("201710");
		
		// Requesting contract details
		logger.log("Requesting contract details");
		tws.reqContractDetails(nextOrderID, inst);
		
		logger.log("Requesting market data");
		Vector<TagValue> mktDataOptions = new Vector<TagValue>();
		tws.reqMktData(1, inst, genericTickList, false, mktDataOptions);
	}
	
	/**
	 * 
	 */
	public void createOrders() {
		logger.log("Creating order structure");
		
		longStop = new Order();
		shortStop = new Order();
		longTrail = new Order();
		shortTrail = new Order();
		
		// Types
		logger.log("Setting order types");
		longStop.orderType(OrderType.STP_LMT);
		shortStop.orderType(OrderType.STP_LMT);
		longTrail.orderType(OrderType.TRAIL_LIMIT);
		shortTrail.orderType(OrderType.TRAIL_LIMIT);
		
		// Actions
		logger.log("Setting order actions");
		longStop.action("BUY");
		shortStop.action("SELL");
		longTrail.action("SELL");
		shortTrail.action("BUY");
		
		// Quantities
		logger.log("Setting order quantities");
		q = 1;
		longStop.totalQuantity(q);
		shortStop.totalQuantity(q);
		longTrail.totalQuantity(q);
		shortTrail.totalQuantity(q);
		
		// Prices
		// First wait to have spot price
		logger.log("Waiting for spot price");
		while (spotPrice < 0) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				logger.error(e);
			}
		}
		
		delta = 0.05;
		trailAmt = 0.1;
		
		logger.log("Setting order limits");
		longStop.lmtPrice(spotPrice + delta);
		longStop.auxPrice(spotPrice + delta);
		longStop.transmit(false);
		longStop.orderId(nextOrderID);
		longStop.outsideRth(true);
		
		shortStop.lmtPrice(spotPrice - delta);
		shortStop.auxPrice(spotPrice - delta);
		shortStop.transmit(false);
		shortStop.orderId(nextOrderID + 1);
		shortStop.outsideRth(true);
		
		longTrail.trailStopPrice(spotPrice);
		longTrail.auxPrice(trailAmt);
		longTrail.parentId(longStop.orderId());
		longTrail.transmit(true);
		longTrail.outsideRth(true);
		
		shortTrail.trailStopPrice(spotPrice);
		shortTrail.auxPrice(trailAmt);
		shortTrail.parentId(shortStop.orderId());
		shortTrail.transmit(true);
		shortTrail.outsideRth(true);
		
		// OCO groupings and attached orders
		longStop.ocaGroup("NT");
		shortStop.ocaGroup("NT");
		
		logger.log("Placing orders");
		oID = nextOrderID;
		tws.placeOrder(oID, inst, longStop);
		tws.placeOrder(oID + 1, inst, shortStop);
		tws.placeOrder(oID + 2, inst, longTrail);
		tws.placeOrder(oID + 3, inst, shortTrail);
		
		currentSpot = spotPrice;
	}
	
	/**
	 * 
	 * @param spotPrice
	 */
	public void adjustOrders(double spotPrice) {
		if (spotPrice != currentSpot) {
			logger.log("Adjusting orders");
			longStop.lmtPrice(spotPrice + delta);
			shortStop.lmtPrice(spotPrice - delta);
			longTrail.trailStopPrice(spotPrice);
			shortTrail.trailStopPrice(spotPrice);	
			
			logger.log("Placing orders");
			tws.placeOrder(oID, inst, longStop);
			tws.placeOrder(oID + 1, inst, shortStop);
			tws.placeOrder(oID + 2, inst, longTrail);
			tws.placeOrder(oID + 3, inst, shortTrail);
			
			currentSpot = spotPrice;
		}
	}
	
	/**
	 * 
	 */
	public void cancelOrders() {
		// tws.cancelOrder(id);
	}
	
	@Override
	public void tickPrice(int tickerId, int field, double price, int canAutoExecute) {
		String tckType = null;
		
		switch(field) {
		case 1: 
			tckType = "bid";
			break;
		case 2:
			tckType = "ask";
			break;
		case 4:
			tckType = "last";
			this.spotPrice = price;
			break;
		default:
			tckType = null;
		}
		if (tckType != null) {
			logger.log("Price ticker " + tickerId + " field " + tckType + " price " + price);	
		}
			
	}

}
