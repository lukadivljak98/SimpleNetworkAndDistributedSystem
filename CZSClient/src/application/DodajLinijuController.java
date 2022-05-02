package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Linija;
import model.StanicaDolazak;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class DodajLinijuController {

    @FXML
    private TextField dolazakTf;

    @FXML
    private TextField stanicaTf;
    
    @FXML
    private TextField nazivLinijeTf;
    
    @FXML
    private TextArea linijaZaDodatiTf;
    
    private String linija = "";
    
    //Linija novaLinija = new Linija();
    private static String instanceName = "Lines";
    private List<StanicaDolazak> pomList = new ArrayList<>();
    
    private int br = 0;
    private Gson json = new Gson();

    public void goBack(ActionEvent event) throws IOException {
		new Main().changeScene("Sample.fxml");
	}

    public void dodaj(ActionEvent event) {
    	String stanica = stanicaTf.getText();
    	String dolazak = dolazakTf.getText();
    	StanicaDolazak novaStanicaDolazak = new StanicaDolazak(stanica, dolazak);
    	if(br == 0)
    		linija += novaStanicaDolazak.toString();
    	else
    		linija += "-" + novaStanicaDolazak.toString();
    	linijaZaDodatiTf.setText(linija);
    	//novaLinija.dodajStanicuDolazak(novaStanicaDolazak);
    	pomList.add(novaStanicaDolazak);
    	dolazakTf.clear();
    	stanicaTf.clear();
    	br++;
    }
    
    public void sacuvaj(ActionEvent event) {
    	String naziv = nazivLinijeTf.getText();
    	Linija novaLinija = new Linija(naziv);
    	novaLinija.setStanicaProlazak(pomList.get(0).toString());
    	novaLinija.setLinija(pomList);
    	linijaZaDodatiTf.clear();
    	nazivLinijeTf.clear();
    	linija = "";
    	br = 0;
		try (Jedis jedis = Main.pool.getResource()) {
			String response = json.toJson(novaLinija);
			jedis.lpush(instanceName, response);
		}
		//pool.close();
		pomList.clear();
    }

}
