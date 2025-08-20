/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.triforce.hyrule.dao;
import java.util.List;

/**
 *
 * @author ludwi
 */

public interface AsistenciaDAO {
    boolean registrarAsistencia(String email, String codigoActividad);
    boolean existeAsistencia(String email, String codigoActividad);
    List<String> obtenerActividadesParticipante(String email, String codigoEvento);
}
