package multicast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

import application.ObavjestenjaController;
import application.ZSController;

public class MCClientThread extends Thread {

	private MulticastSocket socket;
	private InetAddress address;
	private ObavjestenjaController controller;
	
	public MCClientThread(MulticastSocket socket, InetAddress address, ObavjestenjaController controller) {
		this.socket = socket;
		this.address = address;
		this.controller = controller;
	}
	
	public void run() {
		byte[] buffer = new byte[256];
		try {
			socket.joinGroup(address);
			while(true) {
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
				socket.receive(packet);
				String received = new String(packet.getData(), 0, packet.getLength());
				if(received.startsWith("DISCONNECT"))
					break;
				String notification = received.split("#")[1];
				String sender = received.split("#")[2];
				if(!sender.equals(ZSController.korisnik.getUsername()))
					controller.obavjestenjaTA.appendText(notification + "\n");
			}
			socket.close();
			
		} catch(IOException e) {
			Logger.getLogger("").log(Level.WARNING,e.fillInStackTrace().toString());
		}
	}
}
