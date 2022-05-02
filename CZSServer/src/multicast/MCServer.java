package multicast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MCServer {
	
	public static boolean work = true;
	public static HashMap<String, MulticastSocket> mcMap = new HashMap<>();
	private static final String PATH = "C:\\Users\\Luka\\Desktop\\MDP-Luka-Divljak\\CZSServer\\logs";
	
	static{
		try {
			Handler handler = new FileHandler(PATH + "CZSServerMC.log");
			Logger.getLogger("").addHandler(handler);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		
		try {
			ServerSocket serverSocket = new ServerSocket(9000);
			while(work) {
				Socket socket = serverSocket.accept();
				new MCServerThread(socket).start();
			}
		}catch (IOException e) {
			Logger.getLogger("").log(Level.WARNING,e.fillInStackTrace().toString());
		}
	}
}
