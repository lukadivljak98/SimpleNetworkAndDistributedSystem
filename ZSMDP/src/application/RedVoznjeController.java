package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
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
import model.Linija;
import model.StanicaDolazak;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

public class RedVoznjeController implements Initializable{
	
	@FXML
    private TableColumn<Linija, String> LinijaColumn;

    @FXML
    private TableColumn<Linija, String> StaniceColumn;
    
    @FXML
    private TableColumn<Linija, String> StanicaProlazakColumn;

    @FXML
    private TableView<Linija> tableView;

	private static final String BASE_URL = "http://localhost:4200/CZSServer/rest/linije";
	ObservableList<Linija> observableList = FXCollections.observableArrayList();
	public static ArrayList<Linija> evidencijaList = new ArrayList<>();
//	private ObjectMapper mapper = new ObjectMapper();
	private Gson json = new Gson();
	
	public void goBack(ActionEvent event) throws IOException {
		new Main().changeScene("ZS.fxml");
	}

	private String readAll(BufferedReader reader) {
		StringBuilder sb = new StringBuilder();
		int c;
		try {
			while((c = reader.read())!=-1)
				sb.append((char)c);
		} catch (IOException e) {
			Logger.getLogger("").log(Level.WARNING,e.fillInStackTrace().toString());
		}
		return sb.toString();
	}
	
	public JSONArray readAllJSON() throws IOException, JSONException {
		InputStream is = null;
		try {
			is = new URL(BASE_URL).openStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAll(reader);
			JSONArray json = new JSONArray(jsonText);
			return json;
		} 
		finally {
			is.close();
		}
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			getJSONObjects();
		} catch (JsonProcessingException e) {
			Logger.getLogger("").log(Level.WARNING,e.fillInStackTrace().toString());
		}
		
		LinijaColumn.setCellValueFactory(new PropertyValueFactory<Linija, String>("naziv"));
		StaniceColumn.setCellValueFactory(new PropertyValueFactory<Linija, String>("linija"));
		StanicaProlazakColumn.setCellValueFactory(new PropertyValueFactory<Linija, String>("stanicaProlazak"));
		
		tableView.setItems(observableList);
	}
	
	private void getJSONObjects() throws JsonMappingException, JsonProcessingException {
		JSONArray jsonArray = null;
		try {
			jsonArray = readAllJSON();
		} catch (JSONException | IOException e) {
			Logger.getLogger("").log(Level.WARNING,e.fillInStackTrace().toString());
		}
		for(int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			//Linija newLine = mapper.readValue(jsonObject.toString(), Linija.class);
			Linija newLine = json.fromJson(jsonObject.toString(), Linija.class);
			String linija = newLine.toString();
			if(linija.contains(ZSController.korisnik.getStanica())) {
				observableList.add(newLine);
			}
			evidencijaList.add(newLine);
		}
	}
	
	public void evidentiraj(ActionEvent event) throws IOException {
		new Main().changeScene("Evidencija.fxml");
	}
	
	public ArrayList<Linija> getList(){
		return this.evidencijaList;
	}
}
