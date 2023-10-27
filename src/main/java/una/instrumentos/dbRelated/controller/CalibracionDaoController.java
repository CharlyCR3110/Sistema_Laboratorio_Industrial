package una.instrumentos.dbRelated.controller;

import una.factory.ConnectionFactory;
import una.instrumentos.dbRelated.dao.CalibracionDao;
import una.instrumentos.logic.Calibracion;

import java.util.List;

public class CalibracionDaoController {
	private final CalibracionDao calibracionDao;
	public CalibracionDaoController() {
		var factory = new ConnectionFactory();
		this.calibracionDao = new CalibracionDao(factory.recuperarConexion());
	}

	public boolean tieneDuplicados(Calibracion calibracion) {
		return calibracionDao.tieneDuplicados(calibracion);
	}

	public int guardar(Calibracion calibracion) {
		return calibracionDao.guardar(calibracion);
	}
	public List<Calibracion> listar(String instrumento_serie) {
		return calibracionDao.listar(instrumento_serie);
	}
	public int eliminar (String numero) {
		return calibracionDao.eliminar(numero);
	}
	public List<Calibracion> buscarPorNumero(String instrumentoSerie, String numero) {
		return calibracionDao.buscarPorNumero(instrumentoSerie, numero);
	}
	public int modificar(Calibracion calibracion) {
		return calibracionDao.modificar(calibracion);
	}

	public Calibracion obtener(Calibracion calibracion) {
		return calibracionDao.obtener(calibracion);
	}


}
