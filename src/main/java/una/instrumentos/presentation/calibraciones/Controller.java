package una.instrumentos.presentation.calibraciones;

import una.instrumentos.logic.Instrumento;
import una.instrumentos.logic.Medicion;
import una.instrumentos.logic.Service;
import una.instrumentos.logic.Calibracion;
import una.utiles.Utiles;

import java.util.List;

public class Controller {
	private final View view;
	private final Model model;

	public Controller(View view, Model model) {
		this.view = view;
		this.model = model;
		initializeComponents();  // Mover esta llamada después de inicializar this.model
		view.setController(this);
		view.setModel(model);
	}

	private void initializeComponents() {
		model.init(Service.instance().search(new Calibracion()));
	}

	public void search(Calibracion filter) {
		try {
			List<Calibracion> rows = Service.instance().search(filter);
			if (rows.isEmpty()) {
				throw new Exception("NINGUN REGISTRO COINCIDE");
			}
			model.setList(rows);
			model.setCurrent(new Calibracion());
			model.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void edit(int row) {
		Calibracion e = model.getList().get(row);
		try {
			Calibracion current = Service.instance().read(e);
			model.setCurrent(current);
			model.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void edit(Calibracion e, Medicion medicion) {
		try {
			Calibracion current = Service.instance().read(e);
			// buscar la medicion y remplazarla con los nuevos datos
			for (int i = 0; i < current.getMediciones().size(); i++) {
				if ( current.getMediciones().get(i).getNumero() == medicion.getNumero() ) {	// si el numero de la medicion es igual al numero de la medicion que se quiere editar
					current.getMediciones().set(i, medicion);
					break;
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public int save(Calibracion calibracion, Instrumento instrumentoSeleccionado) {
		if (!validateAndHandleEmptyField(calibracion.getNumero(), "numero") ||
				!validateAndHandleEmptyField(calibracion.getFecha().toString(), "fecha") ||
				!validateAndHandleEmptyField(calibracion.getNumeroDeMediciones().toString(), "mediciones")) {
			return 0;
		}

		if (instrumentoSeleccionado == null) {
			view.showError("Debe seleccionar un instrumento");
			return 0;
		}

		try {
			Utiles.parseDate(calibracion.getFecha().toString());
		} catch (Exception e) {
			view.showError("La fecha no es válida");
			return 0;
		}

		calibracion.agregarMediciones(calibracion.getNumeroDeMediciones(), instrumentoSeleccionado.getMinimo(), instrumentoSeleccionado.getMaximo());

		instrumentoSeleccionado.agregarCalibracion(calibracion);
		calibracion.setInstrumento(instrumentoSeleccionado);

		try {
			Service service = Service.instance();
			try {
				service.create(calibracion);
			} catch (Exception e) {
				view.showError("Ya existe una calibración con ese número");
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
		calibracion.getInstrumento().getCalibraciones().remove(calibracion);
		try {
			Service.instance().delete(calibracion);
			model.setList(Service.instance().search(new Calibracion()));
			model.setCurrent(new Calibracion());
			model.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public View getView() {
		return this.view;
	}
}
