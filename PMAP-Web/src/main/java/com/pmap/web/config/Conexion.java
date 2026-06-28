package com.pmap.web.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public final class Conexion {

    private static final String URL = "jdbc:mysql://localhost:3306/pmap?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&useUnicode=true&characterEncoding=UTF-8";
    private static final String USUARIO = "root";
    private static final String CLAVE = "1234";

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
        return DriverManager.getConnection(URL, USUARIO, CLAVE);
    }
}
