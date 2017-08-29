package sigma.trading;

/**
 * 
 * @author Peeter Meos
 * @version 0.1
 *
 */
public class Instrument {
	private String symbol;
	private String secType;
	private String exchange;
	private String expiry;
	
	private double bid;
	private double ask;
	private double last;
	private double prvClose;
	private long secID;
	
	/**
	 * Constructor method for Instrument class
	 */
	public Instrument(String m_symbol, String m_secType, String m_exchange, String m_expiry) {
		this.symbol = m_symbol;
		this.secType = m_secType;
		this.exchange = m_exchange;
		this.expiry = m_expiry;
		
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
}
