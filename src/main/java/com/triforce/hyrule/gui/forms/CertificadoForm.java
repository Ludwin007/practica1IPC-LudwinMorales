/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.triforce.hyrule.gui.forms;
import com.triforce.hyrule.dao.*;
import com.triforce.hyrule.dao.impl.*;
import com.triforce.hyrule.models.*;
import com.triforce.hyrule.util.ReportGenerator;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 *
 * @author ludwi
 */

public class CertificadoForm extends JPanel {
    private JComboBox<String> participanteCombo;
    private JComboBox<String> eventoCombo;
    private JTextField rutaSalidaField;
    
    private ParticipanteDAO participanteDAO;
    private EventoDAO eventoDAO;
    private AsistenciaDAO asistenciaDAO;
    private CertificadoDAO certificadoDAO;
    
    public CertificadoForm() {
        this.participanteDAO = new ParticipanteDAOImpl();
        this.eventoDAO = new EventoDAOImpl();
        this.asistenciaDAO = new AsistenciaDAOImpl();
        this.certificadoDAO = new CertificadoDAOImpl();
        initializeComponents();
        setupLayout();
        cargarDatos();
    }
    
    private void initializeComponents() {
        participanteCombo = new JComboBox<>();
        eventoCombo = new JComboBox<>();
        rutaSalidaField = new JTextField(30);
    }
    
    private void setupLayout() {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
       
        JLabel titleLabel = new JLabel("Generación de Certificados");
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        titleLabel.setForeground(new Color(52, 73, 94));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 3;
        add(titleLabel, gbc);
        
        gbc.gridwidth = 1;
        
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Participante:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        add(participanteCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
        add(new JLabel("Evento:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        add(eventoCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1;
        add(new JLabel("Carpeta de salida:"), gbc);
        gbc.gridx = 1;
        add(rutaSalidaField, gbc);
        
        JButton examinarBtn = new JButton("Examinar");
        examinarBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (chooser.showOpenDialog(CertificadoForm.this) == JFileChooser.APPROVE_OPTION) {
                rutaSalidaField.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        });
        gbc.gridx = 2;
        add(examinarBtn, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton generarBtn = new JButton("Generar Certificado");
        JButton actualizarBtn = new JButton("Actualizar Datos");
        
        generarBtn.setBackground(new Color(46, 204, 113));
        generarBtn.setForeground(Color.WHITE);
        actualizarBtn.setBackground(new Color(52, 152, 219));
        actualizarBtn.setForeground(Color.WHITE);
        
        generarBtn.addActionListener(new GenerarCertificadoListener());
        actualizarBtn.addActionListener(e -> cargarDatos());
        
        buttonPanel.add(generarBtn);
        buttonPanel.add(actualizarBtn);
        
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 3;
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
    
    private String getEmailFromCombo() {
        String selected = (String) participanteCombo.getSelectedItem();
        return selected != null ? selected.split(" - ")[0] : "";
    }
    
    private String getCodigoEventoFromCombo() {
        String selected = (String) eventoCombo.getSelectedItem();
        return selected != null ? selected.split(" - ")[0] : "";
    }
    
    private class GenerarCertificadoListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String email = getEmailFromCombo();
                String codigoEvento = getCodigoEventoFromCombo();
                String rutaSalida = rutaSalidaField.getText().trim();
                
                if (email.isEmpty() || codigoEvento.isEmpty()) {
                    throw new Exception("Debe seleccionar participante y evento");
                }
                
                if (rutaSalida.isEmpty()) {
                    throw new Exception("Debe especificar la carpeta de salida");
                }
                List<String> actividades = asistenciaDAO.obtenerActividadesParticipante(email, codigoEvento);
                if (actividades.isEmpty()) {
                    throw new Exception("El participante debe haber asistido al menos a una actividad del evento");
                }
                if (certificadoDAO.existeCertificado(email, codigoEvento)) {
                    int respuesta = JOptionPane.showConfirmDialog(CertificadoForm.this,
                        "Ya existe un certificado para este participante en el evento.\n¿Desea generar uno nuevo?",
                        "Certificado Existente", JOptionPane.YES_NO_OPTION);
                    
                    if (respuesta != JOptionPane.YES_OPTION) {
                        return;
                    }
                }
                Participante participante = participanteDAO.obtenerPorEmail(email);
                Evento evento = eventoDAO.obtenerPorCodigo(codigoEvento);
                
                if (participante == null || evento == null) {
                    throw new Exception("No se encontraron los datos del participante o evento");
                }
                ReportGenerator reportGenerator = new ReportGenerator(rutaSalida);
                String rutaCertificado = reportGenerator.generarCertificado(participante, evento);
                certificadoDAO.registrarCertificado(email, codigoEvento, rutaCertificado);
                
                JOptionPane.showMessageDialog(CertificadoForm.this,
                    "Certificado generado exitosamente en:\n" + rutaCertificado,
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(CertificadoForm.this,
                    "Error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}