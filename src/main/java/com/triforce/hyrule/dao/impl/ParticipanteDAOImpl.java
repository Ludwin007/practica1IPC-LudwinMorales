/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.triforce.hyrule.dao.impl;
import com.triforce.hyrule.dao.ParticipanteDAO;
import com.triforce.hyrule.database.DatabaseConnection;
import com.triforce.hyrule.models.Participante;
import com.triforce.hyrule.enums.TipoParticipante;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ludwi
 */


public class ParticipanteDAOImpl implements ParticipanteDAO {
    
    @Override
    public boolean insertar(Participante participante) {
        String sql = "INSERT INTO participante (email, nombre_completo, tipo, institucion) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, participante.getEmail());
            stmt.setString(2, participante.getNombreCompleto());
            stmt.setString(3, participante.getTipo().name());
            stmt.setString(4, participante.getInstitucion());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public Participante obtenerPorEmail(String email) {
        String sql = "SELECT * FROM participante WHERE email = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Participante participante = new Participante();
                participante.setEmail(rs.getString("email"));
                participante.setNombreCompleto(rs.getString("nombre_completo"));
                participante.setTipo(TipoParticipante.valueOf(rs.getString("tipo")));
                participante.setInstitucion(rs.getString("institucion"));
                return participante;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public List<Participante> obtenerTodos() {
        List<Participante> participantes = new ArrayList<>();
        String sql = "SELECT * FROM participante";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Participante participante = new Participante();
                participante.setEmail(rs.getString("email"));
                participante.setNombreCompleto(rs.getString("nombre_completo"));
                participante.setTipo(TipoParticipante.valueOf(rs.getString("tipo")));
                participante.setInstitucion(rs.getString("institucion"));
                participantes.add(participante);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return participantes;
    }
    
    @Override
    public List<Participante> obtenerPorEvento(String codigoEvento, TipoParticipante tipo, String institucion) {
        List<Participante> participantes = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT p.*, i.validado FROM participante p " +
            "JOIN inscripcion i ON p.email = i.email " +
            "WHERE i.codigo_evento = ?"
        );
        
        List<Object> parametros = new ArrayList<>();
        parametros.add(codigoEvento);
        
        if (tipo != null) {
            sql.append(" AND p.tipo = ?");
            parametros.add(tipo.name());
        }
        if (institucion != null && !institucion.trim().isEmpty()) {
            sql.append(" AND p.institucion = ?");
            parametros.add(institucion);
        }      
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {           
            for (int i = 0; i < parametros.size(); i++) {
                stmt.setObject(i + 1, parametros.get(i));
            }           
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Participante participante = new Participante();
                participante.setEmail(rs.getString("email"));
                participante.setNombreCompleto(rs.getString("nombre_completo"));
                participante.setTipo(TipoParticipante.valueOf(rs.getString("tipo")));
                participante.setInstitucion(rs.getString("institucion"));
                participantes.add(participante);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return participantes;
    }
}