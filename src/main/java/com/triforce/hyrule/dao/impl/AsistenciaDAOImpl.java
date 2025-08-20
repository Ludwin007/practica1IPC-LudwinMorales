/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.triforce.hyrule.dao.impl;
import com.triforce.hyrule.dao.AsistenciaDAO;
import com.triforce.hyrule.database.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ludwi
 */

public class AsistenciaDAOImpl implements AsistenciaDAO {
    
    @Override
    public boolean registrarAsistencia(String email, String codigoActividad) {
        String sql = "INSERT INTO asistencia (email, codigo_actividad) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            stmt.setString(2, codigoActividad);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean existeAsistencia(String email, String codigoActividad) {
        String sql = "SELECT COUNT(*) FROM asistencia WHERE email = ? AND codigo_actividad = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            stmt.setString(2, codigoActividad);
            ResultSet rs = stmt.executeQuery();
            
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public List<String> obtenerActividadesParticipante(String email, String codigoEvento) {
        List<String> actividades = new ArrayList<>();
        String sql = "SELECT a.codigo_actividad FROM asistencia as ass " +
                    "JOIN actividad a ON ass.codigo_actividad = a.codigo_actividad " +
                    "WHERE ass.email = ? AND a.codigo_evento = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            stmt.setString(2, codigoEvento);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                actividades.add(rs.getString("codigo_actividad"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return actividades;
    }
}