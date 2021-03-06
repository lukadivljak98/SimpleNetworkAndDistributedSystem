package application;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import multicast.MCClientThread;

public class ObavjestenjaController {
	
	@FXML
	public TextArea obavjestenjaTA = new TextArea();

	@FXML
	private TextField obavjestenjaTF = new TextField();
	    
	private static final int PORT = 20000;
	private static final String HOST = "224.0.0.11";
	private static final int PORT2 = 9000;
	private ObjectOutputStream oos;
	private Socket socket2;

	public ObavjestenjaController() {
		try {
			MulticastSocket socket = new MulticastSocket(PORT);
			InetAddress address = InetAddress.getByName(HOST);
			new MCClientThread(socket, address, this).start();
			socket2 = new Socket("127.0.0.1", PORT2);
			oos = new ObjectOutputStream(socket2.getOutputStream());
			oos.writeUTF("HELLO#" + ZSController.korisnik.getUsername());
			oos.flush();
		} catch (IOException e) {
			Logger.getLogger("").log(Level.WARNING,e.fillInStackTrace().toString());
		}
	}
	
	public void goBack(ActionEvent event) throws IOException {
		oos.writeUTF("END" + "#" + ZSController.korisnik.getUsername());
		oos.flush();
		new Main().changeScene("ZS.fxml");
	}
	
	public void posaljiObavjestenje(ActionEvent event) throws IOException {
		String notification = obavjestenjaTF.getText();
		String message = "PORUKA#" + notification + "#" + ZSController.korisnik.getUsername();
		oos.writeUTF(message);
		oos.flush();
		obavjestenjaTF.clear();
	}
}
