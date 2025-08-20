/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.triforce.hyrule.dao;
import com.triforce.hyrule.models.Participante;
import com.triforce.hyrule.enums.TipoParticipante;
import java.util.List;

/**
 *
 * @author ludwi
 */

public interface ParticipanteDAO {
    boolean insertar(Participante participante);
    Participante obtenerPorEmail(String email);
    List<Participante> obtenerTodos();
    List<Participante> obtenerPorEvento(String codigoEvento, TipoParticipante tipo, String institucion);
}