package una.instrumentos.dbRelated.controller;

import una.factory.ConnectionFactory;
import una.instrumentos.dbRelated.dao.MedicionDao;
import una.instrumentos.logic.Medicion;

import java.util.List;

public class MedicionDaoController {
	private final MedicionDao medicionDao;
	public MedicionDaoController() {
		var factory = new ConnectionFactory();
		this.medicionDao = new MedicionDao(factory.recuperarConexion());
	}

	public int guardar(Medicion medicion, String calibracion_numero) {
		return medicionDao.guardar(medicion, calibracion_numero);
	}

	public List<Medicion> listar(String calibracion_numero) {
		return medicionDao.listar(calibracion_numero);
	}

	public int modificar(Medicion medicion) {
		return medicionDao.modificar(medicion);
	}
}
