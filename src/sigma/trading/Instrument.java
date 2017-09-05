package sigma.trading;

import sigma.utils.OptSide;
import com.ib.client.Contract;

/**
 * 
 * @author Peeter Meos
 * @version 0.2
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
	
	private Contract inst;
	private Contract ul;
	
	private double bid;
	private double ask;
	private double last;
	private double prvClose;
	private long secID;
	
	/**
	 * Constructor method for Instrument class for stocks and futures
	 */
	public Instrument(String m_symbol, String m_secType, String m_exchange, String m_expiry) {
		this.symbol = m_symbol;
		this.secType = m_secType;
		this.ulType = "";
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
			this.ulType = "FUT";
			this.setUl(new Contract());
			break;
		case "OPT":
			this.ulType = "STK";
			this.setUl(new Contract());
			break;
		default:
			this.ulType = "";
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
	public long getSecID() {
		return secID;
	}

	/**
	 * @param secID the secID to set
	 */
	public void setSecID(long secID) {
		this.secID = secID;
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
	 * @return the inst
	 */
	public Contract getInst() {
		return inst;
	}

	/**
	 * @param inst the inst to set
	 */
	public void setInst(Contract inst) {
		this.inst = inst;
	}

	/**
	 * @return the ul
	 */
	public Contract getUl() {
		return ul;
	}

	/**
	 * @param ul the ul to set
	 */
	public void setUl(Contract ul) {
		this.ul = ul;
	}
}
