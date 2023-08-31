package una.instrumentos.presentation.tipos;

import una.instrumentos.logic.TipoInstrumento;
import una.instrumentos.logic.Service;

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
		model.init(Service.instance().search(new TipoInstrumento()));
		view.setController(this);
		view.setModel(model);
	}

	public void search(TipoInstrumento filter) {
		try {
			List<TipoInstrumento> rows = Service.instance().search(filter);
			if (rows.isEmpty()) {
				throw new Exception("NINGUN REGISTRO COINCIDE");
			}
			model.setList(rows);
			model.setCurrent(new TipoInstrumento());
			model.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void edit(int row) {
		TipoInstrumento e = model.getList().get(row);
		try {
			TipoInstrumento current = Service.instance().read(e);
			model.setCurrent(current);
			model.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void edit(TipoInstrumento e) {
		try {
			TipoInstrumento current = Service.instance().read(e);
			current.setNombre(view.getNombre());
			current.setUnidad(view.getUnidad());

			Service.instance().update(current);
			model.setCurrent(current);
			model.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public int save(TipoInstrumento tipoInstrumento) {
		if (!validateAndHandleEmptyField(tipoInstrumento.getCodigo(), "codigo") ||
				!validateAndHandleEmptyField(tipoInstrumento.getNombre(), "nombre") ||
				!validateAndHandleEmptyField(tipoInstrumento.getUnidad(), "unidad")) {
			return 0;
		}

		try {
			Service service = Service.instance();
			try {
				service.create(tipoInstrumento);
			} catch (Exception e) {
				view.showError("Ya existe un tipo de instrumento con ese código");
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
		TipoInstrumento emptySearch = new TipoInstrumento();
		try {
			model.setList(service.search(emptySearch));
			model.setCurrent(new TipoInstrumento());
			model.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void delete(TipoInstrumento tipoInstrumento) {
		try {
			Service.instance().delete(tipoInstrumento);
			model.setList(Service.instance().search(new TipoInstrumento()));
			model.setCurrent(new TipoInstrumento());
			model.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public View getView() {
		return view;
	}
}
