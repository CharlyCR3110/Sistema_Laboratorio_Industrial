package una.instrumentos.presentation.instrumentos;

import una.instrumentos.dbRelated.controller.InstrumentoDaoController;
import una.instrumentos.logic.Service;
import una.instrumentos.logic.Instrumento;
import una.instrumentos.logic.TipoInstrumento;
import una.utiles.ReportGenerator;

import java.util.List;

/**
 * Controlador que maneja la lógica de la interfaz de usuario para la gestión de instrumentos.
 */
public class Controller {
	private final View view;
	private final Model model;

	private final InstrumentoDaoController instrumentoDbController = new InstrumentoDaoController();

	Refresher refresher;

	public Controller(View view, Model model) {
		this.view = view;
		this.model = model;
		initializeComponents();

		refresher = new Refresher(this);
		refresher.start();
	}

	public Model getModel() {
		return model;
	}

	private void initializeComponents() {
		model.init(instrumentoDbController.listar());
		view.setController(this);
		view.setModel(model);
	}

	private void setListCurrentAndCommit(List<Instrumento> list, Instrumento current) {
		if (list != null) {	// esta condicion permite llamar al metodo sin actualizar la lista (ej: edit)
			model.setList(list);
		}
		model.setCurrent(current);
		model.commit();
	}

	private void setList(List<Instrumento> list) {
		model.setList(list);
		model.commit();
	}

	/**
	 * Realiza una búsqueda de instrumentos basada en un filtro y actualiza el modelo.
	 *
	 * @param filter El filtro de búsqueda.
	 */
	public void search(Instrumento filter) {
		try {
			List<Instrumento> rows = instrumentoDbController.listarPorDescripcion(filter.getDescripcion());

			if (rows.isEmpty()) {
				// Tirar una excepcion que no se encontro
				throw new Exception("Ningun instrumento coincide con el criterio de busqueda");
			}
			setListCurrentAndCommit(rows, new Instrumento());
		} catch (Exception ex) {
			throw new RuntimeException(ex.getMessage());
		}
	}
	
