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
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author ludwi
 */

public class InscripcionForm extends JPanel {
    private JComboBox<String> participanteCombo;
    private JComboBox<String> eventoCombo;
    private JComboBox<TipoInscripcion> tipoInscripcionCombo;
    private JComboBox<MetodoPago> metodoPagoCombo;
    private JTextField montoField;    
    private ParticipanteDAO participanteDAO;
    private EventoDAO eventoDAO;
    private InscripcionDAO inscripcionDAO;
    private PagoDAO pagoDAO;
    
    public InscripcionForm() {
        this.participanteDAO = new ParticipanteDAOImpl();
        this.eventoDAO = new EventoDAOImpl();
        this.inscripcionDAO = new InscripcionDAOImpl();
        this.pagoDAO = new PagoDAOImpl();
        initializeComponents();
        setupLayout();
        cargarDatos();
    }
    
    private void initializeComponents() {
        participanteCombo = new JComboBox<>();
        eventoCombo = new JComboBox<>();
        tipoInscripcionCombo = new JComboBox<>(TipoInscripcion.values());
        metodoPagoCombo = new JComboBox<>(MetodoPago.values());
        montoField = new JTextField(15);
    }
    
    private void setupLayout() {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        JLabel titleLabel = new JLabel("Gestión de Inscripciones");
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
        add(new JLabel("Evento:"), gbc);
        gbc.gridx = 1;
        add(eventoCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Tipo de inscripción:"), gbc);
        gbc.gridx = 1;
        add(tipoInscripcionCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        add(new JLabel("Método de pago:"), gbc);
        gbc.gridx = 1;
        add(metodoPagoCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5;
        add(new JLabel("Monto:"), gbc);
        gbc.gridx = 1;
        add(montoField, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton inscribirBtn = new JButton("Inscribir");
        JButton pagarBtn = new JButton("Registrar Pago");
        JButton validarBtn = new JButton("Validar Inscripción");
        JButton limpiarBtn = new JButton("Limpiar");
        
        inscribirBtn.setBackground(new Color(46, 204, 113));
        inscribirBtn.setForeground(Color.WHITE);
        pagarBtn.setBackground(new Color(241, 196, 15));
        pagarBtn.setForeground(Color.WHITE);
        validarBtn.setBackground(new Color(155, 89, 182));
        validarBtn.setForeground(Color.WHITE);
        limpiarBtn.setBackground(new Color(52, 152, 219));
        limpiarBtn.setForeground(Color.WHITE);
        
        inscribirBtn.addActionListener(new InscribirListener());
        pagarBtn.addActionListener(new PagarListener());
        validarBtn.addActionListener(new ValidarListener());
        limpiarBtn.addActionListener(e -> limpiarFormulario());
        
        buttonPanel.add(inscribirBtn);
        buttonPanel.add(pagarBtn);
        buttonPanel.add(validarBtn);
        buttonPanel.add(limpiarBtn);
        
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(buttonPanel, gbc);
    }
    
    private void cargarDatos() {
        List<Participante> participantes = participanteDAO.obtenerTodos();
        participanteCombo.removeAllItems();
        for (Participante p : participantes) {
            participanteCombo.addItem(p.getEmail() + " - " + p.getNombreCompleto());
        }
        List<Evento> eventos = eventoDAO.obtenerTodos();
        eventoCombo.removeAllItems();
        for (Evento e : eventos) {
            eventoCombo.addItem(e.getCodigoEvento() + " - " + e.getTitulo());
        }
    }
    
    private void limpiarFormulario() {
        if (participanteCombo.getItemCount() > 0) participanteCombo.setSelectedIndex(0);
        if (eventoCombo.getItemCount() > 0) eventoCombo.setSelectedIndex(0);
        tipoInscripcionCombo.setSelectedIndex(0);
        metodoPagoCombo.setSelectedIndex(0);
        montoField.setText("");
    }
    
    private String getEmailFromCombo(JComboBox<String> combo) {
        String selected = (String) combo.getSelectedItem();
        return selected != null ? selected.split(" - ")[0] : "";
    }
    
    private String getCodigoEventoFromCombo() {
        String selected = (String) eventoCombo.getSelectedItem();
        return selected != null ? selected.split(" - ")[0] : "";
    }
    
    private class InscribirListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String email = getEmailFromCombo(participanteCombo);
                String codigoEvento = getCodigoEventoFromCombo();
                
                if (email.isEmpty() || codigoEvento.isEmpty()) {
                    throw new Exception("Debe seleccionar participante y evento");
                }
                
                if (inscripcionDAO.existeInscripcion(email, codigoEvento)) {
                    throw new Exception("Ya existe una inscripción para este participante en el evento");
                }
                
                Inscripcion inscripcion = new Inscripcion();
                inscripcion.setEmail(email);
                inscripcion.setCodigoEvento(codigoEvento);
                inscripcion.setTipoInscripcion((TipoInscripcion) tipoInscripcionCombo.getSelectedItem());
                
                if (inscripcionDAO.insertar(inscripcion)) {
                    JOptionPane.showMessageDialog(InscripcionForm.this, 
                        "Inscripción realizada exitosamente", 
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    throw new Exception("No se pudo realizar la inscripción");
                }
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(InscripcionForm.this, 
                    "Error: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private class PagarListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String email = getEmailFromCombo(participanteCombo);
                String codigoEvento = getCodigoEventoFromCombo();
                
                if (email.isEmpty() || codigoEvento.isEmpty()) {
                    throw new Exception("Debe seleccionar participante y evento");
                }
                
                if (montoField.getText().trim().isEmpty()) {
                    throw new Exception("Debe ingresar el monto del pago");
                }
                
                if (!inscripcionDAO.existeInscripcion(email, codigoEvento)) {
                    throw new Exception("No existe inscripción para este participante en el evento");
                }
                
                BigDecimal monto = new BigDecimal(montoField.getText().trim());
                if (monto.compareTo(BigDecimal.ZERO) <= 0) {
                    throw new Exception("El monto debe ser mayor a cero");
                }
                
                MetodoPago metodoPago = (MetodoPago) metodoPagoCombo.getSelectedItem();
                
                if (pagoDAO.registrarPago(email, codigoEvento, metodoPago, monto)) {
                    JOptionPane.showMessageDialog(InscripcionForm.this, 
                        "Pago registrado exitosamente", 
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    montoField.setText("");
                } else {
                    throw new Exception("No se pudo registrar el pago");
                }
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(InscripcionForm.this, 
                    "Error: Formato de monto inválido", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(InscripcionForm.this, 
                    "Error: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private class ValidarListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String email = getEmailFromCombo(participanteCombo);
                String codigoEvento = getCodigoEventoFromCombo();
                
                if (email.isEmpty() || codigoEvento.isEmpty()) {
                    throw new Exception("Debe seleccionar participante y evento");
                }
                
                if (!pagoDAO.existePago(email, codigoEvento)) {
                    throw new Exception("No se puede validar la inscripción sin pago registrado");
                }
                
                if (eventoDAO.obtenerCupoDisponible(codigoEvento) <= 0) {
                    throw new Exception("No hay cupo disponible en el evento");
                }
                
                if (inscripcionDAO.validarInscripcion(email, codigoEvento)) {
                    JOptionPane.showMessageDialog(InscripcionForm.this, 
                        "Inscripción validada exitosamente", 
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    throw new Exception("No se pudo validar la inscripción");
                }
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(InscripcionForm.this, 
                    "Error: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}