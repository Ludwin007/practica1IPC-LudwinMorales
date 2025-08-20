/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.triforce.hyrule.gui;
import com.triforce.hyrule.service.FileProcessor;
import com.triforce.hyrule.gui.forms.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 *
 * @author ludwi
 */

public class HyruleEventsApp extends JFrame {
    private JDesktopPane desktopPane;
    private JTextArea logArea;
    private JScrollPane logScrollPane;
    private FileProcessor fileProcessor;
    
    public HyruleEventsApp() {
        initializeComponents();
        setupLayout();
        setupMenuBar();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Reino de Hyrule - Sistema de Gestión de Eventos");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
    
    private void initializeComponents() {
        desktopPane = new JDesktopPane();
        desktopPane.setBackground(new Color(236, 240, 241));
        logArea = new JTextArea(10, 50);
        logArea.setEditable(false);
        logArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        logArea.setBackground(new Color(44, 62, 80));
        logArea.setForeground(Color.BLACK);
        logArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        logScrollPane = new JScrollPane(logArea);
        logScrollPane.setPreferredSize(new Dimension(0, 200));
        logScrollPane.setBorder(BorderFactory.createTitledBorder("Log de Procesamiento"));
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        add(desktopPane, BorderLayout.CENTER);
        add(logScrollPane, BorderLayout.SOUTH);
        
        logArea.append("=== SISTEMA DE GESTIÓN DE EVENTOS - REINO DE HYRULE ===\n");
        logArea.append("Desarrollado poR: Ludwin Gerardo Morales Guarcas\n");
        logArea.append("Carné: 202230173.\n");
        logArea.append("Utilice el menú para cargar archivos o gestionar eventos.\n\n");
    }
    
    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(52, 73, 94));
        
        JMenu archivoMenu = createStyledMenu("Archivo");
        JMenuItem cargarArchivo = createStyledMenuItem("Cargar Archivo de Instrucciones");
        JMenuItem limpiarLog = createStyledMenuItem("Limpiar Log");
        JMenuItem salir = createStyledMenuItem("Salir");
        
        cargarArchivo.addActionListener(e -> mostrarDialogoCargarArchivo());
        limpiarLog.addActionListener(e -> logArea.setText(""));
        salir.addActionListener(e -> System.exit(0));
        
        archivoMenu.add(cargarArchivo);
        archivoMenu.addSeparator();
        archivoMenu.add(limpiarLog);
        archivoMenu.addSeparator();
        archivoMenu.add(salir);
        
        JMenu eventosMenu = createStyledMenu("Eventos");
        JMenuItem registrarEvento = createStyledMenuItem("Registrar Evento");
        JMenuItem listarEventos = createStyledMenuItem("Listar Eventos");
        
        registrarEvento.addActionListener(e -> abrirFormularioEvento());
        listarEventos.addActionListener(e -> abrirListaEventos());
        
        eventosMenu.add(registrarEvento);
        eventosMenu.add(listarEventos);
        
        JMenu participantesMenu = createStyledMenu("Participantes");
        JMenuItem registrarParticipante = createStyledMenuItem("Registrar Participante");
        JMenuItem listarParticipantes = createStyledMenuItem("Listar Participantes");
        JMenuItem gestionarInscripciones = createStyledMenuItem("Gestionar Inscripciones");
        
        registrarParticipante.addActionListener(e -> abrirFormularioParticipante());
        listarParticipantes.addActionListener(e -> abrirListaParticipantes());
        gestionarInscripciones.addActionListener(e -> abrirGestionInscripciones());
        
        participantesMenu.add(registrarParticipante);
        participantesMenu.add(listarParticipantes);
        participantesMenu.addSeparator();
        participantesMenu.add(gestionarInscripciones);
        
        JMenu actividadesMenu = createStyledMenu("Actividades");
        JMenuItem registrarActividad = createStyledMenuItem("Registrar Actividad");
        JMenuItem gestionarAsistencia = createStyledMenuItem("Gestionar Asistencia");
        
        registrarActividad.addActionListener(e -> abrirFormularioActividad());
        gestionarAsistencia.addActionListener(e -> abrirGestionAsistencia());
        
