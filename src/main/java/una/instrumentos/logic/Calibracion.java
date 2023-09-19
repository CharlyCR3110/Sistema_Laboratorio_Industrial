package una.instrumentos.logic;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlID;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import una.utiles.LocalDateAdapter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
public class Calibracion {
	@XmlID
	private String numero;	// numero de calibracion
	@XmlJavaTypeAdapter(LocalDateAdapter.class)
	private LocalDate fecha;	// fecha de calibracion
	private Integer numeroDeMediciones;	// numero de mediciones
	@XmlTransient
	private Instrumento instrumento;	// instrumento calibrado
	private List<Medicion> mediciones;	// mediciones de la calibracion

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
		// Generar mediciones aleatorias
		int rangoReferencia = (maximo - minimo) / numeroDeMediciones;	// rango de referencia entre mediciones
		for (int i = 0; i < numeroDeMediciones; i++) {
			int referencia = minimo + (i * rangoReferencia);	// referencia de la medicion

			int maxLecturaVariation = (referencia == minimo) ? 5 : 5;	// variacion maxima de la lectura (es un numero arbitrario, o sea, no tiene ninguna base cientifica)
			int lecturaVariation = (int) (Math.random() * (2 * maxLecturaVariation + 1)) - maxLecturaVariation;	// variacion de la lectura
			int lectura = referencia + lecturaVariation;	// lectura de la medicion

			this.mediciones.add(new Medicion(i + 1, referencia, lectura));	// agregar medicion
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
}
