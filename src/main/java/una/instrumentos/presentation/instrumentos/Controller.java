package una.instrumentos.presentation.instrumentos;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import una.instrumentos.logic.Service;
import una.instrumentos.logic.Instrumento;
import una.instrumentos.logic.TipoInstrumento;

import java.io.FileOutputStream;
import java.util.List;

public class Controller {
	private final View view;
	private final Model model;

	public Controller(View view, Model model) {
		this.view = view;
		this.model = model;
		initializeComponents();
	}

	public Model getModel() {
		return model;
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
				// Tirar una excepcion que no se encontro
				throw new Exception("Ningun instrumento coincide con el criterio de busqueda");
			}
			model.setList(rows);
			model.setCurrent(new Instrumento());
			model.commit();
		} catch (Exception ex) {
			throw new RuntimeException(ex.getMessage());
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
			current.setTipo(view.getTipoSeleccionado());
			try {
				Service.instance().update(current);
				view.showMessage("Instrumento actualizado exitosamente");
				view.clearAction();
			} catch (Exception ex) {
				view.showError("No se pudo actualizar el instrumento");
			}
		} catch (Exception ex) {
			view.showError("No se pudo actualizar el instrumento");
		}
	}

	public int save(String serie, String descripcion,Integer minimo, Integer maximo, Integer tolerancia,  String tipo) {
		if (!validateAndHandleEmptyField(serie, "serie") ||
				!validateAndHandleEmptyField(descripcion, "descripcion") ||
				tipo == null || tipo.isEmpty()) {
			return 0;
		}

		// validar que el mínimo sea menor que el máximo
		if (minimo > maximo) {
			view.showError("El mínimo no puede ser mayor que el máximo");
			return 0;
		}

		try {
			Service service = Service.instance();
			try {
				service.create(new Instrumento(serie, descripcion, minimo, maximo, tolerancia, stringToTipo(tipo)));
			} catch (Exception e) {
				view.showError("Ya existe un instrumento con esa serie");
			}
			updateModelAfterSave(service);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}

		return 1;
	}

	private TipoInstrumento stringToTipo(String tipo) {
		for (TipoInstrumento tipoInstrumento : getTipos()) {
			if (tipoInstrumento.getNombre().equals(tipo)) {
				return tipoInstrumento;
			}
		}
		return null;
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
			throw new RuntimeException(e.getMessage());
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

	public void generateReport() {
		Document document = new Document();

		try {
			// Especifica la ruta y el nombre del archivo PDF que se generará
			String filePath = "src/main/java/una/reportes/instrumentos_report.pdf";
			PdfWriter.getInstance(document, new FileOutputStream(filePath));

			document.open();

			// Agrega el título al documento
			Paragraph title = new Paragraph("Reporte de Instrumentos");
			title.setAlignment(Element.ALIGN_CENTER);
			document.add(title);

			// Agrega la lista de instrumentos al documento
			PdfPTable table = new PdfPTable(5); // 5 columnas para serie, descripción, mínimo, máximo y tolerancia
			table.setWidthPercentage(100);
			table.addCell("Serie");
			table.addCell("Descripción");
			table.addCell("Mínimo");
			table.addCell("Máximo");
			table.addCell("Tolerancia");

			for (Instrumento instrumento : model.getList()) {
				table.addCell(instrumento.getSerie());
				table.addCell(instrumento.getDescripcion());
				table.addCell(String.valueOf(instrumento.getMinimo()));
				table.addCell(String.valueOf(instrumento.getMaximo()));
				table.addCell(String.valueOf(instrumento.getTolerancia()));
			}

			document.add(table);

			// Cierra el documento
			document.close();

			System.out.println("Reporte de instrumentos generado exitosamente en: " + filePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<TipoInstrumento> getTipos() {
		return Service.instance().getTipos();
	}

	public TipoInstrumento getTipoSeleccionado(String tipo) {
		return Service.instance().getTipoSeleccionado(tipo);
	}

	public void loadList(List<Instrumento> instrumentoList) {
		try {
			Service.instance().loadInstrumentoList(instrumentoList);
			model.setList(instrumentoList);
			model.setCurrent(new Instrumento());
			model.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void handleSaveAction(String serie, String descripcion, int minimo, int maximo, int tolerancia, String tipo) {
		try {
			if (save(serie, descripcion, minimo, maximo, tolerancia, tipo) == 1) {
				view.showMessage("Instrumento guardado exitosamente");
			} else {
				view.showError("No se pudo guardar el instrumento");
			}
		} catch (Exception e) {
			view.showError("Exception: No se pudo guardar el instrumento");
		}
	}

	public void handleDeleteAction(int selectedRow) {
		try {
			delete(model.getList().get(selectedRow));
			view.showMessage("Instrumento eliminado exitosamente");
		} catch (Exception e) {
			view.showError("No se pudo eliminar el instrumento");
		}
	}

	public void handleSearchAction(String searchDesc) {
		try {
			Instrumento filter = new Instrumento();
			filter.setDescripcion(searchDesc);
			search(filter);
		} catch (Exception e) {
			view.showError(e.getMessage());
		}
	}

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
		} catch (IndexOutOfBoundsException e) {
			view.showError("Debe seleccionar un elemento de la lista");
		}catch (Exception e) {
			view.showError("No se pudo editar el instrumento");
		}
	}
}