        actividadesMenu.add(registrarActividad);
        actividadesMenu.add(gestionarAsistencia);
        
        JMenu reportesMenu = createStyledMenu("Reportes");
        JMenuItem reporteParticipantes = createStyledMenuItem("Reporte de Participantes");
        JMenuItem reporteActividades = createStyledMenuItem("Reporte de Actividades");
        JMenuItem reporteEventos = createStyledMenuItem("Reporte de Eventos");
        JMenuItem generarCertificado = createStyledMenuItem("Generar Certificado");
        
        reporteParticipantes.addActionListener(e -> abrirReporteParticipantes());
        reporteActividades.addActionListener(e -> abrirReporteActividades());
        reporteEventos.addActionListener(e -> abrirReporteEventos());
        generarCertificado.addActionListener(e -> abrirGenerarCertificado());
        
        reportesMenu.add(reporteParticipantes);
        reportesMenu.add(reporteActividades);
        reportesMenu.add(reporteEventos);
        reportesMenu.addSeparator();
        reportesMenu.add(generarCertificado);
        
        JMenu ayudaMenu = createStyledMenu("Ayuda");
        JMenuItem acercaDe = createStyledMenuItem("Acerca de");
        
        acercaDe.addActionListener(e -> mostrarAcercaDe());
        
        ayudaMenu.add(acercaDe);
        
        menuBar.add(archivoMenu);
        menuBar.add(eventosMenu);
        menuBar.add(participantesMenu);
        menuBar.add(actividadesMenu);
        menuBar.add(reportesMenu);
        menuBar.add(ayudaMenu);
        
