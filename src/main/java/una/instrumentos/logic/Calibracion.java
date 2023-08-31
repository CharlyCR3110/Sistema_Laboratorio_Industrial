package una.instrumentos.logic;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Calibracion {
	public Calibracion() {
		this("", LocalDate.now(), 0, null);
	}

	public Calibracion(String numero, LocalDate fecha, Integer numeroDeMediciones, Instrumento instrumento) {
		this.numero = numero;
		this.fecha = fecha;
		this.numeroDeMediciones = numeroDeMediciones;
		this.instrumento = instrumento;
		this.mediciones = new ArrayList<>();
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public Integer getNumeroDeMediciones() {
		return numeroDeMediciones;
	}

	public Instrumento getInstrumento() {
		return instrumento;
	}

	public void setInstrumento(Instrumento instrumento) {
		this.instrumento = instrumento;
	}

	public List<Medicion> getMediciones() {
		return mediciones;
	}

	public void setMediciones(List<Medicion> mediciones) {
		this.mediciones = mediciones;
	}
	public void agregarMediciones(int numeroDeMediciones, int minimo, int maximo) {
		for (int i = 0; i < numeroDeMediciones; i++) {
			// lectura es un numero aleatorio entre minimo y maximo

			int referencia = maximo / (numeroDeMediciones - i);
			int lectura = (int) (Math.random() * (maximo - minimo + 1) + minimo);

			this.mediciones.add(new Medicion(i + 1, referencia, lectura));
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Calibracion that = (Calibracion) o;
		return Objects.equals(numero, that.numero) && Objects.equals(fecha, that.fecha) && Objects.equals(numeroDeMediciones, that.numeroDeMediciones);
	}

	@Override
	public int hashCode() {
		return Objects.hash(numero, fecha, numeroDeMediciones);
	}
	public void setNumeroDeMediciones(Integer numeroDeMediciones) {
		this.numeroDeMediciones = numeroDeMediciones;
	}

	private String numero;	// numero de calibracion
	private LocalDate fecha;	// fecha de calibracion
	private Integer numeroDeMediciones;	// numero de mediciones
	private Instrumento instrumento;	// instrumento calibrado
	private List<Medicion> mediciones;	// mediciones de la calibracion
}
