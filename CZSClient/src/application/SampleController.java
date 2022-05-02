package application;

import java.io.IOException;

import javafx.event.ActionEvent;

public class SampleController {
	
	public void dodajKorisnika(ActionEvent event) throws IOException{
		Main main = new Main();
		main.changeScene("dodajKorisnika.fxml");
	}
	
	public void obrisiKorisnika(ActionEvent event) throws IOException{
		Main main = new Main();
		main.changeScene("ObrisiKorisnika.fxml");
	}
	
	public void pregledajKorisnike(ActionEvent event) throws IOException{
		Main main = new Main();
		main.changeScene("PregledajKorisnike.fxml");	
	}
	
	public void pregledajRedVoznje(ActionEvent event) throws IOException{
		Main main = new Main();
		main.changeScene("PregledajLinije.fxml");
	}
	
	public void dodajLiniju(ActionEvent event) throws IOException{
		Main main = new Main();
		main.changeScene("DodajLiniju.fxml");
	}
	
	public void obrisiLiniju(ActionEvent event) throws IOException{
		Main main = new Main();
		main.changeScene("ObrisiLiniju.fxml");
	}
	
	public void pregledajIzvjestaje(ActionEvent event) throws IOException{
		Main main = new Main();
		main.changeScene("PregledajIzvjestaje.fxml");
	}
	
	public void pregledajObavjestenja(ActionEvent event) throws IOException{
		Main main = new Main();
		main.changeScene("PregledajObavjestenja.fxml");
	}
}
