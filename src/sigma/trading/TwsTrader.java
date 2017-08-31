/**
 * Package for handling TWS connectivity and 
 * trading algorithms.
 * 
 * @author Peeter Meos
 */
package sigma.trading;

import java.io.IOException;
import java.util.Vector;

import com.ib.client.Contract;
import com.ib.client.Order;
import com.ib.client.OrderType;
import com.ib.client.TagValue;

import sigma.utils.TraderState;

/**
 * The News Trader object.
 * The strategy is to have stop loss orders on both sides of the 
 * market to wait for a substantial jump or drop on previously
 * known news event. After the jump use trail stop to get the 
 * profit.
 * 
 * @author Peeter Meos
 * @version 0.3
 *
 */
public class TwsTrader extends TwsConnector {

	private TraderState state;
	
    private int oID = 0;
    private String genericTickList = null;
    
    // Contract structure
    private Contract inst = null;
   
    private Order longStop;
    private Order shortStop;
    private Order longTrail;
    private Order shortTrail;
    
    private double spotPrice = -1;
    private double currentSpot = -1;
    private double delta = 0;
    private double adjLimit = 0.02;
    private double trailAmt = 0;
    private double q = 0;
    
    private boolean simulated = true;
      
    /**
     * Constructor for TwsTrader object. 
     * Simply creates the instance and starts
     * the logger. The default for safety is to start in
     * simulated mode.
     */
	public TwsTrader() {
		this(true);
	}
	
	/**
	 * Constructor for TwsTrader object.
	 * Sets the initial safety parameter to either
	 * allow or prevent live trading.
	 * 
	 * @param m_sim
	 */
	public TwsTrader(boolean m_simulated) {
		super("Sigma News Trader");
		simulated = m_simulated;
		logger.log("Simulated mode :" + simulated);
		this.state = TraderState.WAIT;
	}
	
	/**
	 * Method disarms the trader and turns off live
	 * trading mode.
	 */
	public void safetyOn( ) {
		this.simulated = true;
	}
	
	/**
	 * Method allows Trader object to create active orders and
	 * in fact do live trading.
	 */
	public void safetyOff() {
		this.simulated = false;
	}
	
	/**
	 * This method implements the main trading loop for the 
	 * news trading object.
	 */
	public void doTrading() {
		if (!tws.isConnected()) {
			logger.error("doTrading(): Not connected to TWS.");
			return;
		}
		
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
	 * Method creates contracts for the instrument to be traded
	 * Currently assumes that we trade futures (such as WTI Crude)
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
	 *  This method creates orders for the instrument and 
	 *  transmits them to the market. The method should naturally
	 *  be run after createContracts() is called.
	 */
	public void createOrders() {
		// Exit if the instrument has not been created
		if (inst == null) {
			logger.error("createOrders(): Instrument to be traded is not created.");
			return;
		}
		
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
		
		// Long order
		logger.log("Setting order limits");
		longStop.lmtPrice(spotPrice + delta);
		longStop.auxPrice(spotPrice + delta);
		longStop.transmit(false);
		longStop.orderId(nextOrderID);
		longStop.outsideRth(true);
		
		// Short order
		shortStop.lmtPrice(spotPrice - delta);
		shortStop.auxPrice(spotPrice - delta);
		shortStop.transmit(false);
		shortStop.orderId(nextOrderID + 1);
		shortStop.outsideRth(true);
		
		// Long profit taker
		longTrail.trailStopPrice(spotPrice);
		longTrail.auxPrice(trailAmt);
		longTrail.parentId(longStop.orderId());
		longTrail.transmit(true);
		longTrail.outsideRth(true);
		
		// Short profit taker
		shortTrail.trailStopPrice(spotPrice);
		shortTrail.auxPrice(trailAmt);
		shortTrail.parentId(shortStop.orderId());
		shortTrail.transmit(true);
		shortTrail.outsideRth(true);
		
		// OCO groupings and attached orders
		longStop.ocaGroup("NT" + inst.symbol());
		shortStop.ocaGroup("NT" + inst.symbol());
		
		if (tws.isConnected() && !simulated) {
			logger.log("Placing orders");
			oID = nextOrderID;

			tws.placeOrder(oID, inst, longStop);
			tws.placeOrder(oID + 1, inst, shortStop);
			tws.placeOrder(oID + 2, inst, longTrail);
			tws.placeOrder(oID + 3, inst, shortTrail);	
			
			this.state = TraderState.LIVE;
		}

		// Save the current price
		currentSpot = spotPrice;
	}
	
	/**
	 * This method adjust the orders to reflect the current
	 * spot price for the instrument.
	 * 
	 * @param spotPrice double - spot price for the instrument
	 */
	public void adjustOrders(double spotPrice) {
		if (Math.abs(spotPrice - this.currentSpot) > this.adjLimit) {
			logger.log("Adjusting orders");
			longStop.lmtPrice(spotPrice + delta);
			shortStop.lmtPrice(spotPrice - delta);
			longTrail.trailStopPrice(spotPrice);
			shortTrail.trailStopPrice(spotPrice);	
			
			logger.log("Placing orders");
			if (!simulated ) {
				tws.placeOrder(oID, inst, longStop);
				tws.placeOrder(oID + 1, inst, shortStop);
				tws.placeOrder(oID + 2, inst, longTrail);
				tws.placeOrder(oID + 3, inst, shortTrail);	
			}
						
			this.currentSpot = spotPrice;
		}
	}
	
	/**
	 * Cancels all the active orders for the instrument
	 */
	public void cancelOrders() {
		tws.cancelOrder(oID);
		tws.cancelOrder(oID + 1);
		tws.cancelOrder(oID + 2);
		tws.cancelOrder(oID + 3);
		
		// TODO Before this, we need to be sure, that in fact the orders were cancelled.
		this.state = TraderState.WAIT;
	}
	
	/**
	 * Overriden tickPrice method that updates current spot price of the instrument and
	 * if needed, adjusts the orders.
	 */
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
			
		// Adjust orders if price has moved too much
		if (this.currentSpot > 0 && this.state == TraderState.LIVE) {
			adjustOrders(price);
		}
	}

}
