/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.triforce.hyrule.dao;
import com.triforce.hyrule.models.Evento;
import com.triforce.hyrule.enums.TipoEvento;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author ludwi
 */

public interface EventoDAO {
    boolean insertar(Evento evento);
    Evento obtenerPorCodigo(String codigo);
    List<Evento> obtenerTodos();
    List<Evento> obtenerPorFiltros(TipoEvento tipo, LocalDate fechaInicio, LocalDate fechaFin, 
                                   Integer cupoMin, Integer cupoMax);
    int obtenerCupoDisponible(String codigoEvento);
}