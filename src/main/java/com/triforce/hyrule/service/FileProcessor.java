/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.triforce.hyrule.service;
import com.triforce.hyrule.dao.*;
import com.triforce.hyrule.dao.impl.*;
import com.triforce.hyrule.models.*;
import com.triforce.hyrule.enums.*;
import com.triforce.hyrule.util.ReportGenerator;
import javax.swing.JTextArea;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author ludwi
 */

public class FileProcessor {
    private final EventoDAO eventoDAO;
    private final ParticipanteDAO participanteDAO;
    private final InscripcionDAO inscripcionDAO;
    private final ActividadDAO actividadDAO;
    private final AsistenciaDAO asistenciaDAO;
    private final PagoDAO pagoDAO;
    private final CertificadoDAO certificadoDAO;
    private final ReportGenerator reportGenerator;
    private JTextArea logArea;
    private String outputPath;
    
    public FileProcessor(JTextArea logArea, String outputPath) {
        this.eventoDAO = new EventoDAOImpl();
        this.participanteDAO = new ParticipanteDAOImpl();
        this.inscripcionDAO = new InscripcionDAOImpl();
        this.actividadDAO = new ActividadDAOImpl();
        this.asistenciaDAO = new AsistenciaDAOImpl();
        this.pagoDAO = new PagoDAOImpl();
        this.certificadoDAO = new CertificadoDAOImpl();
        this.reportGenerator = new ReportGenerator(outputPath);
        this.logArea = logArea;
        this.outputPath = outputPath;
    }
    
