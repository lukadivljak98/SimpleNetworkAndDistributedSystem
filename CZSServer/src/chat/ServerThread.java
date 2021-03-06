package chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.SSLSocket;

public class ServerThread extends Thread {

	private SSLSocket socket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	
	public ServerThread(SSLSocket socket) {
		this.socket = socket;
		try {
			this.oos = new ObjectOutputStream(this.socket.getOutputStream());
			this.ois = new ObjectInputStream(this.socket.getInputStream());
		} catch(IOException e) {
			Logger.getLogger("").log(Level.WARNING,e.fillInStackTrace().toString());
		}
	}
	
	public void run() {
		while(true) {
			String request;
			try {
				request = ois.readUTF();
				if("END".equals(request.split("#")[0])) {
					String username = request.split("#")[1];
					ObjectOutputStream oos = Server.outputs.get(username);
					oos.writeUTF("DISCONNECT");
					Server.outputs.remove(username);
					if(Server.outputs.isEmpty())
						Server.work = false;
					break;
				}
				else if(request != null && request.startsWith("HELLO#")) {
					String username = request.split("#")[1];
					Server.outputs.put(username, oos);
				}
				else if(request != null && request.startsWith("PORUKA#")) {
					synchronized(Server.outputs) {
						String username = request.split("#")[1];
						ObjectOutputStream userOut = Server.outputs.get(username);
						if(userOut != null) {
							userOut.writeUTF(request);
							userOut.flush();
						}
					}
				}
				else if (request != null && request.startsWith("FILE#")) {
					String username = request.split("#")[1];
					long fileSize = Long.parseLong(request.split("#")[4]);
					synchronized(Server.outputs.get(username)) {
						ObjectOutputStream userOut = Server.outputs.get(username);
						if(userOut != null) {
							userOut.writeUTF(request);
							userOut.flush();
							
							byte[] buffer = new byte[2*1024*1024];
							int c, count = 0;
							while((c = ois.read(buffer)) > 0) {
								userOut.write(buffer, 0, c);
								userOut.flush();
								count += c;
								if(fileSize == count)
									break;
							}
						}
					}
				}
			} catch(IOException e) {
				Logger.getLogger("").log(Level.WARNING,e.fillInStackTrace().toString());
			}
		}
	}
	
}
