package instrumentos.presentation.tipos;

import instrumentos.logic.TipoInstrumento;

import java.util.List;
import java.util.Observer;

public class Model extends java.util.Observable {
	@Override
	public void addObserver(Observer o) {	// Para que el modelo notifique a la vista de los cambios
		super.addObserver(o);
		commit();
	}
	public void commit() {	// Para que el modelo notifique a la vista de los cambios
		setChanged();
		notifyObservers(changedProps);
		changedProps = NONE;
	}
	public Model() {
	}
	public void init(List<TipoInstrumento> list) {	// Inicializa el modelo con una lista de instrumentos
		setList(list);
		setCurrent(new TipoInstrumento());
	}
	public List<TipoInstrumento> getList() {	// Devuelve la lista de instrumentos
		return list;
	}
	public void setList(List<TipoInstrumento> list) {	// Establece la lista de instrumentos
		this.list = list;
		changedProps += LIST;
	}
	public TipoInstrumento getCurrent() {	// Devuelve el instrumento actual
		return current;
	}
	public void setCurrent(TipoInstrumento current) {	// Establece el instrumento actual
		changedProps += CURRENT;
		this.current = current;
	}
	List<TipoInstrumento> list;	// Lista de instrumentos
	TipoInstrumento current;	// Instrumento actual
	int changedProps = NONE;	// Propiedades que han cambiado
	public static int NONE = 0;	// Ninguna propiedad ha cambiado
	public static int LIST = 1;	// La lista de instrumentos ha cambiado
	public static int CURRENT = 2;	// El instrumento actual ha cambiado
}
