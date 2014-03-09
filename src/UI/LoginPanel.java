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
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

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
	private JButton btnExit;
	private JPanel panel;
	private static byte[] queryResp = new byte[12];
	private static byte[] querySMT = new byte[4];
	private static byte[] querySize = new byte[4];
	private static int queryMsgSize;
	private static ArrayList<String> messages;
	private byte[] response;

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
		setPreferredSize(new Dimension(600, 400));
		panel.setBounds(0, 6, 600, 400);
		add(panel);

		btnLogin = new JButton("Login");
		btnLogin.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {

				try {
					loginResult = TelecomClient.loginUser(username.getText(), (password.getText()));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				switch (loginResult) {
				case 0:
					ClientApp.chatPanel = new ChatPanel(username.getText());
<<<<<<< HEAD
					//					ClientApp.loggedInUser = username.getText();
=======
>>>>>>> 4ec3b981219eedb3a981ecb0315106b245aef4a4
					ClientApp.deck.add("chatPanel", ClientApp.chatPanel);
					((ClientApp) getTopLevelAncestor()).swapView("chatPanel");
					messages = new ArrayList<String>();
					Timer t = new Timer();
					t.scheduleAtFixedRate(
							new TimerTask()
							{
								public void run()
								{
									try {
										TelecomClient.out.write(ClientApp.queryMsg);
										while (TelecomClient.in.read(queryResp, 0, 12) != 0) {
											System.arraycopy(queryResp, 4, querySMT, 0, 4);
											if (ByteBuffer.wrap(querySMT).getInt() == 1) {
												System.arraycopy(queryResp, 8, querySize, 0, 4);
												queryMsgSize = ByteBuffer.wrap(querySize).getInt();
												byte[] msg = new byte[queryMsgSize];
												TelecomClient.in.read(msg, 0, queryMsgSize);
												System.out.println(new String(msg));
											}
										}
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							},
							0,      // run first occurrence immediately
							1000); 
					break;
				case 1:
					JOptionPane.showMessageDialog(null, "User already logged in!", "Error", JOptionPane.ERROR_MESSAGE);
					break;
				case 2:
					JOptionPane.showMessageDialog(null, "Bad credentials", "Error", JOptionPane.ERROR_MESSAGE);
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

		btnExit = new JButton("Exit");
		btnExit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					TelecomClient.exit();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnExit.setBounds(158, 279, 117, 29);
		panel.add(btnExit);
	}
}
