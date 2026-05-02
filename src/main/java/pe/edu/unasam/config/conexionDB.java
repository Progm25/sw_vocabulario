
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
            System.out.println("✅¡Conexión exitosa a la base de datos MySQL!");
        } catch (SQLException e) {
            // Si algo falla, el catch "atrapa" el error y nos dice qué pasó
            System.err.println("❌Error al conectar con la base de datos: " + e.getMessage());
        }
        return conexion;
        
    
    }
    
}






/*


-- Creación de la base de datos
CREATE DATABASE IF NOT EXISTS jass3030; -- Usando el nombre que tienes en tu código Java
USE jass3030;

-- Tabla: propiedad (Obligatoria)
-- Esta tabla reemplaza los arreglos en memoria y define las reglas lógicas.
CREATE TABLE IF NOT EXISTS propiedad (
    id_propiedad INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255),
    patron_regex VARCHAR(100) NOT NULL, -- Almacena la expresión regular o clave lógica
    tipo_operacion VARCHAR(50) NOT NULL  -- Ej: "Cerradura", "Regex", "Filtro"
);

-- Tabla: historial_ejecucion (Opcional - Valor Agregado)
-- Para registrar qué vocabularios se han procesado y cuántos resultados dieron.
CREATE TABLE IF NOT EXISTS historial_ejecucion (
    id_ejecucion INT AUTO_INCREMENT PRIMARY KEY,
    vocabulario VARCHAR(255) NOT NULL, -- Ej: "0,1" o "a,b,c"
    cantidad_lenguajes INT NOT NULL,     -- Cuántas cadenas se generaron
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Inserción de datos iniciales (Propiedades teóricas)
-- Estos datos te servirán para poblar tus JComboBox en la interfaz Java.
INSERT INTO propiedad (nombre, descripcion, patron_regex, tipo_operacion) VALUES 
('Longitud Par', 'Acepta cadenas con una cantidad par de símbolos.', '^(..)*$', 'Regex'),
('Cerradura de Kleene (*)', 'Genera todas las combinaciones posibles incluyendo la cadena vacía.', 'kleene_star', 'Operacion_Core'),
('Inicia con primer símbolo', 'Garantiza que la cadena inicie con el primer carácter del vocabulario.', 'inicia_primero', 'Filtro_Logico'),
('Potencia de Lenguaje', 'Aplica la concatenación repetida de los símbolos', 'potencia_n', 'Cerradura'),
('Inicia y Termina igual', 'La cadena debe empezar y finalizar con el mismo símbolo', '^(.).*\\1$', 'Expresion Regular');





*/




