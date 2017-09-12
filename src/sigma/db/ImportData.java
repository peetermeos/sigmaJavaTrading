/**
 * 
 */
package sigma.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.ib.client.Contract;
import com.ib.client.TagValue;
import com.ib.client.Types.SecType;

import sigma.trading.TwsConnector;

/**
 * Loads historic data from TWS into database
 * 
 * @author Peeter Meos
 * @version 0.1
 */
public class ImportData extends TwsConnector {

	// TWS stuff
	protected Contract c = null;
	
	// MySQL stuff
	protected Connection conn = null;
	protected Statement stmt = null;
	protected ResultSet rs = null;
	
	private String server = "mysql";
	private String db = "db";
	private String uid = "user";
	private String pwd = "pwd";
	
	/**
	 * Quick and dirty constructor for data retrieval
	 */
	public ImportData() {
		super("Data pull and db injection");
		
		c = new Contract();
		c.symbol("CL");
		c.secType(SecType.FUT);
		c.exchange("NYMEX");
		c.lastTradeDateOrContractMonth("201710");
	}
	
	
	/**
	 * Historical data request from tws
	 */
	public void reqData() {
		String endDateTime = "";
		int formatDate = 0;
		List<TagValue> chartOptions = null;
		
		this.getTws().reqHistoricalData(1, c, endDateTime, "! M", "5 min", "MID", 0, formatDate, chartOptions);
	}
	
	/**
	 * Override to save the requested data
	 */
	@Override
	public void historicalData(int reqId, String date, double open, double high, double low, double close, int volume,
			int count, double WAP, boolean hasGaps) {
		logger.log("Historical data received: " + date + " : " + close);		
	}
	
	/**
	 * Simple database connection
	 */
	public void dbConnect() {
	    try {
			conn = DriverManager.getConnection("jdbc:mysql://" + server + "/" + db + "?" +
				                               "user=" + uid + "&password=" + pwd);
		} catch (SQLException e) {
			logger.error(e);
		}
	}
	
	/**
	 * Data injection into database
	 */
	public void importData() {
		try {
		    // Do something with the Connection
		    stmt = conn.createStatement();
		    rs = stmt.executeQuery("INSERT INTO foo FROM bar");
		} catch (SQLException e) {
			logger.error(e);
		} finally {
		    if (rs != null) {
		        try {
		            rs.close();
		        } catch (SQLException sqlEx) { } // ignore

		        rs = null;
		    }

		    if (stmt != null) {
		        try {
		            stmt.close();
		        } catch (SQLException sqlEx) { } // ignore

		        stmt = null;
		    }			
		}
	}
	
	/**
	 * Release resources
	 */
	public void dbDisconnect() {
		// Do stuff here
	}
	
	/** 
	 * Main entry point 
	 * 
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		ImportData t;
		
		t = new ImportData();
		t.twsConnect();
		
		// Open database
		t.dbConnect();
		
		// Request data 
		
		// Save results to database
		t.importData();
		
		// Loop until finished
		// Close database
		
		t.twsDisconnect();
	}
}
