package backend;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;

public class TelecomClient {

	static String host = "dsp2014.ece.mcgill.ca";
	static int port = 5000;
	static Socket clientConnection;
//	static BufferedWriter out;
//	static BufferedReader br ;
//	static OutputStream outStream;
//	static PrintStream ps;
	static DataInputStream in;
	static DataOutputStream out;

	public static int registerUser(String username, String password) throws IOException {

		String payload;

		payload = username + "," + password;

		byte[] msgType = ByteBuffer.allocate(4).putInt(5).array();
		byte[] subMsgType = ByteBuffer.allocate(4).putInt(0).array();
		byte[] msgData = payload.getBytes();
		byte[] size = ByteBuffer.allocate(4).putInt(msgData.length).array();

		byte[] msg = createMessage(msgType, subMsgType, size, msgData);

		out.write(msg);
		
		// TO DO: read from server
		while (in.read() == 0) {
			
		}

		int registerResult = ByteBuffer.wrap(subMsgType).getInt();
		return registerResult;
	}

	public static int loginUser(String username, String password) throws IOException {

		String payload;

		payload = username + "," + password;

		byte[] msgType = ByteBuffer.allocate(4).putInt(5).array();
		byte[] subMsgType = ByteBuffer.allocate(4).putInt(0).array();
		byte[] msgData = payload.getBytes();
		byte[] size = ByteBuffer.allocate(4).putInt(msgData.length).array();

		byte[] msg = createMessage(msgType, subMsgType, size, msgData);
		
		out.write(msg);
		
		// TO DO: read from server
		while (in.read() == 0) {
			
		}

		int loginResult = ByteBuffer.wrap(subMsgType).getInt();
		return loginResult;
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

	public static void closeSocket() throws Exception {
		clientConnection.close();
		System.out.println("hhrhrhg");
	}


}
