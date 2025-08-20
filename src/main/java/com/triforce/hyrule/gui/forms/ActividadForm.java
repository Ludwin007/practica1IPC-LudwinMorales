/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.triforce.hyrule.gui.forms;
import com.triforce.hyrule.dao.*;
import com.triforce.hyrule.dao.impl.*;
import com.triforce.hyrule.models.*;
import com.triforce.hyrule.enums.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 *
 * @author ludwi
 */

public class ActividadForm extends JPanel {
    private JTextField codigoField;
    private JComboBox<String> eventoCombo;
    private JComboBox<TipoActividad> tipoCombo;
    private JTextField tituloField;
    private JComboBox<String> encargadoCombo;
    private JTextField horaInicioField;
    private JTextField horaFinField;
    private JSpinner cupoSpinner;   
    private EventoDAO eventoDAO;
    private ParticipanteDAO participanteDAO;
    private ActividadDAO actividadDAO;
    private InscripcionDAO inscripcionDAO;
    
    public ActividadForm() {
        this.eventoDAO = new EventoDAOImpl();
        this.participanteDAO = new ParticipanteDAOImpl();
        this.actividadDAO = new ActividadDAOImpl();
        this.inscripcionDAO = new InscripcionDAOImpl();
        initializeComponents();
        setupLayout();
        cargarDatos();
    }
    
