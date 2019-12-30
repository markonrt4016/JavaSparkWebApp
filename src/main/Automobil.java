package main;

public class Automobil {
	private int id, snaga;
	private double kubikaza, cena;
	private String marka, model, slika, karoserija, gorivo;
	
	public Automobil() {
		this.id = -1;
		this.snaga = -1;
		this.kubikaza = -1;
		this.cena = -1;
		this.marka = "";
		this.model = "";
		this.slika = "";
		this.karoserija = "";
		this.gorivo = "";
	}
	
	
	public Automobil(Automobil a) {
		this.id = a.id;
		this.snaga = a.snaga;
		this.kubikaza = a.kubikaza;
		this.cena = a.cena;
		this.marka = a.marka;
		this.model = a.model;
		this.slika = a.slika;
		this.karoserija = a.karoserija;
		this.gorivo = a.gorivo;
	}
	
	public Automobil(int id, int snaga, double kubikaza, double cena, String marka, String model, String slika,
			String karoserija, String gorivo) {
		this.id = id;
		this.snaga = snaga;
		this.kubikaza = kubikaza;
		this.cena = cena;
		this.marka = marka;
		this.model = model;
		this.slika = slika;
		this.karoserija = karoserija;
		this.gorivo = gorivo;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getSnaga() {
		return snaga;
	}
	public void setSnaga(int snaga) {
		this.snaga = snaga;
	}
	public double getKubikaza() {
		return kubikaza;
	}
	public void setKubikaza(double kubikaza) {
		this.kubikaza = kubikaza;
	}
	public double getCena() {
		return cena;
	}
	public void setCena(double cena) {
		this.cena = cena;
	}
	public String getMarka() {
		return marka;
	}
	public void setMarka(String marka) {
		this.marka = marka;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getSlika() {
		return slika;
	}
	public void setSlika(String slika) {
		this.slika = slika;
	}
	public String getKaroserija() {
		return karoserija;
	}
	public void setKaroserija(String karoserija) {
		this.karoserija = karoserija;
	}
	public String getGorivo() {
		return gorivo;
	}
	public void setGorivo(String gorivo) {
		this.gorivo = gorivo;
	}


	@Override
	public String toString() {
		return "Automobil [id=" + id + ", snaga=" + snaga + ", kubikaza=" + kubikaza + ", cena=" + cena + ", marka="
				+ marka + ", model=" + model + ", slika=" + slika + ", karoserija=" + karoserija + ", gorivo=" + gorivo
				+ "]";
	}

	
	
	
}
