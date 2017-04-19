package it.polito.tdp.meteo.bean;

public class SimpleCity {

	private String nome;
	private int costo;
	
	
	public SimpleCity( String nome, int costo) {
		
		this.nome = nome;
		this.costo=costo;
	}
	
/*	public SimpleCity(String nome, int costo) {
		this.nome = nome;
		this.costo = costo;
	}
*/
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public int getCosto() {
		return costo;
	}

	public void setCosto(int costo) {
		this.costo = costo;
	}



	@Override
	public String toString() {
		return nome;
	}
	
}
