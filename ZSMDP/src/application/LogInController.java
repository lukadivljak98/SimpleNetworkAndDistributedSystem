package application;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.xml.rpc.ServiceException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import soap.SoapLogIn;
import soap.SoapLogInServiceLocator;

public class LogInController {
	
	@FXML
    private PasswordField passwordPf;

    @FXML
    private TextField usernameTf;
    
    @FXML
    private Label errorLbl;
    
    public static String username;

	public void logIn(ActionEvent event) throws IOException, ServiceException, NoSuchAlgorithmException{
		SoapLogInServiceLocator locator = new SoapLogInServiceLocator();
		SoapLogIn service = locator.getSoapLogIn();
		
		username = usernameTf.getText();
		String password = passwordPf.getText();
		
		
		if(service != null && service.checkLogIn(username, password)) {
			new Main().changeScene("ZS.fxml");
		}
		else {
			errorLbl.setVisible(true);
		}
		usernameTf.clear();
		passwordPf.clear();
		
	}
	
}
