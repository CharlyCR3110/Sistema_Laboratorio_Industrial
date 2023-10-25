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

	public void loadInstrumentoList(List<Instrumento> list ) throws Exception {
		data.setInstrumentos(list);
		data.setInstrumentos(data.getInstrumentos());
	}
	public void loadCalibracionList(List<Calibracion> calibracionList) {
		data.setCalibraciones(calibracionList);
		data.setCalibraciones(data.getCalibraciones());
	}
	// CREATE
		// INSTRUMENTO
	public void create(Instrumento e) throws Exception {
		Instrumento result = data.getInstrumentos().stream()
				.filter(i->i.getSerie().equals(e.getSerie())).findFirst().orElse(null);
		if (result==null) data.getInstrumentos().add(e);
		else throw new Exception("Instrumento ya existe");
	}
		// CALIBRACION
	public void create(Calibracion e) throws Exception {
		Calibracion result = data.getCalibraciones().stream()
				.filter(i->i.getNumero().equals(e.getNumero())).findFirst().orElse(null);
		if (result==null) data.getCalibraciones().add(e);
		else throw new Exception("Calibracion ya existe");
	}
	// READ
		// Instrumento
	public Instrumento read(Instrumento e ) throws  Exception {
		Instrumento result = data.getInstrumentos().stream()
				.filter(i->i.getSerie().equals(e.getSerie())).findFirst().orElse(null);
		if (result!=null) return result;
		else throw new Exception("Instrumento no existe");
	}
		// Calibracion
	public Calibracion read(Calibracion e) throws Exception {
		Calibracion result = data.getCalibraciones().stream()
				.filter(i->i.getNumero().equals(e.getNumero())).findFirst().orElse(null);
		if (result!=null) return result;
		else throw new Exception("Calibracion no existe");
	}
	// UPDATE
		// Instrumento
	public void update(Instrumento e) throws Exception {
		if (e == null || !data.getInstrumentos().contains(e)) {
			throw new Exception("Instrumento no existe");
		}
		// Si el instrumento existe, se actualiza la información
		search(e).get(0).updateInfo(e);
	}
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
		// Instrumento
	public void delete(Instrumento e) throws Exception {
		// Verificar que no existan calibraciones con ese instrumento
		if (e.hasCalibraciones()) {
			throw new Exception("Parece que hay calibraciones asociadas a este instrumento");
		}
		data.getInstrumentos().remove(e);
	}
		// Calibracion
	public void delete(Calibracion e) throws Exception {
		data.getCalibraciones().remove(e);
	}
	// SEARCH
		// Instrumento
	public List<Instrumento> search(Instrumento e){
		return data.getInstrumentos().stream()
				.filter(i->i.getDescripcion().contains(e.getDescripcion()))
				.sorted(Comparator.comparing(Instrumento::getDescripcion))
				.collect(Collectors.toList());
	}
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
