package sigma.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.GridLayout;
import javax.swing.JTable;
import javax.swing.JButton;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JCheckBox;
import java.awt.ScrollPane;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class NewsTrader {

	private JFrame frmSigmaNewsTrader;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NewsTrader window = new NewsTrader();
					window.frmSigmaNewsTrader.setVisible(true);
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
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmSigmaNewsTrader = new JFrame();
		frmSigmaNewsTrader.setTitle("Sigma News Trader");
		frmSigmaNewsTrader.setBounds(100, 100, 661, 407);
		frmSigmaNewsTrader.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 119, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		frmSigmaNewsTrader.getContentPane().setLayout(gridBagLayout);
		
		JCheckBox chckbxNewCheckBox = new JCheckBox("New check box");
		GridBagConstraints gbc_chckbxNewCheckBox = new GridBagConstraints();
		gbc_chckbxNewCheckBox.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxNewCheckBox.gridx = 2;
		gbc_chckbxNewCheckBox.gridy = 1;
		frmSigmaNewsTrader.getContentPane().add(chckbxNewCheckBox, gbc_chckbxNewCheckBox);
		
		JButton btnNewButton_1 = new JButton("Connect");
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
			}
		});
		btnNewButton_1.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
		gbc_btnNewButton_1.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton_1.gridx = 3;
		gbc_btnNewButton_1.gridy = 1;
		frmSigmaNewsTrader.getContentPane().add(btnNewButton_1, gbc_btnNewButton_1);
		
		JCheckBox chckbxNewCheckBox_1 = new JCheckBox("New check box");
		GridBagConstraints gbc_chckbxNewCheckBox_1 = new GridBagConstraints();
		gbc_chckbxNewCheckBox_1.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxNewCheckBox_1.gridx = 2;
		gbc_chckbxNewCheckBox_1.gridy = 2;
		frmSigmaNewsTrader.getContentPane().add(chckbxNewCheckBox_1, gbc_chckbxNewCheckBox_1);
		
		JButton btnNewButton = new JButton("Disconnect");
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton.gridx = 3;
		gbc_btnNewButton.gridy = 2;
		frmSigmaNewsTrader.getContentPane().add(btnNewButton, gbc_btnNewButton);
		
		JCheckBox chckbxNewCheckBox_2 = new JCheckBox("New check box");
		GridBagConstraints gbc_chckbxNewCheckBox_2 = new GridBagConstraints();
		gbc_chckbxNewCheckBox_2.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxNewCheckBox_2.gridx = 2;
		gbc_chckbxNewCheckBox_2.gridy = 3;
		frmSigmaNewsTrader.getContentPane().add(chckbxNewCheckBox_2, gbc_chckbxNewCheckBox_2);
		
		JButton btnNewButton_2 = new JButton("Activate");
		btnNewButton_2.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_btnNewButton_2 = new GridBagConstraints();
		gbc_btnNewButton_2.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton_2.gridx = 3;
		gbc_btnNewButton_2.gridy = 3;
		frmSigmaNewsTrader.getContentPane().add(btnNewButton_2, gbc_btnNewButton_2);
		
		JCheckBox chckbxNewCheckBox_3 = new JCheckBox("New check box");
		GridBagConstraints gbc_chckbxNewCheckBox_3 = new GridBagConstraints();
		gbc_chckbxNewCheckBox_3.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxNewCheckBox_3.gridx = 2;
		gbc_chckbxNewCheckBox_3.gridy = 4;
		frmSigmaNewsTrader.getContentPane().add(chckbxNewCheckBox_3, gbc_chckbxNewCheckBox_3);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridwidth = 3;
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 5;
		frmSigmaNewsTrader.getContentPane().add(scrollPane, gbc_scrollPane);
		
		JTextPane textPane = new JTextPane();
		scrollPane.setViewportView(textPane);
	}

}
