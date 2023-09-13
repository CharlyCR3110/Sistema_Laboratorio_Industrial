package una.instrumentos.logic;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlID;
import jakarta.xml.bind.annotation.XmlList;

import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
public class TipoInstrumento {
	@XmlID
	String codigo;
	String nombre;
	String unidad;

	public TipoInstrumento() {
		this("","","");
	}
	public TipoInstrumento(String codigo, String nombre, String unidad) {
		this.codigo = codigo;
		this.nombre = nombre;
		this.unidad = unidad;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getUnidad() {
		return unidad;
	}

	public void setUnidad(String unidad) {
		this.unidad = unidad;
	}

	@Override
	public int hashCode() {
		int hash = 7;	// 7 es un numero primo
		hash = 23 * hash + Objects.hashCode(this.codigo);	// 23 es un numero primo
		return hash;	// 23 * 7 = 161
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final TipoInstrumento other = (TipoInstrumento) obj;
		if (!Objects.equals(this.codigo, other.codigo)) {
			return false;
		}
		return true;
	}
}
