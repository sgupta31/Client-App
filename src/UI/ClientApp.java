package UI;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

import javax.swing.BoxLayout;


public class ClientApp extends JFrame {

	public static LoginPanel loginPanel;
	//public static ChatPanel chatPanel;
	static CardLayout cardLayout;
	public static JPanel deck = new JPanel();
	private JPanel contentPane;
	public static int x;
	public static int y;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientApp frame = new ClientApp();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public ClientApp() {

		//super("Light Battles Demo");

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
