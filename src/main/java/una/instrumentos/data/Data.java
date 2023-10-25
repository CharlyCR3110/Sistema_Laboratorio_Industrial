package una.instrumentos.data;

import jakarta.xml.bind.annotation.*;
import una.instrumentos.logic.Calibracion;
import una.instrumentos.logic.Instrumento;

import java.util.ArrayList;
import java.util.List;

// Clase que contiene los datos que se van a serializar
@XmlRootElement(name = "RootElementData")
@XmlAccessorType(XmlAccessType.FIELD)	// Para que se serialicen los atributos
public class Data implements java.io.Serializable {
	@XmlElementWrapper(name = "instrumentos")
	@XmlElement(name = "instrumento")
	private List<Instrumento> instrumentos;
	@XmlTransient	// No se serializa, ya que se serializa en la clase Instrumento, ademas, el uso de esta lista es solo para facilitar algunas cosas y ser consistente con la forma en que se esta trabajando
	private List<Calibracion> calibraciones;

	public Data() {
		instrumentos = new ArrayList<>();
		calibraciones = new ArrayList<>();
	}

	public void setInstrumentos(List<Instrumento> instrumentos) {
		this.instrumentos = instrumentos;
	}

	public void setCalibraciones(List<Calibracion> calibraciones) {
		this.calibraciones = calibraciones;
	}
	public List<Instrumento> getInstrumentos() {
		return instrumentos;
	}
	public List<Calibracion> getCalibraciones() {
		return calibraciones;
	}
}
