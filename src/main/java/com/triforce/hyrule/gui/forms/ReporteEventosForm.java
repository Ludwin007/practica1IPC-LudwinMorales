/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.triforce.hyrule.gui.forms;
import com.triforce.hyrule.enums.TipoEvento;
import com.triforce.hyrule.util.ReportGenerator;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 *
 * @author ludwi
 */

public class ReporteEventosForm extends JPanel {
    private JComboBox<TipoEvento> tipoCombo;
    private JTextField fechaInicioField;
    private JTextField fechaFinField;
    private JSpinner cupoMinSpinner;
    private JSpinner cupoMaxSpinner;
    private JTextField rutaSalidaField;
    
    public ReporteEventosForm() {
        initializeComponents();
        setupLayout();
    }
    
    private void initializeComponents() {
        tipoCombo = new JComboBox<>();
        tipoCombo.addItem(null);
        for (TipoEvento tipo : TipoEvento.values()) {
            tipoCombo.addItem(tipo);
        }
        
        fechaInicioField = new JTextField(15);
        fechaInicioField.setToolTipText("Formato: dd/MM/yyyy");
        
        fechaFinField = new JTextField(15);
        fechaFinField.setToolTipText("Formato: dd/MM/yyyy");
        
        cupoMinSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 10000, 1));
        cupoMaxSpinner = new JSpinner(new SpinnerNumberModel(1000, 1, 10000, 1));
        rutaSalidaField = new JTextField(30);
    }
    
    private void setupLayout() {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        JLabel titleLabel = new JLabel("Reporte de Eventos");
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        titleLabel.setForeground(new Color(52, 73, 94));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4;
        add(titleLabel, gbc);
        
        JLabel infoLabel = new JLabel("<html><i>Al menos uno de los filtros debe ser especificado</i></html>");
        infoLabel.setForeground(new Color(127, 140, 141));
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 4;
        add(infoLabel, gbc);
        
        gbc.gridwidth = 1;
        
        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Tipo de evento:"), gbc);
        gbc.gridx = 1;
        add(tipoCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Fecha inicio:"), gbc);
        gbc.gridx = 1;
        add(fechaInicioField, gbc);
        
        gbc.gridx = 2;
        add(new JLabel("Fecha fin:"), gbc);
        gbc.gridx = 3;
        add(fechaFinField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        add(new JLabel("Cupo mínimo:"), gbc);
        gbc.gridx = 1;
        add(cupoMinSpinner, gbc);
        
        gbc.gridx = 2;
        add(new JLabel("Cupo máximo:"), gbc);
        gbc.gridx = 3;
        add(cupoMaxSpinner, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5;
        JLabel salidaLabel = new JLabel("Carpeta de salida:*");
        salidaLabel.setForeground(Color.RED);
        add(salidaLabel, gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        add(rutaSalidaField, gbc);
        
        JButton examinarBtn = new JButton("Examinar");
        examinarBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (chooser.showOpenDialog(ReporteEventosForm.this) == JFileChooser.APPROVE_OPTION) {
                rutaSalidaField.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        });
        gbc.gridx = 3; gbc.gridwidth = 1;
        add(examinarBtn, gbc);
       
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton generarBtn = new JButton("Generar Reporte");
        JButton limpiarBtn = new JButton("Limpiar Filtros");
        
        generarBtn.setBackground(new Color(46, 204, 113));
        generarBtn.setForeground(Color.WHITE);
        limpiarBtn.setBackground(new Color(52, 152, 219));
        limpiarBtn.setForeground(Color.WHITE);
        
        generarBtn.addActionListener(new GenerarReporteListener());
        limpiarBtn.addActionListener(e -> limpiarFiltros());
        
        buttonPanel.add(generarBtn);
        buttonPanel.add(limpiarBtn);
        
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        add(buttonPanel, gbc);
    }
    
    private void limpiarFiltros() {
        tipoCombo.setSelectedIndex(0);
        fechaInicioField.setText("");
        fechaFinField.setText("");
        cupoMinSpinner.setValue(0);
        cupoMaxSpinner.setValue(1000);
    }
    
    private class GenerarReporteListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String rutaSalida = rutaSalidaField.getText().trim();
                
                if (rutaSalida.isEmpty()) {
                    throw new Exception("Debe especificar la carpeta de salida");
                }
                
                TipoEvento tipo = (TipoEvento) tipoCombo.getSelectedItem();
                LocalDate fechaInicio = null;
                LocalDate fechaFin = null;
                Integer cupoMin = null;
                Integer cupoMax = null;
                
                String fechaInicioStr = fechaInicioField.getText().trim();
                String fechaFinStr = fechaFinField.getText().trim();
                
                if (!fechaInicioStr.isEmpty() && !fechaFinStr.isEmpty()) {
                    try {
                        fechaInicio = LocalDate.parse(fechaInicioStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                        fechaFin = LocalDate.parse(fechaFinStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                        
                        if (fechaFin.isBefore(fechaInicio)) {
                            throw new Exception("La fecha fin debe ser posterior a la fecha inicio");
                        }
                    } catch (DateTimeParseException ex) {
                        throw new Exception("Formato de fecha inválido. Use dd/MM/yyyy");
                    }
                } else if (!fechaInicioStr.isEmpty() || !fechaFinStr.isEmpty()) {
                    throw new Exception("Si especifica fechas, debe proporcionar tanto fecha inicio como fecha fin");
                }
                
                int cupoMinValue = (Integer) cupoMinSpinner.getValue();
                int cupoMaxValue = (Integer) cupoMaxSpinner.getValue();
                
                if (cupoMinValue > 0 || cupoMaxValue < 1000) {
                    if (cupoMaxValue <= cupoMinValue) {
                        throw new Exception("El cupo máximo debe ser mayor al cupo mínimo");
                    }
                    cupoMin = cupoMinValue;
                    cupoMax = cupoMaxValue;
                }
                
                if (tipo == null && fechaInicio == null && fechaFin == null && cupoMin == null && cupoMax == null) {
                    throw new Exception("Debe especificar al menos un filtro para generar el reporte");
                }
                
                ReportGenerator reportGenerator = new ReportGenerator(rutaSalida);
                reportGenerator.generarReporteEventos(tipo, fechaInicio, fechaFin, cupoMin, cupoMax);
                
                JOptionPane.showMessageDialog(ReporteEventosForm.this,
                    "Reporte generado exitosamente en la carpeta especificada",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(ReporteEventosForm.this,
                    "Error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}