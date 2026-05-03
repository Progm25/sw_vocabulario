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
import pe.edu.unasam.modelo.HistorialEjecucion;

/**
 *
 * @author PC
 */
public class HistorialEjecucionDAO {
    // 1. CREAR (Registrar una nueva ejecución en el historial)
    public boolean registrarHistorial(HistorialEjecucion historial) {
        // Omitimos id_ejecucion (es AUTO_INCREMENT) y fecha_registro (es CURRENT_TIMESTAMP)
        String sql = "INSERT INTO historial_ejecucion (vocabulario, cantidad_lenguajes) VALUES (?, ?)";
        
        try (Connection conexion = conexionDB.iniciarConexion();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            
            pstmt.setString(1, historial.getVocabulario());
            pstmt.setInt(2, historial.getCantidadLenguajes());
            
            int filasInsertadas = pstmt.executeUpdate();
            
            if (filasInsertadas > 0) {
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al registrar historial: " + e.getMessage());
        }
        return false;
    }

    // 2. LEER (Obtener el historial completo, ordenado del más reciente al más antiguo)
    public List<HistorialEjecucion> listarHistorial() {
        List<HistorialEjecucion> lista = new ArrayList<>();
        // Ordenamos por fecha descendente para ver primero las últimas ejecuciones
        String sql = "SELECT * FROM historial_ejecucion ORDER BY fecha_registro DESC";
        
        try (Connection conexion = conexionDB.iniciarConexion();
             PreparedStatement pstmt = conexion.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                HistorialEjecucion historial = new HistorialEjecucion();
                
                historial.setIdEjecucion(rs.getInt("id_ejecucion"));
                historial.setVocabulario(rs.getString("vocabulario"));
                historial.setCantidadLenguajes(rs.getInt("cantidad_lenguajes"));
                historial.setFechaRegistro(rs.getTimestamp("fecha_registro")); // Rescatamos el Timestamp
                
                lista.add(historial);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al listar historial: " + e.getMessage());
        }
        return lista;
    }
    
}
