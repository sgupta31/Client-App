package backend;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import UI.ChatPanel;
import UI.ClientApp;
import UI.LoginPanel;

public class TelecomClient {

	//static String host = "dsp2014.ece.mcgill.ca";
	static String host = "192.168.0.102";
	static int port = 5000;
	static Socket clientConnection;
	public static DataInputStream in;
	public static DataOutputStream out;

	private static int respMsgType, respSubMsgType, respSize;
	private static String echoResp = ""; 
	public static boolean userIsLoggedIn = false;

	public static int messageCount = 0;
	public static String message;

	public static void connectToServer() throws IOException {

		try {
			InetAddress address = InetAddress.getByName(host);
			clientConnection = new Socket (host, port);
			in = new DataInputStream(clientConnection.getInputStream());
			out = new DataOutputStream(clientConnection.getOutputStream());

		} catch (IOException e) {
			System.err.println("IOException: " + e);
		} catch (Exception exc) {
			System.err.println("Exception: " + exc);
		}
	}

	public static byte[] createMessage(byte[] msgType, byte[] subMsgType, byte[] size, byte[] msgData) {

		byte[] msg = new byte[12 + msgData.length];

		System.arraycopy(msgType, 0, msg, 0, 4);
		System.arraycopy(subMsgType, 0, msg, 4, 4);
		System.arraycopy(size, 0, msg, 8, 4);
		System.arraycopy(msgData, 0, msg, 12, msgData.length);
		if (ByteBuffer.wrap(msgType).getInt() != 9 && ByteBuffer.wrap(msgType).getInt() != 2) 
			System.out.println("Created message: " + Arrays.toString(msg));
		return msg;
	}

	public static int readWriteSocket(int field1, int field2, int field3, String payload) throws IOException {

		int localRespMsgType, localRespSubMsgType, localRespSize;

		byte[] msgType = ByteBuffer.allocate(4).putInt(field1).array();
		byte[] subMsgType = ByteBuffer.allocate(4).putInt(field2).array();
		byte[] size = ByteBuffer.allocate(4).putInt(field3).array();
		byte[] msgData = payload.getBytes();

		byte[] msg = createMessage(msgType, subMsgType, size, msgData);

		out.write(msg);

		byte[] response = new byte[12];
		byte[] responseMsgType = new byte[4];
		byte[] responseSubMsgType = new byte[4];
		byte[] responseSize = new byte[4];
		byte[] responseMsg;

		in.read(response, 0, 12);

		System.arraycopy(response, 0, responseMsgType, 0, 4);
		System.arraycopy(response, 4, responseSubMsgType, 0, 4);
		System.arraycopy(response, 8, responseSize, 0, 4);

		respMsgType = ByteBuffer.wrap(responseMsgType).getInt();
		respSubMsgType = ByteBuffer.wrap(responseSubMsgType).getInt();
		respSize = ByteBuffer.wrap(responseSize).getInt();

		System.out.println("resp size: " + respSize + " for " + respMsgType);
		responseMsg = new byte[respSize];		
		in.read(responseMsg, 0, respSize);
		System.out.println("resp: " + new String(responseMsg));

		// Check if it's a query messages request
		if (respMsgType == 9) {
			while (respMsgType == 9) {
				if (respSubMsgType == 1) {
					if (messageCount == 10) {
						clearMsgTable();
						messageCount = 0;
					}
					writeMsgInTable(responseMsg);
					messageCount++;
					if (in.available() >= 1) {
						int read = in.read(response, 0, 12);
						System.out.println("read " + read);
						System.arraycopy(response, 0, responseMsgType, 0, 4);
						System.arraycopy(response, 4, responseSubMsgType, 0, 4);
						System.arraycopy(response, 8, responseSize, 0, 4);

						localRespMsgType = ByteBuffer.wrap(responseMsgType).getInt();
						localRespSubMsgType = ByteBuffer.wrap(responseSubMsgType).getInt();
						localRespSize = ByteBuffer.wrap(responseSize).getInt();
						responseMsg = new byte[localRespSize];
						in.read(responseMsg, 0, localRespSize);
						System.out.println(new String(responseMsg));
					} else {
						break;
					}
				} else {
					break;
				}
			}
		} else {
			if ((respMsgType == 4) && (field1 != 4))
				if (respSubMsgType == 2) {
					ClientApp.loggedInUser = "";
					LoginPanel.username.setText("");
					LoginPanel.password.setText("");
					((ClientApp) ClientApp.chatPanel.getTopLevelAncestor()).swapView("loginPanel");
				}
			System.out.println("First 12 bytes of response: " + Arrays.toString(response));
			System.out.println("Response Text: " + new String(responseMsg));
		} 
		return respSubMsgType;
}

	public static void writeMsgInTable(byte[] message) throws IOException {

		String msgAsString = new String(message);
		String[] parsedMsg = new String[3];

		int comma = msgAsString.indexOf(",");
		parsedMsg[0] = msgAsString.substring(0, comma);
		msgAsString = msgAsString.substring(comma + 1);

		comma = msgAsString.indexOf(",");
		parsedMsg[1] = msgAsString.substring(0, comma);
		parsedMsg[2] = msgAsString.substring(comma + 1);

		String inbox = checkMsg(parsedMsg[2]);

		ChatPanel.table.setValueAt(parsedMsg[0], messageCount, 0);
		ChatPanel.table.setValueAt(parsedMsg[1], messageCount, 1);
		ChatPanel.table.setValueAt(inbox, messageCount, 2);
	}

	public static void clearMsgTable() {
		for (int i = 0; i < 10; i++) 
			for (int j = 0; j < 3; j++) {
				ChatPanel.table.setValueAt("", i, j);
			}
	}

	public static int createUser(String username, String password) throws IOException {

		String payload;
		payload = username + "," + password;		
		return readWriteSocket(5, 0, payload.getBytes().length, payload);
	}

	public static int loginUser(String username, String password) throws IOException {

		String payload;
		payload = username + "," + password;
		int login = readWriteSocket(3, 0, payload.getBytes().length, payload);
		int createStore = readWriteSocket(7, 0 , 1, " ");
		switch (createStore) {
		case 0:
			System.out.println("Store created successfully.");
			break;
		case 1:
			System.out.println("Store already exists.");
			break;
		case 2:
			System.out.println("Not logged in.");
			break;
		}
		//		echo("echoooooo");
		return login;
	}

	public static int logoutUser() throws IOException {

		userIsLoggedIn = false;
		return readWriteSocket(4, 0, 1, " ");
	}

	public static int deleteUser () throws IOException {

		return readWriteSocket(6, 0, 1, " ");
	}

	public static int sendMessage(String toUser, String message) throws IOException {

		String payload = toUser + "," + message;
		return readWriteSocket(8, 0, payload.getBytes().length, payload);
	}

	public static void echo(String payload) throws IOException {

		readWriteSocket(2, 0, payload.getBytes().length, payload);
	}

	public static String checkMsg(String text) {
		return text.replace("'", "\\'");
	}

	public static void exit() throws Exception {

		readWriteSocket(0, 0, 1, " ");
		in.close();
		out.close();
		clientConnection.close();
	}


}