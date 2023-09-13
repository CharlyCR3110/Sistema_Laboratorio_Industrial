package una.instrumentos.presentation.calibraciones;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import una.instrumentos.logic.Calibracion;
import una.instrumentos.logic.Instrumento;
import una.instrumentos.logic.Medicion;
import una.instrumentos.logic.Service;
import una.utiles.Utiles;

import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
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

	public Model getModel() {
		return model;
	}

	private void initializeComponents() {
		model.init(Service.instance().search(new Calibracion()));
	}

	public void search(Calibracion filter) {
		try {
			List<Calibracion> rows = Service.instance().search(filter);
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

	public void edit(int row) {
		Calibracion e = model.getList().get(row);
		try {
			Calibracion current = Service.instance().read(e);
			model.setCurrent(current);
			model.commit();
		} catch (Exception ex) {
			throw new RuntimeException(ex.getMessage());
		}
	}

	public void edit(Calibracion e, Medicion medicion) {
		try {
			Calibracion current = Service.instance().read(e);
			// buscar la medicion y remplazarla con los nuevos datos
			for (int i = 0; i < current.getMediciones().size(); i++) {
				if (current.getMediciones().get(i).getNumero() == medicion.getNumero()) {    // si el numero de la medicion es igual al numero de la medicion que se quiere editar
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

		updateModelAfterSave(Service.instance());
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

	public void generateReport() {
		Document document = new Document();

		try {
			// Especifica la ruta y el nombre del archivo PDF que se generará
			String filePath = "src/main/java/una/reportes/calibraciones_report.pdf";
			PdfWriter.getInstance(document, new FileOutputStream(filePath));

			document.open();

			// Agrega el título al documento
			Paragraph title = new Paragraph("Reporte de Calibraciones");
			title.setAlignment(Element.ALIGN_CENTER);
			document.add(title);

			// Agrega la lista de calibraciones al documento
			PdfPTable table = new PdfPTable(4); // 4 columnas para número, fecha, mediciones y instrumento
			table.setWidthPercentage(100);
			table.addCell("Número");
			table.addCell("Fecha");
			table.addCell("Mediciones");
			table.addCell("Instrumento");

			for (Calibracion calibracion : model.getList()) {
				table.addCell(calibracion.getNumero());
				table.addCell(Utiles.formatDate(calibracion.getFecha())); // Asumiendo que tienes un método para formatear la fecha
				table.addCell(String.valueOf(calibracion.getMediciones().size()));
				table.addCell(calibracion.getInstrumento().getDescripcion()); // Suponiendo que puedes obtener la descripción del instrumento
			}

			document.add(table);

			// Cierra el documento
			document.close();

			System.out.println("Reporte de calibraciones generado exitosamente en: " + filePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadList(List<Calibracion> calibracionList) {
		try {
			Service.instance().loadCalibracionList(calibracionList);
			model.setList(calibracionList);
			model.setCurrent(new Calibracion());
			model.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void instrumentoSeleccionadoCambiado(Instrumento instrumento) {
		// TODO: Eliminar este metodo y mover su contenido a la clase Mediator
		view.showCalibracionesTable();
		view.mostrarInformacionInstrumento(instrumento);
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

		Calibracion calibracion = model.getList().get(selectedRow);
		delete(calibracion);
	}

	public void handleSaveAction(String numero, LocalDate fecha, Integer numeroDeMediciones) {
		Calibracion calibracion = new Calibracion();
		calibracion.setNumero(numero);
		calibracion.setFecha(fecha);
		calibracion.setNumeroDeMediciones(numeroDeMediciones);
		save(calibracion, model.getInstrumentoSeleccionado());
	}

	public void handleEditAction(int selectedRow) {
		if (selectedRow < 0) {
			view.showError("Debe seleccionar una calibración");
			return;
		}

		Calibracion calibracion = model.getList().get(selectedRow);
		edit(selectedRow);
	}

	public void handleSearchAction(String searchNumero) {
		try {
			Calibracion filter = new Calibracion();
			filter.setNumero(searchNumero);
			search(filter);
		} catch (Exception e) {
			view.showError(e.getMessage());
		}
	}

	public void handleListClick(int selectedRow) {
		if (selectedRow < 0) {
			return;
		}
		try {
			edit(selectedRow);
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