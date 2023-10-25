USE `laboratorio`;

CREATE TABLE IF NOT EXISTS instrumentos (
    serie VARCHAR(255) PRIMARY KEY NOT NULL,
    descripcion VARCHAR(255) NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    FOREIGN KEY (tipo) REFERENCES tipo_instrumentos(nombre),
    minimo INT NOT NULL,
    maximo INT NOT NULL,
    tolerancia INT NOT NULL
);

CREATE TABLE IF NOT EXISTS calibraciones (
    numero INT AUTO_INCREMENT PRIMARY KEY,
    fecha DATE NOT NULL,
    numero_de_mediciones INT NOT NULL,
    instrumento_serie VARCHAR(255) NOT NULL,
    FOREIGN KEY (instrumento_serie) REFERENCES instrumentos(serie)
);

# Esta tabla es para la relacion uno a muchos entre calibracion e instrumento, una calibracion tiene un instrumento y un instrumento puede tener muchas calibraciones
CREATE TABLE IF NOT EXISTS calibraciones_instrumento (
    calibracion_numero INT NOT NULL,
    instrumento_serie VARCHAR(255) NOT NULL,
    PRIMARY KEY (calibracion_numero, instrumento_serie),
    FOREIGN KEY (calibracion_numero) REFERENCES calibraciones(numero),
    FOREIGN KEY (instrumento_serie) REFERENCES instrumentos(serie)
);