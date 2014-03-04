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
import javax.swing.JPasswordField;

public class LoginPanel extends JPanel {
	
	public static JButton btnLogin;
	private static RegistrationFrame regFrame;
	private JLabel lblUsername;
	private JTextField username;
	private JLabel lblPassword;
	private int loginResult;
	private JPasswordField password;

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

		JPanel panel = new JPanel();
		setLayout(null);
		setPreferredSize(new Dimension(444, 288));
		panel.setBounds(0, 6, 444, 288);
		add(panel);

		btnLogin = new JButton("Login");
		btnLogin.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {

				try {
					loginResult = TelecomClient.createUser(username.getText(), (password.getText()));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				switch (loginResult) {
				case 0:
					ClientApp.chatPanel = new ChatPanel(username.getText());
//					ClientApp.loggedInUser = username.getText();
					ClientApp.deck.add("chatPanel", ClientApp.chatPanel);
					((ClientApp) getTopLevelAncestor()).swapView("chatPanel");
					break;
				case 1:
					JOptionPane.showMessageDialog(null, "User already logged in!", "Error", JOptionPane.ERROR_MESSAGE);
					break;
				case 2:
					JOptionPane.showMessageDialog(null, "Please fill in the username and password fields", "Error", JOptionPane.ERROR_MESSAGE);
					break;
				case 3:
					JOptionPane.showMessageDialog(null, "User already logged in!", "Error", JOptionPane.ERROR_MESSAGE);
					break;
				}
			}
		});
		panel.setLayout(null);
		btnLogin.setFont(new Font("Lucida Grande", Font.PLAIN, 17));
		btnLogin.setBounds(164, 172, 90, 29);
		panel.add(btnLogin);

		JButton btnRegister = new JButton("Register");
		btnRegister.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				regFrame = new RegistrationFrame();
				regFrame.setVisible(true);

			}
		});
		btnRegister.setFont(new Font("Lucida Grande", Font.PLAIN, 17));
		btnRegister.setBounds(164, 223, 111, 29);
		panel.add(btnRegister);

		JLabel lblChatClient = new JLabel("Chat Client");
		lblChatClient.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		lblChatClient.setBounds(164, 31, 96, 22);
		panel.add(lblChatClient);

		lblUsername = new JLabel("Username:");
		lblUsername.setFont(new Font("Lucida Grande", Font.PLAIN, 17));
		lblUsername.setBounds(71, 78, 87, 21);
		panel.add(lblUsername);

		username = new JTextField();
		username.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		username.setBounds(233, 74, 154, 31);
		panel.add(username);
		username.setColumns(10);

		lblPassword = new JLabel("Password:");
		lblPassword.setFont(new Font("Lucida Grande", Font.PLAIN, 17));
		lblPassword.setBounds(71, 133, 82, 21);
		panel.add(lblPassword);
		
		password = new JPasswordField();
		password.setBounds(233, 129, 154, 31);
		panel.add(password);
	}
}
