/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.triforce.hyrule.util;
import com.triforce.hyrule.dao.*;
import com.triforce.hyrule.dao.impl.*;
import com.triforce.hyrule.models.*;
import com.triforce.hyrule.enums.*;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 *
 * @author ludwi
 */

public class ReportGenerator {
    private final String outputPath;
    private final EventoDAO eventoDAO;
    private final ParticipanteDAO participanteDAO;
    private final ActividadDAO actividadDAO;
    private final PagoDAO pagoDAO;
    
    public ReportGenerator(String outputPath) {
        this.outputPath = outputPath;
        this.eventoDAO = new EventoDAOImpl();
        this.participanteDAO = new ParticipanteDAOImpl();
        this.actividadDAO = new ActividadDAOImpl();
        this.pagoDAO = new PagoDAOImpl();
    }
    
    public void generarReporteParticipantes(String codigoEvento, TipoParticipante tipo, String institucion) {
        try {
            Evento evento = eventoDAO.obtenerPorCodigo(codigoEvento);
            List<Participante> participantes = participanteDAO.obtenerPorEvento(codigoEvento, tipo, institucion);
            
            String fileName = "reporte_participantes_" + codigoEvento + "_" + 
                             System.currentTimeMillis() + ".html";
            String filePath = outputPath + "/" + fileName;
            
            StringBuilder html = new StringBuilder();
            html.append("<!DOCTYPE html>\n<html>\n<head>\n");
            html.append("<meta charset='UTF-8'>\n");
            html.append("<title>Reporte de Participantes - ").append(evento.getTitulo()).append("</title>\n");
            html.append("<style>\n");
            html.append("body { font-family: Arial, sans-serif; margin: 20px; }\n");
            html.append("h1 { color: #2c3e50; text-align: center; }\n");
            html.append("h2 { color: #34495e; border-bottom: 2px solid #3498db; padding-bottom: 10px; }\n");
            html.append("table { width: 100%; border-collapse: collapse; margin-top: 20px; }\n");
            html.append("th, td { border: 1px solid #bdc3c7; padding: 12px; text-align: left; }\n");
            html.append("th { background-color: #3498db; color: white; }\n");
            html.append("tr:nth-child(even) { background-color: #ecf0f1; }\n");
            html.append(".validado { color: #27ae60; font-weight: bold; }\n");
            html.append(".no-validado { color: #e74c3c; font-weight: bold; }\n");
            html.append("</style>\n</head>\n<body>\n");           
            html.append("<h1>Reino de Hyrule - Sistema de Eventos</h1>\n");
            html.append("<h2>Reporte de Participantes</h2>\n");
            html.append("<div style='background-color: #f8f9fa; padding: 15px; border-radius: 5px; margin-bottom: 20px;'>\n");
            html.append("<p><strong>Evento:</strong> ").append(evento.getTitulo()).append("</p>\n");
            html.append("<p><strong>Código:</strong> ").append(evento.getCodigoEvento()).append("</p>\n");
            html.append("<p><strong>Fecha:</strong> ").append(evento.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append("</p>\n");
            html.append("<p><strong>Generado:</strong> ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))).append("</p>\n");
            html.append("</div>\n");
            
            html.append("<table>\n");
            html.append("<tr><th>Correo Electrónico</th><th>Tipo</th><th>Nombre Completo</th><th>Institución</th><th>Estado</th></tr>\n");
            
            for (Participante participante : participantes) {
                html.append("<tr>\n");
                html.append("<td>").append(participante.getEmail()).append("</td>\n");
                html.append("<td>").append(participante.getTipo().name()).append("</td>\n");
                html.append("<td>").append(participante.getNombreCompleto()).append("</td>\n");
                html.append("<td>").append(participante.getInstitucion()).append("</td>\n");
               
                boolean validado = isParticipanteValidado(participante.getEmail(), codigoEvento);
                html.append("<td class='").append(validado ? "validado" : "no-validado").append("'>");
                html.append(validado ? "VALIDADO" : "NO VALIDADO").append("</td>\n");
                html.append("</tr>\n");
            }
            
            html.append("</table>\n");
            html.append("<p style='margin-top: 20px;'><strong>Total de participantes:</strong> ").append(participantes.size()).append("</p>\n");
            html.append("</body>\n</html>");
            
            try (FileWriter writer = new FileWriter(filePath)) {
                writer.write(html.toString());
            }
            
        } catch (IOException e) {
            throw new RuntimeException("Error al generar reporte de participantes: " + e.getMessage());
        }
    }
    
    public void generarReporteActividades(String codigoEvento, TipoActividad tipo, String emailEncargado) {
        try {
            Evento evento = eventoDAO.obtenerPorCodigo(codigoEvento);
            List<Actividad> actividades = actividadDAO.obtenerPorEvento(codigoEvento, tipo, emailEncargado);
            
            String fileName = "reporte_actividades_" + codigoEvento + "_" + 
                             System.currentTimeMillis() + ".html";
            String filePath = outputPath + "/" + fileName;
            
            StringBuilder html = new StringBuilder();
            html.append("<!DOCTYPE html>\n<html>\n<head>\n");
            html.append("<meta charset='UTF-8'>\n");
            html.append("<title>Reporte de Actividades - ").append(evento.getTitulo()).append("</title>\n");
            html.append("<style>\n");
            html.append("body { font-family: Arial, sans-serif; margin: 20px; }\n");
            html.append("h1 { color: #2c3e50; text-align: center; }\n");
            html.append("h2 { color: #34495e; border-bottom: 2px solid #3498db; padding-bottom: 10px; }\n");
            html.append("table { width: 100%; border-collapse: collapse; margin-top: 20px; }\n");
            html.append("th, td { border: 1px solid #bdc3c7; padding: 12px; text-align: left; }\n");
            html.append("th { background-color: #3498db; color: white; }\n");
            html.append("tr:nth-child(even) { background-color: #ecf0f1; }\n");
            html.append("</style>\n</head>\n<body>\n");
            
            html.append("<h1>Reino de Hyrule - Sistema de Eventos</h1>\n");
            html.append("<h2>Reporte de Actividades</h2>\n");
            
            html.append("<div style='background-color: #f8f9fa; padding: 15px; border-radius: 5px; margin-bottom: 20px;'>\n");
            html.append("<p><strong>Evento:</strong> ").append(evento.getTitulo()).append("</p>\n");
            html.append("<p><strong>Código:</strong> ").append(evento.getCodigoEvento()).append("</p>\n");
            html.append("<p><strong>Generado:</strong> ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))).append("</p>\n");
            html.append("</div>\n");
            
            html.append("<table>\n");
            html.append("<tr><th>Código Actividad</th><th>Código Evento</th><th>Título</th><th>Encargado</th><th>Hora Inicio</th><th>Cupo Máximo</th><th>Participantes</th></tr>\n");
            
            for (Actividad actividad : actividades) {
                Participante encargado = participanteDAO.obtenerPorEmail(actividad.getEmailEncargado());
                int cantidadParticipantes = actividadDAO.obtenerCantidadAsistentes(actividad.getCodigoActividad());
                
                html.append("<tr>\n");
                html.append("<td>").append(actividad.getCodigoActividad()).append("</td>\n");
                html.append("<td>").append(actividad.getCodigoEvento()).append("</td>\n");
                html.append("<td>").append(actividad.getTitulo()).append("</td>\n");
                html.append("<td>").append(encargado != null ? encargado.getNombreCompleto() : "N/A").append("</td>\n");
                html.append("<td>").append(actividad.getHoraInicio().toString()).append("</td>\n");
                html.append("<td>").append(actividad.getCupoMaximo()).append("</td>\n");
                html.append("<td>").append(cantidadParticipantes).append("</td>\n");
                html.append("</tr>\n");
            }
            
            html.append("</table>\n");
            html.append("<p style='margin-top: 20px;'><strong>Total de actividades:</strong> ").append(actividades.size()).append("</p>\n");
            html.append("</body>\n</html>");
            
            try (FileWriter writer = new FileWriter(filePath)) {
                writer.write(html.toString());
            }
            
        } catch (IOException e) {
            throw new RuntimeException("Error al generar reporte de actividades: " + e.getMessage());
        }
    }
    
    public void generarReporteEventos(TipoEvento tipo, LocalDate fechaInicio, LocalDate fechaFin, 
                                     Integer cupoMin, Integer cupoMax) {
        try {
            List<Evento> eventos = eventoDAO.obtenerPorFiltros(tipo, fechaInicio, fechaFin, cupoMin, cupoMax);
            
            String fileName = "reporte_eventos_" + System.currentTimeMillis() + ".html";
            String filePath = outputPath + "/" + fileName;
            
            StringBuilder html = new StringBuilder();
            html.append("<!DOCTYPE html>\n<html>\n<head>\n");
            html.append("<meta charset='UTF-8'>\n");
            html.append("<title>Reporte de Eventos</title>\n");
            html.append("<style>\n");
            html.append("body { font-family: Arial, sans-serif; margin: 20px; }\n");
            html.append("h1 { color: #2c3e50; text-align: center; }\n");
            html.append("h2 { color: #34495e; border-bottom: 2px solid #3498db; padding-bottom: 10px; }\n");
            html.append("table { width: 100%; border-collapse: collapse; margin-top: 20px; }\n");
            html.append("th, td { border: 1px solid #bdc3c7; padding: 12px; text-align: left; }\n");
            html.append("th { background-color: #3498db; color: white; }\n");
            html.append("tr:nth-child(even) { background-color: #ecf0f1; }\n");
            html.append(".evento-header { background-color: #2980b9 !important; color: white; }\n");
            html.append(".resumen { background-color: #f39c12; color: white; font-weight: bold; }\n");
            html.append("</style>\n</head>\n<body>\n");          
            html.append("<h1>Reino de Hyrule - Sistema de Eventos</h1>\n");
            html.append("<h2>Reporte de Eventos</h2>\n");         
            html.append("<div style='background-color: #f8f9fa; padding: 15px; border-radius: 5px; margin-bottom: 20px;'>\n");
            html.append("<p><strong>Generado:</strong> ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))).append("</p>\n");
            html.append("</div>\n");
            
            for (Evento evento : eventos) {
                html.append("<table>\n");
                html.append("<tr class='evento-header'><th colspan='10'>").append(evento.getTitulo()).append("</th></tr>\n");
                html.append("<tr><th>Código</th><th>Fecha</th><th>Título</th><th>Tipo</th><th>Ubicación</th><th>Cupo Máximo</th><th>Email Participante</th><th>Nombre</th><th>Tipo Participante</th><th>Método Pago</th><th>Monto</th></tr>\n");
                
                List<Participante> participantes = participanteDAO.obtenerPorEvento(evento.getCodigoEvento(), null, null);
                
                BigDecimal montoTotal = pagoDAO.obtenerMontoTotalEvento(evento.getCodigoEvento());
                int validados = 0;
                int noValidados = 0;
                
                if (participantes.isEmpty()) {
                    html.append("<tr><td>").append(evento.getCodigoEvento()).append("</td>");
                    html.append("<td>").append(evento.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append("</td>");
                    html.append("<td>").append(evento.getTitulo()).append("</td>");
                    html.append("<td>").append(evento.getTipo().name()).append("</td>");
                    html.append("<td>").append(evento.getUbicacion()).append("</td>");
                    html.append("<td>").append(evento.getCupoMaximo()).append("</td>");
                    html.append("<td colspan='5'>Sin participantes registrados</td></tr>\n");
                } else {
                    for (Participante participante : participantes) {
                        boolean esValidado = isParticipanteValidado(participante.getEmail(), evento.getCodigoEvento());
                        if (esValidado) validados++; else noValidados++;
                        
                        html.append("<tr><td>").append(evento.getCodigoEvento()).append("</td>");
                        html.append("<td>").append(evento.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append("</td>");
                        html.append("<td>").append(evento.getTitulo()).append("</td>");
                        html.append("<td>").append(evento.getTipo().name()).append("</td>");
                        html.append("<td>").append(evento.getUbicacion()).append("</td>");
                        html.append("<td>").append(evento.getCupoMaximo()).append("</td>");
                        html.append("<td>").append(participante.getEmail()).append("</td>");
                        html.append("<td>").append(participante.getNombreCompleto()).append("</td>");
                        html.append("<td>").append(participante.getTipo().name()).append("</td>");
                        html.append("<td>").append(getMetodoPago(participante.getEmail(), evento.getCodigoEvento())).append("</td>");
                        html.append("<td>").append(getMontoPago(participante.getEmail(), evento.getCodigoEvento())).append("</td></tr>\n");
                    }
                }
                
                html.append("<tr class='resumen'><td colspan='8'>TOTALES</td>");
                html.append("<td>MONTO TOTAL: Q.").append(montoTotal).append("</td>");
                html.append("<td>VALIDADOS: ").append(validados).append("</td>");
                html.append("<td>NO VALIDADOS: ").append(noValidados).append("</td></tr>\n");
                html.append("</table>\n<br>\n");
            }
            
            html.append("</body>\n</html>");
            
            try (FileWriter writer = new FileWriter(filePath)) {
                writer.write(html.toString());
            }
            
        } catch (IOException e) {
            throw new RuntimeException("Error al generar reporte de eventos: " + e.getMessage());
        }
    }
    
    public String generarCertificado(Participante participante, Evento evento) {
        try {
            String fileName = "certificado_" + participante.getEmail().replace("@", "_").replace(".", "_") + 
                             "_" + evento.getCodigoEvento() + ".html";
            String filePath = outputPath + "/" + fileName;
            
            StringBuilder html = new StringBuilder();
            html.append("<!DOCTYPE html>\n<html>\n<head>\n");
            html.append("<meta charset='UTF-8'>\n");
            html.append("<title>Certificado de Participación</title>\n");
            html.append("<style>\n");
            html.append("body { font-family: 'Times New Roman', serif; margin: 0; padding: 40px; background-color: #f5f5f5; }\n");
            html.append(".certificado { width: 800px; margin: 0 auto; background: white; padding: 60px; border: 10px solid #d4af37; box-shadow: 0 0 20px rgba(0,0,0,0.1); }\n");
            html.append(".header { text-align: center; margin-bottom: 40px; }\n");
            html.append(".titulo { font-size: 48px; color: #2c3e50; margin: 0; font-weight: bold; }\n");
            html.append(".subtitulo { font-size: 24px; color: #7f8c8d; margin: 10px 0 0 0; }\n");
            html.append(".contenido { text-align: center; margin: 40px 0; }\n");
            html.append(".texto-principal { font-size: 20px; line-height: 1.8; color: #2c3e50; }\n");
            html.append(".nombre { font-size: 36px; font-weight: bold; color: #2980b9; margin: 20px 0; text-decoration: underline; }\n");
            html.append(".evento { font-size: 24px; font-style: italic; color: #27ae60; margin: 20px 0; }\n");
            html.append(".fecha { font-size: 18px; color: #7f8c8d; margin-top: 40px; }\n");
            html.append(".firma { margin-top: 60px; text-align: center; }\n");
            html.append(".linea-firma { border-bottom: 2px solid #2c3e50; width: 300px; margin: 0 auto 10px auto; }\n");
            html.append(".texto-firma { font-size: 16px; color: #2c3e50; }\n");
            html.append("</style>\n</head>\n<body>\n");            
            html.append("<div class='certificado'>\n");
            html.append("<div class='header'>\n");
            html.append("<h1 class='titulo'>CERTIFICADO</h1>\n");
            html.append("<h2 class='subtitulo'>de Participación</h2>\n");
            html.append("</div>\n");           
            html.append("<div class='contenido'>\n");
            html.append("<p class='texto-principal'>El Reino de Hyrule certifica que</p>\n");
            html.append("<div class='nombre'>").append(participante.getNombreCompleto()).append("</div>\n");
            html.append("<p class='texto-principal'>participó exitosamente en el evento</p>\n");
            html.append("<div class='evento'>").append(evento.getTitulo()).append("</div>\n");
            html.append("<p class='texto-principal'>realizado el ").append(evento.getFecha().format(DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy"))).append("</p>\n");
            html.append("<p class='texto-principal'>en ").append(evento.getUbicacion()).append("</p>\n");
            html.append("</div>\n");            
            html.append("<div class='fecha'>\n");
            html.append("<p>Expedido el ").append(LocalDate.now().format(DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy"))).append("</p>\n");
            html.append("</div>\n");            
            html.append("<div class='firma'>\n");
            html.append("<div class='linea-firma'></div>\n");
            html.append("<p class='texto-firma'>Triforce Software<br>Sistema de Gestión de Eventos</p>\n");
            html.append("</div>\n");            
            html.append("</div>\n");
            html.append("</body>\n</html>");
            
            try (FileWriter writer = new FileWriter(filePath)) {
                writer.write(html.toString());
            }
            
            return filePath;
            
        } catch (IOException e) {
            throw new RuntimeException("Error al generar certificado: " + e.getMessage());
        }
    }
    
    private boolean isParticipanteValidado(String email, String codigoEvento) {
        InscripcionDAO inscripcionDAO = new InscripcionDAOImpl();
        Inscripcion inscripcion = inscripcionDAO.obtener(email, codigoEvento);
        return inscripcion != null && inscripcion.isValidado();
    }
    
    private String getMetodoPago(String email, String codigoEvento) {
     try {
            String sql = "SELECT metodo_pago FROM pago WHERE email = ? AND codigo_evento = ? LIMIT 1";
            return "EFECTIVO"; 
        } catch (Exception e) {
            return "N/A";
        }
    }
    
    private String getMontoPago(String email, String codigoEvento) {
        try {
            String sql = "SELECT monto FROM pago WHERE email = ? AND codigo_evento = ? LIMIT 1";
            return "0.00"; 
        } catch (Exception e) {
            return "0.00";
        }
    }
}
