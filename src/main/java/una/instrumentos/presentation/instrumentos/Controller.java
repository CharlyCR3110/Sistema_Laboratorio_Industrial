package una.instrumentos.presentation.instrumentos;

import una.instrumentos.logic.Service;
import una.instrumentos.logic.Instrumento;

import java.util.List;

public class Controller{
	public Controller(una.instrumentos.presentation.instrumentos.View view, Model model) {
		model.init(Service.instance().search(new Instrumento()));
		this.view = view;
		this.model = model;
		view.setController(this);
		view.setModel(model);
	}
	public void search(Instrumento filter) throws  Exception{
		List<Instrumento> rows = Service.instance().search(filter);
		if (rows.isEmpty()){
			throw new Exception("NINGUN REGISTRO COINCIDE");
		}
		model.setList(rows);
		model.setCurrent(new Instrumento());
		model.commit();
	}
	public void edit(int row){	// se llama edit, pero realmente simplemente carga un elemento de la tabla en los campos de texto
		Instrumento e = model.getList().get(row);
		try {
			// Carga los datos a ser editados
			model.setCurrent(Service.instance().read(e));
			model.commit();
		} catch (Exception ex) {}
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
				// mostrar una ventana de error
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
	View view;
	Model model;
}