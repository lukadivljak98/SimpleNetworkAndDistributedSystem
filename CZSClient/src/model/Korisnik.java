package model;

public class Korisnik {

	private String username;
	private String passwordHash;
	private String stanica;
	private boolean aktivan;
	private int port;
	
	public Korisnik() {}

	public Korisnik(String username, String passwordHash, String stanica) {
		super();
		this.username = username;
		this.passwordHash = passwordHash;
		this.stanica = stanica;
	}

	public String getStanica() {
		return stanica;
	}

	public void setStanica(String stanica) {
		this.stanica = stanica;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public boolean isAktivan() {
		return aktivan;
	}

	public void setAktivan(boolean aktivan) {
		this.aktivan = aktivan;
	}

	@Override
	public String toString() {
		return "Korisnik [username=" + username + ", stanica=" + stanica + "]";
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	
}
