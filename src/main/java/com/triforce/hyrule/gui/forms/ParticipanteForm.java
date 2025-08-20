/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.triforce.hyrule.gui.forms;
import com.triforce.hyrule.dao.ParticipanteDAO;
import com.triforce.hyrule.dao.impl.ParticipanteDAOImpl;
import com.triforce.hyrule.models.Participante;
import com.triforce.hyrule.enums.TipoParticipante;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

/**
 *
 * @author ludwi
 */

public class ParticipanteForm extends JPanel {
    private JTextField nombreField;
    private JComboBox<TipoParticipante> tipoCombo;
    private JTextArea institucionArea;
    private JTextField emailField;
    
    private ParticipanteDAO participanteDAO;
    
    public ParticipanteForm() {
        this.participanteDAO = new ParticipanteDAOImpl();
        initializeComponents();
        setupLayout();
    }
    
    private void initializeComponents() {
        nombreField = new JTextField(30);
        tipoCombo = new JComboBox<>(TipoParticipante.values());
        
        institucionArea = new JTextArea(3, 30);
        institucionArea.setLineWrap(true);
        institucionArea.setWrapStyleWord(true);
        
        emailField = new JTextField(30);
    }
    
    private void setupLayout() {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        JLabel titleLabel = new JLabel("Registro de Participante");
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        titleLabel.setForeground(new Color(52, 73, 94));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(titleLabel, gbc);
        
        gbc.gridwidth = 1;
        
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Nombre completo:"), gbc);
        gbc.gridx = 1;
        add(nombreField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Tipo de participante:"), gbc);
        gbc.gridx = 1;
        add(tipoCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        add(new JLabel("Institución:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(new JScrollPane(institucionArea), gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        add(new JLabel("Correo electrónico:"), gbc);
        gbc.gridx = 1;
        add(emailField, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton guardarBtn = new JButton("Guardar");
        JButton limpiarBtn = new JButton("Limpiar");
        
        guardarBtn.setBackground(new Color(46, 204, 113));
        guardarBtn.setForeground(Color.WHITE);
        limpiarBtn.setBackground(new Color(52, 152, 219));
        limpiarBtn.setForeground(Color.WHITE);
        
        guardarBtn.addActionListener(new GuardarParticipanteListener());
        limpiarBtn.addActionListener(e -> limpiarFormulario());
        
        buttonPanel.add(guardarBtn);
        buttonPanel.add(limpiarBtn);
        
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(buttonPanel, gbc);
    }
    
    private void limpiarFormulario() {
        nombreField.setText("");
        tipoCombo.setSelectedIndex(0);
        institucionArea.setText("");
        emailField.setText("");
    }
    
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }
    
    private class GuardarParticipanteListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                if (nombreField.getText().trim().isEmpty()) {
                    throw new Exception("El nombre completo es obligatorio");
                }
                if (institucionArea.getText().trim().isEmpty()) {
                    throw new Exception("La institución es obligatoria");
                }
                if (emailField.getText().trim().isEmpty()) {
                    throw new Exception("El correo electrónico es obligatorio");
                }
                if (nombreField.getText().length() > 45) {
                    throw new Exception("El nombre completo no puede exceder 45 caracteres");
                }
                
                if (institucionArea.getText().length() > 150) {
                    throw new Exception("La institución no puede exceder 150 caracteres");
                }
                if (!isValidEmail(emailField.getText().trim())) {
                    throw new Exception("El formato del correo electrónico no es válido");
                }
                
                Participante participante = new Participante();
                participante.setNombreCompleto(nombreField.getText().trim());
                participante.setTipo((TipoParticipante) tipoCombo.getSelectedItem());
                participante.setInstitucion(institucionArea.getText().trim());
                participante.setEmail(emailField.getText().trim());
                
                if (participanteDAO.insertar(participante)) {
                    JOptionPane.showMessageDialog(ParticipanteForm.this, 
                        "Participante registrado exitosamente", 
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    limpiarFormulario();
                } else {
                    throw new Exception("No se pudo guardar el participante. Verifique que el correo no esté duplicado.");
                }
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(ParticipanteForm.this, 
                    "Error: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}