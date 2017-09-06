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
 * @author Peeter Meos
 * @version 0.1
 *
 */
public class Instrument extends sigma.trading.Instrument {
    // Contract structure
	private Contract inst = null;
	
	private long id;
   
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
    
    /** 
     * Standard constructors for the news trader instrument class
     */
    public Instrument(String m_symbol, String m_secType, String m_exchange, String m_expiry) {
    	super(m_symbol, m_secType, m_exchange, m_expiry);
    	
    	inst = new Contract();
    	inst.symbol(m_symbol);
    	inst.secType(m_secType);
    	inst.exchange(m_exchange);
    	inst.lastTradeDateOrContractMonth(m_expiry);
    	
    	id = Math.round(Math.random() * 1000);
    }
    
    public Instrument(int iD, String m_symbol, String m_secType, String m_exchange, String m_expiry) {
    	this(m_symbol, m_secType, m_exchange, m_expiry);
    	this.id = iD;
    	
    	inst = new Contract();
    	
    	longStop = new Order();
    	shortStop = new Order();
    	longTrail = new Order();
    	shortTrail = new Order();
    } 
    
    public Instrument(String m_symbol, String m_secType, String m_exchange, String m_expiry, Double m_strike, OptSide m_side) {
    	super(m_symbol, m_secType, m_exchange, m_expiry, m_strike, m_side);
    	
    	inst = new Contract();
    	inst.symbol(m_symbol);
    	inst.secType(m_secType);
    	inst.exchange(m_exchange);
    	inst.lastTradeDateOrContractMonth(m_expiry);
    	inst.strike(m_strike);
    	inst.right(m_side.toString());
    	
    	id = Math.round(Math.random() * 1000);
    }
    
    public Instrument(int iD, String m_symbol, String m_secType, String m_exchange, String m_expiry, Double m_strike, OptSide m_side) {
    	this(m_symbol, m_secType, m_exchange, m_expiry, m_strike, m_side);
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
    	longStop.outsideRth(true);
    	longStop.transmit(false);
    	
    	shortStop.action(Action.SELL);
    	shortStop.orderType(OrderType.STP_LMT);
    	shortStop.totalQuantity(q);
    	shortStop.outsideRth(true);
    	shortStop.transmit(false);
    	
    	longTrail.action(Action.SELL);
    	longTrail.orderType(OrderType.TRAIL_LIMIT);
    	longTrail.totalQuantity(q);
    	longTrail.outsideRth(true);
    	longTrail.transmit(true);
    	
    	shortTrail.action(Action.BUY);
    	shortTrail.orderType(OrderType.TRAIL_LIMIT);
    	shortTrail.totalQuantity(q);
    	shortTrail.outsideRth(true);
    	shortTrail.transmit(true);
    	
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


	/**
	 * @return the iD
	 */
	public long getID() {
		return id;
	}

	/**
	 * @param iD the iD to set
	 */
	public void setID(int iD) {
		id = iD;
	}
}
