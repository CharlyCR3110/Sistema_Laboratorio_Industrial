package una.instrumentos.data;

import una.instrumentos.logic.Calibracion;
import una.instrumentos.logic.Instrumento;
import una.instrumentos.logic.TipoInstrumento;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Data {
	private List<TipoInstrumento> tipos;
	private List<Instrumento> instrumentos;
	private List<Calibracion> calibraciones;

	public Data() {
		tipos = new ArrayList<>();
		instrumentos = new ArrayList<>();
		calibraciones = new ArrayList<>();


		// TODO: Remove this dummy data
		tipos.add(new TipoInstrumento("TER","Termómetro","Grados Celcius"));
		tipos.add(new TipoInstrumento("BAR","Barómetro","PSI") );

		Instrumento instrumento1 = new Instrumento("123","Termómetro de mercurio", 0, 100, 5, "TIPO 1");
		Instrumento instrumento2 = new Instrumento("456","Barómetro de mercurio", 0, 100, 5, "TIPO 2");

		instrumentos.add(instrumento1);
		instrumentos.add(instrumento2);

		calibraciones.add(new Calibracion("1",LocalDate.now(), 3, instrumento1));
		calibraciones.add(new Calibracion("2",LocalDate.now(), 3, instrumento2));

		calibraciones.get(0).agregarMediciones(3, 0, 100);
		calibraciones.get(1).agregarMediciones(5, 0, 100);
	}

	public List<TipoInstrumento> getTipos() {
		return tipos;
	}
	public List<Instrumento> getInstrumentos() {
		return instrumentos;
	}
	public List<Calibracion> getCalibraciones() {
		return calibraciones;
	}
}
