package model;

import java.util.ArrayList;
import java.util.List;

public class Linija {

	private String naziv;
	//private int id;
	//private static int brojac;
	private List<StanicaDolazak> linija = new ArrayList<>();
	private String stanicaProlazak;
	
	public Linija(String naziv) {
		this.naziv = naziv;
	}
	
	public Linija() {
		
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public List<StanicaDolazak> getLinija() {
		return linija;
	}

	public void setLinija(List<StanicaDolazak> linija) {
		this.linija = linija;
	}
	
	public void dodajStanicuDolazak(StanicaDolazak sd) {
		this.linija.add(sd);
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for(StanicaDolazak sd : linija) {
			sb.append(sd.toString());
			sb.append("-");
		}
		sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}

	public String getStanicaProlazak() {
		return stanicaProlazak;
	}

	public void setStanicaProlazak(String stanicaProlazak) {
		this.stanicaProlazak = stanicaProlazak;
	}
}
