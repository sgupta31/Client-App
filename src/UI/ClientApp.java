package UI;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BoxLayout;

import backend.TelecomClient;


public class ClientApp extends JFrame {

	public static LoginPanel loginPanel;
	public static ChatPanel chatPanel;
	static CardLayout cardLayout;
	public static JPanel deck = new JPanel();
	private JPanel contentPane;
	public static int x;
	public static int y;
	public static String loggedInUser = "";
	public static byte[] queryMsg;

	public static void main(String[] args) {
		
		
		byte[] msgType = ByteBuffer.allocate(4).putInt(9).array();
		byte[] subMsgType = ByteBuffer.allocate(4).putInt(0).array();
		byte[] size = ByteBuffer.allocate(4).putInt(1).array();
		byte[] msgData = " ".getBytes();

		queryMsg = TelecomClient.createMessage(msgType, subMsgType, size, msgData);
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientApp frame = new ClientApp();
					frame.setVisible(true);
					TelecomClient.connectToServer();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public ClientApp() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = (JPanel) getContentPane();
		setResizable(false);

		/*
		 * Create the default grid used if the players do not select any of the
		 * custom grids
		 */

		deck.setLayout(cardLayout = new CardLayout());

		LoginPanel loginPanel = new LoginPanel();
		deck.add("loginPanel", loginPanel);
		loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.X_AXIS));
		
//		chatPanel = new ChatPanel();
//		deck.add("topTenPanel", chatPanel);

		// Default card
		cardLayout.show(deck, "loginPanel");

		// Set contentPane to add static card panel
		contentPane.add(deck);

		// Set frame parameters
		pack();
		
		// Center frame
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		x = (int) ((dimension.getWidth() - getWidth()) / 2);
		y = (int) ((dimension.getHeight() - getHeight()) / 2);
		setLocation(x, y);

	}
	/**
	 * Method used to swap the panels based on user input
	 * @param key the name of the next panel to display
	 * @return void
	 */
	public void swapView(String key) {
		cardLayout.show(deck, key);
	}

}
