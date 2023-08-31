package una.instrumentos.presentation.calibraciones;

import una.instrumentos.logic.Instrumento;
import una.instrumentos.logic.Service;
import una.instrumentos.logic.Calibracion;
import una.utiles.Utiles;

import java.util.List;

public class Controller{
	public Controller(View view, Model model) {
		System.out.println("CONTROLLER CALIBRACIONES" + Service.instance().search(new Calibracion()).size());
		model.init(Service.instance().search(new Calibracion()));
		this.view = view;
		this.model = model;
		view.setController(this);
		view.setModel(model);
	}
	public void search(Calibracion filter) throws  Exception{
		List<Calibracion> rows = Service.instance().search(filter);
		if (rows.isEmpty()){
			throw new Exception("NINGUN REGISTRO COINCIDE");
		}
		model.setList(rows);
		model.setCurrent(new Calibracion());
		model.commit();
	}
	public void edit(int row){	// se llama edit, pero realmente simplemente carga un elemento de la tabla en los campos de texto
		Calibracion e = model.getList().get(row);
		try {
			// Carga los datos a ser editados
			model.setCurrent(Service.instance().read(e));
			model.commit();
		} catch (Exception ex) {}
	}
	public void edit(Calibracion e) {
		try {
			Calibracion current = Service.instance().read(e); // Leer el elemento actual de la base de datos

			// Realizar las operaciones de edición en el elemento actual
			// Se obtiene el valor de los campos de texto y se asigna al elemento actual
			current.setFecha(Utiles.parseDate(view.getFecha()));
			current.setNumeroDeMediciones(Integer.parseInt(view.getMediciones()));

			Service.instance().update(current); // Actualizar el elemento en la base de datos

			model.setCurrent(current); // Actualizar el elemento en el modelo
			model.commit(); // Confirmar los cambios en el modelo
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	View view;
	Model model;

	public int save(Calibracion calibracion, Instrumento instrumentoSeleccionado) {
		if (!validateAndHandleEmptyField(calibracion.getNumero(), "número") ||
				!validateAndHandleEmptyField(calibracion.getFecha().toString(), "fecha") ||
				!validateAndHandleEmptyField(calibracion.getNumeroDeMediciones().toString(), "mediciones")) {
			return 0;
		}

		if (instrumentoSeleccionado == null) {	// Esto no debería pasar, pero por si acaso
			view.showError("Debe seleccionar un instrumento");
			return 0;
		}

		instrumentoSeleccionado.agregarCalibracion(calibracion);	// Se agrega la calibracion al instrumento
		calibracion.setInstrumento(instrumentoSeleccionado);	// Se asocia el instrumento a la calibracion

		try {
			Service service = Service.instance();
			try {
				service.create(calibracion);
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
		Calibracion emptySearch = new Calibracion();
		try {
			model.setList(service.search(emptySearch));
			model.setCurrent(new Calibracion());
			model.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void delete(Calibracion calibracion) {
		calibracion.getInstrumento().getCalibraciones().remove(calibracion);	// Se elimina la calibracion del instrumento
		try {
			Service.instance().delete(calibracion);
			model.setList(Service.instance().search(new Calibracion()));
			model.setCurrent(new Calibracion());
			model.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
