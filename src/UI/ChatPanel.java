package UI;

import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;

import backend.TelecomClient;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JTextField;

import org.apache.commons.lang3.StringUtils;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JScrollBar;
import javax.swing.JComboBox;
import javax.swing.ScrollPaneConstants;

public class ChatPanel extends JPanel {

	public static JButton btnLogin;

	/**
	 * Create the application.
	 */
	public ChatPanel(String loggedInUser) {
		initialize(loggedInUser);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(String loggedInUser) {

		JPanel panel = new JPanel();
		setLayout(null);
		setPreferredSize(new Dimension(444, 288));
		panel.setBounds(0, 0, 444, 288);
		add(panel);
		panel.setLayout(null);
		
		JLabel lblChat = new JLabel("Chat");
		lblChat.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		lblChat.setBounds(188, 44, 55, 29);
		panel.add(lblChat);
		
		JLabel lblNewLabel = new JLabel("Logged in user: " + loggedInUser);
		lblNewLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		lblNewLabel.setBounds(24, 102, 219, 29);
		panel.add(lblNewLabel);
		
		JButton btnLogout = new JButton("Logout");
		btnLogout.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				int logOffResult = 5000;
				try {
					logOffResult = TelecomClient.logoutUser();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				switch (logOffResult) {
				case 0:
					((ClientApp) getTopLevelAncestor()).swapView("loginPanel");
					ClientApp.loggedInUser = "";
					break;
				case 1:
					JOptionPane.showMessageDialog(null, "Not logged in!", "Error", JOptionPane.ERROR_MESSAGE);
					break;
				case 2:
					JOptionPane.showMessageDialog(null, "Session expired!", "Warning", JOptionPane.WARNING_MESSAGE);
					break;
				}
			}
		});
		btnLogout.setBounds(265, 104, 117, 29);
		panel.add(btnLogout);
		
	    JTextArea message = new JTextArea(16, 58);
	    message.setText("");
	    message.setBounds(83, 168, 278, 69);
	    panel.add(message);
//		
//	    JScrollPane scroll = new JScrollPane(message);
//	    scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
//	    panel.add(scroll);
	   
	}
}
