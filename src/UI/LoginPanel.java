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
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JTextField;

import org.apache.commons.lang3.StringUtils;

import javax.swing.JPanel;
import javax.swing.JPasswordField;

public class LoginPanel extends JPanel {

	private static RegistrationFrame regFrame;
	private JLabel lblUsername;
	public static JTextField username;
	private JLabel lblPassword;
	private int loginResult;
	public static JPasswordField password;
	public static JButton btnLogin;
	public static JButton btnRegister;
	private JButton btnExit;
	private JPanel panel;
	public static boolean buttonAsDisconnect = true;

	/**
	 * Create the application.
	 */
	public LoginPanel() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		panel = new JPanel();
		setLayout(null);
		setPreferredSize(new Dimension(700, 500));
		panel.setBounds(0, 6, 700, 500);
		add(panel);

		btnLogin = new JButton("Login");
		btnLogin.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (StringUtils.isBlank(username.getText())) {
					JOptionPane.showMessageDialog(null, "Please provide a username.", "Warning",
							JOptionPane.WARNING_MESSAGE);
				} else {
					if (StringUtils.isBlank(password.getText())) {
						JOptionPane.showMessageDialog(null, "Please provide a password.", "Warning",
								JOptionPane.WARNING_MESSAGE);
					} else {

						try {
							loginResult = TelecomClient.loginUser(username.getText(), (password.getText()));
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						switch (loginResult) {
						case 0:
							ClientApp.chatPanel = new ChatPanel(username.getText());
							ClientApp.deck.add("chatPanel", ClientApp.chatPanel);
							((ClientApp) getTopLevelAncestor()).swapView("chatPanel");
							ClientApp.inChatPanel = true;
							TelecomClient.userIsLoggedIn = true;
							Timer timer = new Timer();
							MessageQuery task = new MessageQuery();
							timer.scheduleAtFixedRate(task, 1000, 1000); 
							break;
						case 1:
							JOptionPane.showMessageDialog(null, "User already logged in!", "Error", JOptionPane.ERROR_MESSAGE);
							username.setText("");
							password.setText("");
							break;
						case 2:
							JOptionPane.showMessageDialog(null, "Bad credentials", "Error", JOptionPane.ERROR_MESSAGE);
							username.setText("");
							password.setText("");
							break;
						case 3:
							JOptionPane.showMessageDialog(null, "User already logged in!", "Error", JOptionPane.ERROR_MESSAGE);
							username.setText("");
							password.setText("");
							break;
						}
					}
				}
			}
		});
		panel.setLayout(null);
		btnLogin.setFont(new Font("Lucida Grande", Font.PLAIN, 17));
		btnLogin.setBounds(282, 260, 111, 29);
		panel.add(btnLogin);

		btnRegister = new JButton("Register");
		btnRegister.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				regFrame = new RegistrationFrame();
				regFrame.setVisible(true);

			}
		});
		btnRegister.setFont(new Font("Lucida Grande", Font.PLAIN, 17));
		btnRegister.setBounds(282, 334, 111, 29);
		panel.add(btnRegister);

		JLabel lblChatClient = new JLabel("Chat Client");
		lblChatClient.setFont(new Font("Lucida Grande", Font.PLAIN, 22));
		lblChatClient.setBounds(286, 54, 154, 36);
		panel.add(lblChatClient);

		lblUsername = new JLabel("Username:");
		lblUsername.setFont(new Font("Lucida Grande", Font.PLAIN, 17));
		lblUsername.setBounds(170, 131, 87, 21);
		panel.add(lblUsername);

		username = new JTextField();
		username.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		username.setBounds(392, 127, 154, 31);
		panel.add(username);
		username.setColumns(10);

		lblPassword = new JLabel("Password:");
		lblPassword.setFont(new Font("Lucida Grande", Font.PLAIN, 17));
		lblPassword.setBounds(170, 185, 82, 21);
		panel.add(lblPassword);

		password = new JPasswordField();
		password.setBounds(392, 182, 154, 31);
		panel.add(password);

		btnExit = new JButton("Close connection");
		btnExit.setFont(new Font("Lucida Grande", Font.PLAIN, 17));
		btnExit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (buttonAsDisconnect) {
					btnLogin.setEnabled(false);
					btnRegister.setEnabled(false);
					username.setEnabled(false);
					password.setEnabled(false);
					buttonAsDisconnect = false;
					username.setText("");
					password.setText("");
					btnExit.setText("Connect!");
					try {
						TelecomClient.exit();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				} else {
					try {
						TelecomClient.connectToServer();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					btnLogin.setEnabled(true);
					btnRegister.setEnabled(true);
					username.setEnabled(true);
					password.setEnabled(true);
					buttonAsDisconnect = true;
					btnExit.setText("Close connection");
				}
			}
		});
		btnExit.setBounds(254, 405, 169, 36);
		panel.add(btnExit);
	}

	class MessageQuery extends TimerTask {

		public void run() {
			if (!TelecomClient.userIsLoggedIn) {
				this.cancel();
			}
			try {
				TelecomClient.readWriteSocket(9, 0, 1, " ");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
