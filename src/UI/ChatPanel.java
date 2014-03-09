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

import javax.swing.JPanel;
import javax.swing.JTextArea;

public class ChatPanel extends JPanel {

	public static JButton btnLogin;
	private JTextField txtInbox;
	private static JTextArea message;
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
		setPreferredSize(new Dimension(700, 500));
		panel.setBounds(0, 0, 700, 500);
		add(panel);
		panel.setLayout(null);

		JLabel lblChat = new JLabel("Chat");
		lblChat.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		lblChat.setBounds(323, 23, 55, 29);
		panel.add(lblChat);

		JLabel lblNewLabel = new JLabel("Logged in user: " + loggedInUser);
		lblNewLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		lblNewLabel.setBounds(104, 69, 219, 29);
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
					LoginPanel.username.setText("");
					LoginPanel.password.setText("");
					break;
				case 1:
					JOptionPane.showMessageDialog(null, "Not logged in!", "Error", JOptionPane.ERROR_MESSAGE);
					((ClientApp) getTopLevelAncestor()).swapView("loginPanel");
					ClientApp.loggedInUser = "";
					LoginPanel.username.setText("");
					LoginPanel.password.setText("");
					break;
				case 2:
					JOptionPane.showMessageDialog(null, "Session expired!", "Warning", JOptionPane.WARNING_MESSAGE);
					((ClientApp) getTopLevelAncestor()).swapView("loginPanel");
					ClientApp.loggedInUser = "";
					LoginPanel.username.setText("");
					LoginPanel.password.setText("");
					break;
				}
			}
		});
		btnLogout.setBounds(379, 71, 117, 29);
		panel.add(btnLogout);

		message = new JTextArea(16, 58);
		message.setText("");
		message.setBounds(265, 146, 311, 117);
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
					txtReceiver.setText("");
					message.setText("");
					break;
				case 1: 
					JOptionPane.showMessageDialog(null, "Failure to write to the user's datastore.", "Warning", JOptionPane.WARNING_MESSAGE);
					txtReceiver.setText("");
					message.setText("");
					break;		
				case 2: 
					JOptionPane.showMessageDialog(null, "User or his/her datastore doesn't exist!", "Error", JOptionPane.ERROR_MESSAGE);
					txtReceiver.setText("");
					message.setText("");
					break;
				case 3:
					JOptionPane.showMessageDialog(null, "You are not logged in!", "Error", JOptionPane.ERROR_MESSAGE);
					txtReceiver.setText("");
					message.setText("");
					break;
				case 4:
					JOptionPane.showMessageDialog(null, "Badly formatted request.", "Error", JOptionPane.ERROR_MESSAGE);
					txtReceiver.setText("");
					message.setText("");
					break;
				}
			}
		});
		btnSend.setBounds(582, 158, 117, 29);
		panel.add(btnSend);

		JLabel lblInbox = new JLabel("Inbox");
		lblInbox.setFont(new Font("Lucida Grande", Font.PLAIN, 17));
		lblInbox.setBounds(323, 291, 106, 29);
		panel.add(lblInbox);

		txtInbox = new JTextField();
		txtInbox.setBounds(66, 332, 560, 106);
		panel.add(txtInbox);
		txtInbox.setColumns(10);
		
		txtInbox.setText(TelecomClient.message);
		
		JButton btnDeleteAccount = new JButton("Delete Account");
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
					LoginPanel.username.setText("");
					LoginPanel.password.setText("");
					((ClientApp) getTopLevelAncestor()).swapView("loginPanel");
					break;
				case 1:
					JOptionPane.showMessageDialog(null, "Not logged in", "Error", JOptionPane.ERROR_MESSAGE);
					LoginPanel.username.setText("");
					LoginPanel.password.setText("");
					((ClientApp) getTopLevelAncestor()).swapView("loginPanel");
					break;
				case 2:
					JOptionPane.showMessageDialog(null, "General error", "Error", JOptionPane.ERROR_MESSAGE);
					LoginPanel.username.setText("");
					LoginPanel.password.setText("");
					((ClientApp) getTopLevelAncestor()).swapView("loginPanel");
					break;
				}
			}
		});
		btnDeleteAccount.setBounds(526, 71, 142, 29);
		panel.add(btnDeleteAccount);

		JLabel lblTo = new JLabel("To:");
		lblTo.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		lblTo.setBounds(44, 111, 61, 16);
		panel.add(lblTo);

		txtReceiver = new JTextField();
		txtReceiver.setBounds(26, 146, 209, 28);
		panel.add(txtReceiver);
		txtReceiver.setColumns(10);

	}
}
