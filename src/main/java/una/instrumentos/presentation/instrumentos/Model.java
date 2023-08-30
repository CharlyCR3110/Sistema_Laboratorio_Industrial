package una.instrumentos.presentation.instrumentos;

import una.instrumentos.logic.Instrumento;

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
	public void init(List<Instrumento> list) {	// Inicializa el modelo con una lista de instrumentos
		setList(list);
		setCurrent(new Instrumento());
	}
	public List<Instrumento> getList() {	// Devuelve la lista de instrumentos
		return list;
	}
	public void setList(List<Instrumento> list) {	// Establece la lista de instrumentos
		this.list = list;
		changedProps += LIST;
	}
	public Instrumento getCurrent() {	// Devuelve el instrumento actual
		return current;
	}
	public void setCurrent(Instrumento current) {	// Establece el instrumento actual
		changedProps += CURRENT;
		this.current = current;
	}
	List<Instrumento> list;	// Lista de instrumentos
	Instrumento current;	// Instrumento actual
	int changedProps = NONE;	// Propiedades que han cambiado
	public static int NONE = 0;	// Ninguna propiedad ha cambiado
	public static int LIST = 1;	// La lista de instrumentos ha cambiado
	public static int CURRENT = 2;	// El instrumento actual ha cambiado
}
