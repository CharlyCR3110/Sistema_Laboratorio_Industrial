package una.instrumentos.dbRelated.controller;

import una.factory.ConnectionFactory;
import una.instrumentos.dbRelated.dao.InstrumentoDao;
import una.instrumentos.logic.Instrumento;

import java.util.List;

public class InstrumentoDaoController {
	private final InstrumentoDao instrumentoDao;
	public InstrumentoDaoController() {
		var factory = new ConnectionFactory();
		this.instrumentoDao = new InstrumentoDao(factory.recuperarConexion());
	}

	public Instrumento obtener(Instrumento e) { return instrumentoDao.obtener(e);	}
	public int guardar(Instrumento instrumento) {
		return instrumentoDao.guardar(instrumento);
	}
	public List<Instrumento> listar() {
		return instrumentoDao.listar();
	}
	public int eliminar(String serie) {
		return instrumentoDao.eliminar(serie);
	}
	public List<Instrumento> listarPorDescripcion(String descripcion) {
		return instrumentoDao.listarPorDescripcion(descripcion);
	}
	public int modificar(Instrumento instrumento) {
		return instrumentoDao.modificar(instrumento);
	}
}
