/**
 * 
 */
package sigma.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * 
 * @author Peeter Meos
 * @version 0.1
 *
 */
public class QuoteMonitor {
	private JFrame mainFrame;
	private JLabel headerLabel;
	private JLabel statusLabel;
	private JPanel controlPanel;
	private JTable quoteTable;
	
	String[] columnNames = {"First Name",
            "Last Name",
            "Sport",
            "# of Years",
            "Vegetarian"};
	
	Object[][] data = {
		    {"Kathy", "Smith",
		     "Snowboarding", new Integer(5), new Boolean(false)},
		    {"John", "Doe",
		     "Rowing", new Integer(3), new Boolean(true)},
		    {"Sue", "Black",
		     "Knitting", new Integer(2), new Boolean(false)},
		    {"Jane", "White",
		     "Speed reading", new Integer(20), new Boolean(true)},
		    {"Joe", "Brown",
		     "Pool", new Integer(10), new Boolean(false)}
		};
	
	/**
	 * 
	 */
	public QuoteMonitor() {
		prepareGUI();
	}
	
	/**
	 * 
	 */
	public void prepareGUI() {
	    mainFrame = new JFrame("Java SWING Examples");
	    mainFrame.setSize(800,400);
	    mainFrame.setLayout(new GridLayout(4, 1));

	    headerLabel = new JLabel("Header",JLabel.CENTER );
	    
	    statusLabel = new JLabel("Status",JLabel.CENTER);        
	    statusLabel.setSize(350,100);
	    
	    quoteTable = new JTable(data, columnNames);
	      
	    mainFrame.addWindowListener(new WindowAdapter() {
	       public void windowClosing(WindowEvent windowEvent){
	          System.exit(0);
	       }        
	    });    
	      
	    controlPanel = new JPanel();
	    controlPanel.setLayout(new FlowLayout());

	    mainFrame.add(headerLabel);
	    mainFrame.add(controlPanel);
	    mainFrame.add(quoteTable);
	    mainFrame.add(statusLabel);
	    mainFrame.setVisible(true);		
	}
	
	/**
	 * 
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
	         } else if( command.equals( "Disconnect" ) )  {
	            statusLabel.setText("Disconnect Button clicked."); 
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
	}

}
