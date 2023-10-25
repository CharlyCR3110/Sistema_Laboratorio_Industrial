package una.factory;

import javax.sql.DataSource;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {
	private DataSource dataSource;
	private static final String PROPERTIES_FILE_NAME = "/database.properties";
	public ConnectionFactory() {
		Properties prop = new Properties();
		try {
			prop.load(getClass().getResourceAsStream(PROPERTIES_FILE_NAME));
			String driver = prop.getProperty("database_driver");
			String server = prop.getProperty("database_server");
			String port = prop.getProperty("database_port");
			String user = prop.getProperty("database_user");
			String password = prop.getProperty("database_password");
			String database = prop.getProperty("database_name");
			String maxPoolSize = prop.getProperty("max_pool_size");

			String URL_conexion = "jdbc:mysql://" + server + ":" + port + "/" + database + "?user=" + user
					+ "&password=" + password + "&serverTimezone=UTC";
			Class.forName(driver).newInstance();

			var comboPooledDataSource = new ComboPooledDataSource();
			comboPooledDataSource.setJdbcUrl(URL_conexion);
			comboPooledDataSource.setMaxPoolSize(Integer.parseInt(maxPoolSize));
			this.dataSource = comboPooledDataSource;
		} catch (Exception e) {
			System.err.println("FALLÓ CONEXION A BASE DE DATOS");
			System.err.println(e.getMessage());
			System.exit(-1);
		}
	}

	public Connection recuperarConexion() {
		try {
			return this.dataSource.getConnection();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
