package sigma.utils;

import java.util.Date;
import sigma.utils.LogLevel;

public class Logger {
	
	LogLevel logLevel = LogLevel.INFO;

	public Logger(LogLevel p_logLevel) {
		logLevel = p_logLevel;
	}
	
	public Logger() {
	}
	
	public void log(String m_str) {
		this.log(LogLevel.INFO, m_str);
	}
	
	public void log(LogLevel m_level, String m_str) {
		Date dtg = new Date();
		
		if (m_level.ordinal() <= logLevel.ordinal()) {
			System.out.println(dtg.toString() + ": " + m_level.toString() + ":" + m_str);
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
		//System.out.println(e.getStackTrace().toString());
		e.printStackTrace();
	}

}
