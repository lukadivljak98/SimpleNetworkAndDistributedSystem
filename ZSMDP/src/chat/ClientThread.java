package chat;

import java.io.EOFException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.SSLSocket;

import application.ChatController;

public class ClientThread extends Thread {
	
	private SSLSocket socket;
	private ObjectInputStream chatInput;
	private ChatController chatController;
	
	private static final String DOWNLOAD_PATH = "C:\\Users\\Luka\\Desktop\\MDP-Luka-Divljak\\ZSMDP";

	public ClientThread(SSLSocket socket, ObjectInputStream chatInput, ChatController chatController) {
		this.socket = socket;
		this.chatInput = chatInput;
		this.chatController = chatController;
	}
	
	public void run() {
		while(true) {
			String message;
			try {
				message = chatInput.readUTF();
				if("DISCONNECT".equals(message))
					break;
				else if(message.startsWith("PORUKA#")) {
					chatController.chatArea.appendText("\n" + message.split("#")[3] + ": " + message.split("#")[2] + "\n");
				}
				else if (message.startsWith("FILE#")) {
					String reciever = message.split("#")[1];
					String fileName = message.split("#")[2];
					String extension = message.split("#")[3];
					Long fileSize = Long.parseLong(message.split("#")[4]);
					String sender = message.split("#")[5];
					chatController.chatArea.appendText("\nKorisnik " + sender + " vam salje fajl " + fileName + extension + "\n");
					
					File fileDir = new File(DOWNLOAD_PATH + File.separator + reciever);
					if(!fileDir.exists())
						fileDir.mkdir();
					FileOutputStream fos = new FileOutputStream(DOWNLOAD_PATH + File.separator + reciever + File.separator + fileName + extension);
					
					byte[] buffer = new byte[2*1024*1024];
					int c, count = 0;
					while((c = chatInput.read(buffer)) > 0) {
						fos.write(buffer, 0, c);
						fos.flush();
						count += c;
						if(fileSize == count)
							break;
					}
					chatController.chatArea.appendText("Fajl je preuzet u vas direktorijum!");
					fos.close();
				}
				
			} catch(IOException e) {
				Logger.getLogger("").log(Level.WARNING,e.fillInStackTrace().toString());
			}
		}
		chatController.close();
	}
}
