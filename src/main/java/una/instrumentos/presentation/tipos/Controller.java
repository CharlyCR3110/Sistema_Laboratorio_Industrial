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
			search(new TipoInstrumento());	// Para refrescar la lista
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
			model.setList(tipoInstrumentoList);
			model.setCurrent(new TipoInstrumento());
			model.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void handleDeleteAction(int selectedRow) {
		if (selectedRow < 0) {
			view.showError("Debe seleccionar un elemento de la lista");
			return;
		}
		TipoInstrumento tipoInstrumento = model.getList().get(selectedRow);
		try {
			delete(tipoInstrumento);
		} catch (Exception e) {
			view.showError(e.getMessage());
		}
	}

	public void handleSaveAction(String codigo, String nombre, String unidad) {
		TipoInstrumento tipoInstrumento = new TipoInstrumento();
		tipoInstrumento.setCodigo(codigo);
		tipoInstrumento.setNombre(nombre);
		tipoInstrumento.setUnidad(unidad);

		int result = save(tipoInstrumento);
		if (result == 1) {
			System.out.println("Tipo de instrumento guardado exitosamente");
//			view.showMessage("Tipo de instrumento guardado exitosamente");
		}
	}

	public void handleEditAction(int selectedRow) {
		if (selectedRow < 0) {
			view.showError("Debe seleccionar un elemento de la lista");
			return;
		}
		try {
			TipoInstrumento tipoInstrumento = model.getList().get(selectedRow);
			edit(tipoInstrumento);
//			view.showMessage("Tipo de instrumento editado exitosamente");
		} catch (Exception e) {
			view.showError(e.getMessage());
		}
	}
}
