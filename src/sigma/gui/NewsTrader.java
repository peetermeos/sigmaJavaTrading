package sigma.gui;

import sigma.trading.Instrument;
import sigma.trading.TwsConnector;
import sigma.utils.Ticker;
import sigma.utils.TraderState;

import java.awt.EventQueue;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JButton;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * News trader working on Swing GUI
 * @author Peeter Meos
 * @version 0.1
 *
 */
public class NewsTrader {

	private JFrame frame;
	private Connector con;
	private JTable statusTable;
	private JTextArea logWindow;
	private PrintStream printStream;
	
	private Thread procThread;
	
	private List<Instrument> portfolio = null; 

	String[] columnNames = {"Contract", "Bid", "Ask", "Last", "Status"};
	
	Object[][] data = {
		    {"CL",   0, 0, 0, TraderState.WAIT.toString()},
		    {"RB",   0, 0, 0, TraderState.WAIT.toString()},
		    {"SVXY", 0, 0, 0, TraderState.WAIT.toString()},
		    {"JDST", 0, 0, 0, TraderState.WAIT.toString()},
		    {"JNUG", 0, 0, 0, TraderState.WAIT.toString()}
		};
	
	/**
	 * TwsConnector access method
	 * @return TwsConnector
	 */
	public TwsConnector getCon() {
		return(con);
	}
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NewsTrader window = new NewsTrader();
					window.frame.setVisible(true);
								    
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
    /**
     * Simple message processor for price updates
     * 
     * @author Peeter Meos
     *
     */
    protected class Task implements Runnable {
        public void run() {
          List<Ticker> tickers = null;
          
          while (!Thread.currentThread().isInterrupted()){
            // do the processing crap
        	if (con.isConnected()) {
        		// Get tickers and update data[][]
        		tickers = con.getTickers();
        		
        		// check if anything changed
        		// update table
        		if (statusTable != null) {
        			// Update table
        		}
        	}
          }
        }
    }

	
	/**
	 * Create the application.
	 */
	public NewsTrader() {
		initialize();
		
		// Wonder if the reassignment of logging works?
        this.printStream = new PrintStream(new TextAreaOutputStream(logWindow));               
        System.setOut(this.printStream);
        System.setErr(this.printStream);
		
		con = new Connector("Sigma News Trader");	
		
	    // Start message thread
	    Task myTask = new Task();
	    procThread = new Thread(myTask, "T1");
	    procThread.start();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
			
	    frame.addWindowListener(new WindowAdapter() {
	    	
	       /**
	        * Window close event handler.
	        * Closes twsConnection and exits.
	        * 
	        * @param windowEvent
	        */
	       @Override
	       public void windowClosing(WindowEvent windowEvent){
	    	   if(con.getTws().isConnected()) {	    		   
	    		   con.twsDisconnect();   
	    	   }
	           System.exit(0);
	       }        
		  }); 
		    
	    // General layout
		frame.setBounds(100, 100, 820, 620);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{10, 0, 0, 0, 0, 10};
		gridBagLayout.rowHeights = new int[]{10, 10, 23, 100, 10, 300, 10};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 1.0, 1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);
		
		// Connect button
		JButton btnConnect = new JButton("Connect TWS");
		btnConnect.setActionCommand("Connect");
		btnConnect.addActionListener(new ButtonClickListener());
		
		GridBagConstraints gbcBtnConnect = new GridBagConstraints();
		gbcBtnConnect.insets = new Insets(0, 0, 5, 5);
		gbcBtnConnect.gridx = 1;
		gbcBtnConnect.gridy = 1;
		frame.getContentPane().add(btnConnect, gbcBtnConnect);
		
		// Disconnect button
		JButton btnDisconnect = new JButton("Disconnect TWS");
		btnDisconnect.setActionCommand("Disconnect");
		btnDisconnect.addActionListener(new ButtonClickListener());
		
		GridBagConstraints gbcBtnDisconnect = new GridBagConstraints();
		gbcBtnDisconnect.insets = new Insets(0, 0, 5, 5);
		gbcBtnDisconnect.gridx = 2;
		gbcBtnDisconnect.gridy = 1;
		frame.getContentPane().add(btnDisconnect, gbcBtnDisconnect);
		
		// Exit button
		JButton btnExit = new JButton("Exit");
		btnExit.setActionCommand("Exit");
		btnExit.addActionListener(new ButtonClickListener());
		
