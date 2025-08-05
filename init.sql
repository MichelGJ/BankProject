CREATE TABLE IF NOT EXISTS persona (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(255),
    genero VARCHAR(50),
    edad INT,
    identificacion VARCHAR(255) UNIQUE,
    direccion VARCHAR(255),
    telefono VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS cliente (
    clienteid SERIAL PRIMARY KEY,
    contrasena VARCHAR(255),
    estado BOOLEAN,
    persona_id INT UNIQUE,
    FOREIGN KEY (persona_id) REFERENCES persona(id)
);

CREATE TABLE IF NOT EXISTS cuenta (
    id SERIAL PRIMARY KEY,
    numero_cuenta VARCHAR(255) UNIQUE,
    tipo_cuenta VARCHAR(50),
    saldo_inicial DECIMAL(10, 2),
    estado BOOLEAN,
    cliente_id INT,
    FOREIGN KEY (cliente_id) REFERENCES cliente(clienteid)
);

CREATE TABLE IF NOT EXISTS movimiento (
    id SERIAL PRIMARY KEY,
    fecha TIMESTAMP,
    tipo_movimiento VARCHAR(50),
    valor DECIMAL(10, 2),
    saldo_disponible DECIMAL(10, 2),
    cuenta_id INT,
    FOREIGN KEY (cuenta_id) REFERENCES cuenta(id)
);


-- Clean up existing data to ensure idempotency
DELETE FROM movimiento;
DELETE FROM cuenta;
DELETE FROM cliente;
DELETE FROM persona;

-- 1. Insert data into the persona table
-- This now includes 'genero' and 'edad'
INSERT INTO persona (id, nombre, genero, edad, identificacion, direccion, telefono) VALUES
(1, 'Jose Lema', 'Masculino', 33, '1234567890', 'Otavalo sn y principal', '098254785');

INSERT INTO persona (id, nombre, genero, edad, identificacion, direccion, telefono) VALUES
(2, 'Marianela Montalvo', 'Femenino', 31, '9876543210', 'Amazonas y NNUU', '097548965');

INSERT INTO persona (id, nombre, genero, edad, identificacion, direccion, telefono) VALUES
(3, 'Juan Osorio', 'Masculino', 28, '1112223330', '13 junio y Equinoccial', '098874587');

-- 2. Insert data into the cliente table, linking to persona
INSERT INTO cliente (clienteid, contrasena, estado, persona_id) VALUES
(1, '1234', true, 1);

INSERT INTO cliente (clienteid, contrasena, estado, persona_id) VALUES
(2, '5678', true, 2);

INSERT INTO cliente (clienteid, contrasena, estado, persona_id) VALUES
(3, '1245', true, 3);

-- 3. Insert data into the cuenta table, linking to cliente
INSERT INTO cuenta (id, numero_cuenta, tipo_cuenta, saldo_inicial, estado, cliente_id) VALUES
(1, 478758, 'Ahorros', 2000, true, 1);

INSERT INTO cuenta (id, numero_cuenta, tipo_cuenta, saldo_inicial, estado, cliente_id) VALUES
(2, 225487, 'Corriente', 100, true, 2);

INSERT INTO cuenta (id, numero_cuenta, tipo_cuenta, saldo_inicial, estado, cliente_id) VALUES
(3, 495878, 'Ahorros', 0, true, 3);

INSERT INTO cuenta (id, numero_cuenta, tipo_cuenta, saldo_inicial, estado, cliente_id) VALUES
(4, 496825, 'Ahorros', 540, true, 2);

-- 4. Insert data into the movimientos table
-- The 'saldo' field is now a single value representing the final balance after the transaction.
INSERT INTO movimiento (id, fecha, tipo_movimiento, valor, saldo_disponible, cuenta_id) VALUES
(1, '2025-08-04', 'Retiro de 575', -575, 1425, 1);

INSERT INTO movimiento (id, fecha, tipo_movimiento, valor, saldo_disponible, cuenta_id) VALUES
(2, '2025-08-04', 'Deposito de 600', 600, 700, 2);

INSERT INTO movimiento (id, fecha, tipo_movimiento, valor, saldo_disponible, cuenta_id) VALUES
(3, '2025-08-04', 'Deposito de 150', 150, 150, 3);

INSERT INTO movimiento (id, fecha, tipo_movimiento, valor, saldo_disponible, cuenta_id) VALUES
(4, '2025-08-04', 'Retiro de 540', -540, 0, 4);

