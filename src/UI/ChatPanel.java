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

import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;

public class ChatPanel extends JPanel {

	public static JButton btnLogin;
	private static int deleteResult;
	private JTextField txtReceiver;
	private static int sendResult;
	private static String user;
	public static JTable table;
	private static JTextArea message;
	
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

		user = loggedInUser;
		JPanel panel = new JPanel();
		setLayout(null);
		setPreferredSize(new Dimension(700, 500));
		panel.setBounds(0, 0, 700, 500);
		add(panel);
		panel.setLayout(null);

		JLabel lblChat = new JLabel("Chat");
		lblChat.setFont(new Font("Lucida Grande", Font.PLAIN, 23));
		lblChat.setBounds(309, 28, 75, 29);
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

		JLabel lblMessageToSend = new JLabel("Message to send:");
		lblMessageToSend.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		lblMessageToSend.setBounds(403, 333, 142, 29);
		panel.add(lblMessageToSend);

		JButton btnSend = new JButton("Send");
		btnSend.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (txtReceiver.getText().equals(user)) {
					sendResult = 5;
				}
				if (txtReceiver.getText().equals("")) {
					sendResult = 6;
				} else {
					try {
						sendResult = TelecomClient.sendMessage(txtReceiver.getText(), message.getText());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
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
				case 5: 
					JOptionPane.showMessageDialog(null, "Can't sent message to yourself!", "Error", JOptionPane.ERROR_MESSAGE);
					message.setText("");
					break;
				case 6:
					JOptionPane.showMessageDialog(null, "Please specify recepient.", "Error", JOptionPane.ERROR_MESSAGE);
					break;
				}
			}
		});
		btnSend.setBounds(281, 446, 117, 29);
		panel.add(btnSend);

		JLabel lblInbox = new JLabel("Inbox");
		lblInbox.setFont(new Font("Lucida Grande", Font.PLAIN, 17));
		lblInbox.setBounds(323, 112, 61, 29);
		panel.add(lblInbox);

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
		lblTo.setBounds(101, 339, 39, 16);
		panel.add(lblTo);

		txtReceiver = new JTextField();
		txtReceiver.setBounds(28, 361, 209, 28);
		panel.add(txtReceiver);
		txtReceiver.setColumns(10);

		String[] columns = {"From", "Date and Time", "Message"};
		DefaultTableModel dm = new DefaultTableModel(columns, 10);
  
		table = new JTable(dm);
//		ChatPanel.table.getColumnModel().getColumn(1).setMaxWidth(140);
//		ChatPanel.table.getColumnModel().getColumn(0).setMaxWidth(70);
        table.setPreferredScrollableViewportSize(table.getPreferredSize());

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(28, 153, 640, 120);
        panel.add( scrollPane );
        
        message = new JTextArea();
        message.setLineWrap(true);
        JScrollPane scrollPane_1 = new JScrollPane(message);
        scrollPane_1.setBounds(309, 361, 369, 79);
        scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane_1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        panel.add(scrollPane_1);
       
	}		
}
