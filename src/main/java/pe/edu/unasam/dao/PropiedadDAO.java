/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.edu.unasam.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import pe.edu.unasam.config.conexionDB;
import pe.edu.unasam.modelo.Propiedad;

/**
 *
 * @author PC
 */
public class PropiedadDAO {
    // 1. CREAR (Insertar)
    public boolean registrarPropiedad(Propiedad pro) {
        String sql = "INSERT INTO propiedad (nombre, descripcion, patron_regex, tipo_operacion) VALUES (?, ?, ?, ?)";
        
        // Uso de try-with-resources para cierre automático
        try (Connection conexion = conexionDB.iniciarConexion();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            
            pstmt.setString(1, pro.getNombre());
            pstmt.setString(2, pro.getDescripcion());
            pstmt.setString(3, pro.getPatronRegex());
            pstmt.setString(4, pro.getTipoOperacion());
            
            int filasInsertadas = pstmt.executeUpdate();
            
            if (filasInsertadas > 0) {
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al registrar propiedad: " + e.getMessage());
        }
        return false;
    }

    // 2. LEER (Obtener todas para el JComboBox)
    public List<Propiedad> listarPropiedades() {
        List<Propiedad> lista = new ArrayList<>();
        String sql = "SELECT * FROM propiedad";
        
        try (Connection conexion = conexionDB.iniciarConexion();
             PreparedStatement pstmt = conexion.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Propiedad pro = new Propiedad();
                pro.setIdPropiedad(rs.getInt("id_propiedad"));
                pro.setNombre(rs.getString("nombre"));
                pro.setDescripcion(rs.getString("descripcion"));
                pro.setPatronRegex(rs.getString("patron_regex"));
                pro.setTipoOperacion(rs.getString("tipo_operacion"));
                
                lista.add(pro);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al listar propiedades: " + e.getMessage());
        }
        return lista;
    }

    // 3. ACTUALIZAR
    public boolean modificarPropiedad(Propiedad pro) {
        String sql = "UPDATE propiedad SET nombre=?, descripcion=?, patron_regex=?, tipo_operacion=? WHERE id_propiedad=?";
        
        try (Connection conexion = conexionDB.iniciarConexion();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            
            pstmt.setString(1, pro.getNombre());
            pstmt.setString(2, pro.getDescripcion());
            pstmt.setString(3, pro.getPatronRegex());
            pstmt.setString(4, pro.getTipoOperacion());
            pstmt.setInt(5, pro.getIdPropiedad());
            
            int filasActualizadas = pstmt.executeUpdate();
            
            if (filasActualizadas > 0) {
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al modificar propiedad: " + e.getMessage());
        }
        return false;
    }

    // 4. ELIMINAR
    public boolean eliminarPropiedad(int idPropiedad) {
        String sql = "DELETE FROM propiedad WHERE id_propiedad=?";
        
        try (Connection conexion = conexionDB.iniciarConexion();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            
            pstmt.setInt(1, idPropiedad);
            
            int filasEliminadas = pstmt.executeUpdate();
            
            if (filasEliminadas > 0) {
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar propiedad: " + e.getMessage());
        }
        return false;
    }
    
}
