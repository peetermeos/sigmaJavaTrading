/**
 * 
 */
package sigma.trading.news;

import java.util.Random;

import com.ib.client.Contract;
import com.ib.client.Order;
import com.ib.client.OrderType;
import com.ib.client.Types.Action;
import com.ib.client.Types.SecType;

import sigma.trading.Instrument;
import sigma.utils.Helper;
import sigma.utils.Logger;
import sigma.utils.OptSide;
import sigma.utils.TraderState;

/**
 * Instrument class for News Trading.
 * Extends general instrument.
 * 
 * @author Peeter Meos
 * @version 0.2
 *
 */
public class NewsInstrument extends Instrument {
	// State
	TraderState state;
	
	// Orders
    private Order longStop;
    private Order shortStop;
    private Order longTrail;
    private Order shortTrail;
    
    // Prices and parameters
    private double delta = 0;
    private double adjLimit = 0.02;
    private double trailAmt = 0;
    private double q = 0;
    
    private int oid = 0;
    
    /**
     * Constructor for news trader instrument class.
     * 
     * @param m_symbol
     * @param m_secType
     * @param m_exchange
     * @param m_expiry
     * @param q
     * @param delta
     * @param trailAmt
     * @param adjLimit
     */
    public NewsInstrument(String m_symbol, String m_secType, String m_exchange, String m_expiry,
    				  int q, double delta, double trailAmt, double adjLimit) {
    	super(m_symbol, m_secType, m_exchange, m_expiry);
    	
    	// Initialise trading parameters
    	this.q = q;
    	this.delta = delta;
    	this.trailAmt = trailAmt;
    	this.adjLimit = adjLimit;
    	
    	inst = new Contract();
    	inst.symbol(m_symbol);

    	switch(m_secType) {
    	case "FUT":
    		inst.secType(SecType.FUT);
    		break;
    	case "STK":
    		inst.secType(SecType.STK);
    		break;
    	case "FOP":
    		inst.secType(SecType.FOP);
    		break;
        default:
        	inst.secType(SecType.STK);
    	}
    	
    	inst.exchange(m_exchange);
    	inst.lastTradeDateOrContractMonth(m_expiry);
    	
    	// id = (int) (Math.round(Math.random() * 1000));
    	id = new Random().nextInt(1000);
    	state = TraderState.WAIT;
    }
    
    /**
     * Constructor for news trader instrument class.
     * 
     * @param iD
     * @param m_symbol
     * @param m_secType
     * @param m_exchange
     * @param m_expiry
     * @param q
     * @param delta
     * @param trailAmt
     * @param adjLimit
     */
    public NewsInstrument(int iD, String m_symbol, String m_secType, String m_exchange, String m_expiry,
    		int q, double delta, double trailAmt, double adjLimit) {
    	this(m_symbol, m_secType, m_exchange, m_expiry, q, delta, trailAmt, adjLimit);
    	this.id = iD;  	
    } 
    
    /**
     * Constructor for news trader instrument class.
     * 
     * @param m_symbol
     * @param m_secType
     * @param m_exchange
     * @param m_expiry
     * @param m_strike
     * @param m_side
     * @param q
     * @param delta
     * @param trailAmt
     * @param adjLimit
     */
    public NewsInstrument(String m_symbol, String m_secType, String m_exchange, String m_expiry, Double m_strike, OptSide m_side,
    		int q, double delta, double trailAmt, double adjLimit) {
    	super(m_symbol, m_secType, m_exchange, m_expiry, m_strike, m_side);
    
    	// Initialise trading parameters
    	this.q = q;
    	this.delta = delta;
    	this.trailAmt = trailAmt;
    	this.adjLimit = adjLimit;
    	
    	inst = new Contract();
    	inst.symbol(m_symbol);
    	switch(m_secType) {
    	case "FUT":
    		inst.secType(SecType.FUT);
    		break;
    	case "STK":
    		inst.secType(SecType.STK);
    		break;
    	case "FOP":
    		inst.secType(SecType.FOP);
    		break;
        default:
        	inst.secType(SecType.STK);
    	}
    	
    	inst.exchange(m_exchange);
    	inst.lastTradeDateOrContractMonth(m_expiry);
    	inst.strike(m_strike);
    	inst.right(m_side.toString());
    	
    	//id = (int) (Math.round(Math.random() * 1000));
    	id = new Random().nextInt(1000);
    	state = TraderState.WAIT;
    }
    
    /**
     * Constructor for news trader instrument class.
     * 
     * @param iD
     * @param m_symbol
     * @param m_secType
     * @param m_exchange
     * @param m_expiry
     * @param m_strike
     * @param m_side
     * @param q
     * @param delta
     * @param trailAmt 
     * @param adjLimit
     */
    public NewsInstrument(int iD, String m_symbol, String m_secType, String m_exchange, String m_expiry, Double m_strike, OptSide m_side,
    		int q, double delta, double trailAmt, double adjLimit) {
    	this(m_symbol, m_secType, m_exchange, m_expiry, m_strike, m_side, q, delta, trailAmt, adjLimit);
    	this.id = iD;
    }
    
