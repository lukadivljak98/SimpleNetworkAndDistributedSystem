package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import model.Linija;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class EvidencijaController {

	 @FXML
	 private TextField linijaTf;

	 @FXML
	 private TextField vrijemeTf;
	 
	 private ArrayList<Linija> list;
	 public JedisPool pool = new JedisPool("localhost");
	 private static String instanceName = "Lines";
	 private Gson json = new Gson();
	 
	 public EvidencijaController() {
		 this.list = RedVoznjeController.evidencijaList;
	 }
	 
	 public void goBack(ActionEvent event) throws IOException {
			new Main().changeScene("RedVoznje.fxml");
	}
	 
	 public void sacuvaj(ActionEvent event) throws IOException {
		 String nazivLinije = linijaTf.getText();
		 String vrijemeDolaska = vrijemeTf.getText();
//		 for(Linija l : this.list) {
//			 if(nazivLinije.equals(l.getNaziv())) {
//				 l.setStanicaProlazak(ZSController.korisnik.getStanica());
//				 try(Jedis jedis = pool.getResource()){
//					 jedis.lset(instanceName, this.list.indexOf(l), json.toJson(l));
//				 }
//			 }
//		 }
		 List<Linija> list2 = new ArrayList<>();
		 List<String> response;
			try (Jedis jedis = Main.pool.getResource()) {
				response = jedis.lrange(instanceName, 0, -1);
				for(String s : response) {
					Linija line = json.fromJson(s, Linija.class);
					list2.add(line);
					if(nazivLinije.equals(line.getNaziv())) {
						line.setStanicaProlazak(ZSController.korisnik.getStanica()+"("+vrijemeDolaska+")");
						jedis.lset(instanceName, list2.indexOf(line), json.toJson(line));
					}
				}
			}
	 }
}
