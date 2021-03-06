package application;
	
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import redis.clients.jedis.JedisPool;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	
	private static final String CONFIG_FILE = "C:\\Users\\Luka\\Desktop\\MDP-Luka-Divljak\\CZSServer\\resources\\config.properties";
	private static final String PATH = "C:\\Users\\Luka\\Desktop\\MDP-Luka-Divljak\\ZSMDP\\logs";
	private static Stage stage;
	public static JedisPool pool = new JedisPool("localhost");
	
	static{
		try {
			Handler handler = new FileHandler(PATH + "ZSMDP.log");
			Logger.getLogger("").addHandler(handler);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception{
		stage = primaryStage;
		primaryStage.setResizable(false);
		primaryStage.setTitle("Zeljeznicka Stanica");
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
		    @Override
		    public void handle(WindowEvent t) {
		        Platform.exit();
		        System.exit(0);
		    }
		});
		AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("LogIn.fxml"));
		Scene scene = new Scene(root,600,400);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}
	
	
//	public static boolean checkProperties() {
//		FileReader reader = null;
//		Properties p = new Properties();
//		try {
//			reader = new FileReader(CONFIG_FILE);
//			p.load(reader);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		if(p.getProperty("startujServer").equals("1"))
//			return true;
//		else return false;
//	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void changeScene(String fxml) throws IOException{
		Parent pane = FXMLLoader.load(getClass().getResource(fxml));
		stage.getScene().setRoot(pane);
	}
}
