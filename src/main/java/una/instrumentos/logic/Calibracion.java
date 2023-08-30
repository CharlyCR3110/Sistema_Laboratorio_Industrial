package una.instrumentos.logic;

import java.time.LocalDate;
import java.util.Objects;

public class Calibracion {
	public Calibracion() {
		this(0, LocalDate.now(), 0, new Instrumento());
	}

	public Calibracion(Integer numero, LocalDate fecha, Integer numeroDeMediciones, Instrumento instrumento) {
		this.numero = numero;
		this.fecha = fecha;
		this.numeroDeMediciones = numeroDeMediciones;
		this.instrumento = instrumento;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Calibracion that = (Calibracion) o;
		return Objects.equals(numero, that.numero) && Objects.equals(fecha, that.fecha) && Objects.equals(numeroDeMediciones, that.numeroDeMediciones) && Objects.equals(instrumento, that.instrumento);
	}

	@Override
	public int hashCode() {
		return Objects.hash(numero, fecha, numeroDeMediciones, instrumento);
	}
	public void setNumeroDeMediciones(Integer numeroDeMediciones) {
		this.numeroDeMediciones = numeroDeMediciones;
	}

	public Instrumento getInstrumento() {
		return instrumento;
	}

	public void setInstrumento(Instrumento instrumento) {
		this.instrumento = instrumento;
	}

	private Integer numero;	// numero de calibracion
	private LocalDate fecha;	// fecha de calibracion
	private Integer numeroDeMediciones;	// numero de mediciones
	private Instrumento instrumento;	// instrumento calibrado
}
