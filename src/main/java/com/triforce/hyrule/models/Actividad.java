/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.triforce.hyrule.models;
import com.triforce.hyrule.enums.TipoActividad;
import java.time.LocalTime;

/**
 *
 * @author ludwi
 */

public class Actividad {
    private String codigoActividad;
    private String codigoEvento;
    private TipoActividad tipo;
    private String titulo;
    private String emailEncargado;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private int cupoMaximo;
    
    public Actividad() {}
    
    public Actividad(String codigoActividad, String codigoEvento, TipoActividad tipo, 
                     String titulo, String emailEncargado, LocalTime horaInicio, 
                     LocalTime horaFin, int cupoMaximo) {
        this.codigoActividad = codigoActividad;
        this.codigoEvento = codigoEvento;
        this.tipo = tipo;
        this.titulo = titulo;
        this.emailEncargado = emailEncargado;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.cupoMaximo = cupoMaximo;
    }
    
    public String getCodigoActividad() { return codigoActividad; }
    public void setCodigoActividad(String codigoActividad) { this.codigoActividad = codigoActividad; }
    
    public String getCodigoEvento() { return codigoEvento; }
    public void setCodigoEvento(String codigoEvento) { this.codigoEvento = codigoEvento; }
    
    public TipoActividad getTipo() { return tipo; }
    public void setTipo(TipoActividad tipo) { this.tipo = tipo; }
    
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    
    public String getEmailEncargado() { return emailEncargado; }
    public void setEmailEncargado(String emailEncargado) { this.emailEncargado = emailEncargado; }
    
    public LocalTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }
    
    public LocalTime getHoraFin() { return horaFin; }
    public void setHoraFin(LocalTime horaFin) { this.horaFin = horaFin; }
    
    public int getCupoMaximo() { return cupoMaximo; }
    public void setCupoMaximo(int cupoMaximo) { this.cupoMaximo = cupoMaximo; }
}