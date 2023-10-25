USE `laboratorio`;
CREATE TABLE IF NOT EXISTS tipo_instrumentos (
  `codigo` VARCHAR(20) NOT NULL,
  `nombre` VARCHAR(50) NOT NULL,
  `unidad` VARCHAR(30) NULL,
  PRIMARY KEY (`codigo`));

-- insert de pruebe
insert into TipoInstrumento (codigo, nombre, unidad) values('TER','Termómetro','Grados Celcius');
