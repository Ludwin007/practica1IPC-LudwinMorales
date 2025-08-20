/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.triforce.hyrule.gui.forms;
import com.triforce.hyrule.dao.EventoDAO;
import com.triforce.hyrule.dao.impl.EventoDAOImpl;
import com.triforce.hyrule.models.Evento;
import com.triforce.hyrule.enums.TipoEvento;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 *
 * @author ludwi
 */

public class EventoForm extends JPanel {
    private JTextField codigoField;
    private JTextField fechaField;
    private JComboBox<TipoEvento> tipoCombo;
    private JTextField tituloField;
    private JTextArea ubicacionArea;
    private JSpinner cupoSpinner;
    private JTextField costoField;
    private EventoDAO eventoDAO;
    
    public EventoForm() {
        this.eventoDAO = new EventoDAOImpl();
        initializeComponents();
        setupLayout();
    }
    
    private void initializeComponents() {
        codigoField = new JTextField(20);
        fechaField = new JTextField(20);
        fechaField.setToolTipText("Formato: dd/MM/yyyy");
        
        tipoCombo = new JComboBox<>(TipoEvento.values());
        
        tituloField = new JTextField(30);
        
        ubicacionArea = new JTextArea(3, 30);
        ubicacionArea.setLineWrap(true);
        ubicacionArea.setWrapStyleWord(true);
        
        cupoSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10000, 1));
        
        costoField = new JTextField(15);
        costoField.setToolTipText("Formato: 0.00 (usar punto decimal)");
    }
    
    private void setupLayout() {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        JLabel titleLabel = new JLabel("Registro de Evento");
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        titleLabel.setForeground(new Color(52, 73, 94));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(titleLabel, gbc);
        
        gbc.gridwidth = 1;
        
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Código del evento:"), gbc);
        gbc.gridx = 1;
        add(codigoField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Fecha (dd/MM/yyyy):"), gbc);
        gbc.gridx = 1;
        add(fechaField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Tipo de evento:"), gbc);
        gbc.gridx = 1;
        add(tipoCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        add(new JLabel("Título:"), gbc);
        gbc.gridx = 1;
        add(tituloField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        add(new JLabel("Ubicación:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(new JScrollPane(ubicacionArea), gbc);
        
        gbc.gridx = 0; gbc.gridy = 6;
        add(new JLabel("Cupo máximo:"), gbc);
        gbc.gridx = 1;
        add(cupoSpinner, gbc);
       
        gbc.gridx = 0; gbc.gridy = 7;
        add(new JLabel("Costo de inscripción:"), gbc);
        gbc.gridx = 1;
        add(costoField, gbc);
       
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton guardarBtn = new JButton("Guardar");
        JButton limpiarBtn = new JButton("Limpiar");
        
        guardarBtn.setBackground(new Color(46, 204, 113));
        guardarBtn.setForeground(Color.WHITE);
        limpiarBtn.setBackground(new Color(52, 152, 219));
        limpiarBtn.setForeground(Color.WHITE);
        
        guardarBtn.addActionListener(new GuardarEventoListener());
        limpiarBtn.addActionListener(e -> limpiarFormulario());
        
        buttonPanel.add(guardarBtn);
        buttonPanel.add(limpiarBtn);
        
        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(buttonPanel, gbc);
    }
    
    private void limpiarFormulario() {
        codigoField.setText("");
        fechaField.setText("");
        tipoCombo.setSelectedIndex(0);
        tituloField.setText("");
        ubicacionArea.setText("");
        cupoSpinner.setValue(1);
        costoField.setText("");
    }
    
    private class GuardarEventoListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                if (codigoField.getText().trim().isEmpty()) {
                    throw new Exception("El código del evento es obligatorio");
                }
                
                if (fechaField.getText().trim().isEmpty()) {
                    throw new Exception("La fecha es obligatoria");
                }
                
                if (tituloField.getText().trim().isEmpty()) {
                    throw new Exception("El título es obligatorio");
                }
                
                if (ubicacionArea.getText().trim().isEmpty()) {
                    throw new Exception("La ubicación es obligatoria");
                }
                
                if (costoField.getText().trim().isEmpty()) {
                    throw new Exception("El costo de inscripción es obligatorio");
                }
                if (tituloField.getText().length() > 200) {
                    throw new Exception("El título no puede exceder 200 caracteres");
                }
                
                if (ubicacionArea.getText().length() > 150) {
                    throw new Exception("La ubicación no puede exceder 150 caracteres");
                }
                Evento evento = new Evento();
                evento.setCodigoEvento(codigoField.getText().trim());
                
                try {
                    LocalDate fecha = LocalDate.parse(fechaField.getText().trim(), 
                                                    DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    evento.setFecha(fecha);
                } catch (DateTimeParseException ex) {
                    throw new Exception("Formato de fecha inválido. Use dd/MM/yyyy");
                }
                
                evento.setTipo((TipoEvento) tipoCombo.getSelectedItem());
                evento.setTitulo(tituloField.getText().trim());
                evento.setUbicacion(ubicacionArea.getText().trim());
                evento.setCupoMaximo((Integer) cupoSpinner.getValue());
                
                try {
                    BigDecimal costo = new BigDecimal(costoField.getText().trim());
                    if (costo.compareTo(BigDecimal.ZERO) < 0) {
                        throw new Exception("El costo no puede ser negativo");
                    }
                    evento.setCostoInscripcion(costo);
                } catch (NumberFormatException ex) {
                    throw new Exception("Formato de costo inválido. Use formato decimal (ej: 75.00)");
                }
                if (eventoDAO.insertar(evento)) {
                    JOptionPane.showMessageDialog(EventoForm.this, 
                        "Evento registrado exitosamente", 
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    limpiarFormulario();
                } else {
                    throw new Exception("No se pudo guardar el evento en la base de datos");
                }
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(EventoForm.this, 
                    "Error: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}