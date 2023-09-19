package una.utiles;

import java.time.LocalDate;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;

// Adaptador para poder guardar LocalDate en XML
public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {
	@Override
	public LocalDate unmarshal(String v) throws Exception {
		return LocalDate.parse(v);
	}

	@Override
	public String marshal(LocalDate v) throws Exception {
		return v.toString();
	}
}
