package sigma.utils;

/**
 * Possible states of trader algorithm
 * WAIT - no live orders, waiting for trigger
 * LIVE - orders are live, nothing has executed
 * EXEC - post execution stage with at least one order
 *        executed, some cancelled and some live (like trail stop)
 * 
 * @author Peeter Meos
 * @version 0.1
 *
 */
public enum TraderState {
	WAIT, LIVE, EXEC;
	
	/**
	 * @return String value of enum
	 */
	@Override
	public String toString() {
		switch(this) {
		case WAIT:
			return("WAIT");
		case LIVE:
			return("LIVE");
		case EXEC:
			return("EXEC");
		default:
			return("NONE");
		}
	}
	
	/**
	 * Simple standard comparison method
	 * @param t
	 * @return
	 */
	public boolean equals(TraderState t) {
		if (this == t) {
			return(true);
		} else {
			return(false);
		}
	}
}
