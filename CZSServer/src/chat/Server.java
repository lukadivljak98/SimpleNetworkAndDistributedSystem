package chat;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

public class Server {

	private static final String KEYSTORE_PATH = "C:\\Users\\Luka\\Desktop\\MDP-Luka-Divljak\\CZSServer\\resources\\keystore.jks";
    private static final String KEYSTORE_PASS = "securemdp";
    private static final String PATH = "C:\\Users\\Luka\\Desktop\\MDP-Luka-Divljak\\CZSServer\\logs";
	
	public static boolean work = true;
	public static HashMap<String, ObjectOutputStream> outputs = new HashMap<>();
	
	static{
		try {
			Handler handler = new FileHandler(PATH + "CZSServer.log");
			Logger.getLogger("").addHandler(handler);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		try {
			System.setProperty("javax.net.ssl.keyStore", KEYSTORE_PATH);
			System.setProperty("javax.net.ssl.keyStorePassword", KEYSTORE_PASS);
			
			SSLServerSocketFactory factory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
			ServerSocket serverSocket = (SSLServerSocket) factory.createServerSocket(8443);
			while(work) {
				SSLSocket socket = (SSLSocket) serverSocket.accept();
				new ServerThread(socket).start();
			}
		} catch (IOException e) {
			Logger.getLogger("").log(Level.WARNING,e.fillInStackTrace().toString());
		}
	}
}
