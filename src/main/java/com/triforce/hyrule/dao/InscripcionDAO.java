/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.triforce.hyrule.dao;
import com.triforce.hyrule.models.Inscripcion;
import java.util.List;

/**
 *
 * @author ludwi
 */

public interface InscripcionDAO {
    boolean insertar(Inscripcion inscripcion);
    Inscripcion obtener(String email, String codigoEvento);
    boolean validarInscripcion(String email, String codigoEvento);
    List<Inscripcion> obtenerPorEvento(String codigoEvento);
    boolean existeInscripcion(String email, String codigoEvento);
}