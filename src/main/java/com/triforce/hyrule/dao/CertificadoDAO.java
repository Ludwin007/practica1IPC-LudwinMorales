/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.triforce.hyrule.dao;

/**
 *
 * @author ludwi
 */

public interface CertificadoDAO {
    boolean registrarCertificado(String email, String codigoEvento, String rutaArchivo);
    boolean existeCertificado(String email, String codigoEvento);
}