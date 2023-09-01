package una.instrumentos.data;

import una.instrumentos.logic.Calibracion;
import una.instrumentos.logic.Instrumento;
import una.instrumentos.logic.TipoInstrumento;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Data implements java.io.Serializable {
	private List<TipoInstrumento> tipos;
	private List<Instrumento> instrumentos;
	private List<Calibracion> calibraciones;

	public Data() {
		tipos = new ArrayList<>();
		instrumentos = new ArrayList<>();
		calibraciones = new ArrayList<>();calibraciones.get(1).agregarMediciones(5, 0, 100);
	}

	public void setTipos(List<TipoInstrumento> tipos) {
		this.tipos = tipos;
	}

	public void setInstrumentos(List<Instrumento> instrumentos) {
		this.instrumentos = instrumentos;
	}

	public void setCalibraciones(List<Calibracion> calibraciones) {
		this.calibraciones = calibraciones;
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