        setJMenuBar(menuBar);
    }
    
    private JMenu createStyledMenu(String text) {
        JMenu menu = new JMenu(text);
        menu.setForeground(Color.BLACK);
        menu.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        return menu;
    }
    
    private JMenuItem createStyledMenuItem(String text) {
        JMenuItem item = new JMenuItem(text);
        item.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        return item;
    }
    
    private void mostrarDialogoCargarArchivo() {
        JDialog dialog = new JDialog(this, "Cargar Archivo de Instrucciones", true);
        dialog.setSize(500, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Archivo de instrucciones:"), gbc);
        
        JTextField archivoField = new JTextField(30);
        JButton examinarBtn = new JButton("Examinar");
        JPanel archivoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        archivoPanel.add(archivoField);
        archivoPanel.add(examinarBtn);
        
        gbc.gridx = 1;
        panel.add(archivoPanel, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Velocidad (ms):"), gbc);
        
        JSpinner velocidadSpinner = new JSpinner(new SpinnerNumberModel(1000, 0, 10000, 100));
        gbc.gridx = 1;
        panel.add(velocidadSpinner, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Carpeta de salida:"), gbc);
        
        JTextField salidaField = new JTextField(30);
        JButton examinarSalidaBtn = new JButton("Examinar");
        JPanel salidaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        salidaPanel.add(salidaField);
        salidaPanel.add(examinarSalidaBtn);
        
        gbc.gridx = 1;
        panel.add(salidaPanel, gbc);
        
        JPanel botonesPanel = new JPanel(new FlowLayout());
        JButton procesarBtn = new JButton("Procesar Archivo");
        JButton cancelarBtn = new JButton("Cancelar");
        
        procesarBtn.setBackground(new Color(46, 204, 113));
        procesarBtn.setForeground(Color.BLACK);
        cancelarBtn.setBackground(new Color(231, 76, 60));
        cancelarBtn.setForeground(Color.BLACK);
        
        botonesPanel.add(procesarBtn);
        botonesPanel.add(cancelarBtn);
        
        examinarBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            if (chooser.showOpenDialog(dialog) == JFileChooser.APPROVE_OPTION) {
                archivoField.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        });
        
        examinarSalidaBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (chooser.showOpenDialog(dialog) == JFileChooser.APPROVE_OPTION) {
                salidaField.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        });
        
        procesarBtn.addActionListener(e -> {
            String archivo = archivoField.getText().trim();
            String salida = salidaField.getText().trim();
            int velocidad = (Integer) velocidadSpinner.getValue();
            
            if (archivo.isEmpty() || salida.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Debe seleccionar el archivo y la carpeta de salida", 
                                                "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            File archivoFile = new File(archivo);
            File salidaDir = new File(salida);
            
            if (!archivoFile.exists()) {
                JOptionPane.showMessageDialog(dialog, "El archivo seleccionado no existe", 
                                                "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!salidaDir.exists() || !salidaDir.isDirectory()) {
                JOptionPane.showMessageDialog(dialog, "La carpeta de salida no es válida", 
                                                "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            fileProcessor = new FileProcessor(logArea, salida);
            fileProcessor.processFile(archivo, velocidad);
            
            logArea.append("Iniciando procesamiento del archivo: " + archivo + "\n");
            logArea.append("Carpeta de salida: " + salida + "\n");
            logArea.append("Velocidad: " + velocidad + " ms por instrucción\n\n");
            
            dialog.dispose();
        });
        
        cancelarBtn.addActionListener(e -> dialog.dispose());
        
        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(botonesPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    private void abrirFormularioEvento() {
        EventoForm form = new EventoForm();
        agregarVentanaInterna(form, "Registrar Evento");
    }
    
    private void abrirListaEventos() {
        EventoListForm form = new EventoListForm();
        agregarVentanaInterna(form, "Lista de Eventos");
    }
    
    private void abrirFormularioParticipante() {
        ParticipanteForm form = new ParticipanteForm();
        agregarVentanaInterna(form, "Registrar Participante");
    }
    
    private void abrirListaParticipantes() {
        ParticipanteListForm form = new ParticipanteListForm();
        agregarVentanaInterna(form, "Lista de Participantes");
    }
    
    private void abrirGestionInscripciones() {
        InscripcionForm form = new InscripcionForm();
        agregarVentanaInterna(form, "Gestionar Inscripciones");
    }
    
    private void abrirFormularioActividad() {
        ActividadForm form = new ActividadForm();
        agregarVentanaInterna(form, "Registrar Actividad");
    }
    
    private void abrirGestionAsistencia() {
        AsistenciaForm form = new AsistenciaForm();
        agregarVentanaInterna(form, "Gestionar Asistencia");
    }
    
    private void abrirReporteParticipantes() {
        ReporteParticipantesForm form = new ReporteParticipantesForm();
        agregarVentanaInterna(form, "Reporte de Participantes");
    }
    
    private void abrirReporteActividades() {
        ReporteActividadesForm form = new ReporteActividadesForm();
        agregarVentanaInterna(form, "Reporte de Actividades");
    }
    
    private void abrirReporteEventos() {
        ReporteEventosForm form = new ReporteEventosForm();
        agregarVentanaInterna(form, "Reporte de Eventos");
    }
    
    private void abrirGenerarCertificado() {
        CertificadoForm form = new CertificadoForm();
        agregarVentanaInterna(form, "Generar Certificado");
    }
    
    private void agregarVentanaInterna(JPanel form, String titulo) {
        JInternalFrame frame = new JInternalFrame(titulo, true, true, true, true);
        frame.add(form);
        frame.pack();
        frame.setVisible(true);
        
        Dimension desktopSize = desktopPane.getSize();
        Dimension frameSize = frame.getSize();
        frame.setLocation(
            (desktopSize.width - frameSize.width) / 2,
            (desktopSize.height - frameSize.height) / 2
        );
        
        desktopPane.add(frame);
        
        try {
            frame.setSelected(true);
        } catch (Exception e) {
            // Ignore
        }
    }
    
    private void mostrarAcercaDe() {
        String mensaje = "<html><center>" +
                        "<h2>Reino de Hyrule - Sistema de Gestión de Eventos</h2>" +
                        "<p><b>Versión:</b> 2.0</p>" +
                        "<p><b>Desarrollado por:</b> Ludwin Gerardo Morales Guarcas</p>" +
                        "<p><b>Carné::</b> 202230173 </p>" +
                        "<br>" +
                        "<p>Introducción a la Programacion y Computacion 2</p>" +
                        "</center></html>";
        
        JOptionPane.showMessageDialog(this, mensaje, "Acerca de", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            new HyruleEventsApp().setVisible(true);
        });
    }
}