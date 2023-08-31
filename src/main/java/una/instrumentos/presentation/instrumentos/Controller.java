package una.instrumentos.presentation.instrumentos;

import una.instrumentos.logic.Service;
import una.instrumentos.logic.Instrumento;

import java.util.List;

public class Controller {
	private final View view;
	private final Model model;

	public Controller(View view, Model model) {
		this.view = view;
		this.model = model;
		initializeComponents();
	}

	private void initializeComponents() {
		model.init(Service.instance().search(new Instrumento()));
		view.setController(this);
		view.setModel(model);
	}

	public void search(Instrumento filter) {
		try {
			List<Instrumento> rows = Service.instance().search(filter);
			if (rows.isEmpty()) {
				throw new Exception("NINGUN REGISTRO COINCIDE");
			}
			model.setList(rows);
			model.setCurrent(new Instrumento());
			model.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void edit(int row) {
		Instrumento e = model.getList().get(row);
		try {
			Instrumento current = Service.instance().read(e);
			model.setCurrent(current);
			model.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void edit(Instrumento e) {
		try {
			Instrumento current = Service.instance().read(e);
			current.setDescripcion(view.getDescripcion());
			current.setMinimo(Integer.valueOf(view.getMinimo()));
			current.setMaximo(Integer.valueOf(view.getMaximo()));
			current.setTolerancia(Integer.valueOf(view.getTolerancia()));
			current.setTipo(view.getTipo());

			try {
				Service.instance().update(current);
			} catch (Exception ex) {
				ex.printStackTrace();
				// TO-DO: Manejar correctamente la excepción
			}

			model.setCurrent(current);
			model.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public int save(Instrumento instrumento) {
		if (!validateAndHandleEmptyField(instrumento.getSerie(), "serie") ||
				!validateAndHandleEmptyField(instrumento.getDescripcion(), "descripcion")) {
			return 0;
		}

		try {
			Service service = Service.instance();
			try {
				service.create(instrumento);
			} catch (Exception e) {
				view.showError("Ya existe un instrumento con esa serie");
			}
			updateModelAfterSave(service);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return 1;
	}

	private boolean validateAndHandleEmptyField(String value, String fieldName) {
		if (value.isEmpty()) {
			view.showError("El " + fieldName + " no puede estar vacío");
			view.highlightEmptyField(fieldName);
			return false;
		}
		return true;
	}

	private void updateModelAfterSave(Service service) {
		Instrumento emptySearch = new Instrumento();
		try {
			model.setList(service.search(emptySearch));
			model.setCurrent(new Instrumento());
			model.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void delete(Instrumento instrumento) {
		try {
			Service.instance().delete(instrumento);
			model.setList(Service.instance().search(new Instrumento()));
			model.setCurrent(new Instrumento());
			model.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Instrumento getSelected() {
		int selectedRow = view.getSelectedRow();
		if (selectedRow < 0) {
			return null;
		}
		return model.getList().get(selectedRow);
	}

	public View getView() {
		return this.view;
	}
}
