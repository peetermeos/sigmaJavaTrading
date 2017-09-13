package sigma.utils;

import sigma.utils.OptSide;
import cern.jet.stat.tdouble.Probability;

/**
 * Simple Black-Scholes implementation of option Greeks.
 * Includes also selection of third order Greeks.
 * 
 * @author Peeter Meos
 * @version 0.2
 *
 */
public class Option {
	private int id; // ID sequence
	private double sigma; // Volatility
	private double K; // Strike
	private double S; // Spot
	private double t; // Maturity
	private double r; // Risk free rate
	private double q; // Annual dividend yield
	private String expiry;
	private OptSide side;
	
	private double price;
	private double delta;
	private double gamma;
	private double theta;
	private double vega;
	
	/**
	 * Default constructor for the option
	 */
	public Option() {
		this.r = 1.0;
		this.q = 0.0;
		this.sigma = 0;
	}
	
	/**
	 * Constructor that assumes that risk free rates and dividend rates are zero
	 * @param id
	 * @param strike
	 * @param spot
	 * @param expiry
	 * @param side
	 */
	public Option(int id, double strike, double spot, String expiry, OptSide side) {
		this.K = strike;
		this.S = spot;
		this.side = side;
		this.expiry = expiry;
		this.id = id;
		this.q = 0;
		this.r = 0;
	}
	
	/**
	 * Constructor that assumes that risk free rates and dividend rates are zero
	 * @param strike
	 * @param spot
	 * @param maturity
	 * @param side
	 */
	public Option(double strike, double spot, double maturity, OptSide side) {
		this.K = strike;
		this.S = spot;
		this.side = side;
		this.t = maturity;
		this.q = 0;
		this.r = 0;
	}
	
	/**
	 * Full constructor for an option
	 * @param strike
	 * @param spot
	 * @param maturity
	 * @param side
	 * @param q
	 * @param r
	 */
	public Option(double strike, double spot, double maturity, OptSide side, double q, double r) {
		this.K = strike;
		this.S = spot;
		this.side = side;
		this.t = maturity;
		this.r = r;
		this.q = q;
	}

	/**
	 * Classical vanilla Black Scholes formula d2 calculation
	 *  d_1 = \frac{\ln(S/K) + (r - q + \sigma^2/2)\tau}{\sigma\sqrt{\tau}} 
	 * @return Black Scholes D1 value
	 */
	public double d1() {
		return((Math.log(this.S / this.K) + (this.r + this.q + (this.sigma * this.sigma) / 2) + this.t) 
				/ (this.sigma * Math.sqrt(this.t)));
	}
	
	/**
	 * Classical vanilla Black Scholes formula d2 calculation
	 *  d_2 = \frac{\ln(S/K) + (r - q - \sigma^2/2)\tau}{\sigma\sqrt{\tau}} = d_1 - \sigma\sqrt{\tau} 
	 * @return Black Scholes D2 value
	 */
	public double d2() {
		return(this.d1() - this.sigma * Math.sqrt(this.t));
	}

	/**
	 * phi function for classical Black Scholes
	 *  \phi(x) = \frac{e^{- \frac{x^2}{2}}}{\sqrt{2 \pi}} 
	 * @return
	 */
	public double phi(double x) {
		return((Math.exp(-(x * x)/2)) / (Math.sqrt(2 * Math.PI)));
	}
	
	/**
	 * Cumulative normal distribution function
	 *  \Phi(x) = \frac{1}{\sqrt{2\pi }} \int_{-\infty}^x e^{- \frac{y^2}{2}} \,dy =1- \frac{1}{\sqrt{2\pi }} \int_{x}^\infty e^{- \frac{y^2}{2}} \,dy
	 * @return
	 */
	public double Phi(double x) {
		return(Probability.normal(0, 1, x));
	}
	
	/**
	 * Calculates vanilla Black Scholes option value
	 * 
	 * Call:   Se^{-q \tau}\Phi(d_1) - e^{-r \tau} K\Phi(d_2) \, 
	 * Put:    e^{-r \tau} K\Phi(-d_2) -  Se^{-q \tau}\Phi(-d_1)  \,
	 *  
	 * @return Vanilla Black-Scholes option value
	 */
	public double value() {
		switch(this.side) {
		case CALL:
			return( this.S * Math.exp(-this.q * this.t) * Phi(-d1())
					- Math.exp(-this.r * this.t) * this.K * Phi(-this.d2())); 
		default:
			return(Math.exp(-this.r * this.t) * this.K * Phi(-this.d2()) 
					- this.S * Math.exp(-this.q * this.t) * Phi(-d1()));
		}
	}

