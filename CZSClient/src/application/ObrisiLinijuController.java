package application;

import java.io.IOException;
import java.util.List;

import com.google.gson.Gson;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import model.Linija;
import redis.clients.jedis.Jedis;

public class ObrisiLinijuController {

    @FXML
    private TextField linijaTf;
    
	private static String instanceName = "Lines";
	private Gson json = new Gson();

    public void goBack(ActionEvent event) throws IOException {
		new Main().changeScene("Sample.fxml");
	}
    
    public void obrisi(ActionEvent event) {
    	String linijaDel = linijaTf.getText();
    	
    	List<String> response;
		try (Jedis jedis = Main.pool.getResource()) {
			response = jedis.lrange(instanceName, 0, -1);
			for(String s : response) {
				Linija line = json.fromJson(s, Linija.class);
				if(line.getNaziv().equals(linijaDel)) {
					String delStr = json.toJson(line);
					jedis.lrem(instanceName, 1, delStr);
				}
			}
		}
		linijaTf.clear();
    }
}
