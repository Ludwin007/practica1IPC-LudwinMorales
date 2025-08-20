/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.triforce.hyrule.dao.impl;
import com.triforce.hyrule.dao.EventoDAO;
import com.triforce.hyrule.database.DatabaseConnection;
import com.triforce.hyrule.models.Evento;
import com.triforce.hyrule.enums.TipoEvento;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ludwi
 */

public class EventoDAOImpl implements EventoDAO {
    
    @Override
    public boolean insertar(Evento evento) {
        String sql = "INSERT INTO evento (codigo_evento, fecha, tipo, titulo, ubicacion, cupo_maximo, costo_inscripcion) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, evento.getCodigoEvento());
            stmt.setDate(2, Date.valueOf(evento.getFecha()));
            stmt.setString(3, evento.getTipo().name());
            stmt.setString(4, evento.getTitulo());
            stmt.setString(5, evento.getUbicacion());
            stmt.setInt(6, evento.getCupoMaximo());
            stmt.setBigDecimal(7, evento.getCostoInscripcion());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public Evento obtenerPorCodigo(String codigo) {
        String sql = "SELECT * FROM evento WHERE codigo_evento = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, codigo);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Evento evento = new Evento();
                evento.setCodigoEvento(rs.getString("codigo_evento"));
                evento.setFecha(rs.getDate("fecha").toLocalDate());
                evento.setTipo(TipoEvento.valueOf(rs.getString("tipo")));
                evento.setTitulo(rs.getString("titulo"));
                evento.setUbicacion(rs.getString("ubicacion"));
                evento.setCupoMaximo(rs.getInt("cupo_maximo"));
                evento.setCostoInscripcion(rs.getBigDecimal("costo_inscripcion"));
                return evento;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public List<Evento> obtenerTodos() {
        List<Evento> eventos = new ArrayList<>();
        String sql = "SELECT * FROM evento";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Evento evento = new Evento();
                evento.setCodigoEvento(rs.getString("codigo_evento"));
                evento.setFecha(rs.getDate("fecha").toLocalDate());
                evento.setTipo(TipoEvento.valueOf(rs.getString("tipo")));
                evento.setTitulo(rs.getString("titulo"));
                evento.setUbicacion(rs.getString("ubicacion"));
                evento.setCupoMaximo(rs.getInt("cupo_maximo"));
                evento.setCostoInscripcion(rs.getBigDecimal("costo_inscripcion"));
                eventos.add(evento);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return eventos;
    }
    
    @Override
    public List<Evento> obtenerPorFiltros(TipoEvento tipo, LocalDate fechaInicio, LocalDate fechaFin, 
                                          Integer cupoMin, Integer cupoMax) {
        List<Evento> eventos = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM evento WHERE 1=1");
        List<Object> parametros = new ArrayList<>();
        
        if (tipo != null) {
            sql.append(" AND tipo = ?");
            parametros.add(tipo.name());
        }
        if (fechaInicio != null && fechaFin != null) {
            sql.append(" AND fecha BETWEEN ? AND ?");
            parametros.add(Date.valueOf(fechaInicio));
            parametros.add(Date.valueOf(fechaFin));
        }
        if (cupoMin != null && cupoMax != null) {
            sql.append(" AND cupo_maximo BETWEEN ? AND ?");
            parametros.add(cupoMin);
            parametros.add(cupoMax);
        }
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            
            for (int i = 0; i < parametros.size(); i++) {
                stmt.setObject(i + 1, parametros.get(i));
            }
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Evento evento = new Evento();
                evento.setCodigoEvento(rs.getString("codigo_evento"));
                evento.setFecha(rs.getDate("fecha").toLocalDate());
                evento.setTipo(TipoEvento.valueOf(rs.getString("tipo")));
                evento.setTitulo(rs.getString("titulo"));
                evento.setUbicacion(rs.getString("ubicacion"));
                evento.setCupoMaximo(rs.getInt("cupo_maximo"));
                evento.setCostoInscripcion(rs.getBigDecimal("costo_inscripcion"));
                eventos.add(evento);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return eventos;
    }
    
    @Override
    public int obtenerCupoDisponible(String codigoEvento) {
        String sql = "SELECT e.cupo_maximo - COUNT(i.email) as cupo_disponible " +
                    "FROM evento e LEFT JOIN inscripcion i ON e.codigo_evento = i.codigo_evento " +
                    "AND i.validado = true WHERE e.codigo_evento = ? GROUP BY e.codigo_evento";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, codigoEvento);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("cupo_disponible");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
