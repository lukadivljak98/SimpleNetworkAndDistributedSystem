package application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONObject;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import rmi.ArhivaInterface;

public class SlanjeIzvjestajaController implements Initializable {
	
	@FXML
    private TableColumn<String, String> izvjestajiColumn;

    @FXML
    private TextField nazivTf;

    @FXML
    private TableView<String> tableView;

    @FXML
    private TextArea textArea;
	
	private static final String PATH = "C:\\Users\\Luka\\Desktop\\MDP-Luka-Divljak\\AZSMDP\\resources";
	private ArhivaInterface arhiva;
	ObservableList<String> izvjestaji = FXCollections.observableArrayList();
	
	public SlanjeIzvjestajaController() {
		System.setProperty("java.security.policy", PATH + File.separator + "client_policyfile.txt");
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		try {
			String name = "ArhivaServer";
			Registry registry = LocateRegistry.getRegistry(1099);
			this.arhiva = (ArhivaInterface) registry.lookup(name);
		}catch(Exception e) {
			Logger.getLogger("").log(Level.WARNING,e.fillInStackTrace().toString());
		}
	}

	public void goBack(ActionEvent event) throws IOException {
		new Main().changeScene("ZS.fxml");
	}
	
	public void posaljiIzvjestaj() {
		String text = textArea.getText();
		String fileName = nazivTf.getText();
		String username = ZSController.korisnik.getUsername();
		try {
			arhiva.upload(text, fileName, username);
		} catch (RemoteException e) {
			Logger.getLogger("").log(Level.WARNING,e.fillInStackTrace().toString());
		}
	}

	public void preuzmiIzvjestaj() {
		String fileName = tableView.getSelectionModel().getSelectedItem();
		try {
			arhiva.download(fileName, ZSController.korisnik.getUsername());
		} catch (RemoteException e) {
			Logger.getLogger("").log(Level.WARNING,e.fillInStackTrace().toString());
		}
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ArrayList<File> list = null;
		try {
			list = arhiva.list();
		} catch (RemoteException e) {
			Logger.getLogger("").log(Level.WARNING,e.fillInStackTrace().toString());
		}
		for(File f : list)
			izvjestaji.add(f.getName());
		
		izvjestajiColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
		tableView.setItems(izvjestaji);
	}
}
