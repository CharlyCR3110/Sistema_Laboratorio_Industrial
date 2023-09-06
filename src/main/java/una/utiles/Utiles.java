package una.utiles;

import com.itextpdf.text.pdf.PdfPTable;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Utiles {
	public static LocalDate parseDate(String dateString) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate localDate = LocalDate.parse(dateString, formatter);
		return localDate;
	}

	public static PdfPTable formatDate(LocalDate fecha) {
		PdfPTable table = new PdfPTable(1);
		table.addCell(fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		return table;
	}

	public static String generateRandomStringNumber() {
		return String.valueOf((int) (Math.random() * 1000000));
	}
}
