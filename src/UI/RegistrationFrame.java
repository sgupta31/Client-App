package UI;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.JButton;

import backend.TelecomClient;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

public class RegistrationFrame extends JFrame {
	
	private int registerResult;

	private JPanel contentPane;
	private JTextField username;
	private JTextField password;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RegistrationFrame frame = new RegistrationFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public RegistrationFrame() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		username = new JTextField();
		username.setBounds(229, 63, 134, 28);
		contentPane.add(username);
		username.setColumns(10);
		
		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setFont(new Font("Lucida Grande", Font.PLAIN, 17));
		lblUsername.setBounds(76, 54, 96, 42);
		contentPane.add(lblUsername);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setFont(new Font("Lucida Grande", Font.PLAIN, 17));
		lblPassword.setBounds(76, 124, 120, 28);
		contentPane.add(lblPassword);
		
		password = new JTextField();
		password.setBounds(229, 126, 134, 28);
		contentPane.add(password);
		password.setColumns(10);
		
		JButton btnRegister = new JButton("Register");
		btnRegister.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				if (StringUtils.isBlank(username.getText())) {
					JOptionPane.showMessageDialog(null, "Please provide a username.", "Warning",
							JOptionPane.WARNING_MESSAGE);
				} else {
					if (!StringUtils.isAlphanumeric(username.getText())) {
						JOptionPane.showMessageDialog(null, "Username must only contain letters and numbers.",
										"Warning", JOptionPane.WARNING_MESSAGE);
					} else {
						if (StringUtils.isBlank(password.getText())) {
							JOptionPane.showMessageDialog(null, "Please provide a password.", "Warning",
									JOptionPane.WARNING_MESSAGE);
						} else {
							try {
								registerResult = TelecomClient.registerUser(username.getText(),
										(password.getText()));
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							switch (registerResult) {
							case 0:
								JOptionPane.showMessageDialog(null, "Success!");
								dispose();
								LoginPanel.btnLogin.setEnabled(true);
								break;
							case 1:
								JOptionPane.showMessageDialog(null, "User already exists!", "Error", JOptionPane.ERROR_MESSAGE);
								break;
							case 2:
								JOptionPane.showMessageDialog(null, "User already logged in!", "Error", JOptionPane.ERROR_MESSAGE);
								break;
							case 3:
								JOptionPane.showMessageDialog(null, "Badly formatted user request!", "Error", JOptionPane.ERROR_MESSAGE);
								break;
							}
						}
					}
				}
			}
		});
		btnRegister.setFont(new Font("Lucida Grande", Font.PLAIN, 17));
		btnRegister.setBounds(154, 210, 125, 35);
		contentPane.add(btnRegister);
	}
}
