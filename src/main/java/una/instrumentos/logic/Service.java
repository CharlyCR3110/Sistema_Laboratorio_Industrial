package una.instrumentos.logic;

import una.instrumentos.data.Data;
import una.instrumentos.dbRelated.controller.TipoInstrumentoDaoController;
import una.utiles.XmlPersister;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Service {
	public static Service instance(){
		if (theInstance == null) theInstance = new Service();
		return theInstance;
	}
	private Data data;

	private Service(){
		try {
			data = XmlPersister.instance().load();
		} catch (Exception e) {
			data = new Data();
		}
	}

	public void stop() {
		try {
			XmlPersister.instance().store(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void loadCalibracionList(List<Calibracion> calibracionList) {
		data.setCalibraciones(calibracionList);
		data.setCalibraciones(data.getCalibraciones());
	}
	// CREATE
		// CALIBRACION
	public void create(Calibracion e) throws Exception {
		Calibracion result = data.getCalibraciones().stream()
				.filter(i->i.getNumero().equals(e.getNumero())).findFirst().orElse(null);
		if (result==null) data.getCalibraciones().add(e);
		else throw new Exception("Calibracion ya existe");
	}
	// READ
		// Calibracion
	public Calibracion read(Calibracion e) throws Exception {
		Calibracion result = data.getCalibraciones().stream()
				.filter(i->i.getNumero().equals(e.getNumero())).findFirst().orElse(null);
		if (result!=null) return result;
		else throw new Exception("Calibracion no existe");
	}
	// UPDATE

		// Calibracion
	public void update (Calibracion e) throws Exception {
		Calibracion result;
		try{
			result = this.read(e);
			data.getCalibraciones().remove(result);
			data.getCalibraciones().add(e);
		}catch (Exception ex) {
			throw new Exception("Calibracion no existe");
		}
	}
	// DELETE
		// Calibracion
	public void delete(Calibracion e) throws Exception {
		data.getCalibraciones().remove(e);
	}
	// SEARCH
		// Calibracion
	public List<Calibracion> search(Calibracion e) {
		// si el numero de calibracion es null, entonces se devuelve la lista completa
		return e.equals(new Calibracion()) ? data.getCalibraciones() : data.getCalibraciones().stream()
				.filter(i->i.getNumero().equals(e.getNumero()))
				.sorted(Comparator.comparing(Calibracion::getNumero))
				.collect(Collectors.toList());
	}
	private static Service theInstance;


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
