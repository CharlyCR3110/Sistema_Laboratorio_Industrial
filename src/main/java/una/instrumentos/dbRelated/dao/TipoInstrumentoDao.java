package una.instrumentos.dbRelated.dao;

import una.instrumentos.logic.TipoInstrumento;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TipoInstrumentoDao {
	private Connection connection;

	public TipoInstrumentoDao(Connection connection) {
		this.connection = connection;
	}

	public boolean tieneDuplicados(TipoInstrumento tipoInstrumento) {
		boolean r = false;
		// Consulta SQL para verificar si existe un registro con el mismo codigo
		String query = "SELECT * FROM tipo_instrumentos WHERE codigo = ?";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			// Setear los valores de la consulta SQL
			statement.setString(1, tipoInstrumento.getCodigo());

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

	public int guardar(TipoInstrumento tipoInstrumento) {
		// comprueba si ya existe un TipoInstrumento con el mismo codigo
		if (tieneDuplicados(tipoInstrumento)) {
			System.err.println("Ya existe un TipoInstrumento con el mismo codigo");	//debug
			return -1;
		}

		System.out.println("Guardando TipoInstrumento" + tipoInstrumento.toString());	//debug

		// Consulta SQL para insertar un TipoInstrumento
		String query = "INSERT INTO tipo_instrumentos (codigo, nombre, unidad) VALUES (?, ?, ?)";

		try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
			// settear los valor
			statement.setString(1, tipoInstrumento.getCodigo());
			statement.setString(2, tipoInstrumento.getNombre());
			statement.setString(3, tipoInstrumento.getUnidad());

			// Ejecutar la consulta y guardar el numero de filas afectadas
			int rowsAffected = statement.executeUpdate();

			// Obtener el id generado
			if (rowsAffected == 1) {
				try (ResultSet resultSet = statement.getGeneratedKeys()) {
					if (resultSet.next()) {
						return resultSet.getInt(1);
					}
				}
			} else {
				System.err.println("No se guardo el TipoInstrumento");	//debug
			}
		} catch (SQLException e) {
			// Lanzar una excepción en caso de que ocurra un error
			throw new RuntimeException(e);
		}

		return -1;	// No se guardo el TipoInstrumento
	}

	public List<TipoInstrumento> listar() {
		List<TipoInstrumento> r = new ArrayList<>();

		// Consulta SQL para listar todos los TipoInstrumentos
		String query = "SELECT * FROM tipo_instrumentos";

		try (PreparedStatement statement = connection.prepareStatement(query)) {
			// Ejecutar la consulta SQL y obtener el resultado
			try (ResultSet resultSet = statement.executeQuery()) {
				// Recorrer el resultado y agregarlo a la lista
				while (resultSet.next()) {
					// se crea un nuevo TipoInstrumento y se agrega a la lista
					r.add(new TipoInstrumento(
							resultSet.getString("codigo"),
							resultSet.getString("nombre"),
							resultSet.getString("unidad")
					));
				}
			}
		} catch (SQLException e) {
			// Lanzar una excepción en caso de que ocurra un error
			throw new RuntimeException(e);
		}
		return r;
	}

	public int eliminar(String codigo) {
		String query = "DELETE FROM tipo_instrumentos WHERE codigo = ?";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			// Setear los valores de la consulta SQL
			statement.setString(1, codigo);

			// Ejecutar la consulta SQL y obtener el resultado
			int rowsAffected = statement.executeUpdate();

			// Obtener el id generado

			// Retornar el número de filas afectadas
			return rowsAffected;
		} catch (SQLException e) {
			// Lanzar una excepción en caso de que ocurra un error
			throw new RuntimeException(e);
		}
	}

	public List<TipoInstrumento> listarPorNombre(String nombre) {
		// Query SQL para listar los TipoInstrumentos que tengan el nombre especificado
		String query;
		List<TipoInstrumento> r = new ArrayList<>();

		// Verificar si el nombre es nulo o vacío
		if (nombre == null || nombre.isEmpty()) {
			// Si es nulo o vacío, obtener todos los elementos de la tabla
			query = "SELECT * FROM tipo_instrumentos";
		} else {
			// Si no es nulo o vacío, obtener los elementos con el nombre especificado
			query = "SELECT * FROM tipo_instrumentos WHERE nombre LIKE ?";
		}

		try (PreparedStatement statement = connection.prepareStatement(query)) {
			// setear el valor del nombre si no es nulo o vacío
			if (nombre != null && !nombre.isEmpty()) {
				// Utilizar % para indicar que el nombre puede contener el valor en cualquier posición
				statement.setString(1, "%" + nombre + "%");
			}

			// Ejecutar la consulta SQL y obtener el resultado
			try (ResultSet resultSet = statement.executeQuery()) {
				// Agregar los tipos a la lista
				while (resultSet.next()) {
					r.add(new TipoInstrumento(
							resultSet.getString("codigo"),
							resultSet.getString("nombre"),
							resultSet.getString("unidad")
					));
				}
			}
		} catch (SQLException e) {
			// Lanzar una excepción en caso de que ocurra un error
			throw new RuntimeException(e);
		}

		// Retornar la lista de tipos
		return r;
	}


	public int modificar(TipoInstrumento tipoInstrumento) {
		// Consulta SQL para modificar un TipoInstrumento
		String query = "UPDATE tipo_instrumentos SET nombre = ?, unidad = ? WHERE codigo = ?";

		try (PreparedStatement statement = connection.prepareStatement(query)) {
			// settear los valores
			statement.setString(1, tipoInstrumento.getNombre());
			statement.setString(2, tipoInstrumento.getUnidad());
			statement.setString(3, tipoInstrumento.getCodigo());

			// Ejecutar la consulta y guardar el numero de filas afectadas
			int rowsAffected = statement.executeUpdate();

			// Retornar el número de filas afectadas
			return rowsAffected;
		} catch (SQLException e) {
			// Lanzar una excepción en caso de que ocurra un error
			throw new RuntimeException(e);
		}
	}

	public TipoInstrumento obtener(TipoInstrumento e) {
String query = "SELECT * FROM tipo_instrumentos WHERE codigo = ?";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			// Setear los valores de la consulta SQL
			statement.setString(1, e.getCodigo());

			// Ejecutar la consulta SQL y obtener el resultado
			try (ResultSet resultSet = statement.executeQuery()) {
				// Si existe un registro con el mismo codigo, setear el resultado a true
				if (resultSet.next()) {
					return new TipoInstrumento(
							resultSet.getString("codigo"),
							resultSet.getString("nombre"),
							resultSet.getString("unidad")
					);
				}
			}
		} catch (SQLException ex) {
			// Lanzar una excepción en caso de que ocurra un error
			throw new RuntimeException(ex);
		}
		return null;
	}
}