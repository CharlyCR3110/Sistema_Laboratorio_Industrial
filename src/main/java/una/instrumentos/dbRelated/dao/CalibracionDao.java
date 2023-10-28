package una.instrumentos.dbRelated.dao;

import una.instrumentos.dbRelated.controller.InstrumentoDaoController;
import una.instrumentos.dbRelated.controller.MedicionDaoController;
import una.instrumentos.logic.Calibracion;
import una.instrumentos.logic.Instrumento;
import una.instrumentos.logic.Medicion;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CalibracionDao {
	Connection connection;
	MedicionDaoController medicionDaoController;
	InstrumentoDaoController instrumentoDaoController;

	public CalibracionDao(Connection connection) {
		this.connection = connection;
		this.medicionDaoController = new MedicionDaoController();
		this.instrumentoDaoController = new InstrumentoDaoController();
	}

	public boolean tieneDuplicados(Calibracion calibracion) {
		boolean r = false;
		// Consulta SQL para verificar si existe un registro con el mismo codigo
		String query = "SELECT * FROM calibraciones WHERE numero = ?";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			// Setear los valores de la consulta SQL
			statement.setString(1, calibracion.getNumero());

			// Ejecutar la consulta SQL y obtener el resultado
			try (ResultSet resultSet = statement.executeQuery()) {
				// Si existe un registro con el mismo codigo, setear el resultado a true
				if (resultSet.next()) {
					// Obtener el numero de registros
					int rows = resultSet.getInt(1);

					// Si el numero de registros es mayor a 0, entonces existe un registro con el mismo codigo
					if (rows > 0) {
						r = true;
					}
				}
			}
		} catch (SQLException e) {
			// Lanzar una excepción en caso de que ocurra un error
			throw new RuntimeException(e);
		}
		return r;
	}

	public int guardar(Calibracion calibracion) {
		int r = 0;
		// Consulta SQL para insertar un registro en la tabla de calibraciones
		String query = "INSERT INTO calibraciones (numero, fecha, numero_de_mediciones, instrumento_serie) VALUES (?, ?, ?, ?)";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			// Setear los valores de la consulta SQL
			statement.setString(1, calibracion.getNumero());
			statement.setDate(2, Date.valueOf(calibracion.getFecha()));
			statement.setInt(3, calibracion.getNumeroDeMediciones());
			statement.setString(4, calibracion.getInstrumento().getSerie());

			// Ejecutar la consulta SQL y obtener el resultado
			r = statement.executeUpdate();

			// Guardar las mediciones
			for (Medicion medicion : calibracion.getMediciones()) {
				try {
					medicionDaoController.guardar(medicion, calibracion.getNumero());
				} catch (RuntimeException e) {
					// Lanzar una excepción en caso de que ocurra un error
					throw new RuntimeException(e);
				}
			}
		} catch (SQLException e) {
			// Lanzar una excepción en caso de que ocurra un error
			throw new RuntimeException(e);
		}
		return r;
	}

	public List<Calibracion> listar(String instrumento_serie) {	// lista de calibraciones de un instrumento
		List<Calibracion> r = new ArrayList<>();
		// Consulta SQL para obtener las calibraciones de un instrumento
		String query = "SELECT * FROM calibraciones WHERE instrumento_serie = ?";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			// Setear los valores de la consulta SQL
			statement.setString(1, instrumento_serie);

			// Ejecutar la consulta SQL y obtener el resultado
			try (ResultSet resultSet = statement.executeQuery()) {
				// Recorrer el resultado obtenido
				while (resultSet.next()) {
					// Crear una nueva instancia de Calibracion
					Calibracion calibracion = new Calibracion();

					// Llenar los datos de la calibracion con los datos obtenidos de la base de datos
					calibracion.setNumero(resultSet.getString("numero"));
					calibracion.setFecha(resultSet.getDate("fecha").toLocalDate());
					calibracion.setNumeroDeMediciones(resultSet.getInt("numero_de_mediciones"));
					// obtener las mediciones de la calibracion
					List<Medicion> mediciones = medicionDaoController.listar(calibracion.getNumero());
					calibracion.setMediciones(mediciones);
					// obtener el instrumento de la calibracion
					Instrumento instrumentoFilter = new Instrumento();
					instrumentoFilter.setSerie(resultSet.getString("instrumento_serie"));

					Instrumento instrumentoFinal = instrumentoDaoController.obtener(instrumentoFilter);

					calibracion.setInstrumento(instrumentoFinal);
					// Agregar la calibracion a la lista de calibraciones
					r.add(calibracion);
				}
			}
		} catch (SQLException e) {
			// Lanzar una excepción en caso de que ocurra un error
			throw new RuntimeException(e);
		}
		return r;
	}

	// por el DELETE CASCADE, no es necesario eliminar las mediciones de la calibracion
	public int eliminar (String numero) {
		// Consulta SQL para eliminar un registro de la tabla de calibraciones
		String query = "DELETE FROM calibraciones WHERE numero = ?";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			// Setear los valores de la consulta SQL
			statement.setString(1, numero);

			// Ejecutar la consulta SQL y obtener el resultado
			return statement.executeUpdate();
		} catch (SQLException e) {
			// Lanzar una excepción en caso de que ocurra un error
			throw new RuntimeException(e);
		}
	}

	public List<Calibracion> buscarPorNumero(String instrumentoSerie, String numero) {
		List<Calibracion> r = new ArrayList<>();
		// Consulta SQL para obtener las calibraciones de un instrumento con un numero especifico
		String query = "SELECT * FROM calibraciones WHERE instrumento_serie = ? AND numero = ?";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			// settear los valores
			statement.setInt(1, Integer.parseInt(instrumentoSerie));
			statement.setInt(2, Integer.parseInt(numero));

			// Ejecutar la consulta SQL y obtener el resultado
			try (ResultSet resultSet = statement.executeQuery()) {
				// Recorrer el resultado obtenido
				while (resultSet.next()) {
					// Crear una nueva instancia de Calibracion
					Calibracion calibracion = new Calibracion();

					// Llenar los datos de la calibracion con los datos obtenidos de la base de datos
					calibracion.setNumero(resultSet.getString("numero"));
					calibracion.setFecha(resultSet.getDate("fecha").toLocalDate());
					calibracion.setNumeroDeMediciones(resultSet.getInt("numero_de_mediciones"));
					// obtener las mediciones de la calibracion
					List<Medicion> mediciones = medicionDaoController.listar(calibracion.getNumero());
					calibracion.setMediciones(mediciones);
					// obtener el instrumento de la calibracion
					Instrumento instrumentoFilter = new Instrumento();
					instrumentoFilter.setSerie(resultSet.getString("instrumento_serie"));

					Instrumento instrumentoFinal = instrumentoDaoController.obtener(instrumentoFilter);

					calibracion.setInstrumento(instrumentoFinal);
					// Agregar la calibracion a la lista de calibraciones
					r.add(calibracion);
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return r;
	}

	public int modificar(Calibracion calibracion) {
		// Unicamente se puede modificar la fecha
		// Consulta SQL para modificar un registro de la tabla de calibraciones
		String query = "UPDATE calibraciones SET fecha = ? WHERE numero = ?";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			// Setear los valores de la consulta SQL
			statement.setDate(1, Date.valueOf(calibracion.getFecha()));
			statement.setString(2, calibracion.getNumero());
			// Ejecutar la consulta SQL y obtener el resultado
			return statement.executeUpdate();
		} catch (SQLException e) {
			// Lanzar una excepción en caso de que ocurra un error
			throw new RuntimeException(e);
		}
	}

	public Calibracion obtener(Calibracion calibracion) {
		// Consulta SQL para obtener un registro de la tabla de calibraciones
		String query = "SELECT * FROM calibraciones WHERE numero = ?";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			// Setear los valores de la consulta SQL
			statement.setString(1, calibracion.getNumero());

			// Ejecutar la consulta SQL y obtener el resultado
			try (ResultSet resultSet = statement.executeQuery()) {
				// Si existe un registro con el mismo codigo, setear el resultado a true
				if (resultSet.next()) {
					// Obtener el numero de registros
					int rows = resultSet.getInt(1);

					// Si el numero de registros es mayor a 0, entonces existe un registro con el mismo codigo
					if (rows > 0) {
						// Crear una nueva instancia de Calibracion
						Calibracion calibracionFinal = new Calibracion();

						// Llenar los datos de la calibracion con los datos obtenidos de la base de datos
						calibracionFinal.setNumero(resultSet.getString("numero"));
						calibracionFinal.setFecha(resultSet.getDate("fecha").toLocalDate());
						calibracionFinal.setNumeroDeMediciones(resultSet.getInt("numero_de_mediciones"));
						// obtener las mediciones de la calibracion
						List<Medicion> mediciones = medicionDaoController.listar(calibracionFinal.getNumero());
						calibracionFinal.setMediciones(mediciones);
						// obtener el instrumento de la calibracion
						Instrumento instrumentoFilter = new Instrumento();
						instrumentoFilter.setSerie(resultSet.getString("instrumento_serie"));

						Instrumento instrumentoFinal = instrumentoDaoController.obtener(instrumentoFilter);

						calibracionFinal.setInstrumento(instrumentoFinal);

						return calibracionFinal;
					}
				}
			}
		} catch (SQLException e) {
			// Lanzar una excepción en caso de que ocurra un error
			throw new RuntimeException(e);
		}
		return null;
	}
}
