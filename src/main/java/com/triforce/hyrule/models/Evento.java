/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.triforce.hyrule.models;
import com.triforce.hyrule.enums.TipoEvento;
import java.time.LocalDate;
import java.math.BigDecimal;

/**
 *
 * @author ludwi
 */


public class Evento {
    private String codigoEvento;
    private LocalDate fecha;
    private TipoEvento tipo;
    private String titulo;
    private String ubicacion;
    private int cupoMaximo;
    private BigDecimal costoInscripcion;
    
    public Evento() {}
    
    public Evento(String codigoEvento, LocalDate fecha, TipoEvento tipo, String titulo, 
                  String ubicacion, int cupoMaximo, BigDecimal costoInscripcion) {
        this.codigoEvento = codigoEvento;
        this.fecha = fecha;
        this.tipo = tipo;
        this.titulo = titulo;
        this.ubicacion = ubicacion;
        this.cupoMaximo = cupoMaximo;
        this.costoInscripcion = costoInscripcion;
    }
    
    public String getCodigoEvento() { return codigoEvento; }
    public void setCodigoEvento(String codigoEvento) { this.codigoEvento = codigoEvento; }
    
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    
    public TipoEvento getTipo() { return tipo; }
    public void setTipo(TipoEvento tipo) { this.tipo = tipo; }
    
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    
    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
    
    public int getCupoMaximo() { return cupoMaximo; }
    public void setCupoMaximo(int cupoMaximo) { this.cupoMaximo = cupoMaximo; }
    
    public BigDecimal getCostoInscripcion() { return costoInscripcion; }
    public void setCostoInscripcion(BigDecimal costoInscripcion) { this.costoInscripcion = costoInscripcion; }
}
