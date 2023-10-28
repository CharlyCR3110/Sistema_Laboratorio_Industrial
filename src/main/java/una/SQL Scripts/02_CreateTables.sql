USE `laboratorio`;

CREATE TABLE IF NOT EXISTS tipo_instrumentos (
    codigo VARCHAR(20) NOT NULL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    unidad VARCHAR(30) NULL
);

CREATE TABLE IF NOT EXISTS instrumentos (
    serie VARCHAR(255) PRIMARY KEY NOT NULL,
    descripcion VARCHAR(255) NOT NULL,
    tipo VARCHAR(20) NOT NULL,
    minimo INT NOT NULL,
    maximo INT NOT NULL,
    tolerancia INT NOT NULL,
    FOREIGN KEY (tipo) REFERENCES tipo_instrumentos(codigo)
);

CREATE TABLE IF NOT EXISTS calibraciones (
    numero INT AUTO_INCREMENT PRIMARY KEY,
    fecha DATE NOT NULL,
    numero_de_mediciones INT NOT NULL,
    instrumento_serie VARCHAR(255) NOT NULL,
    FOREIGN KEY (instrumento_serie) REFERENCES instrumentos(serie)
);

CREATE TABLE IF NOT EXISTS mediciones (
    numero INT AUTO_INCREMENT PRIMARY KEY,
    referencia INT NOT NULL,
    medicion INT NOT NULL,
    calibracion_numero INT NOT NULL,
    FOREIGN KEY (calibracion_numero) REFERENCES calibraciones(numero) ON DELETE CASCADE ON UPDATE CASCADE
);
