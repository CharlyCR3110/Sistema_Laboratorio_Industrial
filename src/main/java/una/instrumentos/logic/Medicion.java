package una.instrumentos.logic;

import java.util.Objects;

public class Medicion {
	private int numero;
	private int referencia;
	private int medicion;

	public Medicion() {
		this(0,0,0);
	}
	public Medicion(int numero, int referencia, int medicion) {
		this.numero = numero;
		this.referencia = referencia;
		this.medicion = medicion;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public int getReferencia() {
		return referencia;
	}

	public void setReferencia(int referencia) {
		this.referencia = referencia;
	}

	public int getMedicion() {
		return medicion;
	}

	public void setMedicion(int medicion) {
		this.medicion = medicion;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Medicion medicion1 = (Medicion) o;
		return numero == medicion1.numero && referencia == medicion1.referencia && medicion == medicion1.medicion;
	}

	@Override
	public int hashCode() {
		return Objects.hash(numero, referencia, medicion);
	}
}
