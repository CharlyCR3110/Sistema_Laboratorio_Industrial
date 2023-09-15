package una.utiles;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import una.instrumentos.logic.Calibracion;
import una.instrumentos.logic.Instrumento;

import java.io.FileOutputStream;
import java.util.List;

public class ReportGenerator {

	public static void generateInstrumentsReport(una.instrumentos.presentation.instrumentos.Model model, String filePath) {
		Document document = new Document();

		try {
			PdfWriter.getInstance(document, new FileOutputStream(filePath));
			document.open();

			Paragraph title = new Paragraph("Reporte de Instrumentos");
			title.setAlignment(Element.ALIGN_CENTER);
			document.add(title);


			if (model.getList().isEmpty() || model.getList() == null) {
				Paragraph noData = new Paragraph("No hay instrumentos registrados");
				noData.setAlignment(Element.ALIGN_CENTER);
				document.add(noData);
				document.close();
				return;
			}

			PdfPTable table = new PdfPTable(5);
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
			document.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void generateCalibrationsReport(una.instrumentos.presentation.calibraciones.Model model, String filePath) {
		Document document = new Document();

		try {
			PdfWriter.getInstance(document, new FileOutputStream(filePath));
			document.open();

			Paragraph title = new Paragraph("Reporte de Calibraciones");
			title.setAlignment(Element.ALIGN_CENTER);
			document.add(title);

			if (model.getInstrumentoSeleccionado() == null) {
				Paragraph noData = new Paragraph("No hay instrumentos seleccionados");
				noData.setAlignment(Element.ALIGN_CENTER);
				document.add(noData);
				document.close();
				return;
			}

			if (model.getList().isEmpty()) {
				Paragraph noData = new Paragraph("No hay calibraciones para el " + model.getInstrumentoSeleccionado().getDescripcion());
				noData.setAlignment(Element.ALIGN_CENTER);
				document.add(noData);
				document.close();
				return;
			}

			PdfPTable table = new PdfPTable(4);
			table.setWidthPercentage(100);
			table.addCell("Número");
			table.addCell("Fecha");
			table.addCell("Mediciones");
			table.addCell("Instrumento");

			for (Calibracion calibracion : model.getList()) {
				table.addCell(calibracion.getNumero());
				table.addCell(Utiles.formatDate(calibracion.getFecha()));
				table.addCell(String.valueOf(calibracion.getMediciones().size()));
				table.addCell(model.getInstrumentoSeleccionado().getDescripcion());
			}

			document.add(table);
			document.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

