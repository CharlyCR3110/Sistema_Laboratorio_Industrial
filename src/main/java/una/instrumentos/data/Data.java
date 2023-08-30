package una.instrumentos.data;

import una.instrumentos.logic.Instrumento;
import una.instrumentos.logic.TipoInstrumento;

import java.util.ArrayList;
import java.util.List;

public class Data {
	private List<TipoInstrumento> tipos;
	private List<Instrumento> instrumentos;

	public Data() {
		tipos = new ArrayList<>();
		instrumentos = new ArrayList<>();

		tipos.add(new TipoInstrumento("TER","Termómetro","Grados Celcius") );
		tipos.add(new TipoInstrumento("BAR","Barómetro","PSI") );

		instrumentos.add(new Instrumento("123","Termómetro de mercurio", 0, 100, 5, "TIPO 1"));
		instrumentos.add(new Instrumento("456","Barómetro de mercurio", 0, 100, 5, "TIPO 2"));
	}

	public List<TipoInstrumento> getTipos() {
		return tipos;
	}
	public List<Instrumento> getInstrumentos() {
		return instrumentos;
	}
}
