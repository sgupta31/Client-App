package backend;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class TelecomClient {

	static String host = "dsp2014.ece.mcgill.ca";
	static int port = 5000;
	static Socket clientConnection;
	static DataInputStream in;
	static DataOutputStream out;

	static byte[] response;
	private static int respMsgType, respSubMsgType, respSize;
	private static String respMsgData = ""; 
	
	public static String[] messages;

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

		byte[] msg = new byte[4*3 + msgData.length];

		System.arraycopy(msgType, 0, msg, 0, 4);
		System.arraycopy(subMsgType, 0, msg, 4, 4);
		System.arraycopy(size, 0, msg, 8, 4);
		System.arraycopy(msgData, 0, msg, 12, msgData.length);

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
		
		return respSubMsgType;
	}

	public static int createUser(String username, String password) throws IOException {

		String payload;
		payload = username + "," + password;		
		return readWriteSocket(5, 0, payload.getBytes().length, payload);
	}

	public static int loginUser(String username, String password) throws IOException {

		String payload;
		payload = username + "," + password;
		return readWriteSocket(3, 0, payload.getBytes().length, payload);
	}

	public static int logoutUser() throws IOException {

		return readWriteSocket(4, 0, 0, "");
	}

	public static int deleteUser (String username) throws IOException {

		logoutUser();
		return readWriteSocket(6, 0, 0, "");
	}

	public static int queryMessages() throws IOException {

		byte[] msgTypeB = ByteBuffer.allocate(4).putInt(9).array();
		byte[] subMsgTypeB = ByteBuffer.allocate(4).putInt(0).array();
		byte[] sizeB = ByteBuffer.allocate(4).putInt(0).array();
		byte[] msgDataB = "".getBytes();

		int size;

		byte[] msg = createMessage(msgTypeB, subMsgTypeB, sizeB, msgDataB);

		out.write(msg);

		in.read(response, 0, 12);
		System.arraycopy(response, 0, msgTypeB, 0, 4);
		int readSoFar = 0;
		ArrayList<byte[]> messagesList = new ArrayList<byte[]>();

		while (msgTypeB != null) {
			
			System.arraycopy(response, 4, subMsgTypeB, 0, 4);
			if (ByteBuffer.wrap(subMsgTypeB).getInt() == 0) {
				return 0;

			} else {
				
				System.arraycopy(response, 8, sizeB, 0, 4);
				size = ByteBuffer.wrap(sizeB).getInt();
				readSoFar += size + 12;
				byte[] messagesB = new byte[size];
				
				System.arraycopy(response, 12, messagesB, 0, size);
				
				messagesList.add(messagesB);

				// Set up variables for the next message
				
				in.read(response, readSoFar, 12);
				System.arraycopy(response, 0, msgTypeB, 0, 4);
			}
		}
		messages = new String[messagesList.size()];
		messagesList.toArray(messages);
		return 1;
	}

	public static void echo(String payload) throws IOException {

		readWriteSocket(2, 0, payload.getBytes().length, payload);

		System.out.println("echo fct");
		System.out.println("sent msg: " + payload);
//		System.out.println("msg echoed: " + responseMsgData);
	}

	public static void exit() throws Exception {

		logoutUser();
		in.close();
		out.close();
		clientConnection.close();
	}


}