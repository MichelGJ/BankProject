package com.example.bank.account.controller;

import com.example.bank.account.dto.ReporteEstadoCuentaDTO;
import com.example.bank.account.model.Cuenta;
import com.example.bank.account.model.Movimiento;
import com.example.bank.account.service.MovimientoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/movimientos")
public class MovimientoController {

    @Autowired
    private MovimientoService movimientoService;

    @GetMapping
    public List<Movimiento> getAllCuentas() {
        return movimientoService.getAllMovimientos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movimiento> getMovimientoById(@PathVariable Long id) {
        Movimiento movimiento = movimientoService.getMovimientoById(id);
        return ResponseEntity.ok(movimiento);
    }

    @PostMapping
    public ResponseEntity<Movimiento> createMovimiento(@Valid @RequestBody Movimiento movimiento) {
        return new ResponseEntity<>(movimientoService.createMovimiento(movimiento), HttpStatus.CREATED);
    }

}