package sigma.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;

import sigma.trading.TwsConnector;
import java.awt.GridBagLayout;
import javax.swing.JTextPane;
import java.awt.GridBagConstraints;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * News trader working on Swing GUI
 * @author Peeter Meos
 * @version 0.1
 *
 */
public class NewsTrader {

	private JFrame frame;
	private TwsConnector con;
	private JTable table;
	

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
	 * Create the application.
	 */
	public NewsTrader() {
		initialize();
		con = new TwsConnector();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
			}
		});
		frame.setBounds(100, 100, 814, 624);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{10, 0, 0, 0, 0, 10};
		gridBagLayout.rowHeights = new int[]{10, 10, 23, 100, 10, 300, 10};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 1.0, 1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);
		
		JButton btnConnect = new JButton("Connect");
		GridBagConstraints gbc_btnConnect = new GridBagConstraints();
		gbc_btnConnect.insets = new Insets(0, 0, 5, 5);
		gbc_btnConnect.gridx = 1;
		gbc_btnConnect.gridy = 1;
		frame.getContentPane().add(btnConnect, gbc_btnConnect);
		
		JButton btnDisconnect = new JButton("Disconnect");
		GridBagConstraints gbc_btnDisconnect = new GridBagConstraints();
		gbc_btnDisconnect.insets = new Insets(0, 0, 5, 5);
		gbc_btnDisconnect.gridx = 2;
		gbc_btnDisconnect.gridy = 1;
		frame.getContentPane().add(btnDisconnect, gbc_btnDisconnect);
		
		JButton btnExit = new JButton("Exit");
		GridBagConstraints gbc_btnExit = new GridBagConstraints();
		gbc_btnExit.insets = new Insets(0, 0, 5, 5);
		gbc_btnExit.gridx = 3;
		gbc_btnExit.gridy = 1;
		frame.getContentPane().add(btnExit, gbc_btnExit);
		
		JLabel lblTraderStatus = new JLabel("Trader status");
		GridBagConstraints gbc_lblTraderStatus = new GridBagConstraints();
		gbc_lblTraderStatus.anchor = GridBagConstraints.WEST;
		gbc_lblTraderStatus.insets = new Insets(0, 0, 5, 5);
		gbc_lblTraderStatus.gridx = 1;
		gbc_lblTraderStatus.gridy = 2;
		frame.getContentPane().add(lblTraderStatus, gbc_lblTraderStatus);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.gridwidth = 3;
		gbc_scrollPane_1.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane_1.gridx = 1;
		gbc_scrollPane_1.gridy = 3;
		frame.getContentPane().add(scrollPane_1, gbc_scrollPane_1);
		
		table = new JTable();
		scrollPane_1.setViewportView(table);
		
		JLabel lblTraderLog = new JLabel("Trader log");
		GridBagConstraints gbc_lblTraderLog = new GridBagConstraints();
		gbc_lblTraderLog.anchor = GridBagConstraints.WEST;
		gbc_lblTraderLog.insets = new Insets(0, 0, 5, 5);
		gbc_lblTraderLog.gridx = 1;
		gbc_lblTraderLog.gridy = 4;
		frame.getContentPane().add(lblTraderLog, gbc_lblTraderLog);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 0, 5);
		gbc_scrollPane.gridwidth = 3;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 5;
		frame.getContentPane().add(scrollPane, gbc_scrollPane);
		
		JTextPane textPane = new JTextPane();
		scrollPane.setViewportView(textPane);
	}

}
