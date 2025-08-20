/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.triforce.hyrule.dao;
import com.triforce.hyrule.models.Actividad;
import com.triforce.hyrule.enums.TipoActividad;
import java.util.List;

/**
 *
 * @author ludwi
 */

public interface ActividadDAO {
    boolean insertar(Actividad actividad);
    Actividad obtenerPorCodigo(String codigoActividad);
    List<Actividad> obtenerPorEvento(String codigoEvento, TipoActividad tipo, String emailEncargado);
    int obtenerCantidadAsistentes(String codigoActividad);
}
