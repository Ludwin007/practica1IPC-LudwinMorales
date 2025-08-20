/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.triforce.hyrule.models;
import com.triforce.hyrule.enums.TipoParticipante;

/**
 *
 * @author ludwi
 */

public class Participante {
    private String email;
    private String nombreCompleto;
    private TipoParticipante tipo;
    private String institucion;
    
    public Participante() {}
    
    public Participante(String email, String nombreCompleto, TipoParticipante tipo, String institucion) {
        this.email = email;
        this.nombreCompleto = nombreCompleto;
        this.tipo = tipo;
        this.institucion = institucion;
    }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
    
    public TipoParticipante getTipo() { return tipo; }
    public void setTipo(TipoParticipante tipo) { this.tipo = tipo; }
    
    public String getInstitucion() { return institucion; }
    public void setInstitucion(String institucion) { this.institucion = institucion; }
}
