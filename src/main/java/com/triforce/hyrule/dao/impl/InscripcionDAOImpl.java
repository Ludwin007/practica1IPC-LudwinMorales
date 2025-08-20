/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.triforce.hyrule.dao.impl;
import com.triforce.hyrule.dao.InscripcionDAO;
import com.triforce.hyrule.database.DatabaseConnection;
import com.triforce.hyrule.models.Inscripcion;
import com.triforce.hyrule.enums.TipoInscripcion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ludwi
 */

public class InscripcionDAOImpl implements InscripcionDAO {
    
    @Override
    public boolean insertar(Inscripcion inscripcion) {
        String sql = "INSERT INTO inscripcion (email, codigo_evento, tipo_inscripcion, validado) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, inscripcion.getEmail());
            stmt.setString(2, inscripcion.getCodigoEvento());
            stmt.setString(3, inscripcion.getTipoInscripcion().name());
            stmt.setBoolean(4, inscripcion.isValidado());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public Inscripcion obtener(String email, String codigoEvento) {
        String sql = "SELECT * FROM inscripcion WHERE email = ? AND codigo_evento = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            stmt.setString(2, codigoEvento);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Inscripcion inscripcion = new Inscripcion();
                inscripcion.setEmail(rs.getString("email"));
                inscripcion.setCodigoEvento(rs.getString("codigo_evento"));
                inscripcion.setTipoInscripcion(TipoInscripcion.valueOf(rs.getString("tipo_inscripcion")));
                inscripcion.setValidado(rs.getBoolean("validado"));
                return inscripcion;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public boolean validarInscripcion(String email, String codigoEvento) {
        String sql = "UPDATE inscripcion SET validado = true WHERE email = ? AND codigo_evento = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            stmt.setString(2, codigoEvento);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public List<Inscripcion> obtenerPorEvento(String codigoEvento) {
        List<Inscripcion> inscripciones = new ArrayList<>();
        String sql = "SELECT * FROM inscripcion WHERE codigo_evento = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, codigoEvento);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Inscripcion inscripcion = new Inscripcion();
                inscripcion.setEmail(rs.getString("email"));
                inscripcion.setCodigoEvento(rs.getString("codigo_evento"));
                inscripcion.setTipoInscripcion(TipoInscripcion.valueOf(rs.getString("tipo_inscripcion")));
                inscripcion.setValidado(rs.getBoolean("validado"));
                inscripciones.add(inscripcion);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return inscripciones;
    }
    
    @Override
    public boolean existeInscripcion(String email, String codigoEvento) {
        String sql = "SELECT COUNT(*) FROM inscripcion WHERE email = ? AND codigo_evento = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            stmt.setString(2, codigoEvento);
            ResultSet rs = stmt.executeQuery();
            
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