	/**
	 * Carga los datos de un instrumento en el modelo.
	 * 
	 * @param row
	 */
	public void edit(int row) {
		// Se obtiene el instrumento seleccionado
		Instrumento e = model.getList().get(row);
		try {
			// Se obtiene el instrumento actualizado desde la base de datos
			Instrumento current = instrumentoDbController.obtener(e);
			// Se actualiza el modelo
			setListCurrentAndCommit(null, current);
			// actualizarlo
			instrumentoDbController.modificar(current);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}


	/**
	 * Actualiza los datos de un instrumento en la base de datos.
	 * 
	 * @param e
	 */
	public void edit(Instrumento e) {
		try {
			// Obtener el instrumento actualizado desde la base de datos
			Instrumento current = instrumentoDbController.obtener(e);

			// Actualizar el modelo con los valores de la vista
			current.setDescripcion(view.getDescripcion());
			current.setMinimo(parseToInt(view.getMinimo()));
			current.setMaximo(parseToInt(view.getMaximo()));
			current.setTolerancia(parseToInt(view.getTolerancia()));
			current.setTipo(view.getTipoSeleccionado());

			// Actualizar el instrumento en la base de datos
			instrumentoDbController.modificar(current);
		} catch (NumberFormatException ex) {
			throw new RuntimeException("Los valores de mínimo, máximo y tolerancia deben ser números enteros");
		}
		catch (Exception ex) {
			throw new RuntimeException("No se pudo actualizar el instrumento");
		}
	}

	// Método de utilidad para parsear un valor String a Integer de manera segura
	private Integer parseToInt(String value) {
		try {
			return Integer.valueOf(value);
		} catch (NumberFormatException ex) {
			// Manejar la excepción si no se puede parsear el valor
			throw new NumberFormatException();
		}
	}


	/**
	 * 
	 * Guarda un instrumento en la base de datos.
	 * 
	 * @param serie
	 * @param descripcion
	 * @param minimo
	 * @param maximo
	 * @param tolerancia
	 * @param tipo
	 * @return 1 si se guardó correctamente, 0 si no.
	 */
	public int save(String serie, String descripcion, Integer minimo, Integer maximo, Integer tolerancia, String tipo) {
		// Validar que los campos no estén vacíos
		if (isEmptyField(serie, "serie") || isEmptyField(descripcion, "descripcion") || isEmptyField(tipo, "tipo")) {
			return 0;
		}

		// Validar que el mínimo sea menor que el máximo
		if (minimo > maximo) {
			view.showError("El mínimo no puede ser mayor que el máximo");
			return 0;
		}

		// Validar que la tolerancia no sea negativa
		if (tolerancia < 0) {
			view.showError("La tolerancia no puede ser un valor negativo");
			return 0;
		}

		try {
			Service service = Service.instance();
			try {
				TipoInstrumento tipoInstrumento = stringToTipo(tipo);
				if (tipoInstrumento == null) {
					view.showError("Tipo de instrumento no válido");
					return 0;
				}

				instrumentoDbController.guardar(new Instrumento(serie, descripcion, minimo, maximo, tolerancia, tipoInstrumento));
			} catch (Exception e) {
				view.showError("Ya existe un instrumento con esa serie");
			}
			updateModelAfterSave(service);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}

		return 1;
	}

	// Método auxiliar para convertir un string a un tipo de instrumento
	private TipoInstrumento stringToTipo(String tipo) {
		for (TipoInstrumento tipoInstrumento : getTipos()) {
			if (tipoInstrumento.getNombre().equals(tipo)) {
				return tipoInstrumento;
			}
		}
		return null;
	}

	// Método auxiliar para validar que un campo no esté vacío
	private boolean isEmptyField(String value, String fieldName) {
		if (value.isEmpty()) {
			view.showError("El " + fieldName + " no puede estar vacío");
			view.highlightEmptyField(fieldName);
			return true;
		}
		return false;
	}


	/**
	 * 
	 * Actualiza el modelo después de guardar un instrumento.
	 * 
	 * @param service
	 */
	private void updateModelAfterSave(Service service) {
		try {
			Instrumento emptySearch = new Instrumento();
			setListCurrentAndCommit(instrumentoDbController.listar(), new Instrumento());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Elimina un instrumento de la base de datos.
	 * 
	 * @param instrumento
	 */
	public void delete(Instrumento instrumento) {
		try {
			instrumentoDbController.eliminar(instrumento.getSerie());
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	// Método para obtener el instrumento seleccionado en la tabla
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

	public void generateReport() {
		String filePath = "src/main/java/una/reportes/instrumentos_report.pdf";
		ReportGenerator.generateInstrumentsReport(model, filePath);
		view.showMessage("Reporte generado exitosamente en: " + filePath);
	}

	public List<TipoInstrumento> getTipos() {
		return Service.instance().getTipos();
	}

	public TipoInstrumento getTipoSeleccionado(String tipo) {
		return Service.instance().getTipoSeleccionado(tipo);
	}

	/**
	 * 
	 * Metodo encargadodo de guardar un instrumento en la base de datos.
	 * 
	 * @param serie 	 Serie del instrumento.
	 * @param descripcion Descripcion del instrumento.
	 * @param minimo 	 Valor minimo del instrumento.
	 * @param maximo 	 Valor maximo del instrumento.
	 * @param tolerancia Tolerancia del instrumento.
	 * @param tipo 		 Tipo del instrumento.
	 * 
	 */
	public void handleSaveAction(String serie, String descripcion, String minimo, String maximo, String tolerancia, String tipo) {
		try {
			if (save(serie, descripcion, parseToInt(minimo), parseToInt(maximo), parseToInt(tolerancia), tipo) == 1) {
				view.showMessage("El instrumento con serie " + serie + " se guardó exitosamente");
				setListCurrentAndCommit(instrumentoDbController.listar(), new Instrumento());
			} else {
				view.showError("No se pudo guardar el instrumento");
			}
		} catch (NumberFormatException e) {
			view.showError("Los valores de mínimo, máximo y tolerancia deben ser números enteros");
		} catch (Exception e) {
			view.showError("Exception: No se pudo guardar el instrumento");
		}
	}


	/**
	 * 
	 * Metodo encargado de eliminar un instrumento de la base de datos.
	 * 
	 * @param selectedRow
	 */

	public void handleDeleteAction(int selectedRow) {
		if (selectedRow < 0) {
			view.showError("Debe seleccionar un elemento de la lista");
			return;
		}

		try {
			delete(model.getList().get(selectedRow));
			view.showMessage("Instrumento eliminado exitosamente");
			setListCurrentAndCommit(instrumentoDbController.listar(), new Instrumento());
		} catch (Exception e) {
			view.showError("No se pudo eliminar el instrumento porque tiene calibraciones asociadas");
		}
	}


	/**
	 * 
	 * Metodo encargado de buscar un instrumento en la base de datos.
	 * 
	 * @param searchDesc
	 */
	public void handleSearchAction(String searchDesc) {
		Instrumento filter = new Instrumento();
		filter.setDescripcion(searchDesc);
		try {
			search(filter);
		} catch (Exception e) {
			view.showError(e.getMessage());
		}
	}

	// metodo auxiliar para validar los campos de edicion
	private boolean isValidEditInput() {
		System.out.println("VALIDANDO");
		boolean isValid = true;

		if (view.getDescripcion().isEmpty()) {
			view.showError("La descripción no puede estar vacía");
			isValid = false;
		}
		if (view.getTipoSeleccionado() == null) {
			view.showError("Debe seleccionar un tipo");
			isValid = false;
		}

		int minimo = Integer.valueOf(view.getMinimo());
		int maximo = Integer.valueOf(view.getMaximo());

		if (minimo > maximo) {
			view.showError("El valor mínimo no puede ser mayor que el valor máximo");
			isValid = false;
		}

		int tolerancia = Integer.valueOf(view.getTolerancia());

		if (tolerancia < 0) {
			view.showError("La tolerancia no puede ser un valor negativo");
			isValid = false;
		}
		
		return isValid;
	}

	/**
	 * 
	 * Metodo encargado de editar un instrumento en la base de datos.
	 * 
	 * @param selectedRow
	 */
	public void handleEditAction(int selectedRow) {
		try {
			Instrumento instrumento = model.getList().get(selectedRow);
			if (instrumento == null) {
				view.showError("Debe seleccionar un elemento de la lista");
				return;
			}
			if (!isValidEditInput()) {
				throw new Exception("No se pudo actualizar el instrumento");
			}
			edit(instrumento);
			view.showMessage("Instrumento actualizado exitosamente");
			view.clearAction();
			setListCurrentAndCommit(instrumentoDbController.listar(), new Instrumento());
		} catch (IndexOutOfBoundsException e) {
			view.showError("Debe seleccionar un elemento de la lista");
		} catch (NumberFormatException e) {
			view.showError("Los valores de mínimo, máximo y tolerancia deben ser números enteros");
		} catch (Exception e) {
			view.showError("No se pudo editar el instrumento");
		}
	}

	public void refresh() {
		try {
			setList(instrumentoDbController.listar());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