	/**
	 * Call:   e^{-q \tau} \Phi(d_1) \, 
	 * Put:  -e^{-q \tau} \Phi(-d_1)\, 
	 * @return
	 */
	public double delta() {
		switch(side) {
		case CALL:
			return(Math.exp(-this.q * this.t) * Phi(d1()));
		default:
			return(-Math.exp(-this.q * this.t) * Phi(-d1()));		
		}
	}
	
	/**
	 * Calculates vanilla Black Scholes gamma value for the option
	 * 
	 * e^{-q\tau }{\frac {\phi (d_{1})}{S\sigma {\sqrt {\tau }}}}=Ke^{-r\tau }{\frac {\phi (d_{2})}{S^{2}\sigma {\sqrt {\tau }}}}
	 * 
	 * @return gamma value of the option
	 */
	public double gamma() {
		return(Math.exp(-this.q * this.t) * (phi(this.d1())) / (this.S * this.sigma * Math.sqrt(this.t)));
	}
	
	/**
	 * Calculates time decay theta of vanilla Black Scholes option
	 * 
	 * Call: {\displaystyle -e^{-q\tau }{\frac {S\phi (d_{1})\sigma }{2{\sqrt {\tau }}}}-rKe^{-r\tau }\Phi (d_{2})+qSe^{-q\tau }\Phi (d_{1})\,}
	 * Put: {\displaystyle -e^{-q\tau }{\frac {S\phi (d_{1})\sigma }{2{\sqrt {\tau }}}}+rKe^{-r\tau }\Phi (-d_{2})-qSe^{-q\tau }\Phi (-d_{1})\,}
	 *  
	 * @return theta time decay of the vanilla Black Scholes option
	 */
	public double theta() {
		double d = -Math.exp(-this.q * this.t) * (this.S * phi(this.d1()) * this.sigma) / (2 * Math.sqrt(this.t));
		double dk  = this.r * this.K * Math.exp(-this.r * this.t) * Phi(this.d2());
		double ds = this.q * this.S * Math.exp(-this.q * this.t) * Phi(this.d1());
		
		switch(side) {
		case CALL:
			return(d - dk + ds);
		default:
			return(d + dk - ds);
		}
	}
	
	/**
	 * Returns vanilla Black Scholes vega value
	 *  S e^{-q \tau} \phi(d_1) \sqrt{\tau} = K e^{-r \tau} \phi(d_2) \sqrt{\tau} 
	 * @return vanilla Black Scholes vega value
	 */
	public double vega() {
		return(this.S * Math.exp(-this.q * this.t) * phi(this.d1()) * Math.sqrt(this.t) );
	}
	
	/**
	 * Calculates delta decay (change of delta over time) for vanilla Black Scholes option
	 * 
	 * Call:  qe^{-q \tau} \Phi(d_1) - e^{-q \tau} \phi(d_1) \frac{2(r-q) \tau - d_2 \sigma \sqrt{\tau}}{2\tau \sigma \sqrt{\tau}} \, 
	 * Put:  -qe^{-q \tau} \Phi(-d_1) - e^{-q \tau} \phi(d_1) \frac{2(r-q) \tau - d_2 \sigma \sqrt{\tau}}{2\tau \sigma \sqrt{\tau}} \,
	 *  
	 * @return Delta decay (change of delta over passage of time)
	 */
	public double charm() {
		double d = Math.exp(-this.q * this.t) * phi(this.d1()) * (2 * (this.r - this.q) * this.t - this.d2() * this.sigma * Math.sqrt(this.t)) / (2 * this.t * this.sigma * Math.sqrt(this.t));
		
		switch(side) {
		case CALL:
			return(this.q * Math.exp(-this.q * this.t) * Phi(this.d1()) - d);
		default:
			return(-this.q * Math.exp(-this.q * this.t) * Phi(-this.d1())- d);
		}
	}
	
