package una.utiles;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Utiles {
	public static LocalDate parseDate(String dateString) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate localDate = LocalDate.parse(dateString, formatter);
		return localDate;
	}
}
