package ejercicio;





import org.sqlite.SQLiteConfig;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Conexion {
    private static Conexion conexion;
    private Connection connection;

    private Conexion() {
        Properties properties = new Properties();
        try (InputStream input = ClassLoader.getSystemClassLoader().getResourceAsStream("db.properties")) { // Cierre automático
            if (input == null) {
                throw new RuntimeException("Archivo db.properties no encontrado");
            }
            properties.load(input);
            String driver = properties.getProperty("driver");
            String db = properties.getProperty("db");
            String url = driver + db;
            SQLiteConfig config = new SQLiteConfig();
            config.enforceForeignKeys(true);
            connection = DriverManager.getConnection(url, config.toProperties());
        } catch (IOException | SQLException e) {
            throw new RuntimeException("Error en la conexión: " + e.getMessage(), e);
        }
    }

    public static Conexion getInstance() {
        if (conexion == null)
            conexion = new Conexion();
        return conexion;
    }

    public Connection getConnection() {
        return connection;
    }
}
