package application;

import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.xml.rpc.ServiceException;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import model.Korisnik;
import soap.SoapLogIn;
import soap.SoapLogInServiceLocator;

public class ZSController implements Initializable {
	
	private static final String USERS_DIR = "C:\\Users\\Luka\\Desktop\\MDP-Luka-Divljak\\CZSServer\\users";
	private static final String CONFIG_FILE = "C:\\Users\\Luka\\Desktop\\MDP-Luka-Divljak\\CZSServer\\resources\\config.properties";
	
	@FXML
    private Label zsLbl;
	@FXML
    private Button chatBtn;
	
	public static Korisnik korisnik;
	private Properties prop;
	private String stanica;
	
	public ZSController() throws FileNotFoundException {
		XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(new File(USERS_DIR + File.separator + LogInController.username + ".xml"))));
		this.korisnik = (Korisnik) decoder.readObject();
		decoder.close();
		this.korisnik.setAktivan(true);
		this.stanica = korisnik.getStanica();
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		zsLbl.setText("ZELJEZNICKA STANICA: " + stanica);
	}
	
	public void chat(ActionEvent event) throws IOException{
		Main main = new Main();
		main.changeScene("Chat.fxml");
	}
	
	public void obavjestenja(ActionEvent event) throws IOException{
		Main main = new Main();
		main.changeScene("Obavjestenja.fxml");
	}
	
	public void redVoznje(ActionEvent event) throws IOException {
		Main main = new Main();
		main.changeScene("RedVoznje.fxml");
	}
	
	public void slanjeIzvjestaja(ActionEvent event) throws IOException {
		Main main = new Main();
		main.changeScene("SlanjeIzvjestaja.fxml");
	}
	
	public void odjava(ActionEvent event) throws IOException, ServiceException{
		SoapLogInServiceLocator locator = new SoapLogInServiceLocator();
		SoapLogIn service = locator.getSoapLogIn();
		service.logOut(this.korisnik.getUsername());
        this.korisnik.setAktivan(false);
        Main main = new Main();
		main.changeScene("LogIn.fxml");
	}
}
