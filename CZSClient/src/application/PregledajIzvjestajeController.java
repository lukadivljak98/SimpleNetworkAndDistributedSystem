package application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import rmi.ArhivaInterface;

public class PregledajIzvjestajeController implements Initializable {
	
	@FXML
    private TableView<String> tableView;
	@FXML
    private TableColumn<String, String> izvjestajiColumn;

	private static final String PATH = "C:\\Users\\Luka\\Desktop\\MDP-Luka-Divljak\\AZSMDP\\resources";
	private ArhivaInterface arhiva;
	ObservableList<String> izvjestaji = FXCollections.observableArrayList();
	
	public PregledajIzvjestajeController() {
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
		new Main().changeScene("Sample.fxml");
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
