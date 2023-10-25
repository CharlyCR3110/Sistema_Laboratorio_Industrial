package una.instrumentos.dbRelated.dao;

import una.instrumentos.logic.Instrumento;
import una.instrumentos.logic.TipoInstrumento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InstrumentoDao {
	private Connection connection;
	public InstrumentoDao(Connection connection) {
		this.connection = connection;
	}
	public boolean tieneDuplicados(Instrumento instrumento) {
		boolean r = false;
		// Consulta SQL para verificar si existe un registro con el mismo codigo
		String query = "SELECT * FROM instrumentos WHERE serie = ?";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			// Setear los valores de la consulta SQL
			statement.setString(1, instrumento.getSerie());

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

	public int guardar(Instrumento instrumento) {
		if (tieneDuplicados(instrumento) ) {
			System.err.println("Ya existe un instrumento con el mismo codigo");	//debug
			return -1;
		}

		System.out.println("Guardando instrumento" + instrumento.toString());	//debug

		// Consulta SQL para insertar un instrumento
		String query = "INSERT INTO instrumentos (serie, descripcion, tipo, minimo, maximo, tolerancia) VALUES (?, ?, ?, ?, ?, ?)";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			// Setear los valores de la consulta SQL
			statement.setString(1, instrumento.getSerie());
			statement.setString(2, instrumento.getDescripcion());
			statement.setString(3, instrumento.getTipo().getCodigo());
			statement.setInt(4, instrumento.getMinimo());
			statement.setInt(5, instrumento.getMaximo());
			statement.setInt(6, instrumento.getTolerancia());

			// Ejecutar la consulta SQL
			int rowsAffected = statement.executeUpdate();

			// DEBUG (CAMBIAR POR UN RETURN DIRECTO)
			if (rowsAffected == 0) {
				System.out.println("No se guardo el instrumento");
				throw new SQLException("No se guardo el instrumento");
			}

			return rowsAffected;
		} catch (SQLException e) {
			// Lanzar una excepción en caso de que ocurra un error
			throw new RuntimeException(e);
		}
	}

	public List<Instrumento> listar() {
		List<Instrumento> r = new ArrayList<>();

		// Consulta SQL para obtener todos los instrumentos
		String query = "SELECT * FROM instrumentos";

		try (PreparedStatement statement = connection.prepareStatement(query)) {
			// Ejecutar la consulta SQL y obtener el resultado
			try (ResultSet resultSet = statement.executeQuery()) {
				// Recorrer el resultado y agregar cada registro a la lista
				while (resultSet.next()) {
					// Obtener los valores de cada columna
					String serie = resultSet.getString("serie");
					String descripcion = resultSet.getString("descripcion");
					String tipo = resultSet.getString("tipo");
					int minimo = resultSet.getInt("minimo");
					int maximo = resultSet.getInt("maximo");
					int tolerancia = resultSet.getInt("tolerancia");

					// Crear un objeto de tipo instrumento con los valores de la consulta
					Instrumento instrumento = new Instrumento(serie, descripcion, minimo, maximo, tolerancia, new TipoInstrumentoDao(connection).obtener(new TipoInstrumento(tipo, tipo, tipo)));	// Se crea un objeto de tipo TipoInstrumento con todos los valores iguales, porque realmente solo es necesario el codigo
					// Agregar el objeto a la lista
					r.add(instrumento);
				}
			}
		} catch (SQLException e) {
			// Lanzar una excepción en caso de que ocurra un error
			throw new RuntimeException(e);
		}

		return r;
	}

	public int eliminar(String serie) {
		String query = "DELETE FROM instrumentos WHERE serie = ?";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			// Setear los valores de la consulta SQL
			statement.setString(1, serie);

			// Ejecutar la consulta SQL y obtener el resultado
			int rowsAffected = statement.executeUpdate();

			// DEBUG (CAMBIAR POR UN RETURN DIRECTO)
			if (rowsAffected == 0) {
				System.out.println("No se elimino el instrumento");
				throw new SQLException("No se elimino el instrumento");
			}

			return rowsAffected;
		} catch (SQLException e) {
			// Lanzar una excepción en caso de que ocurra un error
			throw new RuntimeException(e);
		}
	}

	public List<Instrumento> listarPorDescripcion(String descripcion) {
		String query;
		List<Instrumento> r = new ArrayList<>();

		// Verificar si la descripcion es vacia\
		if (descripcion == null || descripcion.isEmpty()) {
			// Si es nulo o vacío, obtener todos los elementos de la tabla
			query = "SELECT * FROM instrumentos";
		} else {
			// Si no es nulo o vacío, obtener los elementos con el nombre especificado
			query = "SELECT * FROM instrumentos WHERE descripcion LIKE ?";
		}

		try (PreparedStatement statement = connection.prepareStatement(query)) {
			// settear la descripcion si no es nula o vacia
			if (descripcion != null && !descripcion.isEmpty()) {
				statement.setString(1, "%" + descripcion + "%");
			}

			// Ejectuar la consulta SQL y obtener el resultado
			try (ResultSet resultSet = statement.executeQuery()) {
				// Agregar cada registro a la lista
				while (resultSet.next()) {
					// Obtener los valores de cada columna
					String serie = resultSet.getString("serie");
					String descripcionInstrumento = resultSet.getString("descripcion");
					String tipo = resultSet.getString("tipo");
					int minimo = resultSet.getInt("minimo");
					int maximo = resultSet.getInt("maximo");
					int tolerancia = resultSet.getInt("tolerancia");

					// Crear un objeto de tipo instrumento con los valores de la consulta
					Instrumento instrumento = new Instrumento(serie, descripcionInstrumento, minimo, maximo, tolerancia, new TipoInstrumentoDao(connection).obtener(new TipoInstrumento(tipo, tipo, tipo)));
					// Agregar el objeto a la lista
					r.add(instrumento);
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		// devolver la lista
		return r;
	}

	public int modificar (Instrumento instrumento) {
		// consulta SQL para modificar un instrumento
		String query = "UPDATE instrumentos SET descripcion = ?, tipo = ?, minimo = ?, maximo = ?, tolerancia = ? WHERE serie = ?";

		try (PreparedStatement statement = connection.prepareStatement(query)) {
			// settear los valores
			statement.setString(1, instrumento.getDescripcion());
			statement.setString(2, instrumento.getTipo().getCodigo());
			statement.setInt(3, instrumento.getMinimo());
			statement.setInt(4, instrumento.getMaximo());
			statement.setInt(5, instrumento.getTolerancia());
			statement.setString(6, instrumento.getSerie());

			// ejecutar la consulta y guardar el numero de filas afectadas
			int rowsAffected = statement.executeUpdate();

			// DEBUG
			if (rowsAffected == 0) {
				System.out.println("No se modifico el instrumento");
			}

			// retornar el numero de filas afectadas
			return rowsAffected;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public Instrumento obtener(Instrumento instrumento) {
		String query = "SELECT * FROM instrumentos WHERE serie = ?";

		try (PreparedStatement statement = connection.prepareStatement(query)) {
			// Settear los valores
			statement.setString(1, instrumento.getSerie());

			// ejecutar la consultar y obtener el resultado
			try (ResultSet resultSet = statement.executeQuery()) {
				// Si existe un registro con el mismo codigo
				if (resultSet.next()) {
					return new Instrumento(
							resultSet.getString("serie"),
							resultSet.getString("descripcion"),
							resultSet.getInt("minimo"),
							resultSet.getInt("maximo"),
							resultSet.getInt("tolerancia"),
							new TipoInstrumentoDao(connection).obtener(new TipoInstrumento(resultSet.getString("tipo"), resultSet.getString("tipo"), resultSet.getString("tipo")))
					);
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return null;
	}
}
