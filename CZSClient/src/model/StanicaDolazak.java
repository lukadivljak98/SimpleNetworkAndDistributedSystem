package model;

public class StanicaDolazak {

	private String stanica;
	private String dolazak;
	
	public StanicaDolazak(String stanica, String dolazak) {
		this.stanica = stanica;
		this.dolazak = dolazak;
	}

	public String getStanica() {
		return stanica;
	}

	public void setStanica(String stanica) {
		this.stanica = stanica;
	}

	public String getDolazak() {
		return dolazak;
	}

	public void setDolazak(String dolazak) {
		this.dolazak = dolazak;
	}
	
	@Override
	public String toString() {
		return this.stanica + "(" + this.dolazak + ")";
	}
}
