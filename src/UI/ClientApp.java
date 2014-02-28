package UI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;

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
import javax.swing.JTextField;

public class ClientApp {

	private static JFrame frame;
	public static JButton btnLogin;
	private static RegistrationFrame regFrame;
	private JLabel lblUsername;
	private JTextField username;
	private JLabel lblPassword;
	private JTextField password;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TelecomClient.connectToServer();
					ClientApp window = new ClientApp();
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
	public ClientApp() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		btnLogin = new JButton("Login");
		btnLogin.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				TelecomClient.loginUser(username.getText(), password.getText());
				
			}
		});
		btnLogin.setFont(new Font("Lucida Grande", Font.PLAIN, 17));
		btnLogin.setBounds(100, 191, 106, 36);
		frame.getContentPane().add(btnLogin);
		
		JButton btnRegister = new JButton("Register");
		btnRegister.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {

//				frame.setEnabled(t);
				regFrame = new RegistrationFrame();
				regFrame.setVisible(true);
				//btnLogin.setEnabled(false);
				
			}
		});
		btnRegister.setFont(new Font("Lucida Grande", Font.PLAIN, 17));
		btnRegister.setBounds(259, 191, 111, 36);
		frame.getContentPane().add(btnRegister);
		
		JLabel lblChatClient = new JLabel("Chat Client");
		lblChatClient.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
		lblChatClient.setBounds(172, 6, 118, 60);
		frame.getContentPane().add(lblChatClient);
		
		lblUsername = new JLabel("Username:");
		lblUsername.setFont(new Font("Lucida Grande", Font.PLAIN, 17));
		lblUsername.setBounds(74, 78, 102, 36);
		frame.getContentPane().add(lblUsername);
		
		username = new JTextField();
		username.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		username.setBounds(259, 83, 134, 28);
		frame.getContentPane().add(username);
		username.setColumns(10);
		
		lblPassword = new JLabel("Password:");
		lblPassword.setFont(new Font("Lucida Grande", Font.PLAIN, 17));
		lblPassword.setBounds(74, 144, 91, 23);
		frame.getContentPane().add(lblPassword);
		
		password = new JTextField();
		password.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		password.setBounds(259, 142, 134, 28);
		frame.getContentPane().add(password);
		password.setColumns(10);
	}
}
