package sigma.utils;

/**
 * Generic error levels for logging stuff.
 * 
 * @author Peeter Meos
 *
 */
public enum LogLevel {
	ERROR, WARN, INFO, VERBOSE;
	
	/**
	 * Standard toSting() method for the LogLevel enum.
	 */
	public String toString() {
		switch(this) {
		case ERROR:
			return("ERROR");
		case WARN:
			return("WARN");
		case INFO:
			return("INFO");
		default:
			return("VERBOSE");
		}
	}
}
