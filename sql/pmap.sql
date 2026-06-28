CREATE DATABASE IF NOT EXISTS pmap
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_general_ci;

USE pmap;

DROP TABLE IF EXISTS materias;

CREATE TABLE materias(
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    categoria VARCHAR(60),
    estado VARCHAR(20)
);

INSERT INTO materias (nombre, descripcion, categoria, estado) VALUES
('Matemáticas Básicas', 'Fundamentos de aritmética, álgebra y geometría.', 'Ciencias Básicas', 'Activa'),
('Programación Java', 'Introducción a Java orientado a objetos y JDBC.', 'Tecnología', 'Activa'),
('Lógica de Programación', 'Pensamiento algorítmico y estructuras de control.', 'Tecnología', 'Activa'),
('Comunicación Oral', 'Habilidades de expresión y presentación.', 'Transversal', 'Activa'),
('Bases de Datos', 'Modelado relacional, SQL y consultas preparadas.', 'Tecnología', 'Activa');
