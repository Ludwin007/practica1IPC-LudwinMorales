/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.triforce.hyrule.gui.forms;
import com.triforce.hyrule.dao.ParticipanteDAO;
import com.triforce.hyrule.dao.impl.ParticipanteDAOImpl;
import com.triforce.hyrule.models.Participante;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 *
 * @author ludwi
 */

public class ParticipanteListForm extends JPanel {
    private JTable participantesTable;
    private DefaultTableModel tableModel;
    private ParticipanteDAO participanteDAO;
    
    public ParticipanteListForm() {
        this.participanteDAO = new ParticipanteDAOImpl();
        initializeComponents();
        setupLayout();
        cargarParticipantes();
    }
    
    private void initializeComponents() {
        String[] columnNames = {"Email", "Nombre Completo", "Tipo", "Institución"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        participantesTable = new JTable(tableModel);
        participantesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        participantesTable.getTableHeader().setReorderingAllowed(false);
        participantesTable.getColumnModel().getColumn(0).setPreferredWidth(200); 
        participantesTable.getColumnModel().getColumn(1).setPreferredWidth(200); 
        participantesTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        participantesTable.getColumnModel().getColumn(3).setPreferredWidth(250);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel titleLabel = new JLabel("Lista de Participantes");
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        titleLabel.setForeground(new Color(52, 73, 94));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        add(titleLabel, BorderLayout.NORTH);
        
        JScrollPane scrollPane = new JScrollPane(participantesTable);
        scrollPane.setPreferredSize(new Dimension(800, 400));
        add(scrollPane, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton actualizarBtn = new JButton("Actualizar");
        
        actualizarBtn.setBackground(new Color(52, 152, 219));
        actualizarBtn.setForeground(Color.WHITE);
        actualizarBtn.addActionListener(e -> cargarParticipantes());
        
        buttonPanel.add(actualizarBtn);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void cargarParticipantes() {
        tableModel.setRowCount(0);
        
        List<Participante> participantes = participanteDAO.obtenerTodos();
        
        for (Participante participante : participantes) {
            Object[] row = {
                participante.getEmail(),
                participante.getNombreCompleto(),
                participante.getTipo().name(),
                participante.getInstitucion()
            };
            tableModel.addRow(row);
        }
    }
}
