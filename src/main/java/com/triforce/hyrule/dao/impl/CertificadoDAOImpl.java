/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.triforce.hyrule.dao.impl;
import com.triforce.hyrule.dao.CertificadoDAO;
import com.triforce.hyrule.database.DatabaseConnection;
import java.sql.*;

/**
 *
 * @author ludwi
 */

public class CertificadoDAOImpl implements CertificadoDAO {
    
    @Override
    public boolean registrarCertificado(String email, String codigoEvento, String rutaArchivo) {
        String sql = "INSERT INTO certificado (email, codigo_evento, ruta_archivo) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            stmt.setString(2, codigoEvento);
            stmt.setString(3, rutaArchivo);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean existeCertificado(String email, String codigoEvento) {
        String sql = "SELECT COUNT(*) FROM certificado WHERE email = ? AND codigo_evento = ?";
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