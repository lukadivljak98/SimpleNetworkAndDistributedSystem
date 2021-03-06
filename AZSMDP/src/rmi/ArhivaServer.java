package rmi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ArhivaServer implements ArhivaInterface {
	
	private static final String PATH = "C:\\Users\\Luka\\Desktop\\MDP-Luka-Divljak\\AZSMDP\\izvjestaji";
	private static final String PATH2 = "C:\\Users\\Luka\\Desktop\\MDP-Luka-Divljak\\ZSMDP";
	private static final String PATH3 = "C:\\Users\\Luka\\Desktop\\MDP-Luka-Divljak\\AZSMDP\\logs";
	private static final String RESOURCES = "resources";
	
	static{
		try {
			Handler handler = new FileHandler(PATH3 + "ArhivaServer.log");
			Logger.getLogger("").addHandler(handler);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void upload(String text, String fileName, String username) throws RemoteException {
		
		PDDocument document = new PDDocument();
		PDPage page = new PDPage();
		document.addPage(page);
		File file = null;
		try {
			PDPageContentStream contentStream = new PDPageContentStream(document, page);
			contentStream.beginText();
			PDFont font = PDType1Font.TIMES_ROMAN;
			contentStream.setFont(font, 12);
			contentStream.showText(text);
			contentStream.endText();
			contentStream.close();
			file = new File(PATH + File.separator + fileName + ".pdf");
			document.save(file);
			document.close();
		}
		catch (IOException e) {
			Logger.getLogger("").log(Level.WARNING,e.fillInStackTrace().toString());
		}
		long size = file.length();
		LocalTime time = LocalTime.now();
		String timeString = time.toString();
		JSONObject obj = new JSONObject();
		obj.put("username", username);
		obj.put("fileSize", size);
		obj.put("time", timeString);
		try {
			FileWriter writer = new FileWriter(PATH + File.separator + fileName + ".json");
			writer.write(obj.toJSONString());
			writer.close();
		} catch (IOException e) {
			Logger.getLogger("").log(Level.WARNING,e.fillInStackTrace().toString());
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void download(String fileName, String username) throws RemoteException {
		 InputStream is = null;
		    OutputStream os = null;
		    try {
		        is = new FileInputStream(PATH + File.separator + fileName);
		        os = new FileOutputStream(PATH2 + File.separator + username + File.separator + fileName);
		        byte[] buffer = new byte[1024];
		        int length;
		        while ((length = is.read(buffer)) > 0) {
		            os.write(buffer, 0, length);
		        }
		        is.close();
				os.close();
		    }
		     catch (FileNotFoundException e) {
		    	 Logger.getLogger("").log(Level.WARNING,e.fillInStackTrace().toString());
			} catch (IOException e) {
				Logger.getLogger("").log(Level.WARNING,e.fillInStackTrace().toString());
			} 
	}
	
	@Override
	public ArrayList<File> list() throws RemoteException {
		ArrayList<File> list = new ArrayList<>();
		File[] files = new File(PATH).listFiles();
		if(files.length == 0)
			return list;
		for (File file : files) {
		    if (file.isFile() && file.getName().endsWith(".pdf")) {
		        list.add(file);
		    }
		}
		return list;
	}
	
	public static void main(String[] args) {
		System.setProperty("java.security.policy", RESOURCES + File.separator + "server_policyfile.txt");
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		try {
			ArhivaServer server = new ArhivaServer();
			ArhivaInterface stub = (ArhivaInterface) UnicastRemoteObject.exportObject(server, 0);
			Registry registry = LocateRegistry.createRegistry(1099);
			registry.rebind("ArhivaServer", stub);
		} catch (Exception e) {
			Logger.getLogger("").log(Level.WARNING,e.fillInStackTrace().toString());
		}
	}

}
