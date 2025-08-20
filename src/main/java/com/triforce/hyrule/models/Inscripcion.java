/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.triforce.hyrule.models;
import com.triforce.hyrule.enums.TipoInscripcion;

/**
 *
 * @author ludwi
 */

public class Inscripcion {
    private String email;
    private String codigoEvento;
    private TipoInscripcion tipoInscripcion;
    private boolean validado;
    
    public Inscripcion() {}
    
    public Inscripcion(String email, String codigoEvento, TipoInscripcion tipoInscripcion) {
        this.email = email;
        this.codigoEvento = codigoEvento;
        this.tipoInscripcion = tipoInscripcion;
        this.validado = false;
    }
    
    // Getters y Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getCodigoEvento() { return codigoEvento; }
    public void setCodigoEvento(String codigoEvento) { this.codigoEvento = codigoEvento; }
    
    public TipoInscripcion getTipoInscripcion() { return tipoInscripcion; }
    public void setTipoInscripcion(TipoInscripcion tipoInscripcion) { this.tipoInscripcion = tipoInscripcion; }
    
    public boolean isValidado() { return validado; }
    public void setValidado(boolean validado) { this.validado = validado; }
}
