/**
 * 
 */
package sigma.utils;

/**
 * @author Peeter Meos
 * @version 0.1
 *
 */
public class Ticker {
	private double price;
	private double bid;
	private double ask;
	
	private int id;
	
	/**
	 * Simple constructor.
	 * @param id
	 * @param price
	 */
	public Ticker(int id, double price) {
		this.price = price;
		this.id = id;
	}
	
	/**
	 * @return the price
	 */
	public double getPrice() {
		return price;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(double price) {
		this.price = price;
	}
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
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
}
