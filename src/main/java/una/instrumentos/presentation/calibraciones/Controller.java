package una.instrumentos.presentation.calibraciones;


import una.instrumentos.dbRelated.controller.CalibracionDaoController;
import una.instrumentos.logic.Calibracion;
import una.instrumentos.logic.Instrumento;
import una.utiles.ReportGenerator;
import una.utiles.Utiles;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Controller {
	private final View view;
	private final Model model;
	private final CalibracionDaoController calibracionDaoController = new CalibracionDaoController();

	public Controller(View view, Model model) {
		this.view = view;
		this.model = model;
		initializeComponents();  // Mover esta llamada después de inicializar this.model
		view.setController(this);
		view.setModel(model);
	}

	public Model getModel() {
		return model;
	}

	private void initializeComponents() {
		model.init(calibracionDaoController.listar(""));
	}

	public void search(Calibracion filter) {
		try {
			List<Calibracion> rows = calibracionDaoController.listar(filter.getInstrumento().getSerie());
			// se tira la excepcion despues, porque
			if (rows.isEmpty()) {
				// Tirar una excepcion que no se encontro
				throw new Exception("Ninguna calibración coincide con el criterio de busqueda");
			}
			model.setList(rows);
			model.setCurrent(new Calibracion());
			model.commit();
		} catch (Exception ex) {
			throw new RuntimeException(ex.getMessage());
		}
	}

	public void setCurrent(int row) {
		Calibracion e = model.getList().get(row);
		try {
			Calibracion current = calibracionDaoController.obtener(e);
			model.setCurrent(current);
			model.commit();
			System.out.println("SETTING CURRENT");//DEBUG
		} catch (Exception ex) {
			throw new RuntimeException(ex.getMessage());
		}
	}

	public int edit(Calibracion calibracion) {
		try {
			calibracion.setFecha(Utiles.parseDate(view.getFecha()));
			calibracionDaoController.modificar(calibracion);
			model.setCurrent(calibracion);
			model.commit();
			return 1;
		} catch (Exception ex) {
			throw new RuntimeException(ex.getMessage());
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

		if (calibracion.getNumeroDeMediciones() < 1) {
			view.showError("El número de mediciones debe ser mayor a 0");
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

		calibracionDaoController.guardar(calibracion);
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

	private void updateModelAfterAction() {
		Calibracion emptySearch = new Calibracion();
		emptySearch.setInstrumento(model.getInstrumentoSeleccionado());
		try {
			model.setList(calibracionDaoController.listar(emptySearch.getInstrumento().getSerie()));
			model.setCurrent(new Calibracion());
			model.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int delete(Calibracion calibracion) {
		model.getInstrumentoSeleccionado().getCalibraciones().remove(calibracion);
		try {
			int r = calibracionDaoController.eliminar(calibracion.getNumero());
			System.out.println( r );
			return r;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public View getView() {
		return this.view;
	}

	public void generateReport() {
		String filePath = "src/main/java/una/reportes/calibraciones_report.pdf";
		ReportGenerator.generateCalibrationsReport(model, filePath);
		view.showMessage("Reporte generado exitosamente en: " + filePath);
	}

	public void loadList(List<Calibracion> calibracionList) {
		try {
			calibracionDaoController.listar(calibracionList.get(0).getInstrumento().getSerie());
			model.setList(calibracionList);
			model.setCurrent(new Calibracion());
			model.commit();
		} catch (Exception e) {

		}
	}

	public void instrumentoSeleccionadoCambiado(Instrumento instrumento) {
		view.showCalibracionesTable(instrumento);
		view.mostrarInformacionInstrumento(instrumento);
		model.setInstrumentoSeleccionado(instrumento);
		if (instrumento == null) {
			return;
		}
		// recargar la lista de calibraciones
		loadList(instrumento.getCalibraciones());
	}

	public void noInstrumentSelected() {
		model.setList(new ArrayList<>());
		model.commit();
	}

	public void setList(ArrayList<Calibracion> list) {
		model.setList(list);
	}

	public void handleDeleteAction(int selectedRow) {
		if (selectedRow < 0) {
			view.showError("Debe seleccionar una calibración");
			return;
		}

		try {
			Calibracion calibracion = model.getList().get(selectedRow);
			delete(calibracion);
			view.showMessage("La calibración número " + calibracion.getNumero() + " ha sido eliminada exitosamente");
			updateModelAfterAction();
		} catch (Exception e) {
			view.showError(e.getMessage());
		}
	}

	public void handleSaveAction(String numero, LocalDate fecha, Integer numeroDeMediciones) {
		Calibracion calibracion = new Calibracion();
		calibracion.setNumero(numero);
		calibracion.setFecha(fecha);
		calibracion.setNumeroDeMediciones(numeroDeMediciones);
		if (save(calibracion, model.getInstrumentoSeleccionado()) == 1) {
			view.showMessage("Calibración número " + numero + " guardada exitosamente");
			updateModelAfterAction();
		} else {
			view.showError("No se pudo guardar la calibración");
		}
	}

	public void handleEditAction(int selectedRow) {
		if (selectedRow < 0) {
			view.showError("Debe seleccionar una calibración");
			return;
		}

		Calibracion calibracion = calibracionDaoController.obtener(model.getList().get(selectedRow));
		try {
			if (edit(calibracion) == 1) {
				view.showMessage("Calibración número " + calibracion.getNumero() + " ha sido editada exitosamente");
				updateModelAfterAction();
			} else {
				view.showError("No se pudo editar la calibración");
			}
		} catch (Exception e) {
			view.showError("No se pudo editar la calibracion, verifique la fecha tenga el formato correcto");
		}
	}

	public void handleSearchAction(String searchNumero) {
		try {
			Calibracion filter = new Calibracion();
			filter.setNumero(searchNumero);
			search(filter);
		} catch (Exception e) {
			view.showError("No se pudo realizar la búsqueda");
		}
	}

	public void handleListClick(int selectedRow) {
		if (selectedRow < 0) {
			return;
		}
		try {
			setCurrent(selectedRow);
		} catch (Exception e) {
			view.showError("Parece que hubo un error al seleccionar la calibración");
		}
	}

	public Instrumento getInstrumentoSeleccionado() {
		return model.getInstrumentoSeleccionado();
	}

	public void setInstrumentoSeleccionado(Instrumento instrumentoSeleccionado) {
		model.setInstrumentoSeleccionado(instrumentoSeleccionado);
	}

}