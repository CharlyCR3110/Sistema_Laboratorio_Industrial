package una.instrumentos.presentation.calibraciones;

import una.instrumentos.logic.Calibracion;
import una.instrumentos.logic.Instrumento;
import una.instrumentos.logic.Service;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class Model extends Observable {
	private List<Calibracion> list;
	private Calibracion current;
	private int changedProps = NONE;

	public static final int NONE = 0;
	public static final int LIST = 1;
	public static final int CURRENT = 2;

	public void addObserver(Observer o) {
		super.addObserver(o);
		commit();
	}

	public void commit() {
		setChanged();
		notifyObservers(changedProps);
		changedProps = NONE;
	}

	public Model() {
	}

	public void init(List<Calibracion> list) {
		setList(list);
		setCurrent(new Calibracion());
	}

	public List<Calibracion> getList() {
		return list;
	}

	public void setList(List<Calibracion> list) {
		this.list = list;
		changedProps |= LIST;  // Usar operador de bits para combinar las propiedades cambiadas
	}

	public Calibracion getCurrent() {
		return current;
	}

	public void setCurrent(Calibracion current) {
		changedProps |= CURRENT;  // Usar operador de bits para combinar las propiedades cambiadas
		this.current = current;
	}
}
