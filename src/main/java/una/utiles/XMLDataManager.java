package una.utiles;

import una.instrumentos.logic.TipoInstrumento;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class XMLDataManager {
	public static void saveToXML(Object data, String filePath) throws IOException {
		try (XMLEncoder encoder = new XMLEncoder(new FileOutputStream(filePath))) {
			encoder.writeObject(data);
		}
	}
	public static <T> List<T> loadFromXML(String filePath) {
		try (XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(filePath)))) {
			return (List<T>) decoder.readObject();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}

