package com.example.bank.account.controller;

import com.example.bank.account.model.Cuenta;
import com.example.bank.account.model.Movimiento;
import com.example.bank.account.repository.CuentaRepository;
import com.example.bank.account.repository.MovimientoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MovimientoIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MovimientoRepository movimientoRepository;

    @Autowired
    private CuentaRepository cuentaRepository;

    @AfterEach
    void tearDown() {
        movimientoRepository.deleteAll();
        cuentaRepository.deleteAll();
    }

    @Test
    void testCreateMovimiento_Success() {

        Cuenta newCuenta = new Cuenta(1L, "555555", "Ahorros", new BigDecimal("100.00"), new BigDecimal("100.00"), true, 1L);
        cuentaRepository.save(newCuenta);

        Movimiento newMovimiento = new Movimiento();
        newMovimiento.setValor(new BigDecimal("50.00")); // A deposit of $50
        newMovimiento.setCuenta(newCuenta);

        String url = "http://localhost:" + port + "/movimientos";

        ResponseEntity<Movimiento> response = restTemplate.postForEntity(url, newMovimiento, Movimiento.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        Movimiento createdMovimiento = response.getBody();
        assertNotNull(createdMovimiento);
        assertEquals(new BigDecimal("50.00"), createdMovimiento.getValor());

        Optional<Cuenta> updatedCuentaOptional = cuentaRepository.findById(newCuenta.getId());
        assertTrue(updatedCuentaOptional.isPresent());
        assertEquals(new BigDecimal("150.00"), updatedCuentaOptional.get().getSaldoActual());
    }

    @Test
    void testCreateMovimiento_InsufficientFunds() {

        Cuenta newCuenta = new Cuenta(1L, "555555", "Ahorros", new BigDecimal("100.00"), new BigDecimal("100.00"), true, 1L);
        cuentaRepository.save(newCuenta);

        Movimiento newMovimiento = new Movimiento();
        newMovimiento.setValor(new BigDecimal("-200.00")); // A withdrawal of $200
        newMovimiento.setCuenta(newCuenta);

        String url = "http://localhost:" + port + "/movimientos";

        ResponseEntity<String> response = restTemplate.postForEntity(url, newMovimiento, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        assertTrue(response.getBody().contains("Saldo no disponible"));

        Optional<Cuenta> updatedCuentaOptional = cuentaRepository.findById(newCuenta.getId());
        assertTrue(updatedCuentaOptional.isPresent());
        assertEquals(new BigDecimal("100.00"), updatedCuentaOptional.get().getSaldoActual());
    }
}