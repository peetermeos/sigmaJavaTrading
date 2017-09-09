/**
 * 
 */
package sigma.gui;

import java.awt.*;
import java.awt.event.*;
import java.io.PrintStream;

import javax.swing.*;

import com.ib.client.Contract;
import com.ib.client.TagValue;

import java.util.List;
import java.util.Vector;
import java.util.ArrayList;

import sigma.trading.TwsConnector;
//import sigma.utils.OptSide;
import sigma.trading.Instrument;


/**
 * Simple quote monitor for TWS
 * 
 * @author Peeter Meos
 * @version 0.2
 *
 */
public class QuoteMonitor extends TwsConnector {
	private JFrame mainFrame;
	private JLabel headerLabel;
	private JLabel statusLabel;
	private JPanel controlPanel;
	private JTable quoteTable;
	private JTextArea logWindow;
	private JScrollPane scrollPane;
	
	private List<Instrument> portfolio;
	
	private PrintStream printStream;
	
	String[] columnNames = {"Contract",
            "Bid", "Ask", "Last",
            "Previous close"};
	
	Object[][] data = {
		    {"CL", new Double(0), new Double(0), new Double(0), new Double(0)},
		    {"RB", new Double(0), new Double(0), new Double(0), new Double(0)},
		    {"SVXY", new Double(0), new Double(0), new Double(0), new Double(0)},
		    {"JDST", new Double(0), new Double(0), new Double(0), new Double(0)},
		    {"JNUG", new Double(0), new Double(0), new Double(0), new Double(0)}
		};
	
	
	/**
	 * The standard constructor. Just prepares the visuals for the GUI.
	 */
	public QuoteMonitor() {
		super("Quote Monitor");
		     
		
		// Initialize the portfolio
		portfolio = new ArrayList<>();
		portfolio.add(new Instrument("CL", "FUT", "NYMEX", "201710"));
		portfolio.add(new Instrument("RB", "FUT", "NYMEX", "201710"));
		portfolio.add(new Instrument("SVXY", "STK", "SMART", ""));
		portfolio.add(new Instrument("JDST", "STK", "SMART", ""));
		portfolio.add(new Instrument("JNUG", "STK", "SMART", ""));
		
		prepareGUI();
		
		// Wonder if the reassignment of logging works?
        this.printStream = new PrintStream(new TextAreaOutputStream(logWindow));               
        System.setOut(this.printStream);
        System.setErr(this.printStream);
	}
	
	/**
	 * Creates visuals and controls for the GUI.
	 */
	public void prepareGUI() {
	    mainFrame = new JFrame("Sigma Quote Monitor");
	    mainFrame.setSize(800, 404);
	    mainFrame.getContentPane().setLayout(new GridLayout(5, 1, 5, 5));

	    headerLabel = new JLabel("Header",JLabel.CENTER );
	    
	    statusLabel = new JLabel("Status",JLabel.CENTER);        
	    statusLabel.setSize(350, 100);
	    
	    
	    quoteTable = new JTable(data, columnNames);
	    scrollPane = new JScrollPane(quoteTable);
	    quoteTable.setFillsViewportHeight(true);
	    
	    logWindow = new JTextArea(5, 20);
	      
	    mainFrame.addWindowListener(new WindowAdapter() {
	    	
	       /**
	        * Window close event handler.
	        * Closes twsConnection and exits.
	        * 
	        * @param windowEvent
	        */
	       @Override
	       public void windowClosing(WindowEvent windowEvent){
	    	   if(getTws().isConnected()) {
	    		   twsDisconnect();   
	    	   }
	           System.exit(0);
	       }        
	    });    
	      
	    controlPanel = new JPanel();
	    controlPanel.setLayout(new FlowLayout());

	    mainFrame.getContentPane().add(headerLabel);
	    mainFrame.getContentPane().add(controlPanel);
	    mainFrame.getContentPane().add(scrollPane);
	    mainFrame.getContentPane().add(logWindow);
	    mainFrame.getContentPane().add(statusLabel);
	    mainFrame.setVisible(true);		
	}
	
	/**
	 * Displays the GUI and activates the controls.
	 */
	public void showGUI() {
	    headerLabel.setText("Control in action: Button"); 

	    JButton okButton = new JButton("Connect");
	    JButton submitButton = new JButton("Disconnect");
	    JButton cancelButton = new JButton("Request Data");

	    okButton.setActionCommand("Connect");
	    submitButton.setActionCommand("Disconnect");
	    cancelButton.setActionCommand("Request Data");

	    okButton.addActionListener(new ButtonClickListener()); 
	    submitButton.addActionListener(new ButtonClickListener()); 
	    cancelButton.addActionListener(new ButtonClickListener()); 

	    controlPanel.add(okButton);
	    controlPanel.add(submitButton);
	    controlPanel.add(cancelButton);       

	    mainFrame.setVisible(true); 		
	}
	
	/**
	 * Requests live market data for the contracts
	 */
	public void getData() {
		Contract c = null;
		String genericTickList = null;
		Vector<TagValue> mktDataOptions = new Vector<TagValue>();
		
		if (tws.isConnected()) {
			for(Instrument i: portfolio) {
				// create contract
				c = new Contract();
				c.symbol(i.getSymbol());
				c.exchange(i.getExchange());
				
				switch (i.getSecType()) {
				case "FUT":
					c.secType("FUT");
					c.lastTradeDateOrContractMonth(i.getExpiry());
					break;
				case "STK":
					c.secType("STK");
					break;
				default:
					break;
				}
				
				// request contract id
				tws.reqContractDetails(nextOrderID, c);
				// request live data
				tws.reqMktData(portfolio.indexOf(i) + 1, c, genericTickList, false, mktDataOptions);
				
			}
		}
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
	            statusLabel.setText("Connect Button clicked.");
	    		twsConnect();
	    		getData();
	         } else if( command.equals( "Disconnect" ) )  {
	            statusLabel.setText("Disconnect Button clicked."); 
	    		twsDisconnect();
	         } else {
	            statusLabel.setText("Request Data clicked.");
	         } 
		}		
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		QuoteMonitor app = new QuoteMonitor();
		app.showGUI();
	}

}
