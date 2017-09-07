package sigma.trading;

import sigma.utils.OptSide;
import com.ib.client.Contract;

/**
 * 
 * @author Peeter Meos
 * @version 0.3
 *
 */
public class Instrument {
	private String symbol;
	private String secType;
	private String ulType;
	private String exchange;
	private String expiry;
	private Double strike;
	private OptSide side;
	
	protected Contract inst;
	private Contract ul;
	
	protected double bid = -1;
	protected double ask = -1;
	protected double last = -1;
	
	private double prvClose;
	
	protected int id;
	
	/**
	 * Constructor method for Instrument class for stocks and futures
	 */
	public Instrument(String m_symbol, String m_secType, String m_exchange, String m_expiry) {
		this.symbol = m_symbol;
		this.secType = m_secType;
		this.setUlType("");
		this.exchange = m_exchange;
		this.expiry = m_expiry;
		this.setStrike(0.0);
		this.setSide(OptSide.NONE);
		
		this.setInst(new Contract());
		this.setUl(null);
		
		this.bid = 0;
		this.ask = 0;
		this.last = 0;
		this.prvClose = 0;	
	}
	
	/**
	 * Constructor method for Instrument class for options
	 */
	public Instrument(String m_symbol, String m_secType, String m_exchange, String m_expiry, Double m_strike, OptSide m_side) {
		this.symbol = m_symbol;
		this.secType = m_secType;
		this.exchange = m_exchange;
		this.expiry = m_expiry;
		this.setStrike(m_strike);
		this.setSide(m_side);

		switch(this.secType) {
		case "FOP":
			this.setUlType("FUT");
			this.setUl(new Contract());
			break;
		case "OPT":
			this.setUlType("STK");
			this.setUl(new Contract());
			break;
		default:
			this.setUlType("");
			this.setUl(null);
		}
		
		this.setInst(new Contract());
		
		this.bid = 0;
		this.ask = 0;
		this.last = 0;
		this.prvClose = 0;
		
	}
	

	/**
	 * @return the prvClose
	 */
	public double getPrvClose() {
		return prvClose;
	}
	
	/**
	 * @param prvClose the prvClose to set
	 */
	public void setPrvClose(double prvClose) {
		this.prvClose = prvClose;
	}

	/**
	 * @return the last
	 */
	public double getLast() {
		return last;
	}

	/**
	 * @param last the last to set
	 */
	public void setLast(double last) {
		this.last = last;
	}

	/**
	 * @return the ask
	 */
	public double getAsk() {
		return ask;
	}

	/**
	 * @param ask the ask to set
	 */
	public void setAsk(double ask) {
		this.ask = ask;
	}

	/**
	 * @return the bid
	 */
	public double getBid() {
		return bid;
	}

	/**
	 * @param bid the bid to set
	 */
	public void setBid(double bid) {
		this.bid = bid;
	}

	/**
	 * @return the symbol
	 */
	public String getSymbol() {
		return symbol;
	}

	/**
	 * @param symbol the symbol to set
	 */
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	/**
	 * @return the secID
	 */
	public int getID() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setID(int id) {
		this.id = id;
	}

	/**
	 * @return the secType
	 */
	public String getSecType() {
		return secType;
	}

	/**
	 * @param secType the secType to set
	 */
	public void setSecType(String secType) {
		this.secType = secType;
	}

	/**
	 * @return the exchange
	 */
	public String getExchange() {
		return exchange;
	}

	/**
	 * @param exchange the exchange to set
	 */
	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

	/**
	 * @return the expiry
	 */
	public String getExpiry() {
		return expiry;
	}

	/**
	 * @param expiry the expiry to set
	 */
	public void setExpiry(String expiry) {
		this.expiry = expiry;
	}

	/**
	 * @return the side
	 */
	public OptSide getSide() {
		return side;
	}

	/**
	 * @param side the side to set
	 */
	public void setSide(OptSide side) {
		this.side = side;
	}

	/**
	 * @return the strike
	 */
	public Double getStrike() {
		return strike;
	}

	/**
	 * @param strike the strike to set
	 */
	public void setStrike(Double strike) {
		this.strike = strike;
	}

	/**
	 * @return the instrument
	 */
	public Contract getInst() {
		return inst;
	}

	/**
	 * @param inst the instrument to set
	 */
	public void setInst(Contract inst) {
		this.inst = inst;
	}

	/**
	 * @return the underlying
	 */
	public Contract getUl() {
		return ul;
	}

	/**
	 * @param ul the underlying to set
	 */
	public void setUl(Contract ul) {
		this.ul = ul;
	}

	/**
	 * @return the ulType
	 */
	public String getUlType() {
		return ulType;
	}

	/**
	 * @param ulType the ulType to set
	 */
	public void setUlType(String ulType) {
		this.ulType = ulType;
	}
}
