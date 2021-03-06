package multicast;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import chat.Server;

public class MCServerThread extends Thread{
	
	private Socket socket;
	private ObjectInputStream ois;
	private static final int PORT = 20000;
	private static final String HOST = "224.0.0.11";
	private MulticastSocket mcSocket;
	private InetAddress address;

	public MCServerThread(Socket socket) {
		try {
			this.socket = socket;
			this.ois = new ObjectInputStream(socket.getInputStream());
			this.mcSocket = new MulticastSocket();
			this.address = InetAddress.getByName(HOST);
		} catch (IOException e) {
			Logger.getLogger("").log(Level.WARNING,e.fillInStackTrace().toString());
		}
	}
	
	public void run() {
		try {
			mcSocket.joinGroup(address);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		while(true) {
			String request;
			String notification = "";
			try {
				request = ois.readUTF();
				if(request != null && request.startsWith("HELLO")) {
					MCServer.mcMap.put(request.split("#")[1], this.mcSocket);
				}
				else if(request != null && request.startsWith("PORUKA")) {
					notification = request.split("#")[1];
					String sender = request.split("#")[2];
					MulticastSocket removeSocket = MCServer.mcMap.get(sender);
//					if(removeSocket != null) {
//						removeSocket.leaveGroup(address);
//					}
					
					byte[] buffer = new byte[6];
					buffer = request.getBytes();
					DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, PORT);
					mcSocket.send(packet);
				}
				else if(request != null && request.startsWith("END")) {
					String username = request.split("#")[1];
					MulticastSocket socket = MCServer.mcMap.get(username);
					String message = "DISCONNECT";
					byte[] messageBytes = message.getBytes();
					DatagramPacket packet = new DatagramPacket(messageBytes, messageBytes.length, address, PORT);
					MCServer.mcMap.remove(username);
					if(MCServer.mcMap.isEmpty())
						MCServer.work = false;
					break;
				}
			} catch (IOException e) {
				Logger.getLogger("").log(Level.WARNING,e.fillInStackTrace().toString());
			}
		}
	}
}
