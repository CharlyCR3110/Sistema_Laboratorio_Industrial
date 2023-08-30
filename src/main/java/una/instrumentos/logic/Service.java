package una.instrumentos.logic;

import una.instrumentos.data.Data;

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
		data = new Data();
	}
	// CREATE
		// TIPO INSTRUMENTO
	public void create(TipoInstrumento e) throws Exception{
		TipoInstrumento result = data.getTipos().stream()
				.filter(i->i.getCodigo().equals(e.getCodigo())).findFirst().orElse(null);
		if (result==null) data.getTipos().add(e);
		else throw new Exception("Tipo ya existe");
	}
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
		// Tipo Instrumento
	public TipoInstrumento read(TipoInstrumento e) throws Exception{
		TipoInstrumento result = data.getTipos().stream()
				.filter(i->i.getCodigo().equals(e.getCodigo())).findFirst().orElse(null);
		if (result!=null) return result;
		else throw new Exception("Tipo no existe");
	}
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
		// Tipo Instrumento
	public void update(TipoInstrumento e) throws Exception{
		TipoInstrumento result;
		try{
			result = this.read(e);
			data.getTipos().remove(result);
			data.getTipos().add(e);
		}catch (Exception ex) {
			throw new Exception("Tipo no existe");
		}
	}
		// Instrumento
	public void update(Instrumento e) throws Exception {
		Instrumento result;
		try{
			result = this.read(e);
			data.getInstrumentos().remove(result);
			data.getInstrumentos().add(e);
		}catch (Exception ex) {
			throw new Exception("Instrumento no existe");
		}
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
		// Tipo Instrumento
	public void delete(TipoInstrumento e) throws Exception{
		data.getTipos().remove(e);
	}
		// Instrumento
	public void delete(Instrumento e) throws Exception {
		data.getInstrumentos().remove(e);
	}
		// Calibracion
	public void delete(Calibracion e) throws Exception {
		data.getCalibraciones().remove(e);
	}
	// SEARCH
		// Tipo Instrumento
	public List<TipoInstrumento> search(TipoInstrumento e){
		return data.getTipos().stream()
				.filter(i->i.getNombre().contains(e.getNombre()))
				.sorted(Comparator.comparing(TipoInstrumento::getNombre))
				.collect(Collectors.toList());
	}
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

	public TipoInstrumento get(TipoInstrumento e) {
		return data.getTipos().stream()
				.filter(i->i.getCodigo().equals(e.getCodigo())).findFirst().orElse(null);
	}
}
