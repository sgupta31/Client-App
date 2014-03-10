package backend;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class TelecomClient {

	static String host = "dsp2014.ece.mcgill.ca";
	static int port = 5001;
	static Socket clientConnection;
	public static DataInputStream in;
	public static DataOutputStream out;

	private static int respMsgType, respSubMsgType, respSize;
	private static String echoResp = ""; 
	public static boolean userIsLoggedIn = false;

	public static String[] messages;
	public static String message;

	public static void connectToServer() throws IOException {

		try {
			InetAddress address = InetAddress.getByName(host);
			clientConnection = new Socket (address, port);
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
		if (ByteBuffer.wrap(msgType).getInt() != 9) 
			System.out.println("Created message: " + Arrays.toString(msg));
		return msg;
	}

	public static int readWriteSocket(int field1, int field2, int field3, String payload) throws IOException {

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

		in.read(response, 0, 12);
		System.arraycopy(response, 0, responseMsgType, 0, 4);
		System.arraycopy(response, 4, responseSubMsgType, 0, 4);
		System.arraycopy(response, 8, responseSize, 0, 4);

		respMsgType = ByteBuffer.wrap(responseMsgType).getInt();
		respSubMsgType = ByteBuffer.wrap(responseSubMsgType).getInt();
		respSize = ByteBuffer.wrap(responseSize).getInt();
		
		byte[] responseMsg = new byte[respSize];
		in.read(responseMsg, 0, respSize);

		// Check if it's a query messages request
		if (respMsgType == 9) {
			if (respSubMsgType == 1) {
				message = new String (responseMsg);
				System.out.println(message);
			}
		} else {
			if (respMsgType == 1) {
				echoResp = new String(responseMsg);
			}
			System.out.println("First 12 bytes of response: " + Arrays.toString(response));
			System.out.println("Response Text: " + new String(responseMsg));
		}
		return respSubMsgType;
	}

//	public static void retrieveMessages() throws IOException {
//		byte[] responseMsg = new byte[respSize];
//		in.read(responseMsg, 0, respSize);
//		System.out.println(new String(responseMsg));
//	}

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
		echo("echoooooo");
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

	public static void exit() throws Exception {
		
		readWriteSocket(0, 0, 1, " ");
		in.close();
		out.close();
		clientConnection.close();
	}


}