    public void processFile(String filePath, int delay) {
        new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                int lineNumber = 0;
                
                while ((line = reader.readLine()) != null) {
                    lineNumber++;
                    line = line.trim();
                    
                    if (line.isEmpty() || line.startsWith("//")) {
                        continue;
                    }
                    
                    log("Procesando línea " + lineNumber + ": " + line);
                    
                    try {
                        processInstruction(line);
                        log("✓ Instrucción procesada correctamente");
                    } catch (Exception e) {
                        log("✗ Error: " + e.getMessage());
                    }
                    if (delay > 0) {
                        Thread.sleep(delay);
                    }
                }
                
                log("Procesamiento del archivo completado.");
            } catch (IOException | InterruptedException e) {
                log("Error al procesar el archivo: " + e.getMessage());
            }
        }).start();
    }
    
    private void processInstruction(String instruction) {
        if (instruction.startsWith("REGISTRO_EVENTO")) {
            processRegistroEvento(instruction);
        } else if (instruction.startsWith("REGISTRO_PARTICIPANTE")) {
            processRegistroParticipante(instruction);
        } else if (instruction.startsWith("INSCRIPCION")) {
            processInscripcion(instruction);
        } else if (instruction.startsWith("PAGO")) {
            processPago(instruction);
        } else if (instruction.startsWith("VALIDAR_INSCRIPCION")) {
            processValidarInscripcion(instruction);
        } else if (instruction.startsWith("REGISTRO_ACTIVIDAD")) {
            processRegistroActividad(instruction);
        } else if (instruction.startsWith("ASISTENCIA")) {
            processAsistencia(instruction);
        } else if (instruction.startsWith("CERTIFICADO")) {
            processCertificado(instruction);
        } else if (instruction.startsWith("REPORTE_PARTICIPANTES")) {
            processReporteParticipantes(instruction);
        } else if (instruction.startsWith("REPORTE_ACTIVIDADES")) {
            processReporteActividades(instruction);
        } else if (instruction.startsWith("REPORTE_EVENTOS")) {
            processReporteEventos(instruction);
        } else {
            throw new RuntimeException("Instrucción no reconocida: " + instruction);
        }
    }
    
    private String[] parseParameters(String instruction) {
        Pattern pattern = Pattern.compile("\"([^\"]*)\"|([^,()]+)");
        Matcher matcher = pattern.matcher(instruction);
        
        java.util.List<String> params = new java.util.ArrayList<>();
        while (matcher.find()) {
            String param = matcher.group(1) != null ? matcher.group(1) : matcher.group(2);
            if (param != null && !param.trim().isEmpty()) {
                params.add(param.trim());
            }
        }
        if (!params.isEmpty()) {
            String firstParam = params.get(0);
            int parenIndex = firstParam.indexOf('(');
            if (parenIndex != -1) {
                params.set(0, firstParam.substring(parenIndex + 1));
            }
        }
        
        return params.toArray(new String[0]);
    }
    
    private void processRegistroEvento(String instruction) {
        String[] params = parseParameters(instruction);
        if (params.length != 7) {
            throw new RuntimeException("Parámetros incorrectos para REGISTRO_EVENTO");
        }
        
        Evento evento = new Evento();
        evento.setCodigoEvento(params[0]);
        evento.setFecha(LocalDate.parse(params[1], DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        evento.setTipo(TipoEvento.valueOf(params[2]));
        evento.setTitulo(params[3]);
        evento.setUbicacion(params[4]);
        evento.setCupoMaximo(Integer.parseInt(params[5]));
        evento.setCostoInscripcion(new BigDecimal(params[6]));
        
        if (!eventoDAO.insertar(evento)) {
            throw new RuntimeException("No se pudo registrar el evento");
        }
    }
    
    private void processRegistroParticipante(String instruction) {
        String[] params = parseParameters(instruction);
        if (params.length != 4) {
            throw new RuntimeException("Parámetros incorrectos para REGISTRO_PARTICIPANTE");
        }
        
        Participante participante = new Participante();
        participante.setNombreCompleto(params[0]);
        participante.setTipo(TipoParticipante.valueOf(params[1]));
        participante.setInstitucion(params[2]);
        participante.setEmail(params[3]);
        
        if (!participanteDAO.insertar(participante)) {
            throw new RuntimeException("No se pudo registrar el participante");
        }
    }
    
    private void processInscripcion(String instruction) {
        String[] params = parseParameters(instruction);
        if (params.length != 3) {
            throw new RuntimeException("Parámetros incorrectos para INSCRIPCION");
        }
        if (participanteDAO.obtenerPorEmail(params[0]) == null) {
            throw new RuntimeException("El participante no existe");
        }
        if (eventoDAO.obtenerPorCodigo(params[1]) == null) {
            throw new RuntimeException("El evento no existe");
        }
        if (inscripcionDAO.existeInscripcion(params[0], params[1])) {
            throw new RuntimeException("Ya existe una inscripción para este participante en el evento");
        }
        
        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setEmail(params[0]);
        inscripcion.setCodigoEvento(params[1]);
        inscripcion.setTipoInscripcion(TipoInscripcion.valueOf(params[2]));
        
        if (!inscripcionDAO.insertar(inscripcion)) {
            throw new RuntimeException("No se pudo realizar la inscripción");
        }
    }
    
    private void processPago(String instruction) {
        String[] params = parseParameters(instruction);
        if (params.length != 4) {
            throw new RuntimeException("Parámetros incorrectos para PAGO");
        }
        if (!inscripcionDAO.existeInscripcion(params[0], params[1])) {
            throw new RuntimeException("No existe inscripción para este participante en el evento");
        }
        
        MetodoPago metodoPago = MetodoPago.valueOf(params[2]);
        BigDecimal monto = new BigDecimal(params[3]);
        
        if (!pagoDAO.registrarPago(params[0], params[1], metodoPago, monto)) {
            throw new RuntimeException("No se pudo registrar el pago");
        }
    }
    
    private void processValidarInscripcion(String instruction) {
        String[] params = parseParameters(instruction);
        if (params.length != 2) {
            throw new RuntimeException("Parámetros incorrectos para VALIDAR_INSCRIPCION");
        }
        if (!pagoDAO.existePago(params[0], params[1])) {
            throw new RuntimeException("No se puede validar la inscripción sin pago registrado");
        }
        if (eventoDAO.obtenerCupoDisponible(params[1]) <= 0) {
            throw new RuntimeException("No hay cupo disponible en el evento");
        }
        
        if (!inscripcionDAO.validarInscripcion(params[0], params[1])) {
            throw new RuntimeException("No se pudo validar la inscripción");
        }
    }
    
    private void processRegistroActividad(String instruction) {
        String[] params = parseParameters(instruction);
        if (params.length != 8) {
            throw new RuntimeException("Parámetros incorrectos para REGISTRO_ACTIVIDAD");
        }
        if (eventoDAO.obtenerPorCodigo(params[1]) == null) {
            throw new RuntimeException("El evento no existe");
        }
        Inscripcion inscripcionEncargado = inscripcionDAO.obtener(params[4], params[1]);
        if (inscripcionEncargado == null || !inscripcionEncargado.isValidado() || 
            inscripcionEncargado.getTipoInscripcion() == TipoInscripcion.ASISTENTE) {
            throw new RuntimeException("El encargado debe tener inscripción válida y no puede ser ASISTENTE");
        }
        
        Actividad actividad = new Actividad();
        actividad.setCodigoActividad(params[0]);
        actividad.setCodigoEvento(params[1]);
        actividad.setTipo(TipoActividad.valueOf(params[2]));
        actividad.setTitulo(params[3]);
        actividad.setEmailEncargado(params[4]);
        actividad.setHoraInicio(LocalTime.parse(params[5]));
        actividad.setHoraFin(LocalTime.parse(params[6]));
        actividad.setCupoMaximo(Integer.parseInt(params[7]));
        
        if (!actividadDAO.insertar(actividad)) {
            throw new RuntimeException("No se pudo registrar la actividad");
        }
    }
    
    private void processAsistencia(String instruction) {
        String[] params = parseParameters(instruction);
        if (params.length != 2) {
            throw new RuntimeException("Parámetros incorrectos para ASISTENCIA");
        }
        Actividad actividad = actividadDAO.obtenerPorCodigo(params[1]);
        if (actividad == null) {
            throw new RuntimeException("La actividad no existe");
        }
        Inscripcion inscripcion = inscripcionDAO.obtener(params[0], actividad.getCodigoEvento());
        if (inscripcion == null || !inscripcion.isValidado()) {
            throw new RuntimeException("El participante debe tener inscripción validada en el evento");
        }
        if (asistenciaDAO.existeAsistencia(params[0], params[1])) {
            log("Advertencia: Ya existe asistencia registrada para este participante en la actividad");
            return;
        }
        if (actividadDAO.obtenerCantidadAsistentes(params[1]) >= actividad.getCupoMaximo()) {
            throw new RuntimeException("No hay cupo disponible en la actividad");
        }
        
        if (!asistenciaDAO.registrarAsistencia(params[0], params[1])) {
            throw new RuntimeException("No se pudo registrar la asistencia");
        }
    }
    
    private void processCertificado(String instruction) {
        String[] params = parseParameters(instruction);
        if (params.length != 2) {
            throw new RuntimeException("Parámetros incorrectos para CERTIFICADO");
        }
        java.util.List<String> actividades = asistenciaDAO.obtenerActividadesParticipante(params[0], params[1]);
        if (actividades.isEmpty()) {
            throw new RuntimeException("El participante debe haber asistido al menos a una actividad del evento");
        }
        if (certificadoDAO.existeCertificado(params[0], params[1])) {
            throw new RuntimeException("Ya existe un certificado para este participante en el evento");
        }
        Participante participante = participanteDAO.obtenerPorEmail(params[0]);
        Evento evento = eventoDAO.obtenerPorCodigo(params[1]);
        
        if (participante == null || evento == null) {
            throw new RuntimeException("No se encontraron los datos del participante o evento");
        }
        
        String rutaCertificado = reportGenerator.generarCertificado(participante, evento);
        
        if (!certificadoDAO.registrarCertificado(params[0], params[1], rutaCertificado)) {
            throw new RuntimeException("No se pudo registrar el certificado en la base de datos");
        }
    }
    
    private void processReporteParticipantes(String instruction) {
        String[] params = parseParameters(instruction);
        if (params.length != 3) {
            throw new RuntimeException("Parámetros incorrectos para REPORTE_PARTICIPANTES");
        }
        
        String codigoEvento = params[0];
        String tipoParticipante = params[1].isEmpty() ? null : params[1];
        String institucion = params[2].isEmpty() ? null : params[2];
        
        TipoParticipante tipo = null;
        if (tipoParticipante != null) {
            tipo = TipoParticipante.valueOf(tipoParticipante);
        }
        
        reportGenerator.generarReporteParticipantes(codigoEvento, tipo, institucion);
    }
    
    private void processReporteActividades(String instruction) {
        String[] params = parseParameters(instruction);
        if (params.length != 3) {
            throw new RuntimeException("Parámetros incorrectos para REPORTE_ACTIVIDADES");
        }
        
        String codigoEvento = params[0];
        String tipoActividad = params[1].isEmpty() ? null : params[1];
        String emailEncargado = params[2].isEmpty() ? null : params[2];
        
        TipoActividad tipo = null;
        if (tipoActividad != null) {
            tipo = TipoActividad.valueOf(tipoActividad);
        }
        
        reportGenerator.generarReporteActividades(codigoEvento, tipo, emailEncargado);
    }
    
    private void processReporteEventos(String instruction) {
        String[] params = parseParameters(instruction);
        if (params.length != 5) {
            throw new RuntimeException("Parámetros incorrectos para REPORTE_EVENTOS");
        }
        
        String tipoEvento = params[0].isEmpty() ? null : params[0];
        String fechaInicio = params[1].isEmpty() ? null : params[1];
        String fechaFin = params[2].isEmpty() ? null : params[2];
        String cupoMin = params[3].isEmpty() ? null : params[3];
        String cupoMax = params[4].isEmpty() ? null : params[4];
        
        TipoEvento tipo = null;
        LocalDate fechaIni = null;
        LocalDate fechaFinal = null;
        Integer cupoMinimo = null;
        Integer cupoMaximo = null;
        
        if (tipoEvento != null) {
            tipo = TipoEvento.valueOf(tipoEvento);
        }
        
        if (fechaInicio != null && fechaFin != null) {
            fechaIni = LocalDate.parse(fechaInicio, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            fechaFinal = LocalDate.parse(fechaFin, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }
        
        if (cupoMin != null && cupoMax != null) {
            cupoMinimo = Integer.parseInt(cupoMin);
            cupoMaximo = Integer.parseInt(cupoMax);
        }
        
        reportGenerator.generarReporteEventos(tipo, fechaIni, fechaFinal, cupoMinimo, cupoMaximo);
    }
    
    private void log(String message) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            logArea.append(java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + 
                          " - " + message + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }
}
