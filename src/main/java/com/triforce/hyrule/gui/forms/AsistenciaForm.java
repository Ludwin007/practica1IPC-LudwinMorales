/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.triforce.hyrule.gui.forms;
import com.triforce.hyrule.dao.*;
import com.triforce.hyrule.dao.impl.*;
import com.triforce.hyrule.models.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 *
 * @author ludwi
 */

public class AsistenciaForm extends JPanel {
    private JComboBox<String> participanteCombo;
    private JComboBox<String> actividadCombo;
    
    private ParticipanteDAO participanteDAO;
    private ActividadDAO actividadDAO;
    private AsistenciaDAO asistenciaDAO;
    private InscripcionDAO inscripcionDAO;
    
    public AsistenciaForm() {
        this.participanteDAO = new ParticipanteDAOImpl();
        this.actividadDAO = new ActividadDAOImpl();
        this.asistenciaDAO = new AsistenciaDAOImpl();
        this.inscripcionDAO = new InscripcionDAOImpl();
        initializeComponents();
        setupLayout();
        cargarDatos();
    }
    
    private void initializeComponents() {
        participanteCombo = new JComboBox<>();
        actividadCombo = new JComboBox<>();
    }
    
    private void setupLayout() {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        JLabel titleLabel = new JLabel("Gestión de Asistencia");
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        titleLabel.setForeground(new Color(52, 73, 94));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(titleLabel, gbc);
        
        gbc.gridwidth = 1;
        
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Participante:"), gbc);
        gbc.gridx = 1;
        add(participanteCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Actividad:"), gbc);
        gbc.gridx = 1;
        add(actividadCombo, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton registrarBtn = new JButton("Registrar Asistencia");
        JButton actualizarBtn = new JButton("Actualizar Datos");
        
        registrarBtn.setBackground(new Color(46, 204, 113));
        registrarBtn.setForeground(Color.WHITE);
        actualizarBtn.setBackground(new Color(52, 152, 219));
        actualizarBtn.setForeground(Color.WHITE);
        
        registrarBtn.addActionListener(new RegistrarAsistenciaListener());
        actualizarBtn.addActionListener(e -> cargarDatos());
        
        buttonPanel.add(registrarBtn);
        buttonPanel.add(actualizarBtn);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(buttonPanel, gbc);
    }
    
    private void cargarDatos() {
        List<Participante> participantes = participanteDAO.obtenerTodos();
        participanteCombo.removeAllItems();
        for (Participante p : participantes) {
            participanteCombo.addItem(p.getEmail() + " - " + p.getNombreCompleto());
        }
        cargarActividades();
    }
    
    private void cargarActividades() {
        actividadCombo.removeAllItems();
        List<Evento> eventos = new EventoDAOImpl().obtenerTodos();
        for (Evento evento : eventos) {
            List<Actividad> actividades = actividadDAO.obtenerPorEvento(evento.getCodigoEvento(), null, null);
            for (Actividad actividad : actividades) {
                actividadCombo.addItem(actividad.getCodigoActividad() + " - " + actividad.getTitulo());
            }
        }
    }
    
    private String getEmailFromCombo() {
        String selected = (String) participanteCombo.getSelectedItem();
        return selected != null ? selected.split(" - ")[0] : "";
    }
    
    private String getCodigoActividadFromCombo() {
        String selected = (String) actividadCombo.getSelectedItem();
        return selected != null ? selected.split(" - ")[0] : "";
    }
    
    private class RegistrarAsistenciaListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String email = getEmailFromCombo();
                String codigoActividad = getCodigoActividadFromCombo();
                
                if (email.isEmpty() || codigoActividad.isEmpty()) {
                    throw new Exception("Debe seleccionar participante y actividad");
                }
                Actividad actividad = actividadDAO.obtenerPorCodigo(codigoActividad);
                if (actividad == null) {
                    throw new Exception("La actividad seleccionada no existe");
                }
                Inscripcion inscripcion = inscripcionDAO.obtener(email, actividad.getCodigoEvento());
                if (inscripcion == null || !inscripcion.isValidado()) {
                    throw new Exception("El participante debe tener inscripción validada en el evento");
                }
                if (asistenciaDAO.existeAsistencia(email, codigoActividad)) {
                    throw new Exception("Ya existe asistencia registrada para este participante en la actividad");
                }
                if (actividadDAO.obtenerCantidadAsistentes(codigoActividad) >= actividad.getCupoMaximo()) {
                    throw new Exception("No hay cupo disponible en la actividad");
                }
                if (asistenciaDAO.registrarAsistencia(email, codigoActividad)) {
                    JOptionPane.showMessageDialog(AsistenciaForm.this, 
                        "Asistencia registrada exitosamente", 
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    throw new Exception("No se pudo registrar la asistencia");
                }
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(AsistenciaForm.this, 
                    "Error: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}