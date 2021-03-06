package soap;

import model.Korisnik;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Properties;

public class SoapLogIn {
	
	private static final String USERS_DIR = "C:\\Users\\Luka\\Desktop\\MDP-Luka-Divljak\\CZSServer\\users";
	private static final String CONFIG_FILE = "C:\\Users\\Luka\\Desktop\\MDP-Luka-Divljak\\CZSServer\\resources\\config.properties";
	
	public boolean checkLogIn(String username, String password) throws FileNotFoundException, NoSuchAlgorithmException {
		
		File file = new File(USERS_DIR + File.separator + username + ".xml");
		if(!file.exists())
			return false;
		
		XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(file)));
		Korisnik korisnik = (Korisnik) decoder.readObject();
		decoder.close();
		
//		XStream xstream = new XStream();
//		Korisnik korisnik = (Korisnik) xstream.fromXML(file);
		
		String realPasswordHash = korisnik.getPasswordHash();
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		String maybePasswordHash = Base64.getEncoder().encodeToString(digest.digest(password.getBytes()));
		
		if(realPasswordHash.equals(maybePasswordHash)) {
			korisnik.setAktivan(true);
			sacuvaj(korisnik, username);
			 try (InputStream input = new FileInputStream(CONFIG_FILE)) {
		            Properties prop = new Properties();
		            prop.load(input);
		            int br = Integer.valueOf(prop.getProperty("brAktivnihKorisnika"));
		            prop.setProperty("brAktivnihKorisnika", String.valueOf(++br));

		        } catch (IOException ex) {
		            ex.printStackTrace();
		        }
			return true;
		}
		else return false;
	}
	
	public void sacuvaj(Korisnik korisnik, String username) {
		XMLEncoder encoder;
		try {
			encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(USERS_DIR + File.separator + username + ".xml")));
			encoder.writeObject(korisnik);
			encoder.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void logOut(String username) throws FileNotFoundException {
		File file = new File(USERS_DIR + File.separator + username + ".xml");
		XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(file)));
		Korisnik korisnik = (Korisnik) decoder.readObject();
		decoder.close();
		korisnik.setAktivan(false);
		sacuvaj(korisnik, username);
		try (InputStream input = new FileInputStream(CONFIG_FILE)) {
            Properties prop = new Properties();
            prop.load(input);
            int br = Integer.valueOf(prop.getProperty("brAktivnihKorisnika"));
            prop.setProperty("brAktivnihKorisnika", String.valueOf(--br));

        } catch (IOException ex) {
            ex.printStackTrace();
        }
	}
}