    private void initializeComponents() {
        codigoField = new JTextField(20);
        eventoCombo = new JComboBox<>();
        tipoCombo = new JComboBox<>(TipoActividad.values());
        tituloField = new JTextField(30);
        encargadoCombo = new JComboBox<>();
        
        horaInicioField = new JTextField(10);
        horaInicioField.setToolTipText("Formato: HH:mm (ej: 10:00)");
        
        horaFinField = new JTextField(10);
        horaFinField.setToolTipText("Formato: HH:mm (ej: 12:00)");
        
        cupoSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));
    }
    
    private void setupLayout() {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        JLabel titleLabel = new JLabel("Registro de Actividad");
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        titleLabel.setForeground(new Color(52, 73, 94));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(titleLabel, gbc);
        
        gbc.gridwidth = 1;
        
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Código de actividad:"), gbc);
        gbc.gridx = 1;
        add(codigoField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Evento:"), gbc);
        gbc.gridx = 1;
        add(eventoCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Tipo de actividad:"), gbc);
        gbc.gridx = 1;
        add(tipoCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        add(new JLabel("Título:"), gbc);
        gbc.gridx = 1;
        add(tituloField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5;
        add(new JLabel("Encargado:"), gbc);
        gbc.gridx = 1;
        add(encargadoCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 6;
        add(new JLabel("Hora inicio (HH:mm):"), gbc);
        gbc.gridx = 1;
        add(horaInicioField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 7;
        add(new JLabel("Hora fin (HH:mm):"), gbc);
        gbc.gridx = 1;
        add(horaFinField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 8;
        add(new JLabel("Cupo máximo:"), gbc);
        gbc.gridx = 1;
        add(cupoSpinner, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton guardarBtn = new JButton("Guardar");
        JButton limpiarBtn = new JButton("Limpiar");
        JButton actualizarBtn = new JButton("Actualizar Datos");
        
        guardarBtn.setBackground(new Color(46, 204, 113));
        guardarBtn.setForeground(Color.WHITE);
        limpiarBtn.setBackground(new Color(52, 152, 219));
        limpiarBtn.setForeground(Color.WHITE);
        actualizarBtn.setBackground(new Color(155, 89, 182));
        actualizarBtn.setForeground(Color.WHITE);
        
        guardarBtn.addActionListener(new GuardarActividadListener());
        limpiarBtn.addActionListener(e -> limpiarFormulario());
        actualizarBtn.addActionListener(e -> cargarDatos());
        
        buttonPanel.add(guardarBtn);
        buttonPanel.add(limpiarBtn);
        buttonPanel.add(actualizarBtn);
        
        gbc.gridx = 0; gbc.gridy = 9; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(buttonPanel, gbc);
    }
    
    private void cargarDatos() {
        List<Evento> eventos = eventoDAO.obtenerTodos();
        eventoCombo.removeAllItems();
        for (Evento e : eventos) {
            eventoCombo.addItem(e.getCodigoEvento() + " - " + e.getTitulo());
        }
        eventoCombo.addActionListener(e -> cargarEncargados());
        cargarEncargados();
    }
    
    private void cargarEncargados() {
        encargadoCombo.removeAllItems();
        
        String eventoSeleccionado = (String) eventoCombo.getSelectedItem();
        if (eventoSeleccionado != null) {
            String codigoEvento = eventoSeleccionado.split(" - ")[0];

            List<Inscripcion> inscripciones = inscripcionDAO.obtenerPorEvento(codigoEvento);
            
            for (Inscripcion inscripcion : inscripciones) {
                if (inscripcion.getTipoInscripcion() != TipoInscripcion.ASISTENTE && inscripcion.isValidado()) {
                    Participante participante = participanteDAO.obtenerPorEmail(inscripcion.getEmail());
                    if (participante != null) {
                        encargadoCombo.addItem(participante.getEmail() + " - " + participante.getNombreCompleto());
                    }
                }
            }
        }
    }
    
    private void limpiarFormulario() {
        codigoField.setText("");
        if (eventoCombo.getItemCount() > 0) eventoCombo.setSelectedIndex(0);
        tipoCombo.setSelectedIndex(0);
        tituloField.setText("");
        if (encargadoCombo.getItemCount() > 0) encargadoCombo.setSelectedIndex(0);
        horaInicioField.setText("");
        horaFinField.setText("");
        cupoSpinner.setValue(1);
    }
    
    private String getCodigoEventoFromCombo() {
        String selected = (String) eventoCombo.getSelectedItem();
        return selected != null ? selected.split(" - ")[0] : "";
    }
    
    private String getEmailEncargadoFromCombo() {
        String selected = (String) encargadoCombo.getSelectedItem();
        return selected != null ? selected.split(" - ")[0] : "";
    }
    
    private class GuardarActividadListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                if (codigoField.getText().trim().isEmpty()) {
                    throw new Exception("El código de actividad es obligatorio");
                }
                
                if (tituloField.getText().trim().isEmpty()) {
                    throw new Exception("El título es obligatorio");
                }
                
                if (horaInicioField.getText().trim().isEmpty()) {
                    throw new Exception("La hora de inicio es obligatoria");
                }
                
                if (horaFinField.getText().trim().isEmpty()) {
                    throw new Exception("La hora de fin es obligatoria");
                }
                
                if (encargadoCombo.getSelectedItem() == null) {
                    throw new Exception("Debe seleccionar un encargado válido");
                }
                if (tituloField.getText().length() > 200) {
                    throw new Exception("El título no puede exceder 200 caracteres");
                }
                LocalTime horaInicio, horaFin;
                try {
                    horaInicio = LocalTime.parse(horaInicioField.getText().trim());
                    horaFin = LocalTime.parse(horaFinField.getText().trim());
                } catch (DateTimeParseException ex) {
                    throw new Exception("Formato de hora inválido. Use HH:mm (ej: 10:00)");
                }
                
                if (horaFin.isBefore(horaInicio) || horaFin.equals(horaInicio)) {
                    throw new Exception("La hora de fin debe ser posterior a la hora de inicio");
                }
                
                Actividad actividad = new Actividad();
                actividad.setCodigoActividad(codigoField.getText().trim());
                actividad.setCodigoEvento(getCodigoEventoFromCombo());
                actividad.setTipo((TipoActividad) tipoCombo.getSelectedItem());
                actividad.setTitulo(tituloField.getText().trim());
                actividad.setEmailEncargado(getEmailEncargadoFromCombo());
                actividad.setHoraInicio(horaInicio);
                actividad.setHoraFin(horaFin);
                actividad.setCupoMaximo((Integer) cupoSpinner.getValue());
                
                if (actividadDAO.insertar(actividad)) {
                    JOptionPane.showMessageDialog(ActividadForm.this, 
                        "Actividad registrada exitosamente", 
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    limpiarFormulario();
                } else {
                    throw new Exception("No se pudo guardar la actividad");
                }
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(ActividadForm.this, 
                    "Error: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}