    /**
     * Creates order set for the instrument and submits them via API
     * 
     * @param con Connector to TWS
     */
    public void createOrders(Connector con) {
    	Logger logger = new Logger();
  	
    	logger.log("Requesting instrument data for " + this.getSymbol());
    	// First request data for the instrument
    	con.reqMktData(this.getID(), inst);
    	
    	// Wait for the data to arrive
    	logger.log("Waiting for data for " + this.getSymbol() + " with ticker id " + this.getID());
    	while (con.getPrice(id) <= 0) {
    		Helper.sleep(10);
    	}
    	last = con.getPrice(id);
    	
    	logger.log("Price for " + this.getSymbol() + " received: " + last);
    	    	
    	// Update order ID
    	oid = con.getOrderID();
    	con.reqId();
    	logger.log("Waiting for next order id"); 	
   		Helper.sleep(10);

    	oid = con.getOrderID();
    	logger.log("Order ID received");
    	
    	// Create orders
    	longStop = new Order();
    	shortStop = new Order();
    	longTrail = new Order();
    	shortTrail = new Order();
    	
    	// Update order details
    	longStop.action(Action.BUY);
    	longStop.orderType(OrderType.STP_LMT);
    	longStop.totalQuantity(q);
    	longStop.lmtPrice(last + delta);
    	longStop.auxPrice(last + delta);
    	longStop.outsideRth(true);
    	longStop.transmit(false);
    	longStop.orderId((int) oid);
    	
    	shortStop.action(Action.SELL);
    	shortStop.orderType(OrderType.STP_LMT);
    	shortStop.totalQuantity(q);
    	shortStop.lmtPrice(last - delta);
    	shortStop.auxPrice(last - delta);
    	shortStop.outsideRth(true);
    	shortStop.transmit(false);
    	shortStop.orderId((int) (oid + 1));
    	
    	longTrail.action(Action.SELL);
    	longTrail.orderType(OrderType.TRAIL_LIMIT);
    	longTrail.totalQuantity(q);
    	longTrail.trailStopPrice(last);
    	longTrail.auxPrice(trailAmt);
    	longTrail.parentId(longStop.orderId());
    	longTrail.outsideRth(true);
    	longTrail.transmit(true);
    	longTrail.orderId((int) (oid + 2));
    	
    	shortTrail.action(Action.BUY);
    	shortTrail.orderType(OrderType.TRAIL_LIMIT);
    	shortTrail.totalQuantity(q);
		shortTrail.trailStopPrice(last);
		shortTrail.auxPrice(trailAmt);
		shortTrail.parentId(shortStop.orderId());
    	shortTrail.outsideRth(true);
    	shortTrail.transmit(true);
    	shortTrail.orderId((int) (oid + 3));
    	
		// OCO groupings and attached orders
    	int ocaNum = new Random().nextInt(1000);
    	
    	longStop.ocaGroup("News" + inst.symbol() + ocaNum);
		shortStop.ocaGroup("News" + inst.symbol() + ocaNum);
    	
    	// Submit orders to TWS
    	con.placeOrder((int) (oid + 0), inst, longStop);
    	con.placeOrder((int) (oid + 1), inst, shortStop);
    	con.placeOrder((int) (oid + 2), inst, longTrail);
    	con.placeOrder((int) (oid + 3), inst, shortTrail);
    	
    	// Change state
    	state = TraderState.LIVE;
    }
    
    /**
     * Adjust order set to the market
     * 
     * @param con Connector to TWS
     */
    public void adjustOrders(Connector con) {
    	// Check if price differs enough
    	double spot = -1;
    	Logger logger = new Logger();
    	logger.log("Adjusting price for " + getSymbol());
    	
    	// Find last price
    	for (int i=0;i < con.prices.size(); i++) {
    		if (con.prices.get(i).getId() == id) {
    			spot = con.prices.get(i).getPrice();
    			
    		}
    	}
    	
    	// Is the difference significant?
    	if (Math.abs(spot - last) > getAdjLimit() && spot != -1) {
    		last = spot;
    		
        	// Update order details
        	longStop.lmtPrice(last + delta);
        	longStop.auxPrice(last + delta);
        	longStop.outsideRth(true);
        	longStop.transmit(true);
        	
        	shortStop.lmtPrice(last - delta);
        	shortStop.auxPrice(last - delta);
        	shortStop.outsideRth(true);
        	shortStop.transmit(true);
        	
        	longTrail.trailStopPrice(last);
        	longTrail.outsideRth(true);
        	longTrail.transmit(true);
        	
    		shortTrail.trailStopPrice(last);
        	shortTrail.outsideRth(true);
        	shortTrail.transmit(true);
    		
        	// Submit orders to TWS
        	con.placeOrder((int) (oid + 0), inst, longStop);
        	con.placeOrder((int) (oid + 1), inst, shortStop);
        	con.placeOrder((int) (oid + 2), inst, longTrail);
        	con.placeOrder((int) (oid + 3), inst, shortTrail);
    	}    	
    }
    
    /**
     * Processes trades, checks executions adjusts system state accordingly
     * 
     * @param con
     */
    public void processTrades(Connector con) {
    	// TODO not implemented
    }
    
	/**
	 * @return the delta
	 */
	public double getDelta() {
		return delta;
	}
	/**
	 * @param delta the delta to set
	 */
	public void setDelta(double delta) {
		this.delta = delta;
	}
	/**
	 * @return the adjLimit
	 */
	public double getAdjLimit() {
		return adjLimit;
	}
	/**
	 * @param adjLimit the adjLimit to set
	 */
	public void setAdjLimit(double adjLimit) {
		this.adjLimit = adjLimit;
	}
	/**
	 * @return the trailAmt
	 */
	public double getTrailAmt() {
		return trailAmt;
	}
	/**
	 * @param trailAmt the trailAmt to set
	 */
	public void setTrailAmt(double trailAmt) {
		this.trailAmt = trailAmt;
	}
	/**
	 * @return the q
	 */
	public double getQ() {
		return q;
	}
	/**
	 * @param q the q to set
	 */
	public void setQ(double q) {
		this.q = q;
	}
	
	/**
	 * 
	 * @return state 
	 */
	public TraderState getState() {
		return state;
	}
	
	/**
	 * Sets trader state
	 * @param s TraderState
	 */
	public void setState(TraderState s) {
		this.state = s;
	}

}
