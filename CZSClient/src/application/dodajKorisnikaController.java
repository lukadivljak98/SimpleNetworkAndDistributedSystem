package application;

import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

//import javax.xml.rpc.ServiceException;

//import org.bouncycastle.util.encoders.Hex;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.Korisnik;

public class dodajKorisnikaController {
	
	@FXML
	private TextField korisnickoImeTf;
	@FXML
	private PasswordField lozinkaTf;
	@FXML
	private TextField stanicaTf;
	
	private static final String USERS_DIR = "C:\\Users\\Luka\\Desktop\\MDP-Luka-Divljak\\CZSServer\\users";
	private static final String PATH = "C:\\Users\\Luka\\Desktop\\MDP-Luka-Divljak\\ZSMDP";

	public void sacuvajKorisnika(ActionEvent event) throws IOException, NoSuchAlgorithmException {
		String username = korisnickoImeTf.getText();
		String password = lozinkaTf.getText();
		String stanica = stanicaTf.getText();
		saveUser(username, password, stanica);
	}
	
	public void goBack(ActionEvent event) throws IOException {
		new Main().changeScene("Sample.fxml");
	}

	public boolean saveUser(String username, String password, String stanica) throws NoSuchAlgorithmException, FileNotFoundException {
		
		File dir = new File(USERS_DIR);
		File dir2 = new File(PATH + File.separator + username);
		String[] fileNames = dir.list();
		boolean postoji = false;
		if(fileNames != null) {
			for(String name : fileNames) {
				if(name.equals(username + ".xml")) {
					postoji = true;
					break;
				}
			}
		}
		
		if(!postoji) {
			//String salt = getNextSalt();
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			String passwordHash = Base64.getEncoder().encodeToString(digest.digest(password.getBytes()));
			Korisnik korisnik = new Korisnik(username, passwordHash, stanica);
			dir2.mkdir();
			//addPort(korisnik);
		
			XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(USERS_DIR + File.separator + username + ".xml")));
			encoder.writeObject(korisnik);
			encoder.close();
			
//			XStream xstream = new XStream();
//			xstream.toXML(korisnik, new BufferedOutputStream(new FileOutputStream(USERS_DIR + File.separator + username + ".xml")));
			return true;
		}else
			return false;
	}
	
	public void addPort(Korisnik user) {
		File file = new File(USERS_DIR);
		int numOfFiles = file.list().length;
		int portNumber = 9000 + (++numOfFiles);
		user.setPort(portNumber);
	}
	
//	public static String hashText(String s) throws NoSuchAlgorithmException {
//		
//		MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
//		byte[] input = s.getBytes();
//		byte[] output = messageDigest.digest(input);
//		
//		return Hex.toHexString(output);
//	}
	
//	public static String getNextSalt() {
//		byte[] salt = new byte[16];
//		RANDOM.nextBytes(salt);
//		return Hex.toHexString(salt);
//	}
}
