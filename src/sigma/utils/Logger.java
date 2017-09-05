package sigma.utils;

import java.util.Date;
import sigma.utils.LogLevel;

public class Logger {
	
	LogLevel logLevel = LogLevel.INFO;

	public Logger(LogLevel logLevel) {
		this.logLevel = logLevel;
	}
	
	public Logger() {
	}
	
	public void log(String str) {
		this.log(LogLevel.INFO, str);
	}
	
	public void log(LogLevel level, String str) {
		Date dtg = new Date();
		
		if (level.ordinal() <= logLevel.ordinal()) {
			System.out.println(dtg.toString() + ": " + level.toString() + ":" + str);
		}
	}
	
	public void error(String m_str) {
		this.log(LogLevel.ERROR, m_str);
	}
	
	public void warning(String m_str) {
		this.log(LogLevel.WARN, m_str);
	}
	
	public void verbose(String m_str) {
		this.log(LogLevel.VERBOSE, m_str);
	}
	
	public void error(Exception e) {		
		// System.out.println(e.getStackTrace().toString());
		e.printStackTrace();
	}

}
