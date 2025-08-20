/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.triforce.hyrule.dao;
import com.triforce.hyrule.enums.MetodoPago;
import java.math.BigDecimal;

/**
 *
 * @author ludwi
 */

public interface PagoDAO {
    boolean registrarPago(String email, String codigoEvento, MetodoPago metodoPago, BigDecimal monto);
    boolean existePago(String email, String codigoEvento);
    BigDecimal obtenerMontoTotalEvento(String codigoEvento);
}