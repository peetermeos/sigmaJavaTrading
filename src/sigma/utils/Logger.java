package sigma.utils;

public class Logger {
	
	public Logger() {
		
	}
	
	public void log(String m_str) {
		System.out.println(m_str);
	}
	
	public void error(String m_str) {
		System.out.println(m_str);
	}
	
	public void error(Exception e) {
		//System.out.println(e.getStackTrace().toString());
		e.printStackTrace();
	}

}
