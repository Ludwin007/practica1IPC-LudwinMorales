/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.triforce.hyrule.gui.forms;

/**
 *
 * @author ludwi
 */

import com.triforce.hyrule.dao.*;
import com.triforce.hyrule.dao.impl.*;
import com.triforce.hyrule.models.*;
import com.triforce.hyrule.enums.*;
import com.triforce.hyrule.util.ReportGenerator;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ReporteActividadesForm extends JPanel {
    private JComboBox<String> eventoCombo;
    private JComboBox<TipoActividad> tipoCombo;
    private JTextField emailEncargadoField;
    private JTextField rutaSalidaField; 
    private EventoDAO eventoDAO;
    
    public ReporteActividadesForm() {
        this.eventoDAO = new EventoDAOImpl();
        initializeComponents();
        setupLayout();
        cargarEventos();
    }
    
    private void initializeComponents() {
        eventoCombo = new JComboBox<>();
        
        tipoCombo = new JComboBox<>();
        tipoCombo.addItem(null);
        for (TipoActividad tipo : TipoActividad.values()) {
            tipoCombo.addItem(tipo);
        }
        
        emailEncargadoField = new JTextField(25);
        rutaSalidaField = new JTextField(30);
    }
    
    private void setupLayout() {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        JLabel titleLabel = new JLabel("Reporte de Actividades");
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        titleLabel.setForeground(new Color(52, 73, 94));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 3;
        add(titleLabel, gbc);
        
        gbc.gridwidth = 1;
        
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel eventoLabel = new JLabel("Evento:*");
        eventoLabel.setForeground(Color.RED);
        add(eventoLabel, gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        add(eventoCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
        add(new JLabel("Tipo (opcional):"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        add(tipoCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1;
        add(new JLabel("Email encargado (opcional):"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        add(emailEncargadoField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 1;
        JLabel salidaLabel = new JLabel("Carpeta de salida:*");
        salidaLabel.setForeground(Color.RED);
        add(salidaLabel, gbc);
        gbc.gridx = 1;
        add(rutaSalidaField, gbc);
        
        JButton examinarBtn = new JButton("Examinar");
        examinarBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (chooser.showOpenDialog(ReporteActividadesForm.this) == JFileChooser.APPROVE_OPTION) {
                rutaSalidaField.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        });
        gbc.gridx = 2;
        add(examinarBtn, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton generarBtn = new JButton("Generar Reporte");
        JButton actualizarBtn = new JButton("Actualizar Eventos");
        
        generarBtn.setBackground(new Color(46, 204, 113));
        generarBtn.setForeground(Color.WHITE);
        actualizarBtn.setBackground(new Color(52, 152, 219));
        actualizarBtn.setForeground(Color.WHITE);
        
        generarBtn.addActionListener(new GenerarReporteListener());
        actualizarBtn.addActionListener(e -> cargarEventos());
        
        buttonPanel.add(generarBtn);
        buttonPanel.add(actualizarBtn);
        
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        add(buttonPanel, gbc);
    }
    
    private void cargarEventos() {
        eventoCombo.removeAllItems();
        List<Evento> eventos = eventoDAO.obtenerTodos();
        for (Evento evento : eventos) {
            eventoCombo.addItem(evento.getCodigoEvento() + " - " + evento.getTitulo());
        }
    }
    
    private String getCodigoEventoFromCombo() {
        String selected = (String) eventoCombo.getSelectedItem();
        return selected != null ? selected.split(" - ")[0] : "";
    }
    
    private class GenerarReporteListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String codigoEvento = getCodigoEventoFromCombo();
                String rutaSalida = rutaSalidaField.getText().trim();
                
                if (codigoEvento.isEmpty()) {
                    throw new Exception("Debe seleccionar un evento");
                }
                
                if (rutaSalida.isEmpty()) {
                    throw new Exception("Debe especificar la carpeta de salida");
                }
                
                TipoActividad tipo = (TipoActividad) tipoCombo.getSelectedItem();
                String emailEncargado = emailEncargadoField.getText().trim();
                
                if (emailEncargado.isEmpty()) {
                    emailEncargado = null;
                }
                
                ReportGenerator reportGenerator = new ReportGenerator(rutaSalida);
                reportGenerator.generarReporteActividades(codigoEvento, tipo, emailEncargado);
                
                JOptionPane.showMessageDialog(ReporteActividadesForm.this,
                    "Reporte generado exitosamente en la carpeta especificada",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(ReporteActividadesForm.this,
                    "Error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}