package una.instrumentos.logic;

import una.instrumentos.data.Data;
import una.instrumentos.dbRelated.controller.TipoInstrumentoDaoController;
import una.utiles.XmlPersister;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Service {
	private static Service theInstance;
	public static Service instance(){
		if (theInstance == null) theInstance = new Service();
		return theInstance;
	}
	private Service(){
	}
	public Service getInstance() {
		return theInstance;
	}

	public List<TipoInstrumento> getTipos() {
		TipoInstrumentoDaoController tipoInstrumentoDaoController = new TipoInstrumentoDaoController();
		return tipoInstrumentoDaoController.listar();
	}

	public TipoInstrumento getTipoSeleccionado(String tipo) {
		for (TipoInstrumento tipoInstrumento : getTipos()) {
			if (tipoInstrumento.getNombre().equals(tipo)) {
				return tipoInstrumento;
			}
		}
		return null;
	}
}
