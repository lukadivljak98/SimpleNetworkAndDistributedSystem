package application;

import java.io.File;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class ObrisiKorisnikaController {

    @FXML
    private TextField usernameTf;
    
    private static final String USERS_DIR = "C:\\Users\\Luka\\Desktop\\MDP-Luka-Divljak\\CZSServer\\users";

    public void obrisiKorisnika(ActionEvent event) {
    	String username = usernameTf.getText();
    	
    	if(username != null) {
    		File fileDel = new File(USERS_DIR + File.separator + username + ".xml");
    		if(fileDel.exists())
    			fileDel.delete();
    	}
    }
    
    public void goBack(ActionEvent event) throws IOException {
		new Main().changeScene("Sample.fxml");
	}
}

