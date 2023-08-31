package una.utiles;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class XMLDataManager {
	public static void saveToXML(Object data, String filePath) throws IOException {
		try (XMLEncoder encoder = new XMLEncoder(new FileOutputStream(filePath))) {
			encoder.writeObject(data);
		}
	}
	public static Object loadFromXML(String filePath) throws IOException {
		try (XMLDecoder decoder = new XMLDecoder(new FileInputStream(filePath))) {
			return decoder.readObject();
		}
	}
}

