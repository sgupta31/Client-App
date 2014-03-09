package backend;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.TimerTask;

public class MessageQuery extends TimerTask implements Runnable {
	
	private static byte[] msgTypeB = ByteBuffer.allocate(4).putInt(9).array();
	private static byte[] subMsgTypeB = ByteBuffer.allocate(4).putInt(0).array();
	private static byte[] sizeB = ByteBuffer.allocate(4).putInt(1).array();
	private static byte[] msgDataB = " ".getBytes();
	private static byte[] response = new byte[12];
	
	int size;

	byte[] msg = TelecomClient.createMessage(msgTypeB, subMsgTypeB, sizeB, msgDataB);

//	out.write(msg);

	public void run() {
		
		try {
			TelecomClient.out.write(msg);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("smsmsm");
		try {
			if (TelecomClient.queryMessages() == 1) {
				for (int i = 0; i < TelecomClient.messages.length; i++)
					System.out.println(TelecomClient.messages[i]);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
