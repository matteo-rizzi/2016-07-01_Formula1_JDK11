package it.polito.tdp.formulaone.model;

public class Adiacenza {

	private Driver primo;
	private Driver secondo;
	private Integer peso;

	public Adiacenza(Driver primo, Driver secondo, Integer peso) {
		super();
		this.primo = primo;
		this.secondo = secondo;
		this.peso = peso;
	}

	public Driver getPrimo() {
		return primo;
	}

	public void setPrimo(Driver primo) {
		this.primo = primo;
	}

	public Driver getSecondo() {
		return secondo;
	}

	public void setSecondo(Driver secondo) {
		this.secondo = secondo;
	}

	public Integer getPeso() {
		return peso;
	}

	public void setPeso(Integer peso) {
		this.peso = peso;
	}

}
