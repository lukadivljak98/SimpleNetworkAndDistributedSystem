package application;
	
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.stage.Stage;
import model.Linija;
import redis.clients.jedis.JedisPool;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	
	private static final String PATH = "C:\\Users\\Luka\\Desktop\\MDP-Luka-Divljak\\CZSClient//logs";
	
	public static JedisPool pool = new JedisPool("localhost");
	private static List<Linija> lines = new ArrayList<>();
	
	private static Stage stage;
	
	static{
		try {
			Handler handler = new FileHandler(PATH + "CZSClient.log");
			Logger.getLogger("").addHandler(handler);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void start(Stage primaryStage) {
		try {
			stage = primaryStage;
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("Sample.fxml"));
			Scene scene = new Scene(root,600,400);
			primaryStage.setResizable(false);
			primaryStage.setTitle("Centralna Zeljeznicka Stanica");
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			Logger.getLogger("").log(Level.WARNING,e.fillInStackTrace().toString());
		}
	}
	
	public static List<Linija> getLines() {
		return lines;
	}

	public static void main(String[] args) {
//		try {
//			loadConfigFile();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		launch(args);
		pool.close();
	}
	
	public void changeScene(String fxml) throws IOException{
		Parent pane = FXMLLoader.load(getClass().getResource(fxml));
		stage.getScene().setRoot(pane);
	}
	
	public static void addLine(Linija l) {
		lines.add(l);
	}
	
//	public static void loadConfigFile() throws IOException {
//		FileInputStream fis = new FileInputStream(CONFIG_PATH);
//		Properties prop = new Properties();
//		prop.load(fis);
//		USERS_DIR = prop.getProperty("USERS_DIR");
//	}
//	
//	public static String getUsersDir() {
//		return USERS_DIR;
//	}
}
