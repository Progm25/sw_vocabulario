/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  PC
 * Created: 2 may. 2026
 */
-- 1. Crear la base de datos (si no existe) y usarla
CREATE DATABASE IF NOT EXISTS db_generador_lenguajes;
USE db_generador_lenguajes;

-- 2. Eliminar las tablas si ya existen (útil si necesitas reiniciar el script)
DROP TABLE IF EXISTS historial_ejecucion;
DROP TABLE IF EXISTS propiedad;

-- 3. Crear la Tabla Obligatoria: propiedad
-- Aquí se guardan las reglas que el usuario seleccionará en la interfaz
CREATE TABLE propiedad (
    id_propiedad INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255) NULL,
    patron_regex VARCHAR(100) NOT NULL,
    tipo_operacion VARCHAR(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 4. Crear la Tabla Opcional: historial_ejecucion
-- Aquí se registrará cada vez que un usuario genere lenguajes
CREATE TABLE historial_ejecucion (
    id_ejecucion INT AUTO_INCREMENT PRIMARY KEY,
    vocabulario VARCHAR(255) NOT NULL,
    cantidad_lenguajes INT NOT NULL,
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 5. Insertar datos de prueba teóricos en la tabla 'propiedad'
-- Estos son ejemplos clásicos de teoría de lenguajes y autómatas
INSERT INTO propiedad (nombre, descripcion, patron_regex, tipo_operacion) VALUES 
('Empieza con primer símbolo', 'La cadena generada debe iniciar estrictamente con el primer símbolo del vocabulario.', 'inicia_primero', 'Filtro_Logico'),
('Termina con último símbolo', 'La cadena generada debe finalizar con el último símbolo ingresado en el vocabulario.', 'termina_ultimo', 'Filtro_Logico'),
('Longitud Par', 'El autómata solo acepta cadenas cuya cantidad total de caracteres sea un número par.', '^(..)*$', 'Regex_Longitud'),
('Longitud Impar', 'El autómata solo acepta cadenas cuya cantidad total de caracteres sea un número impar.', '^(..)*.$', 'Regex_Longitud'),
('Cerradura de Kleene (*)', 'Genera todas las combinaciones posibles de los símbolos, incluyendo la cadena vacía (λ o ε).', 'kleene_star', 'Operacion_Core'),
('Cerradura Positiva (+)', 'Genera todas las combinaciones posibles, excluyendo la cadena vacía.', 'cerradura_positiva', 'Operacion_Core'),
('Es Palíndromo', 'La cadena se lee igual de izquierda a derecha que de derecha a izquierda.', 'palindromo', 'Filtro_Algoritmico');

