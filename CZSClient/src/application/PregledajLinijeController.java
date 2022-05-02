package application;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.google.gson.Gson;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Linija;
import redis.clients.jedis.Jedis;

public class PregledajLinijeController implements Initializable{
	
	private static String instanceName = "Lines";
	private Gson json = new Gson();

    @FXML
    private TableColumn<Linija, String> nazivCol;

    @FXML
    private TableColumn<Linija, String> staniceCol;
    
    @FXML
    private TableView<Linija> linijeTable;
    
    ObservableList<Linija> list = FXCollections.observableArrayList();

    public void goBack(ActionEvent event) throws IOException {
		new Main().changeScene("Sample.fxml");
	}
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		addLinesToList();
		
		nazivCol.setCellValueFactory(new PropertyValueFactory<Linija, String>("naziv"));
		staniceCol.setCellValueFactory(new PropertyValueFactory<Linija, String>("linija"));
		
		linijeTable.setItems(list);
	}
	
	public void addLinesToList() {
		List<String> response;
		try (Jedis jedis = Main.pool.getResource()) {
			response = jedis.lrange(instanceName, 0, -1);
			for(String s : response) {
				Linija line = json.fromJson(s, Linija.class);
				
				list.add(line);
			}
		}
	}
}
