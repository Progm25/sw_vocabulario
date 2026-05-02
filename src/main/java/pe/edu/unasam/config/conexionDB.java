
package pe.edu.unasam.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class conexionDB {
    // rutas y credenciales de acceso
    public static String user = "root";
    public static String password = "2025";
    public static String host = "localhost";
    public static String port = "3306";
    public static String db = "jass3030";
        
    // metodo para optener la conexion
    public static Connection iniciarConexion(){
        Connection conexion = null;
        
        // construimos la url
        String url = "jdbc:mysql://" + host + ":" + port + "/" + db + "?characterEncoding=utf8";
        
        try {
            // Intentamos conectarnos a la base de datos
            conexion = DriverManager.getConnection(url, user, password);
            System.out.println("✅ ¡Conexión exitosa a la base de datos MySQL!");
        } catch (SQLException e) {
            // Si algo falla, el catch "atrapa" el error y nos dice qué pasó
            System.err.println("❌ Error al conectar con la base de datos: " + e.getMessage());
        }
        return conexion;
        
        
        
    
    }
    
}