	/**
	 * Calculates change of theta over passage of time for vanilla Black Scholes option
	 * 
	 * Call: -\frac {S\sigma \phi (d_{1})}{4\tau {\sqrt {\tau }}}}\left[1+{\frac {2(r-q)\tau -d_{2}\sigma {\sqrt {\tau }}}{\sigma {\sqrt {\tau }}}}d_{1}\right]-r^{2}Ke^{-r\tau }\Phi (d_{2})\\+q^{2}Se^{-q\tau }\Phi (d_{1})+Se^{-q\tau }\phi (d_{1}){\frac {2(r-q)^{2}\tau -\sigma {\sqrt {\tau }}(rd_{1}-qd_{2})}{2\sigma \tau {\sqrt {\tau }}}}\end{aligned}}\,}
	 * Put: -{\frac {S\sigma \phi (d_{1})}{4\tau {\sqrt {\tau }}}}\left[1+{\frac {2(r-q)\tau -d_{2}\sigma {\sqrt {\tau }}}{\sigma {\sqrt {\tau }}}}d_{1}\right]+r^{2}Ke^{-r\tau }\Phi (-d_{2})\\-q^{2}Se^{-q\tau }\Phi (-d_{1})+Se^{-q\tau }\phi (d_{1}){\frac {2(r-q)^{2}\tau -\sigma {\sqrt {\tau }}(rd_{1}-qd_{2})}{2\sigma \tau {\sqrt {\tau }}}}\end{aligned}}\,}
	 * 
	 * @return Thega (change of theta over time) for an option
	 */
	public double thega() {
		double d = -(this.S * this.sigma * phi(this.d1())) / (4 * this.t * Math.sqrt(this.t));
		double di = 1 + (2 * (this.r - this.q) * this.t - this.d2() * this.sigma * Math.sqrt(this.t)) / (this.sigma * Math.sqrt(this.t)) * this.d1();
		double dk = this.r * this.r * this.K * Math.exp(-this.r * this.t) + Phi(this.d2());
		double ds = this.q * this.q * this.S * Math.exp(-this.q * this.t) * Phi(this.d1());
		double ds1 = this.S * Math.exp(-this.q * this.t) * Phi(this.d1()) * (2 * (this.r-this.q)*(this.r-this.q) * this.t - this.d2() * this.sigma * Math.sqrt(this.t)) / (2 * this.sigma * this.t * Math.sqrt(this.t));
		
		switch(side) {
		case CALL:
			return(-d * di - dk + ds + ds1);
		default:
			return(-d * di + dk - ds + ds1);
		}
	}

	/**
	 * Calculates speed of gamma (change of gamma over underlying price)
	 *  -e^{-q \tau} \frac{\phi(d_1)}{S^2 \sigma \sqrt{\tau}} \left(\frac{d_1}{\sigma \sqrt{\tau}} + 1\right) = -\frac{\Gamma}{S}\left(\frac{d_1}{\sigma\sqrt{\tau}}+1\right) \, 
	 * @return speed
	 */
	public double speed() {
		return(-Math.exp(-this.q * this.t) * (phi(this.d1())) / (this.S * this.S * this.sigma * Math.sqrt(this.t)) *
				(this.d1() / (this.sigma * Math.sqrt(this.t)) + 1));
	}
	
	/**
	 * Calculates gamma decay (change of gamma over time) for vanilla Black-Scholes option
	 * 
	 *  -e^{-q \tau} \frac{\phi(d_1)}{2S\tau \sigma \sqrt{\tau}} \left[2q\tau + 1 + \frac{2(r-q) \tau - d_2 \sigma \sqrt{\tau}}{\sigma \sqrt{\tau}}d_1 \right] \,
	 *   
	 * @return Returns gamma decay over time
	 */
	public double color() {
		return(-Math.exp(-this.q * this.t) * ((phi(this.d1())) / (2 * this.S * this.t * this.sigma * Math.sqrt(this.t))) * (2 * this.q * this.t + 1 + (2 * (this.r - this.q) * this.t - this.d2() * this.sigma * Math.sqrt(this.t)) / (this.sigma * Math.sqrt(this.t)) * this.d1()));
	}
	
	/**
	 * @return the sigma
	 */
	public double getSigma() {
		return sigma;
	}

	/**
	 * @param sigma the sigma to set
	 */
	public void setSigma(double sigma) {
		this.sigma = sigma;
	}

	/**
	 * @return the k
	 */
	public double getK() {
		return K;
	}

	/**
	 * @param k the k to set
	 */
	public void setK(double k) {
		K = k;
	}

	/**
	 * @return the s
	 */
	public double getS() {
		return S;
	}

	/**
	 * @param s the s to set
	 */
	public void setS(double s) {
		S = s;
	}

	/**
	 * @return the t
	 */
	public double getT() {
		return t;
	}

	/**
	 * @param t the t to set
	 */
	public void setT(double t) {
		this.t = t;
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
	 * @return the r
	 */
	public double getR() {
		return r;
	}

	/**
	 * @param r the r to set
	 */
	public void setR(double r) {
		this.r = r;
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
	 * @return the gamma
	 */
	public double getGamma() {
		return gamma;
	}

	/**
	 * @param gamma the gamma to set
	 */
	public void setGamma(double gamma) {
		this.gamma = gamma;
	}

	/**
	 * @return the theta
	 */
	public double getTheta() {
		return theta;
	}

	/**
	 * @param theta the theta to set
	 */
	public void setTheta(double theta) {
		this.theta = theta;
	}

	/**
	 * @return the vega
	 */
	public double getVega() {
		return vega;
	}

	/**
	 * @param vega the vega to set
	 */
	public void setVega(double vega) {
		this.vega = vega;
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
}
