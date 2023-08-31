package una.instrumentos.presentation.instrumentos;

import una.instrumentos.logic.Instrumento;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class Model extends Observable {
	private List<Instrumento> list;
	private Instrumento current;
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

	public void init(List<Instrumento> list) {
		setList(list);
		setCurrent(new Instrumento());
	}

	public List<Instrumento> getList() {
		return list;
	}

	public void setList(List<Instrumento> list) {
		this.list = list;
		changedProps |= LIST;
	}

	public Instrumento getCurrent() {
		return current;
	}

	public void setCurrent(Instrumento current) {
		changedProps |= CURRENT;
		this.current = current;
	}
}