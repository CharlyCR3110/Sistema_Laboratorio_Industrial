package una.instrumentos.presentation.calibraciones;

import una.instrumentos.logic.Calibracion;

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
	public void init(List<Calibracion> list) {	// Inicializa el modelo con una lista de instrumentos
		setList(list);
		setCurrent(new Calibracion());
	}
	public List<Calibracion> getList() {	// Devuelve la lista de instrumentos
		return list;
	}
	public void setList(List<Calibracion> list) {	// Establece la lista de calibraciones
		this.list = list;
		changedProps += LIST;
		System.out.println("Model.setList: " + list.size());
	}
	public Calibracion getCurrent() {	// Devuelve el instrumento actual
		return current;
	}
	public void setCurrent(Calibracion current) {	// Establece la calibracion actual
		changedProps += CURRENT;
		this.current = current;
	}
	List<Calibracion> list;	// Lista de calibraciones
	Calibracion current;	// calibracion actual
	int changedProps = NONE;	// Propiedades que han cambiado
	public static int NONE = 0;	// Ninguna propiedad ha cambiado
	public static int LIST = 1;	// La lista de calibraciones ha cambiado
	public static int CURRENT = 2;	// la calibracion actual ha cambiado
}
