package backend;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class TelecomClient {

	String host = "dsp2014.ece.mcgill.ca";
	int port = 5000;

	public void connectToServer() {

		try {
			InetAddress address = InetAddress.getByName(host);
			Socket clientConnection = new Socket (address, port);
		} catch (IOException e) {
			System.err.println("IOException: " + e);
		} catch (Exception exc) {
			System.err.println("Exception: " + exc);
		}

	}


}
