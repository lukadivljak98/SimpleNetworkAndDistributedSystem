package rmi;

import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ArhivaInterface extends Remote {

	void upload(String text, String fileName, String username) throws RemoteException;
	void download(String fileName, String username) throws RemoteException;
	ArrayList<File> list() throws RemoteException;
}
