/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.triforce.hyrule.dao.impl;
import com.triforce.hyrule.dao.ActividadDAO;
import com.triforce.hyrule.database.DatabaseConnection;
import com.triforce.hyrule.models.Actividad;
import com.triforce.hyrule.enums.TipoActividad;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author ludwi
 */

public class ActividadDAOImpl implements ActividadDAO {
    
    @Override
    public boolean insertar(Actividad actividad) {
        String sql = "INSERT INTO actividad (codigo_actividad, codigo_evento, tipo, titulo, email_encargado, hora_inicio, hora_fin, cupo_maximo) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, actividad.getCodigoActividad());
            stmt.setString(2, actividad.getCodigoEvento());
            stmt.setString(3, actividad.getTipo().name());
            stmt.setString(4, actividad.getTitulo());
            stmt.setString(5, actividad.getEmailEncargado());
            stmt.setTime(6, Time.valueOf(actividad.getHoraInicio()));
            stmt.setTime(7, Time.valueOf(actividad.getHoraFin()));
            stmt.setInt(8, actividad.getCupoMaximo());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public Actividad obtenerPorCodigo(String codigoActividad) {
        String sql = "SELECT * FROM actividad WHERE codigo_actividad = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, codigoActividad);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Actividad actividad = new Actividad();
                actividad.setCodigoActividad(rs.getString("codigo_actividad"));
                actividad.setCodigoEvento(rs.getString("codigo_evento"));
                actividad.setTipo(TipoActividad.valueOf(rs.getString("tipo")));
                actividad.setTitulo(rs.getString("titulo"));
                actividad.setEmailEncargado(rs.getString("email_encargado"));
                actividad.setHoraInicio(rs.getTime("hora_inicio").toLocalTime());
                actividad.setHoraFin(rs.getTime("hora_fin").toLocalTime());
                actividad.setCupoMaximo(rs.getInt("cupo_maximo"));
                return actividad;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public List<Actividad> obtenerPorEvento(String codigoEvento, TipoActividad tipo, String emailEncargado) {
        List<Actividad> actividades = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM actividad WHERE codigo_evento = ?");
        List<Object> parametros = new ArrayList<>();
        parametros.add(codigoEvento);
        
        if (tipo != null) {
            sql.append(" AND tipo = ?");
            parametros.add(tipo.name());
        }
        if (emailEncargado != null && !emailEncargado.trim().isEmpty()) {
            sql.append(" AND email_encargado = ?");
            parametros.add(emailEncargado);
        }
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            
            for (int i = 0; i < parametros.size(); i++) {
                stmt.setObject(i + 1, parametros.get(i));
            }
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Actividad actividad = new Actividad();
                actividad.setCodigoActividad(rs.getString("codigo_actividad"));
                actividad.setCodigoEvento(rs.getString("codigo_evento"));
                actividad.setTipo(TipoActividad.valueOf(rs.getString("tipo")));
                actividad.setTitulo(rs.getString("titulo"));
                actividad.setEmailEncargado(rs.getString("email_encargado"));
                actividad.setHoraInicio(rs.getTime("hora_inicio").toLocalTime());
                actividad.setHoraFin(rs.getTime("hora_fin").toLocalTime());
                actividad.setCupoMaximo(rs.getInt("cupo_maximo"));
                actividades.add(actividad);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return actividades;
    }
    
    @Override
    public int obtenerCantidadAsistentes(String codigoActividad) {
        String sql = "SELECT COUNT(*) FROM asistencia WHERE codigo_actividad = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, codigoActividad);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}