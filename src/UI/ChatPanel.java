package UI;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;

import backend.TelecomClient;

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JTextField;

import org.apache.commons.lang3.StringUtils;

import javax.swing.JPanel;
import javax.swing.JTextArea;

public class ChatPanel extends JPanel {

	public static JButton btnLogin;
	private JTextField txtInbox;
	private static JTextArea message;
	private static String text;
	private static int deleteResult;
	private JTextField txtReceiver;
	private static int sendResult;

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
		setPreferredSize(new Dimension(600, 400));
		panel.setBounds(0, 0, 600, 400);
		add(panel);
		panel.setLayout(null);

		JLabel lblChat = new JLabel("Chat");
		lblChat.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		lblChat.setBounds(265, 28, 55, 29);
		panel.add(lblChat);

		JLabel lblNewLabel = new JLabel("Logged in user: " + loggedInUser);
		lblNewLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		lblNewLabel.setBounds(72, 69, 219, 29);
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
					((ClientApp) getTopLevelAncestor()).swapView("loginPanel");
					ClientApp.loggedInUser = "";
					break;
				case 2:
					JOptionPane.showMessageDialog(null, "Session expired!", "Warning", JOptionPane.WARNING_MESSAGE);
					((ClientApp) getTopLevelAncestor()).swapView("loginPanel");
					ClientApp.loggedInUser = "";
					break;
				}
			}
		});
		btnLogout.setBounds(329, 71, 117, 29);
		panel.add(btnLogout);

		message = new JTextArea(16, 58);
		message.setText("");
		message.setBounds(287, 139, 283, 69);
		panel.add(message);

		JLabel lblMessageToSend = new JLabel("Message to send:");
		lblMessageToSend.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		lblMessageToSend.setBounds(287, 105, 142, 29);
		panel.add(lblMessageToSend);

		JButton btnSend = new JButton("Send");
		btnSend.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {
					sendResult = TelecomClient.sendMessage(txtReceiver.getText(), message.getText());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				switch (sendResult) {
				case 0:
					JOptionPane.showMessageDialog(null, "Message sent!");
					break;
				case 1: 
					JOptionPane.showMessageDialog(null, "Failure to write to the user's datastore.", "Warning", JOptionPane.WARNING_MESSAGE);
					break;		
				case 2: 
					JOptionPane.showMessageDialog(null, "User or his/her datastore doesn't exist!", "Error", JOptionPane.ERROR_MESSAGE);
					break;
				case 3:
					JOptionPane.showMessageDialog(null, "You are not logged in!", "Error", JOptionPane.ERROR_MESSAGE);
					break;
				case 4:
					JOptionPane.showMessageDialog(null, "Badly formatted request.", "Error", JOptionPane.ERROR_MESSAGE);
					break;
				}
			}
		});
		btnSend.setBounds(203, 226, 117, 29);
		panel.add(btnSend);

		JLabel lblInbox = new JLabel("Inbox");
		lblInbox.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		lblInbox.setBounds(49, 266, 76, 19);
		panel.add(lblInbox);

		txtInbox = new JTextField();
		txtInbox.setBounds(48, 297, 413, 84);
		panel.add(txtInbox);
		txtInbox.setColumns(10);
		
//		panel.revalidate();
//		panel.repaint();
		
		JButton btnExit = new JButton("Exit");
		btnExit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {
					TelecomClient.exit();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnExit.setBounds(483, 326, 117, 29);
		panel.add(btnExit);
		JButton btnDeleteAccount = new JButton("Delete Account\n");
		btnDeleteAccount.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {
					deleteResult = TelecomClient.deleteUser();
				}
				catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				switch (deleteResult) {
				case 0:
					JOptionPane.showMessageDialog(null, "User deletion success");
					((ClientApp) getTopLevelAncestor()).swapView("loginPanel");
					break;
				case 1:
					JOptionPane.showMessageDialog(null, "Not logged in", "Error", JOptionPane.ERROR_MESSAGE);
					break;
				case 2:
					JOptionPane.showMessageDialog(null, "General error", "Error", JOptionPane.ERROR_MESSAGE);
					break;
				}
			}
		});
		btnDeleteAccount.setBounds(477, 71, 117, 29);
		panel.add(btnDeleteAccount);

		JLabel lblTo = new JLabel("To:");
		lblTo.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		lblTo.setBounds(26, 111, 61, 16);
		panel.add(lblTo);

		txtReceiver = new JTextField();
		txtReceiver.setBounds(24, 139, 209, 28);
		panel.add(txtReceiver);
		txtReceiver.setColumns(10);
		//		
		//	    JScrollPane scroll = new JScrollPane(message);
		//	    scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		//	    panel.add(scroll);

	}
}
