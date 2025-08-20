/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.triforce.hyrule.dao.impl;
import com.triforce.hyrule.dao.PagoDAO;
import com.triforce.hyrule.database.DatabaseConnection;
import com.triforce.hyrule.enums.MetodoPago;
import java.math.BigDecimal;
import java.sql.*;

/**
 *
 * @author ludwi
 */

public class PagoDAOImpl implements PagoDAO {
    
    @Override
    public boolean registrarPago(String email, String codigoEvento, MetodoPago metodoPago, BigDecimal monto) {
        String sql = "INSERT INTO pago (email, codigo_evento, metodo_pago, monto) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            stmt.setString(2, codigoEvento);
            stmt.setString(3, metodoPago.name());
            stmt.setBigDecimal(4, monto);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean existePago(String email, String codigoEvento) {
        String sql = "SELECT COUNT(*) FROM pago WHERE email = ? AND codigo_evento = ?";
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
    
    @Override
    public BigDecimal obtenerMontoTotalEvento(String codigoEvento) {
        String sql = "SELECT COALESCE(SUM(monto), 0) FROM pago WHERE codigo_evento = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, codigoEvento);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getBigDecimal(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }
}
