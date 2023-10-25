USE `laboratorio`;
CREATE TABLE IF NOT EXISTS mediciones (
    numero INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    referencia INT NOT NULL,
    medicion INT NOT NULL
);