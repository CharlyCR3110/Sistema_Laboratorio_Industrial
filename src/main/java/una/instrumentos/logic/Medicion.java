package una.instrumentos.logic;

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
}
