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

		tipos.add(new TipoInstrumento("TER","Termómetro","Grados Celcius") );
		tipos.add(new TipoInstrumento("BAR","Barómetro","PSI") );

		instrumentos.add(new Instrumento("123","Termómetro de mercurio", 0, 100, 5, "TIPO 1"));
		instrumentos.add(new Instrumento("456","Barómetro de mercurio", 0, 100, 5, "TIPO 2"));

		calibraciones.add(new Calibracion("1",LocalDate.now(), 3, instrumentos.get(0) ));
		calibraciones.add(new Calibracion("2",LocalDate.now(), 3, instrumentos.get(1) ));
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
