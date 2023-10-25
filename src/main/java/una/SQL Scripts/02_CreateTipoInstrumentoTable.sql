USE `laboratorio`;
CREATE TABLE IF NOT EXISTS tipo_instrumentos (
  `codigo` VARCHAR(20) NOT NULL,
  `nombre` VARCHAR(50) NOT NULL,
  `unidad` VARCHAR(30) NULL,
  PRIMARY KEY (`codigo`));


# Para poder referenciar a la tabla tipo_instrumentos desde otras tablas
CREATE INDEX idx_nombre ON tipo_instrumentos(nombre);