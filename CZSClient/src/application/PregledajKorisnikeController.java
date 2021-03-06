package application;

import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Korisnik;

public class PregledajKorisnikeController implements Initializable{

    @FXML
    private TableView<Korisnik> korisniciTable;

    @FXML
    private TableColumn<Korisnik, String> korisnikCol;

    @FXML
    private TableColumn<Korisnik, String> stanicaCol;
    
    private static final String USERS_DIR = "C:\\Users\\Luka\\Desktop\\MDP-Luka-Divljak\\CZSServer\\users";
    ObservableList<Korisnik> list = FXCollections.observableArrayList();
    
    public void goBack(ActionEvent event) throws IOException {
		new Main().changeScene("Sample.fxml");
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			addUsersToList();
		} catch (FileNotFoundException e) {
			Logger.getLogger("").log(Level.WARNING,e.fillInStackTrace().toString());
		}
		
		korisnikCol.setCellValueFactory(new PropertyValueFactory<Korisnik, String>("username"));
		stanicaCol.setCellValueFactory(new PropertyValueFactory<Korisnik, String>("stanica"));
		
		korisniciTable.setItems(list);
	}
	
	public void addUsersToList() throws FileNotFoundException {
		File dir = new File(USERS_DIR);
		File[] files = dir.listFiles();
		for(File f : files) {
			XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(f)));
			Korisnik korisnik = (Korisnik) decoder.readObject();
			decoder.close();
			
//			XStream xstream = new XStream();
//			Korisnik korisnik = (Korisnik) xstream.fromXML(f);
			list.add(korisnik);
		}
	}
}
