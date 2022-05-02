package application;

import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import chat.ClientThread;
import model.Korisnik;

public class ChatController implements Initializable{
	
	@FXML
    public TextArea chatArea;
    @FXML
    private ComboBox<String> korisnikCb;
    @FXML
    private Button posaljiFajlBtn;
    @FXML
    private Button posaljiPorukuBtn;
    @FXML
    private Button procitajBtn;
    @FXML
    private ComboBox<String> stanicaCb;
    @FXML
    public TextArea textArea;
    @FXML
    public TextField textField = new TextField();
    @FXML
    private Button zapocniKomunikacijuBtn;
    
    private static final String USERS_DIR = "C:\\Users\\Luka\\Desktop\\MDP-Luka-Divljak\\CZSServer\\users";
    private static final String KEYSTORE_PATH = "C:\\Users\\Luka\\Desktop\\MDP-Luka-Divljak\\CZSServer\\resources\\keystore.jks";
    private static final String KEYSTORE_PASS = "securemdp";
    private ObservableList<String> korisniciIme = FXCollections.observableArrayList();
    private ObservableList<String> stanice = FXCollections.observableArrayList();
    private ArrayList<Korisnik> korisnici = new ArrayList<>();
    
    private ClientThread clientThread;
    private ObjectInputStream chatInput;
    private ObjectOutputStream chatOutput;
    private SSLSocket socket;
    
    public ChatController() {
    	try {
    		SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
    		socket = (SSLSocket) factory.createSocket("127.0.0.1", 8443);
    		chatInput = new ObjectInputStream(socket.getInputStream());
    		chatOutput = new ObjectOutputStream(socket.getOutputStream());
    		clientThread = new ClientThread(socket, chatInput, this);
    		clientThread.start();
    		chatOutput.writeUTF("HELLO#" + ZSController.korisnik.getUsername());
    		chatOutput.flush();
    	}catch(Exception e) {
    		Logger.getLogger("").log(Level.WARNING,e.fillInStackTrace().toString());
    	}
    }
	
	public void goBack(ActionEvent event) throws IOException {
		chatOutput.writeUTF("END#" + ZSController.korisnik.getUsername());
		chatOutput.flush();
		new Main().changeScene("ZS.fxml");
	}
	
	public void zapocniKumunikaciju(ActionEvent event) throws IOException {
		
	}
	
	public void posaljiPoruku(ActionEvent event) throws IOException {
		String otherKorisnik = korisnikCb.getSelectionModel().getSelectedItem();
		String message = "PORUKA#" + otherKorisnik + "#" + textField.getText() + "#" + ZSController.korisnik.getUsername();
		try {
			chatOutput.writeUTF(message);
			chatOutput.flush();
			chatArea.appendText(message.split("#")[2]);
			textField.clear();
		} catch(IOException e){
			Logger.getLogger("").log(Level.WARNING,e.fillInStackTrace().toString());
		}
	}
	
	public void procitaj(ActionEvent event) throws IOException {
		
	}
	
	public void posaljiFajl(ActionEvent event) throws IOException {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Izaberite fajl za poslati");
		File selectedFile = fileChooser.showOpenDialog(null);
		if(selectedFile != null) {
			FileInputStream fis = new FileInputStream(selectedFile.getAbsolutePath());
			String fileName = selectedFile.getName();
			String message = "FILE#" + korisnikCb.getSelectionModel().getSelectedItem() + "#" + fileName.substring(0, fileName.lastIndexOf(".")) + 
					"#"	+ getFileExtension(selectedFile) + "#" + selectedFile.length() + "#" + ZSController.korisnik.getUsername();
			chatOutput.writeUTF(message);
			chatOutput.flush();
			
			byte[] buffer = new byte[4096];
			int c = 0;
			while((c = fis.read(buffer)) > 0) {
				chatOutput.write(buffer, 0, c);
				chatOutput.flush();
			}
			fis.close();
		}
	}
	
    private String getFileExtension(File file) {
        String extension = "";
        try {
            if (file != null && file.exists()) {
                String name = file.getName();
                extension = name.substring(name.lastIndexOf("."));
            }
        } catch (Exception e) {
        	Logger.getLogger("").log(Level.WARNING,e.fillInStackTrace().toString());
        }
        return extension;
    }
	
	public void close() {
		try {
			chatOutput.close();
			chatInput.close();
			socket.close();
		} catch (IOException e) {
			Logger.getLogger("").log(Level.WARNING,e.fillInStackTrace().toString());
		}
	}
	
	public void addUsersToList() throws FileNotFoundException {
		File dir = new File(USERS_DIR);
		File[] files = dir.listFiles();
		for(File f : files) {
			XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(f)));
			Korisnik korisnik = (Korisnik) decoder.readObject();
			decoder.close();
			if(korisnik.isAktivan())
				korisniciIme.add(korisnik.getUsername());
			stanice.add(korisnik.getStanica());
			korisnici.add(korisnik);
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			addUsersToList();
			System.setProperty("javax.net.ssl.trustStore", KEYSTORE_PATH);
			System.setProperty("javax.net.ssl.trustStorePassword", KEYSTORE_PASS);
		} catch (FileNotFoundException e) {
			Logger.getLogger("").log(Level.WARNING,e.fillInStackTrace().toString());
		}
		korisnikCb.setItems(korisniciIme);
		stanicaCb.setItems(stanice);
	}
	
}
