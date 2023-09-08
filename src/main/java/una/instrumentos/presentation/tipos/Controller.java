package una.instrumentos.presentation.tipos;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import una.instrumentos.logic.TipoInstrumento;
import una.instrumentos.logic.Service;

import java.io.FileOutputStream;
import java.util.List;

/**
 * Controlador que maneja la lógica de la interfaz de usuario para la gestión de tipos de instrumentos.
 */
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
		// Inicializa los componentes
		model.init(Service.instance().search(new TipoInstrumento()));
		view.setController(this);
		view.setModel(model);
	}

	public void setListCurrentAndCommit(List<TipoInstrumento> list, TipoInstrumento current) {
		model.setList(list);
		model.setCurrent(current);
		model.commit();
	}

	/**
	 * Realiza una búsqueda de tipos de instrumentos basada en un filtro y actualiza el modelo.
	 *
	 * @param filter El filtro de búsqueda.
	 */
	public void search(TipoInstrumento filter) {
		try {
			List<TipoInstrumento> rows = Service.instance().search(filter);
			if (rows.isEmpty()) {
				throw new Exception("Ningún tipo de instrumento coincide con los criterios de búsqueda");
			}
			setListCurrentAndCommit(rows, new TipoInstrumento());
		} catch (Exception ex) {
			throw new RuntimeException(ex.getMessage());
		}
	}

	public void edit(int row) {
		// Obtener el elemento seleccionado de la lista
		TipoInstrumento e = model.getList().get(row);
		try {
			// Lee el elemento desde la base de datos (Data)
			TipoInstrumento current = Service.instance().read(e);
			// Setea el elemento seleccionado en el modelo
			model.setCurrent(current);
			// Se hace commit para que se actualice la vista
			model.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void edit(TipoInstrumento e) {
		// Se lee el elemento desde la base de datos (Data)
		try {
			TipoInstrumento current = Service.instance().read(e);

			// Se setean los valores desde el view
			current.setNombre(view.getNombre());
			current.setUnidad(view.getUnidad());

			// Se actualiza el elemento en la base de datos
			Service.instance().update(current);
			model.setCurrent(current);
			model.commit();

			// Se actualiza la vista
			search(new TipoInstrumento());
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
		try {
			TipoInstrumento emptySearch = new TipoInstrumento();
			setListCurrentAndCommit(service.search(emptySearch), new TipoInstrumento());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void delete(TipoInstrumento tipoInstrumento) {
		try {
			Service.instance().delete(tipoInstrumento);
			setListCurrentAndCommit(Service.instance().search(new TipoInstrumento()), new TipoInstrumento());
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public View getView() {
		return view;
	}

	public void generateReport() {
		Document document = new Document();

		try {
			// Especifica la ruta y el nombre del archivo PDF que se generará
			String filePath = "src/main/java/una/reportes/tipos_instrumentos.pdf";
			PdfWriter.getInstance(document, new FileOutputStream(filePath));

			document.open();

			// Agrega el título al documento
			Paragraph title = new Paragraph("Reporte de Tipos de Instrumentos");
			title.setAlignment(Element.ALIGN_CENTER);
			document.add(title);

			// Agrega la lista de tipos de instrumentos al documento
			PdfPTable table = new PdfPTable(3); // 3 columnas para código, nombre y unidad
			table.setWidthPercentage(100);
			table.addCell("Código");
			table.addCell("Nombre");
			table.addCell("Unidad");

			for (TipoInstrumento tipo : model.getList()) {
				table.addCell(tipo.getCodigo());
				table.addCell(tipo.getNombre());
				table.addCell(tipo.getUnidad());
			}

			document.add(table);

			// Cierra el documento
			document.close();

			System.out.println("Reporte generado exitosamente en: " + filePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadList(List<TipoInstrumento> tipoInstrumentoList) {
		try {
			Service.instance().loadTipoList(tipoInstrumentoList);
			setListCurrentAndCommit(tipoInstrumentoList, new TipoInstrumento());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Maneja la acción de eliminación de un tipo de instrumento seleccionado.
	 *
	 * @param selectedRow El índice de la fila seleccionada en la lista.
	 */
	public void handleDeleteAction(int selectedRow) {
		if (selectedRow < 0) {
			view.showError("Debe seleccionar un elemento de la lista");
			return;
		}
		TipoInstrumento tipoInstrumento = model.getList().get(selectedRow);
		try {
			delete(tipoInstrumento);
			view.showMessage("El instrumento codigo " + tipoInstrumento.getCodigo() + " ha sido eliminado exitosamente");
		} catch (Exception e) {
			view.showError(e.getMessage());
		}
	}

	/**
	 * Maneja la acción de guardar un nuevo tipo de instrumento.
	 *
	 * @param codigo El código del tipo de instrumento.
	 * @param nombre El nombre del tipo de instrumento.
	 * @param unidad La unidad del tipo de instrumento.
	 */
	public void handleSaveAction(String codigo, String nombre, String unidad) {
		TipoInstrumento tipoInstrumento = new TipoInstrumento();
		tipoInstrumento.setCodigo(codigo);
		tipoInstrumento.setNombre(nombre);
		tipoInstrumento.setUnidad(unidad);

		int result = save(tipoInstrumento);
		if (result == 1) {
			view.showMessage("Instrumento codigo " + codigo + " guardado exitosamente");
		}
	}

	/**
	 * Maneja la acción de editar un tipo de instrumento seleccionado.
	 *
	 * @param selectedRow El índice de la fila seleccionada en la lista.
	 */
	public void handleEditAction(int selectedRow) {
		if (selectedRow < 0) {
			view.showError("Debe seleccionar un elemento de la lista");
			return;
		}
		try {
			TipoInstrumento tipoInstrumento = model.getList().get(selectedRow);
			edit(tipoInstrumento);
			view.showMessage("El instrumento codigo " + tipoInstrumento.getCodigo() + " ha sido editado exitosamente");
		} catch (Exception e) {
			view.showError(e.getMessage());
		}
	}

	/**
	 * Maneja la acción de búsqueda de tipos de instrumento por nombre.
	 *
	 * @param searchNombre El nombre a buscar.
	 */
	public void handleSearchAction(String searchNombre) {
		TipoInstrumento tipoInstrumento = new TipoInstrumento();
		tipoInstrumento.setNombre(searchNombre);
		try {
			search(tipoInstrumento);
		} catch (Exception e) {
			view.showError(e.getMessage());
		}
	}
}
