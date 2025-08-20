/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.triforce.hyrule.database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author ludwi
 */

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/triforce_hyrule_events";
    private static final String USERNAME = "root";
    private static final String PASSWORD = ""; // Cambiar por tu contraseña
    
    private static DatabaseConnection instance;
    private Connection connection;
    
    private DatabaseConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Error al conectar con la base de datos", e);
        }
    }
    
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        } else {
            try {
                if (instance.getConnection().isClosed()) {
                    instance = new DatabaseConnection();
                }
            } catch (SQLException e) {
                instance = new DatabaseConnection();
            }
        }
        return instance;
    }
    
    public Connection getConnection() {
        return connection;
    }
}