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
    protected Order longStop;
    protected Order shortStop;
    protected Order longTrail;
    protected Order shortTrail;
    protected Order longTarget;
    protected Order shortTarget;
    
    // Prices and parameters
    protected double delta = 0;
    protected double adjLimit = 0.02;
    protected double trailAmt = 0;
    protected double target = 0;
    protected double q = 0;
    
    protected int oid = 0;
    
    /**
     * Constructor for news trader instrument class.
     * 
     * @param m_symbol Symbol string 
     * @param m_secType Security type string
     * @param m_exchange Exchange string
     * @param m_expiry Expiry date string
     * @param q Order quantity integer
     * @param delta Delta between trigger price and last price double
     * @param trailAmt Trailing amount for trail order double
     * @param target Profit taker target double
     * @param adjLimit Limit when order will be moved to match the market double
     */
    public NewsInstrument(String m_symbol, String m_secType, String m_exchange, String m_expiry,
    				  int q, double delta, double trailAmt, double target, double adjLimit) {
    	super(m_symbol, m_secType, m_exchange, m_expiry);
    	
    	// Initialise trading parameters
    	this.q = q;
    	this.delta = delta;
    	this.trailAmt = trailAmt;
    	this.adjLimit = adjLimit;
    	this.target = target;
    	
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
     * @param iD Instrument id integer
     * @param m_symbol Symbol string 
     * @param m_secType Security type string
     * @param m_exchange Exchange string
     * @param m_expiry Expiry date string
     * @param q Order quantity integer
     * @param delta Delta between trigger price and last price double
     * @param trailAmt Trailing amount for trail order double
     * @param target Profit taker target double
     * @param adjLimit Limit when order will be moved to match the market double
     */
    public NewsInstrument(int iD, String m_symbol, String m_secType, String m_exchange, String m_expiry,
    		int q, double delta, double trailAmt, double target, double adjLimit) {
    	this(m_symbol, m_secType, m_exchange, m_expiry, q, delta, trailAmt, target, adjLimit);
    	this.id = iD;  	
    } 
    
    /**
     * Constructor for news trader instrument class.
     * 
     * @param m_symbol Symbol string 
     * @param m_secType Security type string
     * @param m_exchange Exchange string
     * @param m_expiry Expiry date string
     * @param m_strike Option strike double
     * @param m_side Option side OptSide
     * @param q Order quantity integer
     * @param delta Delta between trigger price and last price double
     * @param trailAmt Trailing amount for trail order double
     * @param target Profit taker target double
     * @param adjLimit Limit when order will be moved to match the market double
     */
    public NewsInstrument(String m_symbol, String m_secType, String m_exchange, String m_expiry, Double m_strike, OptSide m_side,
    		int q, double delta, double trailAmt, double target, double adjLimit) {
    	super(m_symbol, m_secType, m_exchange, m_expiry, m_strike, m_side);
    
    	// Initialise trading parameters
    	this.q = q;
    	this.delta = delta;
    	this.trailAmt = trailAmt;
    	this.adjLimit = adjLimit;
    	this.target = target;
    	
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
    	
    	id = new Random().nextInt(1000);
    	state = TraderState.WAIT;
    }
    
    /**
     * Constructor for news trader instrument class.
     * 
     * @param iD Instrument id integer
     * @param m_symbol Symbol string 
     * @param m_secType Security type string
     * @param m_exchange Exchange string
     * @param m_expiry Expiry date string
     * @param m_strike Option strike double
     * @param m_side Option side OptSide
     * @param q Order quantity integer
     * @param delta Delta between trigger price and last price double
     * @param trailAmt Trailing amount for trail order double
     * @param target Profit taker target double
     * @param adjLimit Limit when order will be moved to match the market double
     */
    public NewsInstrument(int iD, String m_symbol, String m_secType, String m_exchange, String m_expiry, Double m_strike, OptSide m_side,
    		int q, double delta, double trailAmt, double target, double adjLimit) {
    	this(m_symbol, m_secType, m_exchange, m_expiry, m_strike, m_side, q, delta, trailAmt, target, adjLimit);
    	this.id = iD;
    }
    
    /**
     * Creates order set for the instrument and submits them via API
     * 
     * @param con Connector to TWS
     */
    public void createOrders(Connector con) {
 	
    	con.log("Requesting instrument data for " + this.getSymbol());
    	// First request data for the instrument
    	con.reqMktData(this.getID(), inst);
    	
    	// Wait for the data to arrive
    	con.log("Waiting for data for " + this.getSymbol() + " with ticker id " + this.getID());
    	while (con.getPrice(id) <= 0) {
    		Helper.sleep(10);
    	}
    	last = con.getPrice(id);
    	
    	con.log("Price for " + this.getSymbol() + " received: " + last);
    	    	
    	// Update order ID
    	oid = con.getOrderID();
    	con.reqId();
    	con.log("Waiting for next order id"); 	
   		Helper.sleep(10);

    	oid = con.getOrderID();
    	con.log("Order ID received");
    	
    	// Create orders
    	longStop = new Order();
    	shortStop = new Order();
    	longTrail = new Order();
    	shortTrail = new Order();
    	longTarget = new Order();
    	shortTarget = new Order();
    	
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

    	if (target > 0) {
	    	longTarget.action(Action.SELL);
	    	longTarget.orderType(OrderType.LMT);
	    	longTarget.totalQuantity(q);
	    	longTarget.lmtPrice(last + target);
	    	longTarget.parentId(longStop.orderId());
	    	longTarget.outsideRth(true);
	    	longTarget.transmit(false);
	    	longTarget.orderId((int) (oid + 4));
	    	
	    	shortTarget.action(Action.BUY);
	    	shortTarget.orderType(OrderType.LMT);
	    	shortTarget.totalQuantity(q);
	    	shortTarget.lmtPrice(last - target);
	    	shortTarget.parentId(shortStop.orderId());
	    	shortTarget.outsideRth(true);
	    	shortTarget.transmit(false);
	    	shortTarget.orderId((int) (oid + 5));
    	}
    	
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
		
		if (target > 0) {
	    	longTarget.ocaGroup("News" + inst.symbol() + ocaNum + 1);
			longTrail.ocaGroup("News" + inst.symbol() + ocaNum + 1);
			
	    	shortTarget.ocaGroup("News" + inst.symbol() + ocaNum + 2);
			shortTrail.ocaGroup("News" + inst.symbol() + ocaNum + 2);
		}
    	
    	// Submit orders to TWS
    	con.placeOrder((int) (oid + 0), inst, longStop);
    	con.placeOrder((int) (oid + 1), inst, shortStop);
    	
    	if (target > 0) {
        	con.placeOrder((int) (oid + 4), inst, longTarget);
        	con.placeOrder((int) (oid + 5), inst, shortTarget);    		
    	}
    	
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

    	con.log("Adjusting price for " + getSymbol());
    	
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
        	
        	if (target > 0 ) {
        		longTarget.lmtPrice(last + target);
        		shortTarget.lmtPrice(last + target);
        		longTarget.transmit(true);
        		longTarget.transmit(true);
        	}
    		
        	// Submit orders to TWS
        	con.placeOrder((int) (oid + 0), inst, longStop);
        	con.placeOrder((int) (oid + 1), inst, shortStop);
        	con.placeOrder((int) (oid + 2), inst, longTrail);
        	con.placeOrder((int) (oid + 3), inst, shortTrail);
        	
        	if (target > 0 ) {
            	con.placeOrder((int) (oid + 4), inst, longTarget);
            	con.placeOrder((int) (oid + 5), inst, shortTarget);
        	}
    	}    	
    }
    
    /**
     * Processes trades, checks executions adjusts system state accordingly
     * 
     * @param con Connector for TWS API
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
	 * Returns order quantity
	 * @return  q double
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
	 * Returns target amount
	 * @return target double
	 */
	public double getTarget() {
		return(this.target);
	}
	
	/**
	 * Sets profit target value
	 * @param tgt Profit target
	 */
	public void setTarget(double tgt) {
		this.target = tgt;
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
	
	/**
	 * Returns longStop order
	 * @return Order longStop
	 */
	public Order getLongStop() {
		return(longStop);
	}
	
	/**
	 * Returns shortStop order
	 * @return Order shortStop
	 */
	public Order getShortStop() {
		return(shortStop);
	}
	
	/**
	 * Returns longTrail order
	 * @return Order longTrail
	 */
	public Order getLongTrail() {
		return(longTrail);
	}
	
	/**
	 * Returns shortTrail order
	 * @return Order shortTrail
	 */
	public Order getShortTrail() {
		return(shortTrail);
	}
	
	/**
	 * Returns longTarget order
	 * @return Order longTrail
	 */
	public Order getLongTarget() {
		return(longTarget);
	}
	
	/**
	 * Returns shortTarget order
	 * @return Order shortTarget
	 */
	public Order getShortTarget() {
		return(shortTarget);
	}

}