		GridBagConstraints gbcBtnExit = new GridBagConstraints();
		gbcBtnExit.insets = new Insets(0, 0, 5, 5);
		gbcBtnExit.gridx = 3;
		gbcBtnExit.gridy = 1;
		frame.getContentPane().add(btnExit, gbcBtnExit);
		
		// Label
		JLabel lblTraderStatus = new JLabel("Trader portfolio status");
		GridBagConstraints gbcLblTraderStatus = new GridBagConstraints();
		gbcLblTraderStatus.anchor = GridBagConstraints.WEST;
		gbcLblTraderStatus.insets = new Insets(0, 0, 5, 5);
		gbcLblTraderStatus.gridx = 1;
		gbcLblTraderStatus.gridy = 2;
		frame.getContentPane().add(lblTraderStatus, gbcLblTraderStatus);
		
		// Portfolio table
		JScrollPane scrollPaneStatusTable = new JScrollPane();
		GridBagConstraints gbcScrollPaneStatusTable = new GridBagConstraints();
		gbcScrollPaneStatusTable.fill = GridBagConstraints.BOTH;
		gbcScrollPaneStatusTable.gridwidth = 3;
		gbcScrollPaneStatusTable.insets = new Insets(0, 0, 5, 5);
		gbcScrollPaneStatusTable.gridx = 1;
		gbcScrollPaneStatusTable.gridy = 3;
		frame.getContentPane().add(scrollPaneStatusTable, gbcScrollPaneStatusTable);
		
		statusTable = new JTable(data, columnNames);
		scrollPaneStatusTable.setViewportView(statusTable);
		
		// Label
		JLabel lblTraderLog = new JLabel("Trader log");
		GridBagConstraints gbclblTraderLog = new GridBagConstraints();
		gbclblTraderLog.anchor = GridBagConstraints.WEST;
		gbclblTraderLog.insets = new Insets(0, 0, 5, 5);
		gbclblTraderLog.gridx = 1;
		gbclblTraderLog.gridy = 4;
		frame.getContentPane().add(lblTraderLog, gbclblTraderLog);
		
		// Log window
		JScrollPane scrollPaneLogWindow = new JScrollPane();
		GridBagConstraints gbcScrollPaneLogWindow = new GridBagConstraints();
		gbcScrollPaneLogWindow.insets = new Insets(0, 0, 0, 5);
		gbcScrollPaneLogWindow.gridwidth = 3;
		gbcScrollPaneLogWindow.fill = GridBagConstraints.BOTH;
		gbcScrollPaneLogWindow.gridx = 1;
		gbcScrollPaneLogWindow.gridy = 5;
		frame.getContentPane().add(scrollPaneLogWindow, gbcScrollPaneLogWindow);
		
		logWindow = new JTextArea();
		scrollPaneLogWindow.setViewportView(logWindow);
		
		// Initialize the portfolio
		portfolio = new ArrayList<>();
		portfolio.add(new Instrument("CL", "FUT", "NYMEX", "201710"));
		portfolio.add(new Instrument("RB", "FUT", "NYMEX", "201710"));
		portfolio.add(new Instrument("SVXY", "STK", "SMART", ""));
		portfolio.add(new Instrument("JDST", "STK", "SMART", ""));
		portfolio.add(new Instrument("JNUG", "STK", "SMART", ""));
		
	}
	
	/**
	 * The simple button click listener method.
	 * 
	 * @author Peeter Meos
	 * @version 0.1
	 *
	 */
	private class ButtonClickListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
	         String command = e.getActionCommand();  
	         
	         if( command.equals( "Connect" ))  {
	    		con.twsConnect();
	    		
	    		while(!con.isConnected()) {};
	    		
	    		// Request data
	    		for(int i=0; i < portfolio.size(); i++) {
	    			portfolio.get(i).createContract();
	    			con.log(portfolio.get(i).toString());
	    			con.reqMktData(i, portfolio.get(i).getInst());
	    		}
	         } else if( command.equals( "Disconnect" ) )  {
	        	 // Order cancellations need to go here
	    		con.twsDisconnect();
	         } else if( command.equals( "Exit" ) )  {
	        	 // Order cancellations need to be added here
	        	 if(con.getTws().isConnected()) {	    		   
		    		   con.twsDisconnect();   
		    	   }
		           System.exit(0);
	         } else {
	        	 // Do nothing
	         } 
		}		
	}

}
