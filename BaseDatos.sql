DROP TABLE IF EXISTS movimiento;
DROP TABLE IF EXISTS cuenta;
DROP TABLE IF EXISTS cliente;
DROP TABLE IF EXISTS persona;


CREATE TABLE persona (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(255),
    genero VARCHAR(50),
    edad INT,
    identificacion VARCHAR(255) UNIQUE,
    direccion VARCHAR(255),
    telefono VARCHAR(20)
);

CREATE TABLE cliente (
    clienteid SERIAL PRIMARY KEY,
    contrasena VARCHAR(255),
    estado BOOLEAN,
    persona_id INT UNIQUE,
    FOREIGN KEY (persona_id) REFERENCES persona(id)
);

CREATE TABLE cuenta (
    id SERIAL PRIMARY KEY,
    numero_cuenta VARCHAR(255) UNIQUE,
    tipo_cuenta VARCHAR(50),
    saldo_inicial DECIMAL(10, 2),
    saldo_actual DECIMAL(10, 2),
    estado BOOLEAN,
    cliente_id INT,
    FOREIGN KEY (cliente_id) REFERENCES cliente(clienteid)
);

CREATE TABLE movimiento (
    id SERIAL PRIMARY KEY,
    fecha TIMESTAMP,
    tipo_movimiento VARCHAR(50),
    valor DECIMAL(10, 2),
    saldo_disponible DECIMAL(10, 2),
    cuenta_id INT,
    FOREIGN KEY (cuenta_id) REFERENCES cuenta(id)
);


INSERT INTO persona (nombre, genero, edad, identificacion, direccion, telefono) VALUES
('Jose Lema', 'Masculino', 33, '1234567890', 'Otavalo sn y principal', '098254785');

INSERT INTO persona (nombre, genero, edad, identificacion, direccion, telefono) VALUES
('Marianela Montalvo', 'Femenino', 31, '9876543210', 'Amazonas y NNUU', '097548965');

INSERT INTO persona (nombre, genero, edad, identificacion, direccion, telefono) VALUES
('Juan Osorio', 'Masculino', 28, '1112223330', '13 junio y Equinoccial', '098874587');


INSERT INTO cliente (contrasena, estado, persona_id) VALUES
('1234', true, 1);

INSERT INTO cliente (contrasena, estado, persona_id) VALUES
('5678', true, 2);

INSERT INTO cliente (contrasena, estado, persona_id) VALUES
('1245', true, 3);


INSERT INTO cuenta (numero_cuenta, tipo_cuenta, saldo_inicial, saldo_actual, estado, cliente_id) VALUES
('478758', 'Ahorros', 2000, 2000, true, 1);

INSERT INTO cuenta (numero_cuenta, tipo_cuenta, saldo_inicial, saldo_actual, estado, cliente_id) VALUES
('225487', 'Corriente', 100, 100, true, 2);

INSERT INTO cuenta (numero_cuenta, tipo_cuenta, saldo_inicial, saldo_actual, estado, cliente_id) VALUES
('495878', 'Ahorros', 0, 0, true, 3);

INSERT INTO cuenta (numero_cuenta, tipo_cuenta, saldo_inicial, saldo_actual, estado, cliente_id) VALUES
('496825', 'Ahorros', 540, 540, true, 2);


INSERT INTO movimiento (fecha, tipo_movimiento, valor, saldo_disponible, cuenta_id) VALUES
('2025-08-04', 'Retiro de 575', -575, 1425, 1);

INSERT INTO movimiento (fecha, tipo_movimiento, valor, saldo_disponible, cuenta_id) VALUES
('2025-08-04', 'Deposito de 600', 600, 700, 2);

INSERT INTO movimiento (fecha, tipo_movimiento, valor, saldo_disponible, cuenta_id) VALUES
('2025-08-04', 'Deposito de 150', 150, 150, 3);

INSERT INTO movimiento (fecha, tipo_movimiento, valor, saldo_disponible, cuenta_id) VALUES
('2025-08-04', 'Retiro de 540', -540, 0, 4);
