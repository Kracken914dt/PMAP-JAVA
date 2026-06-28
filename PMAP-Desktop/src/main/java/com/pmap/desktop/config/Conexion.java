package com.pmap.desktop.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public final class Conexion {

    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/pmap?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String DEFAULT_USUARIO = "root";
    private static final String DEFAULT_CLAVE = "1234";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError("No se pudo cargar el driver MySQL: " + e.getMessage());
        }
    }

    private Conexion() {
    }

    public static Connection getConnection() throws SQLException {
        String url = System.getProperty("pmap.db.url", DEFAULT_URL);
        String usuario = System.getProperty("pmap.db.user", DEFAULT_USUARIO);
        String clave = System.getProperty("pmap.db.password", DEFAULT_CLAVE);
        return DriverManager.getConnection(url, usuario, clave);
    }
}
