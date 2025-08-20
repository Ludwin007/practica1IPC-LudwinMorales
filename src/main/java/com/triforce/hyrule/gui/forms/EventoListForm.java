/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.triforce.hyrule.gui.forms;
import com.triforce.hyrule.dao.EventoDAO;
import com.triforce.hyrule.dao.impl.EventoDAOImpl;
import com.triforce.hyrule.models.Evento;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 *
 * @author ludwi
 */

public class EventoListForm extends JPanel {
    private JTable eventosTable;
    private DefaultTableModel tableModel;
    private EventoDAO eventoDAO;
    
    public EventoListForm() {
        this.eventoDAO = new EventoDAOImpl();
        initializeComponents();
        setupLayout();
        cargarEventos();
    }
    
    private void initializeComponents() {
        String[] columnNames = {"Código", "Fecha", "Tipo", "Título", "Ubicación", "Cupo", "Costo"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        eventosTable = new JTable(tableModel);
        eventosTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        eventosTable.getTableHeader().setReorderingAllowed(false);
        
        eventosTable.getColumnModel().getColumn(0).setPreferredWidth(120); 
        eventosTable.getColumnModel().getColumn(1).setPreferredWidth(100); 
        eventosTable.getColumnModel().getColumn(2).setPreferredWidth(80);  
        eventosTable.getColumnModel().getColumn(3).setPreferredWidth(200); 
        eventosTable.getColumnModel().getColumn(4).setPreferredWidth(150); 
        eventosTable.getColumnModel().getColumn(5).setPreferredWidth(60);  
        eventosTable.getColumnModel().getColumn(6).setPreferredWidth(80);  
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel titleLabel = new JLabel("Lista de Eventos");
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        titleLabel.setForeground(new Color(52, 73, 94));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        add(titleLabel, BorderLayout.NORTH);
        
        JScrollPane scrollPane = new JScrollPane(eventosTable);
        scrollPane.setPreferredSize(new Dimension(800, 400));
        add(scrollPane, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton actualizarBtn = new JButton("Actualizar");
        
        actualizarBtn.setBackground(new Color(52, 152, 219));
        actualizarBtn.setForeground(Color.WHITE);
        actualizarBtn.addActionListener(e -> cargarEventos());
        
        buttonPanel.add(actualizarBtn);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void cargarEventos() {
        tableModel.setRowCount(0); 
        
        List<Evento> eventos = eventoDAO.obtenerTodos();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        for (Evento evento : eventos) {
            Object[] row = {
                evento.getCodigoEvento(),
                evento.getFecha().format(formatter),
                evento.getTipo().name(),
                evento.getTitulo(),
                evento.getUbicacion(),
                evento.getCupoMaximo(),
                "Q." + evento.getCostoInscripcion()
            };
            tableModel.addRow(row);
        }
    }
}