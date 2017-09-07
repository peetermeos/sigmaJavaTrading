/**
 * 
 */
package sigma.trading.news;

import com.ib.client.Contract;
import com.ib.client.Order;
import com.ib.client.OrderType;
import com.ib.client.Types.Action;

import sigma.utils.Helper;
import sigma.utils.OptSide;

/**
 * Instrument class for News Trading.
 * Extends general instrument.
 * 
 * @author Peeter Meos
 * @version 0.2
 *
 */
public class Instrument extends sigma.trading.Instrument {
	  
	// Orders
    private Order longStop;
    private Order shortStop;
    private Order longTrail;
    private Order shortTrail;
    
    // Prices and parameters
    private double currentSpot = -1;
    private double delta = 0;
    private double adjLimit = 0.02;
    private double trailAmt = 0;
    private double q = 0;
    
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
    public Instrument(String m_symbol, String m_secType, String m_exchange, String m_expiry,
    				  int q, double delta, double trailAmt, double adjLimit) {
    	super(m_symbol, m_secType, m_exchange, m_expiry);
    	
    	// Initialise trading parameters
    	this.q = q;
    	this.delta = delta;
    	this.trailAmt = trailAmt;
    	this.adjLimit = adjLimit;
    	
    	inst = new Contract();
    	inst.symbol(m_symbol);
    	inst.secType(m_secType);
    	inst.exchange(m_exchange);
    	inst.lastTradeDateOrContractMonth(m_expiry);
    	
    	id = (int) (Math.round(Math.random() * 1000));
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
    public Instrument(int iD, String m_symbol, String m_secType, String m_exchange, String m_expiry,
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
    public Instrument(String m_symbol, String m_secType, String m_exchange, String m_expiry, Double m_strike, OptSide m_side,
    		int q, double delta, double trailAmt, double adjLimit) {
    	super(m_symbol, m_secType, m_exchange, m_expiry, m_strike, m_side);
    
    	// Initialise trading parameters
    	this.q = q;
    	this.delta = delta;
    	this.trailAmt = trailAmt;
    	this.adjLimit = adjLimit;
    	
    	inst = new Contract();
    	inst.symbol(m_symbol);
    	inst.secType(m_secType);
    	inst.exchange(m_exchange);
    	inst.lastTradeDateOrContractMonth(m_expiry);
    	inst.strike(m_strike);
    	inst.right(m_side.toString());
    	
    	id = (int) (Math.round(Math.random() * 1000));
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
    public Instrument(int iD, String m_symbol, String m_secType, String m_exchange, String m_expiry, Double m_strike, OptSide m_side,
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
  	
    	// First request data for the instrument
    	con.reqMktData(inst);
    	
    	// Wait for the data to arrive
    	while (currentSpot < 0) {
    		Helper.sleep(100);
    	}
    	    	
    	// Create orders
    	longStop = new Order();
    	shortStop = new Order();
    	longTrail = new Order();
    	shortTrail = new Order();
    	
    	// TODO Prices are missing
    	longStop.action(Action.BUY);
    	longStop.orderType(OrderType.STP_LMT);
    	longStop.totalQuantity(q);
    	longStop.lmtPrice(last + delta);
    	longStop.auxPrice(last + delta);
    	longStop.outsideRth(true);
    	longStop.transmit(false);
    	longStop.orderId((int) id);
    	
    	shortStop.action(Action.SELL);
    	shortStop.orderType(OrderType.STP_LMT);
    	shortStop.totalQuantity(q);
    	shortStop.lmtPrice(last - delta);
    	shortStop.auxPrice(last - delta);
    	shortStop.outsideRth(true);
    	shortStop.transmit(false);
    	longStop.orderId((int) (id + 1));
    	
    	longTrail.action(Action.SELL);
    	longTrail.orderType(OrderType.TRAIL_LIMIT);
    	longTrail.totalQuantity(q);
    	longTrail.trailStopPrice(last);
    	longTrail.auxPrice(trailAmt);
    	longTrail.parentId(shortStop.orderId());
    	longTrail.outsideRth(true);
    	longTrail.transmit(true);
    	longStop.orderId((int) (id + 2));
    	
    	shortTrail.action(Action.BUY);
    	shortTrail.orderType(OrderType.TRAIL_LIMIT);
    	shortTrail.totalQuantity(q);
		shortTrail.trailStopPrice(last);
		shortTrail.auxPrice(trailAmt);
		shortTrail.parentId(shortStop.orderId());
    	shortTrail.outsideRth(true);
    	shortTrail.transmit(true);
    	longStop.orderId((int) (id + 3));
    	
    	// Submit orders to TWS
    	con.placeOrder((int) (id + 0), inst, longStop);
    	con.placeOrder((int) (id + 1), inst, shortStop);
    	con.placeOrder((int) (id + 2), inst, longTrail);
    	con.placeOrder((int) (id + 3), inst, shortTrail);
    }
    
    /**
     * Adjust order set to the market
     * 
     * @param con Connector to TWS
     */
    public void adjustOrders(Connector con) {
    	// To be implemented
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

}
