package sigma.utils;

import java.io.FileOutputStream;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import sigma.utils.LogLevel;

/**
 * Logger class for general log generation to either stdout or text file.
 * 
 * @author Peeter Meos
 * @version 1.1
 *
 */
public class Logger {
	
	protected LogLevel logLevel = LogLevel.INFO;
	protected Writer f = null;

	/**
	 * Constructor for Logger 
	 * 
	 * @param logLevel Logging level (ERROR, WARN, INFO, VERBOSE)
	 */
	public Logger(LogLevel logLevel) {
		this.logLevel = logLevel;
	}
	
	/**
	 * Constructor for Logger (stdout, logging level INFO)
	 */
	public Logger() {
	}
	
	/**
	 * Constructor for Logger
	 * 
	 * @param fname Filename for text file
	 */
	public Logger(String fname) {
		this(fname, LogLevel.INFO);
	}
	
	/**
	 * Constructor for Logger
	 * 
	 * @param fname Filename for text file
	 * @param logLevel Logging level (ERROR, WARN, INFO, VERBOSE)
	 */
	public Logger(String fname, LogLevel logLevel) {
		this.logLevel = logLevel;
		
		try {
			f = new BufferedWriter(
					new OutputStreamWriter(
							new FileOutputStream(fname + ".log"), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Logging method
	 * 
	 * @param str String to be logged (log level INFO)
	 */
	public void log(String str) {
		this.log(LogLevel.INFO, str);
	}
	
	/**
	 * Logging method
	 * 
	 * @param level logging level
	 * @param str string to be logged
	 */
	public void log(LogLevel level, String str) {
		Date dtg = new Date();
		
		if (level.ordinal() <= logLevel.ordinal()) {
			if (f == null) {
				System.out.println(dtg.toString() + ": " + level.toString() + ":" + str);
			} else {
				try {
					f.write(dtg.toString() + ": " + level.toString() + ":" + str);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Error logging method
	 * 
	 * @param m_str Error text to be logged
	 */
	public void error(String m_str) {
		this.log(LogLevel.ERROR, m_str);
	}
	
	/**
	 * Warning logging method
	 * 
	 * @param m_str Warning text to be logged
	 */
	public void warning(String m_str) {
		this.log(LogLevel.WARN, m_str);
	}
	
	/**
	 * Verbose debugging logging method
	 * 
	 * @param m_str Debug text to be logged
	 */
	public void verbose(String m_str) {
		this.log(LogLevel.VERBOSE, m_str);
	}
	
	/**
	 * Stack trace exception printout
	 * @param e exception
	 */
	public void error(Exception e) {		
		// System.out.println(e.getStackTrace().toString());
		e.printStackTrace();
	}

}
