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

public class Logger {
	
	LogLevel logLevel = LogLevel.INFO;
	Writer f = null;

	public Logger(LogLevel logLevel) {
		this.logLevel = logLevel;
	}
	
	public Logger() {
	}
	
	public Logger(String fname) {
		this(fname, LogLevel.INFO);
	}
	
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
	
	public void log(String str) {
		this.log(LogLevel.INFO, str);
	}
	
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
