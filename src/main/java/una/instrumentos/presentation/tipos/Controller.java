package una.instrumentos.presentation.tipos;

import una.instrumentos.logic.TipoInstrumento;

import una.instrumentos.logic.Service;

import java.util.List;

public class Controller{
	public Controller(View view, Model model) {
		model.init(Service.instance().search(new TipoInstrumento()));
		this.view = view;
		this.model = model;
		view.setController(this);
		view.setModel(model);
	}
	public void search(TipoInstrumento filter) throws  Exception{
		List<TipoInstrumento> rows = Service.instance().search(filter);
		if (rows.isEmpty()){
			throw new Exception("NINGUN REGISTRO COINCIDE");
		}
		model.setList(rows);
		model.setCurrent(new TipoInstrumento());
		model.commit();
	}
	public void edit(int row){	// se llama edit, pero realmente simplemente carga un elemento de la tabla en los campos de texto
		TipoInstrumento e = model.getList().get(row);
		try {
			// Carga los datos a ser editados
			model.setCurrent(Service.instance().read(e));
			model.commit();
		} catch (Exception ex) {}
	}
	public void edit(TipoInstrumento e) {
		try {
			TipoInstrumento current = Service.instance().read(e); // Leer el elemento actual de la base de datos

			// Realizar las operaciones de edición en el elemento actual
			// Se obtiene el valor de los campos de texto y se asigna al elemento actual
			current.setNombre(view.getNombre());
			current.setUnidad(view.getUnidad());

			Service.instance().update(current); // Actualizar el elemento en la base de datos

			model.setCurrent(current); // Actualizar el elemento en el modelo
			model.commit(); // Confirmar los cambios en el modelo
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	View view;
	Model model;

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
}
