package backend;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;

public class TelecomClient {

	static String host = "dsp2014.ece.mcgill.ca";
	static int port = 5000;
	static Socket clientConnection;
	static DataInputStream in;
	static DataOutputStream out;
	
	private static int responseMsgType = 5000, responseSubMsgType = 5000, responseSize = 0;
	private static String responseMsgData = ""; 
	
	public static int readWriteSocket(int field1, int field2, int field3, String payload) throws IOException {

		byte[] msgType = ByteBuffer.allocate(4).putInt(field1).array();
		byte[] subMsgType = ByteBuffer.allocate(4).putInt(field2).array();
		byte[] size = ByteBuffer.allocate(4).putInt(field3).array();
		byte[] msgData = payload.getBytes();

		byte[] msg = createMessage(msgType, subMsgType, size, msgData);

		out.write(msg);
		
		// TO DO: read from server
		while (in.read() == 0) {
			
		}

		int result = ByteBuffer.wrap(subMsgType).getInt();
		return result;
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

	public static byte[] createMessage(byte[] msgType, byte[] subMsgType, byte[] size, byte[] msgData) {

		byte[] msg = new byte[4*3 + msgData.length];

		System.arraycopy(msgType, 0, msg, 0, 4);
		System.arraycopy(subMsgType, 0, msg, 4, 4);
		System.arraycopy(size, 0, msg, 8, 4);
		System.arraycopy(msgData, 0, msg, 12, msgData.length);

		return msg;
	}

	public static void connectToServer() throws IOException {

		try {
			InetAddress address = InetAddress.getByName(host);
			clientConnection = new Socket (address, port);
			
			in = new DataInputStream(clientConnection.getInputStream());
		
//			String line = br.readLine();
			
			//outStream = clientConnection.getOutputStream();
		//	ps = new PrintStream(outStream, true); // Second param: auto-flush on write = true
//			ps.println("Hello, Other side of the connection!");
			
			
			out = new DataOutputStream(clientConnection.getOutputStream());
			
		} catch (IOException e) {
			System.err.println("IOException: " + e);
		} catch (Exception exc) {
			System.err.println("Exception: " + exc);
		}
	}
	
	public static void echo(String payload) throws IOException {
		
		readWriteSocket(2, 0, payload.getBytes().length, payload);
		
		System.out.println("echo fct");
		System.out.println("sent msg: " + payload);
		System.out.println("msg echoed: " + responseMsgData);
	}

	public static void exit() throws Exception {
		
		logoutUser();
		in.close();
		out.close();
		clientConnection.close();
	}


}
