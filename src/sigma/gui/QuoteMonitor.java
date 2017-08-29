/**
 * 
 */
package sigma.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import sigma.trading.TwsConnector;

/**
 * 
 * @author Peeter Meos
 * @version 0.1
 *
 */
public class QuoteMonitor extends TwsConnector{
	private JFrame mainFrame;
	private JLabel headerLabel;
	private JLabel statusLabel;
	private JPanel controlPanel;
	private JTable quoteTable;
	private JTextArea logWindow;
	
	String[] columnNames = {"Contract",
            "Bid", "Ask", "Last",
            "Previous close"};
	
	Object[][] data = {
		    {"CL", new Integer(5), new Integer(5), new Integer(5), new Integer(5)},
		    {"RB", new Integer(5), new Integer(5), new Integer(5), new Integer(5)},
		    {"SVXY", new Integer(5), new Integer(5), new Integer(5), new Integer(5)},
		    {"JDST", new Integer(5), new Integer(5), new Integer(5), new Integer(5)},
		    {"VIX Index", new Integer(5), new Integer(5), new Integer(5), new Integer(5)}
		};
	
	/**
	 * The standard constructor. Just prepares the visuals for the GUI.
	 */
	public QuoteMonitor() {
		super("Quote Monitor");
		prepareGUI();
	}
	
	/**
	 * Creates visuals and controls for the GUI.
	 */
	public void prepareGUI() {
	    mainFrame = new JFrame("Java SWING Examples");
	    mainFrame.setSize(800,400);
	    mainFrame.setLayout(new GridLayout(5, 1));

	    headerLabel = new JLabel("Header",JLabel.CENTER );
	    
	    statusLabel = new JLabel("Status",JLabel.CENTER);        
	    statusLabel.setSize(350,100);
	    
	    quoteTable = new JTable(data, columnNames);
	    
	    logWindow = new JTextArea(5, 20);
	      
	    mainFrame.addWindowListener(new WindowAdapter() {
	    	
	       /**
	        * Window close event handler.
	        * Closes twsConnection and exits.
	        * 
	        * @param windowEvent
	        */
	       public void windowClosing(WindowEvent windowEvent){
	    	   twsDisconnect();
	           System.exit(0);
	       }        
	    });    
	      
	    controlPanel = new JPanel();
	    controlPanel.setLayout(new FlowLayout());

	    mainFrame.add(headerLabel);
	    mainFrame.add(controlPanel);
	    mainFrame.add(quoteTable);
	    mainFrame.add(logWindow);
	    mainFrame.add(statusLabel);
	    mainFrame.setVisible(true);		
	}
	
	/**
	 * Displays the GUI and activates the controls.
	 */
	public void showGUI() {
	    headerLabel.setText("Control in action: Button"); 

	    JButton okButton = new JButton("Connect");
	    JButton submitButton = new JButton("Disconnect");
	    JButton cancelButton = new JButton("Cancel");

	    okButton.setActionCommand("Connect");
	    submitButton.setActionCommand("Disconnect");
	    cancelButton.setActionCommand("Cancel");

	    okButton.addActionListener(new ButtonClickListener()); 
	    submitButton.addActionListener(new ButtonClickListener()); 
	    cancelButton.addActionListener(new ButtonClickListener()); 

	    controlPanel.add(okButton);
	    controlPanel.add(submitButton);
	    controlPanel.add(cancelButton);       

	    mainFrame.setVisible(true); 		
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
	         } else if( command.equals( "Disconnect" ) )  {
	            statusLabel.setText("Disconnect Button clicked."); 
	    		twsDisconnect();
	         } else {
	            statusLabel.setText("Cancel Button clicked.");
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
		app.twsDisconnect();
	}

}
